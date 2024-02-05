package cnuphys.ced.alldata.datacontainer.cc;

import org.jlab.io.base.DataBank;
import org.jlab.io.base.DataEvent;

import cnuphys.ced.alldata.datacontainer.ACommonADCData;

public class HTCCADCData extends ACommonADCData {

	// singleton
	private static volatile HTCCADCData _instance;

	/**
	 * Public access to the singleton
	 *
	 * @return the singleton
	 */
	public static HTCCADCData getInstance() {
		if (_instance == null) {
			synchronized (HTCCADCData.class) {
				if (_instance == null) {
					_instance = new HTCCADCData();
				}
			}
		}
		return _instance;
	}


	@Override
	public void update(DataEvent event) {
		DataBank bank = event.getBank("HTCC::adc");

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
