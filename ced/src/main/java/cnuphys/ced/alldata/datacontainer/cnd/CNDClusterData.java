package cnuphys.ced.alldata.datacontainer.cnd;

import org.jlab.io.base.DataBank;
import org.jlab.io.base.DataEvent;

import cnuphys.ced.alldata.datacontainer.ACommonClusterData;

public class CNDClusterData extends ACommonClusterData {

	// singleton
	private static volatile CNDClusterData _instance;

	/**
	 * Public access to the singleton
	 *
	 * @return the singleton
	 */
	public static CNDClusterData getInstance() {
		if (_instance == null) {
			synchronized (CNDClusterData.class) {
				if (_instance == null) {
					_instance = new CNDClusterData();
				}
			}
		}
		return _instance;
	}

	@Override
	public void update(DataEvent event) {
		DataBank bank = event.getBank("CND::clusters");

		if (bank == null) {
			return;
		}

		sector = bank.getByte("sector");
		layer = bank.getByte("layer");
		energy = bank.getFloat("energy");
		time = bank.getFloat("time");
		id = bank.getShort("id");
		status = bank.getShort("status");
		x = bank.getFloat("x");
		y = bank.getFloat("y");
		z = bank.getFloat("z");

		int n = (sector != null) ? sector.length : 0;
		if (n > 0) {
			ppx = new int[n];
			ppy = new int[n];
		}
	}

}
