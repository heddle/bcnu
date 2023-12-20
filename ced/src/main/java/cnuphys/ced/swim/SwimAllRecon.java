package cnuphys.ced.swim;

import java.util.ArrayList;
import java.util.Vector;

import cnuphys.bCNU.magneticfield.swim.ISwimAll;
import cnuphys.ced.clasio.ClasIoEventManager;
import cnuphys.ced.clasio.ClasIoReconEventView;
import cnuphys.lund.LundId;
import cnuphys.lund.LundSupport;
import cnuphys.lund.TrajectoryRowData;
import cnuphys.swim.Swimming;

/**
 * Swims all the particles in the Recon bank
 *
 * @author heddle
 *
 */
public class SwimAllRecon implements ISwimAll {

	// integration cutoff
	private static final double PATHMAX = 11.;

	/**
	 * Get all the row data so the trajectory dialog can be updated.
	 *
	 * @param manager the swim manager
	 * @return a vector of TrajectoryRowData objects.
	 */
	@Override
	public Vector<TrajectoryRowData> getRowData() {
		return ClasIoReconEventView.getInstance().getRowData();
	}

	/**
	 * Swim all reconstructed particles
	 *
	 * @param manager the swim manager
	 */
	@Override
	public void swimAll() {
		if (ClasIoEventManager.getInstance().isAccumulating()) {
			return;
		}

		ArrayList<SwimThread> swimThreads = new ArrayList<>();

		Swimming.clearReconTrajectories();

		Vector<TrajectoryRowData> data = getRowData();
		if (data == null) {
			return;
		}

		double stepSize = 1.0e-3;
		double eps = 1.0e-6;

		for (TrajectoryRowData trd : data) {
			LundId lid = LundSupport.getInstance().get(trd.getId());

			if (lid != null) {
				double sf = PATHMAX;
				String source = trd.getSource();

				if ((source != null) && (source.contains("CVT"))) {
					sf = 1.5; //shorter max path for cvt tracks
				}
				SwimThread st = new SwimThread(trd, sf, stepSize, eps);
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
