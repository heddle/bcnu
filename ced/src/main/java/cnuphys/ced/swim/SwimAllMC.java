package cnuphys.ced.swim;

import java.util.ArrayList;
import java.util.Vector;

import cnuphys.bCNU.magneticfield.swim.ISwimAll;
import cnuphys.ced.clasio.ClasIoEventManager;
import cnuphys.ced.clasio.ClasIoMonteCarloView;
import cnuphys.lund.LundId;
import cnuphys.lund.LundSupport;
import cnuphys.lund.TrajectoryRowData;
import cnuphys.swim.Swimming;

/**
 * Swims all the particles in the MC bank
 *
 * @author heddle
 *
 */
public class SwimAllMC implements ISwimAll {

	// integration cutoff
	private static final double PATHMAX = 11.0;

	/**
	 * Get all the row data so the trajectory dialog can be updated.
	 *
	 * @param manager the swim manager
	 * @return a vector of TrajectoryRowData objects.
	 */
	@Override
	public Vector<TrajectoryRowData> getRowData() {
		return ClasIoMonteCarloView.getInstance().getRowData();
	}

	/**
	 * Swim all Monte Carlo particles
	 *
	 * @param manager the swim manager
	 */
	@Override
	public void swimAll() {

		if (ClasIoEventManager.getInstance().isAccumulating()) {
			return;
		}

		ArrayList<SwimThread> swimThreads = new ArrayList<>();

		Swimming.clearMCTrajectories(); // clear all existing trajectories

		Vector<TrajectoryRowData> data = getRowData();
		if (data == null) {
			return;
		}

		double stepSize = 1.0e-3;
		double eps = 1.0e-6;

		//used to avoid swimming duplicates
		ArrayList<String> swam = new ArrayList<>();

		for (TrajectoryRowData trd : data) {
			LundId lid = LundSupport.getInstance().get(trd.getId());

			if (lid != null) {

					String summaryStr = String.format("%s %10.6f  %10.6f  %10.6f  %10.6f  %10.6f  %10.6f",
							lid.getName(), trd.getXo(), trd.getYo(), trd.getZo(),
							trd.getMomentum(), trd.getTheta(), trd.getPhi());

					if (swam.contains(summaryStr)) {
//						System.err.println("Skipping duplicate swim, probably MC::Particle and MC::Lund [" + lid.getName() + "]");
						continue;
					}

					swam.add(summaryStr);
					SwimThread st = new SwimThread(trd, PATHMAX, stepSize, eps);
					swimThreads.add(st);
					st.start();
			}

		} //for trd

		for (SwimThread st : swimThreads) {
			try {
				st.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}


	}


}