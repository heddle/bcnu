package cnuphys.ced.alldata.datacontainer.bmt;

import java.util.List;

import org.jlab.io.base.DataBank;
import org.jlab.io.base.DataEvent;

import cnuphys.ced.alldata.datacontainer.ACommonCrossData;

public class BMTCrossData extends ACommonCrossData {

	// singleton
	private static volatile BMTCrossData _instance;

	/** ID */
	public short ID[];

	/** the cross x direction */
	public float ux[];

	/** the cross y direction */
	public float uy[];

	/** the cross z direction*/
	public float uz[];

	public byte layer[];

	/** cluster 1 index */
	public short Cluster1_ID[];

	/** cluster 2 index */
	public short Cluster2_ID[];

	/**
	 * Public access to the singleton
	 *
	 * @return the singleton
	 */
	public static BMTCrossData getInstance() {
		if (_instance == null) {
			synchronized (BMTCrossData.class) {
				if (_instance == null) {
					_instance = new BMTCrossData();
				}
			}
		}
		return _instance;
	}

	@Override
	public void clear() {
		super.clear();
		layer = null;
		ID = null;
		ux = null;
		uy = null;
		uz = null;
		Cluster1_ID = null;
		Cluster2_ID = null;
	}

	@Override
	public void update(DataEvent event) {
		DataBank bank = event.getBank("BMTRec::Crosses");

		if (bank == null) {
			return;
		}

		ID = bank.getShort("ID");
		sector = bank.getByte("sector");
		layer = bank.getByte("layer");
		region = bank.getByte("region");
		x = bank.getFloat("x");
		y = bank.getFloat("y");
		z = bank.getFloat("z");
		err_x = bank.getFloat("err_x");
		err_y = bank.getFloat("err_y");
		err_z = bank.getFloat("err_z");
		ux = bank.getFloat("ux");
		uy = bank.getFloat("uy");
		uz = bank.getFloat("uz");

        Cluster1_ID = bank.getShort("Cluster1_ID");
        Cluster2_ID = bank.getShort("Cluster2_ID");

		int n = (x != null) ? x.length : 0;
		if (n > 0) {
			ppx = new int[n];
			ppy = new int[n];
		}
	}

	/**
	 * Does the direction contain NaNs?
	 *
	 * @param index the index of the cross
	 * @return true if the direction contains NaNs
	 */
	public boolean isDirectionBad(int index) {
		return Float.isNaN(ux[index]) || Float.isNaN(uy[index]) || Float.isNaN(uz[index]);
	}


	/**
	 * Provide feedback for a cross
	 *
	 * @param detectorName    the name of the detector
	 * @param index           the index of the cluster
	 * @param feedbackStrings add strings to this collection
	 */
	@Override
	public void feedback(String detectorName, int index, List<String> feedbackStrings) {
		feedbackStrings.add(String.format("$Forest Green$%s cross layer %d ID %d", detectorName, layer[index], ID[index]));
		super.feedback(detectorName, index, feedbackStrings);
		feedbackStrings.add(String.format("$Forest Green$%s cross cluster 1 ID %d cluster 2 ID %d", detectorName,
				Cluster1_ID[index], Cluster2_ID[index]));
		feedbackStrings.add(String.format("$Forest Green$%s cross direction (%-6.3f, %-6.3f, %-6.3f)", detectorName,
				ux[index], uy[index], uz[index]));
	}


}
