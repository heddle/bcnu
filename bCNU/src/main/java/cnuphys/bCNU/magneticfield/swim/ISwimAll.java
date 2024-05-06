package cnuphys.bCNU.magneticfield.swim;

import java.util.List;

import cnuphys.lund.TrajectoryRowData;

public interface ISwimAll {

	/**
	 * Swim all tracks
	 */
	public void swimAll();

	/**
	 * Get all the row data so the trajectory dialog can be updated.
	 *
	 * @param manager the swim manager
	 * @return a vector of TrajectoryRowData objects.
	 */
	public List<TrajectoryRowData> getRowData();
}
