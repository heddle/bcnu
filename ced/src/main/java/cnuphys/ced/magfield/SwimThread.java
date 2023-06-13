package cnuphys.ced.magfield;

import cnuphys.adaptiveSwim.AdaptiveSwimException;
import cnuphys.adaptiveSwim.AdaptiveSwimResult;
import cnuphys.adaptiveSwim.AdaptiveSwimmer;
import cnuphys.adaptiveSwim.InitialValues;
import cnuphys.lund.GeneratedParticleRecord;
import cnuphys.lund.LundId;
import cnuphys.lund.LundSupport;
import cnuphys.lund.TrajectoryRowData;
import cnuphys.swim.Swimming;

public class SwimThread extends Thread {

	/** Swimming a Monte Carlo particle */
	public static final int MCSWIM = 1;
	
	/** Swimming a reconstructed particle */
	public static final int RECONSWIM = 2;

	//holds the trajectory info
	private final TrajectoryRowData _trd;
	
	//the swimmer
	private final AdaptiveSwimmer _swimmer;
	
	//the max path length
	private final double _sFinal;
	
	private final double _initStepSize;
	
	private final double _eps;
	
	private final int _swimType;

	/** Is the run method complete */
	private boolean _done;
	
	private static int count = 0;

	public SwimThread(TrajectoryRowData trd, double sFinal, double stepSize, double eps,
			int swimType) {
		_swimmer = new AdaptiveSwimmer();
		_trd = trd;
		_sFinal = sFinal;	
		_initStepSize = stepSize;
		_eps = eps;
		_swimType = swimType;
	}

	@Override
	public void run() {
		
		int scount = count++;
		System.err.println("STARTING SWIM " + scount);
		_done = false;
		LundId lid = LundSupport.getInstance().get(_trd.getId());

		AdaptiveSwimResult result = new AdaptiveSwimResult(true);
		
		try {
			_swimmer.swim(lid.getCharge(), _trd.getXo() / 100, _trd.getYo() / 100, _trd.getZo() / 100,
					_trd.getMomentum() / 1000, _trd.getTheta(), _trd.getPhi(), _sFinal, _initStepSize, _eps, result);
		} catch (AdaptiveSwimException e) {
			e.printStackTrace();
		}
		result.getTrajectory().setLundId(lid);
		result.getTrajectory().setSource(_trd.getSource());

		if (result.getTrajectory().getGeneratedParticleRecord() == null) {
			InitialValues iv = result.getInitialValues();
			GeneratedParticleRecord genPart =  new GeneratedParticleRecord(iv.charge,
					iv.xo, iv.yo, iv.zo, iv.p, iv.theta, iv.phi);
			result.getTrajectory().setGeneratedParticleRecord(genPart);
		}

		if (_swimType == MCSWIM) {
			Swimming.addMCTrajectory(result.getTrajectory());
		}
		else if (_swimType == RECONSWIM) {
			Swimming.addReconTrajectory(result.getTrajectory());
		}
		else {
			System.err.println("Uknown swim type in SwimThread: " + _swimType);
		}

		_done = true;
		System.err.println("ENDING SWIM " + scount);
	}
	
	/**
	 * Is the run method done?
	 * @return true if the run method is done
	 */
	public boolean isDone() {
		return _done;
	}
}
