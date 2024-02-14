package cnuphys.ced.alldata.datacontainer.tof;

import org.jlab.io.base.DataBank;
import org.jlab.io.base.DataEvent;

import cnuphys.ced.alldata.datacontainer.ACommonHitData;

public class FTOFHitData extends ACommonHitData {

	// singleton
	private static volatile FTOFHitData _instance;

	/**
	 * Public access to the singleton
	 *
	 * @return the singleton
	 */
	public static FTOFHitData getInstance() {
		if (_instance == null) {
			synchronized (FTOFHitData.class) {
				if (_instance == null) {
					_instance = new FTOFHitData();
				}
			}
		}
		return _instance;
	}


	@Override
	public void update(DataEvent event) {
		DataBank bank = event.getBank("FTOF::hits");

		if (bank == null) {
			return;
		}

        sector = bank.getByte("sector");
        layer = bank.getByte("layer");
        component = bank.getShort("component");
        id = bank.getShort("id");
        x = bank.getFloat("x");
        y = bank.getFloat("y");
        z = bank.getFloat("z");
        energy = bank.getFloat("energy");
        time = bank.getFloat("time");

        int n = (sector != null) ? sector.length : 0;
		if (n > 0) {
			ppx = new int[n];
			ppy = new int[n];
		}

 	}

}
