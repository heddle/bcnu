package cnuphys.ced.alldata.datacontainer.cc;

import org.jlab.io.base.DataBank;
import org.jlab.io.base.DataEvent;

import cnuphys.ced.alldata.datacontainer.ACommonTDCData;

public class HTCCTDCData extends ACommonTDCData {
	
	// singleton
	private static volatile HTCCTDCData _instance;
	

	/**
	 * Public access to the singleton
	 * 
	 * @return the singleton
	 */
	public static HTCCTDCData getInstance() {
		if (_instance == null) {
			synchronized (HTCCTDCData.class) {
				if (_instance == null) {
					_instance = new HTCCTDCData();
				}
			}
		}
		return _instance;
	}


	@Override
	public void update(DataEvent event) {
		DataBank bank = event.getBank("HTCC::tdc");
		
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
