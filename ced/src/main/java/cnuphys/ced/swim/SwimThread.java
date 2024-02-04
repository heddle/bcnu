package cnuphys.ced.swim;

import cnuphys.CLAS12Swim.CLAS12SwimResult;
import cnuphys.CLAS12Swim.CLAS12Swimmer;
import cnuphys.CLAS12Swim.CLAS12Values;
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
	private final CLAS12Swimmer _swimmer;

	//the max path length
	private final double _sMax;

	private final double _h;

	private final double _tolerance;

	/**
	 * @param trd the trajectory row data
	 * @param sMax the max path length
	 * @param h the initial step size
	 * @param tolerance the tolerance
	 */
	public SwimThread(TrajectoryRowData trd, double sMax, double h, double tolerance) {
		_swimmer = new CLAS12Swimmer();
		_trd = trd;
		_sMax = sMax;
		_h = h;
		_tolerance = tolerance;
	}

	@Override
	public void run() {

		LundId lid = LundSupport.getInstance().get(_trd.getId());

		CLAS12SwimResult result = null;

		//have to convert trd momentum to GeV
		double p = _trd.getMomentum() / 1000;
	
		
		result = _swimmer.swim(lid.getCharge(), _trd.getXo(), _trd.getYo(), _trd.getZo(),
				p, _trd.getTheta(), _trd.getPhi(), _sMax, _h, _tolerance);
		result.getTrajectory().setLundId(lid);
		result.getTrajectory().setSource(_trd.getSource());

		if (result.getTrajectory().getGeneratedParticleRecord() == null) {
			CLAS12Values iv = result.getInitialValues();
			GeneratedParticleRecord genPart =  new GeneratedParticleRecord(iv.q,
					iv.x, iv.y, iv.z, iv.p, iv.theta, iv.phi);
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


