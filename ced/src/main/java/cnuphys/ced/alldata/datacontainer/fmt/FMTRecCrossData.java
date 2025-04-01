package cnuphys.ced.alldata.datacontainer.fmt;

import java.util.List;

import org.jlab.io.base.DataBank;
import org.jlab.io.base.DataEvent;

import cnuphys.ced.alldata.datacontainer.ACommonCrossData;

public class FMTRecCrossData extends ACommonCrossData {

	// singleton
	private static volatile FMTRecCrossData _instance;

	/** ID */
	public short ID[];

	/** the cross x direction */
	public float ux[];

	/** the cross y direction */
	public float uy[];

	/** the cross z direction*/
	public float uz[];

	/** cluster 1 index */
	public short cluster1ndex[];

	/** cluster 2 index */
	public short cluster2ndex[];



	/**
	 * Public access to the singleton
	 *
	 * @return the singleton
	 */
	public static FMTRecCrossData getInstance() {
		if (_instance == null) {
			synchronized (FMTRecCrossData.class) {
				if (_instance == null) {
					_instance = new FMTRecCrossData();
				}
			}
		}
		return _instance;
	}

	@Override
	public void clear() {
		super.clear();
		ID = null;
		ux = null;
		uy = null;
		uz = null;
		cluster1ndex = null;
		cluster2ndex = null;
	}


	@Override
	public void update(DataEvent event) {
		DataBank bank = event.getBank("FMTRec::Crosses");

		if (bank == null) {
			return;
		}

		ID = bank.getShort("ID");
		sector = bank.getByte("sector");
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
		cluster1ndex = bank.getShort("cluster1ndex");
		cluster2ndex = bank.getShort("cluster2ndex");

		int n = (x != null) ? x.length : 0;
		if (n > 0) {
			ppx = new int[n];
			ppy = new int[n];
		}

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
		feedbackStrings.add(String.format("$Forest Green$%s cross ID %d", detectorName, ID[index]));
		super.feedback(detectorName, index, feedbackStrings);
		feedbackStrings.add(String.format("$Forest Green$%s cross direction (%-6.3f, %-6.3f, %-6.3f)", detectorName,
				ux[index], uy[index], uz[index]));
	}


}
