package cnuphys.ced.alldata.datacontainer.ftcal;

import org.jlab.io.base.DataBank;
import org.jlab.io.base.DataEvent;

import cnuphys.ced.alldata.datacontainer.ACommonADCData;

public class FTCalADCData extends ACommonADCData {

	// singleton
	private static volatile FTCalADCData _instance;

	/**
	 * Public access to the singleton
	 *
	 * @return the singleton
	 */
	public static FTCalADCData getInstance() {
		if (_instance == null) {
			synchronized (FTCalADCData.class) {
				if (_instance == null) {
					_instance = new FTCalADCData();
				}
			}
		}
		return _instance;
	}


	@Override
	public void update(DataEvent event) {
		DataBank bank = event.getBank("FTCAL::adc");

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
