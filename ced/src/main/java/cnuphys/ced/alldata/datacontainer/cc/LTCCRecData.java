package cnuphys.ced.alldata.datacontainer.cc;

import org.jlab.io.base.DataBank;
import org.jlab.io.base.DataEvent;

import cnuphys.ced.alldata.datacontainer.ACommonRecData;

public class LTCCRecData extends ACommonRecData {
	// singleton
	private static volatile LTCCRecData _instance;

	/**
	 * Public access to the singleton
	 * 
	 * @return the singleton
	 */
	public static LTCCRecData getInstance() {
		if (_instance == null) {
			synchronized (LTCCRecData.class) {
				if (_instance == null) {
					_instance = new LTCCRecData();
				}
			}
		}
		return _instance;
	}


	@Override
	public void update(DataEvent event) {
		DataBank bank = event.getBank("LTCC::rec");
		
		if (bank == null) {
			return;
		}
		
		id = bank.getShort("id");
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
