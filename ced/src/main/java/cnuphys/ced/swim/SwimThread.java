package cnuphys.ced.swim;

import cnuphys.adaptiveSwim.AdaptiveSwimException;
import cnuphys.adaptiveSwim.AdaptiveSwimResult;
import cnuphys.adaptiveSwim.AdaptiveSwimmer;
import cnuphys.adaptiveSwim.InitialValues;
import cnuphys.adaptiveSwim.SwimType;
import cnuphys.lund.GeneratedParticleRecord;
import cnuphys.lund.LundId;
import cnuphys.lund.LundSupport;
import cnuphys.lund.TrajectoryRowData;
import cnuphys.swim.Swimming;

public class SwimThread extends Thread {

	//holds the trajectory info
	private final TrajectoryRowData _trd;
	
	//the swimmer
	private final AdaptiveSwimmer _swimmer;
	
	//the max path length
	private final double _sFinal;
	
	private final double _initStepSize;
	
	private final double _eps;

	public SwimThread(TrajectoryRowData trd, double sFinal, double stepSize, double eps) {
		_swimmer = new AdaptiveSwimmer();
		_trd = trd;
		_sFinal = sFinal;	
		_initStepSize = stepSize;
		_eps = eps;
	}

	@Override
	public void run() {
		
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

		if (_trd.getSwimType() == SwimType.MCSWIM) {
			Swimming.addMCTrajectory(result.getTrajectory());
		}
		else if (_trd.getSwimType() == SwimType.RECONSWIM) {
			Swimming.addReconTrajectory(result.getTrajectory());
		}
		else {
			System.err.println("Unknown swim type in SwimThread: " + _trd.getSwimType());
		}

	}
}
	

