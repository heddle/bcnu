package cnuphys.ced.alldata.datacontainer.tof;

import org.jlab.io.base.DataBank;
import org.jlab.io.base.DataEvent;

import cnuphys.ced.alldata.datacontainer.ACommonClusterData;

public class CTOFClusterData extends ACommonClusterData {

	// singleton
	private static volatile CTOFClusterData _instance;

	/**
	 * Public access to the singleton
	 *
	 * @return the singleton
	 */
	public static CTOFClusterData getInstance() {
		if (_instance == null) {
			synchronized (CTOFClusterData.class) {
				if (_instance == null) {
					_instance = new CTOFClusterData();
				}
			}
		}
		return _instance;
	}


	@Override
	public void update(DataEvent event) {
		DataBank bank = event.getBank("CTOF::clusters");

		if (bank == null) {
			return;
		}

        component = bank.getShort("component");
        energy = bank.getFloat("energy");
        time = bank.getFloat("time");
        id = bank.getShort("id");
        status = bank.getShort("status");
        x = bank.getFloat("x");
        y = bank.getFloat("y");
        z = bank.getFloat("z");

        int n = (energy != null) ? energy.length : 0;
		if (n > 0) {
			ppx = new int[n];
			ppy = new int[n];
		}
	}
	
	@Override
	public int count() {
        return (energy == null) ? 0 : energy.length;
    }

}
