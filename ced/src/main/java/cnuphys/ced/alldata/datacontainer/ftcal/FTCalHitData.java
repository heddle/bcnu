package cnuphys.ced.alldata.datacontainer.ftcal;

import org.jlab.io.base.DataBank;
import org.jlab.io.base.DataEvent;

import cnuphys.ced.alldata.datacontainer.ACommonRecData;

public class FTCalHitData extends ACommonRecData {

	// singleton
	private static volatile FTCalHitData _instance;

	/**
	 * Public access to the singleton
	 *
	 * @return the singleton
	 */
	public static FTCalHitData getInstance() {
		if (_instance == null) {
			synchronized (FTCalHitData.class) {
				if (_instance == null) {
					_instance = new FTCalHitData();
				}
			}
		}
		return _instance;
	}


	@Override
	public void update(DataEvent event) {
		DataBank bank = event.getBank("FTCAL::hits");

		if (bank == null) {
			return;
		}

		id = bank.getShort("hitID");
		x = bank.getFloat("x");
		y = bank.getFloat("y");
		z = bank.getFloat("z");

        int n = (x != null) ? x.length : 0;
		if (n > 0) {
			ppx = new int[n];
			ppy = new int[n];
		}


	}

}
