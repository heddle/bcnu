package cnuphys.ced.alldata.datacontainer.cnd;

import java.util.ArrayList;

import org.jlab.io.base.DataBank;
import org.jlab.io.base.DataEvent;

import cnuphys.ced.alldata.datacontainer.ACommonTDCData;

public class CNDTDCData extends ACommonTDCData {
	
	// singleton
	private static volatile CNDTDCData _instance;
	

	/**
	 * Public access to the singleton
	 * 
	 * @return the singleton
	 */
	public static CNDTDCData getInstance() {
		if (_instance == null) {
			synchronized (CNDTDCData.class) {
				if (_instance == null) {
					_instance = new CNDTDCData();
				}
			}
		}
		return _instance;
	}


	@Override
	public void update(DataEvent event) {
		DataBank bank = event.getBank("CND::tdc");
		
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
