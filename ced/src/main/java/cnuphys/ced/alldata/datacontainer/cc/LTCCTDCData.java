package cnuphys.ced.alldata.datacontainer.cc;

import org.jlab.io.base.DataBank;
import org.jlab.io.base.DataEvent;

import cnuphys.ced.alldata.datacontainer.ACommonTDCData;

public class LTCCTDCData extends ACommonTDCData {

	// singleton
	private static volatile LTCCTDCData _instance;


	/**
	 * Public access to the singleton
	 *
	 * @return the singleton
	 */
	public static LTCCTDCData getInstance() {
		if (_instance == null) {
			synchronized (LTCCTDCData.class) {
				if (_instance == null) {
					_instance = new LTCCTDCData();
				}
			}
		}
		return _instance;
	}


	@Override
	public void update(DataEvent event) {
		DataBank bank = event.getBank("LTCC::tdc");

		if (bank == null) {
			return;
		}

        sector = bank.getByte("sector");
        layer = bank.getByte("layer");
        component = bank.getShort("component");
        order = bank.getByte("order");
        tdc = bank.getInt("TDC");
	}
}
