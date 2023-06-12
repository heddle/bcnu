package cnuphys.ced.magfield;

import cnuphys.adaptiveSwim.AdaptiveSwimResult;
import cnuphys.adaptiveSwim.AdaptiveSwimmer;
import cnuphys.lund.LundId;
import cnuphys.lund.LundSupport;
import cnuphys.lund.TrajectoryRowData;

public class SwimThread extends Thread implements Runnable {


	private TrajectoryRowData trd;


	public SwimThread(AdaptiveSwimmer swimmer, TrajectoryRowData trd, double sFinal) {

	}

	@Override
	public void run() {
		LundId lid = LundSupport.getInstance().get(trd.getId());

		AdaptiveSwimResult result = new AdaptiveSwimResult(true);

	}
}
