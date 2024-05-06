package cnuphys.ced.clasio;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import org.jlab.io.base.DataEvent;

import cnuphys.adaptiveSwim.SwimType;
import cnuphys.ced.alldata.DataWarehouse;
import cnuphys.lund.LundId;
import cnuphys.lund.LundSupport;
import cnuphys.lund.TrajectoryRowData;
import cnuphys.lund.TrajectoryTableModel;

@SuppressWarnings("serial")
public class ClasIoMonteCarloView extends ClasIoTrajectoryInfoView {

	// singleton
	private static volatile ClasIoMonteCarloView instance;

	// one row for each reconstructed trajectory
	private static ArrayList<TrajectoryRowData> _trajData = new ArrayList<>();


	private ClasIoMonteCarloView() {
		super("Monte Carlo Tracks");
	}

	/**
	 * Get the monte carlo event view
	 *
	 * @return the monte carlo event view
	 */
	public static ClasIoMonteCarloView getInstance() {
		if (instance == null) {
			synchronized (ClasIoMonteCarloView.class) {
				if (instance == null) {
					instance = new ClasIoMonteCarloView();
				}
			}
		}
		return instance;
	}

	@Override
	public List<TrajectoryRowData> getRowData() {
		return _trajData;
	}

	@Override
	public void newClasIoEvent(DataEvent event) {
		_trajectoryTable.clear(); // remove existing events

		if (!_eventManager.isAccumulating()) {

			// now fill the table.
			TrajectoryTableModel model = _trajectoryTable.getTrajectoryModel();

			addTracks(_trajData, "MC::Particle");
			addTracks(_trajData, "MC::Lund");

			model.setData(_trajData);
			model.fireTableDataChanged();
			_trajectoryTable.repaint();
			_trajectoryTable.repaint();
		} // !accumulating
	}

	// add tracks
	private void addTracks(ArrayList<TrajectoryRowData> data, String bankName) {
		try {


			DataWarehouse dm = DataWarehouse.getInstance();


			float[] vx = dm.getFloat(bankName, "vx"); // vertex x cm
			if ((vx != null) && (vx.length > 0)) {
				float[] vy = dm.getFloat(bankName, "vy"); // vertex y cm
				float[] vz = dm.getFloat(bankName, "vz"); // vertex z cm
				float px[] = dm.getFloat(bankName, "px");
				float py[] = dm.getFloat(bankName, "py");
				float pz[] = dm.getFloat(bankName, "pz");
				int pid[] = dm.getInt(bankName, "pid");

				for (int i = 0; i < vx.length; i++) {

					LundId lid = LundSupport.getInstance().get(pid[i]);

					if (lid == null) {
						//can't swim if don't know the charge!
						continue;
					}

					double xo = vx[i]; // cm
					double yo = vy[i]; // cm
					double zo = vz[i]; // cm

					double pxo = px[i]; // GeV/c
					double pyo = py[i];
					double pzo = pz[i];

					double p = Math.sqrt(pxo * pxo + pyo * pyo + pzo * pzo); // GeV/c

					double phi = Math.atan2(pyo, pxo);
					double theta = Math.acos(pzo / p);

					// note conversions to degrees and MeV
					TrajectoryRowData row = new TrajectoryRowData(i, lid, xo, yo, zo, 1000 * p,
							Math.toDegrees(theta), Math.toDegrees(phi), 0, bankName, SwimType.MCSWIM);
					data.add(row);

				}
			}
		} catch (Exception e) {
			String warning = "[ClasIoMonteCarloEventView.addTracks] " + e.getMessage();
			System.err.println(warning);
		}
	}


	@Override
	public void openedNewEventFile(String path) {
	}

	/**
	 * Change the event source type
	 *
	 * @param source the new source: File, ET
	 */
	@Override
	public void changedEventSource(ClasIoEventManager.EventSourceType source) {
	}

}
