package cnuphys.ced.alldata.datacontainer.cnd;

import org.jlab.io.base.DataBank;
import org.jlab.io.base.DataEvent;

import cnuphys.ced.alldata.datacontainer.ACommonADCData;

public class CNDADCData extends ACommonADCData {

	// singleton
	private static volatile CNDADCData _instance;

	/**
	 * Public access to the singleton
	 *
	 * @return the singleton
	 */
	public static CNDADCData getInstance() {
		if (_instance == null) {
			synchronized (CNDADCData.class) {
				if (_instance == null) {
					_instance = new CNDADCData();
				}
			}
		}
		return _instance;
	}


	@Override
	public void update(DataEvent event) {
		DataBank bank = event.getBank("CND::adc");

		if (bank == null) {
			return;
		}

        sector = bank.getByte("sector");
        layer = bank.getByte("layer");
        component = bank.getShort("component");
        order = bank.getByte("order");
        adc = bank.getInt("ADC");
        time = bank.getFloat("time");

        computeMaxADC();
	}

}
