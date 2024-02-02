package cnuphys.ced.alldata.datacontainer.tof;

import org.jlab.io.base.DataBank;
import org.jlab.io.base.DataEvent;

import cnuphys.ced.alldata.datacontainer.ACommonADCData;

public class CTOFADCData extends ACommonADCData {
	// singleton
	private static volatile CTOFADCData _instance;

	/**
	 * Public access to the singleton
	 * 
	 * @return the singleton
	 */
	public static CTOFADCData getInstance() {
		if (_instance == null) {
			synchronized (CTOFADCData.class) {
				if (_instance == null) {
					_instance = new CTOFADCData();
				}
			}
		}
		return _instance;
	}


	@Override
	public void update(DataEvent event) {
		DataBank bank = event.getBank("CTOF::adc");
		
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