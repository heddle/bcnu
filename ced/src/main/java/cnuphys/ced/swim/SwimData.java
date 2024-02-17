package cnuphys.ced.swim;

import cnuphys.CLAS12Swim.CLAS12Swimmer;
import cnuphys.lund.TrajectoryRowData;

public class SwimData {
	//holds the trajectory info
	public final TrajectoryRowData trd;

	//the swimmer
	public final CLAS12Swimmer swimmer;

	//the max path length
	public final double sMax;

	public final double h;

	public final double tolerance;

	/**
	 * @param trd the trajectory row data
	 * @param sMax the max path length
	 * @param h the initial step size
	 * @param tolerance the tolerance
	 */
	public SwimData(TrajectoryRowData trd, double sMax, double h, double tolerance) {
		swimmer = new CLAS12Swimmer();
		this.trd = trd;
		this.sMax = sMax;
		this.h = h;
		this.tolerance = tolerance;
	}

}
