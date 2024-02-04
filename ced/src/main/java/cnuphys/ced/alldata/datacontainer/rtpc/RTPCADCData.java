package cnuphys.ced.alldata.datacontainer.rtpc;

import org.jlab.io.base.DataBank;
import org.jlab.io.base.DataEvent;

import cnuphys.ced.alldata.datacontainer.ACommonADCData;

public class RTPCADCData extends ACommonADCData {

	// singleton
	private static volatile RTPCADCData _instance;

	/**
	 * Public access to the singleton
	 * 
	 * @return the singleton
	 */
	public static RTPCADCData getInstance() {
		if (_instance == null) {
			synchronized (RTPCADCData.class) {
				if (_instance == null) {
					_instance = new RTPCADCData();
				}
			}
		}
		return _instance;
	}


	@Override
	public void update(DataEvent event) {
		DataBank bank = event.getBank("RTPC::adc");
		
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
