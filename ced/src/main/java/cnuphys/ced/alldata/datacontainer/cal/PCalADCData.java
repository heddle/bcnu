package cnuphys.ced.alldata.datacontainer.cal;

import org.jlab.io.base.DataBank;
import org.jlab.io.base.DataEvent;

public class PCalADCData extends ACalADCData {

	// singleton
	private static volatile PCalADCData _instance;

	/**
	 * Public access to the singleton
	 *
	 * @return the singleton
	 */
	public static PCalADCData getInstance() {
		if (_instance == null) {
			synchronized (PCalADCData.class) {
				if (_instance == null) {
					_instance = new PCalADCData();
				}
			}
		}
		return _instance;
	}

	@Override
	public void update(DataEvent event) {

		DataBank bank = event.getBank("ECAL::adc");

		if (bank == null) {
			return;
		}

		byte[] sectorArray = bank.getByte("sector");
		if (sectorArray != null) {
			// layers are 1..3 for PCAL and 4..9 for EC
			byte layerArray[] = bank.getByte("layer");
			short componentArray[] = bank.getShort("component");
			int adcArray[] = bank.getInt("ADC");
			float timeArray[] = bank.getFloat("time");

			for (int i = 0; i < sectorArray.length; i++) {

				if (layerArray[i] < 4) { // means it is PCAL, not ECAL
					byte layer = (byte) (layerArray[i] - 1); // 0..2
					byte view0 = (byte) (layer % 3); // 0, 1, 2 for U, V, W

					sector.add(sectorArray[i]);
					view.add(view0);
					strip.add(componentArray[i]);
					adc.add(adcArray[i]);
					time.add(timeArray[i]);
				}

			} // end loop over sector array
		} // end sectorArray not null
		computeMaxADC();
	} // end update

}
