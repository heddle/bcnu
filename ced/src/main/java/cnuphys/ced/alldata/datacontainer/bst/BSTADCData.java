package cnuphys.ced.alldata.datacontainer.bst;

import org.jlab.io.base.DataBank;
import org.jlab.io.base.DataEvent;

import cnuphys.ced.alldata.datacontainer.ACommonADCData;

public class BSTADCData  extends ACommonADCData {

	// singleton
	private static volatile BSTADCData _instance;

	/**
	 * Public access to the singleton
	 *
	 * @return the singleton
	 */
	public static BSTADCData getInstance() {
		if (_instance == null) {
			synchronized (BSTADCData.class) {
				if (_instance == null) {
					_instance = new BSTADCData();
				}
			}
		}
		return _instance;
	}


	@Override
	public void update(DataEvent event) {
		DataBank bank = event.getBank("BST::adc");

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
