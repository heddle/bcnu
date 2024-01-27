package cnuphys.ced.alldata.datacontainer.cal;

import org.jlab.io.base.DataEvent;

public class PCalRawData extends ACalRawDataContainer {

	// singleton
	private static volatile PCalRawData _instance;

	/**
	 * Public access to the singleton
	 * 
	 * @return the singleton
	 */
	public static PCalRawData getInstance() {
		if (_instance == null) {
			synchronized (PCalRawData.class) {
				if (_instance == null) {
					_instance = new PCalRawData();
				}
			}
		}
		return _instance;
	}

	@Override
	public void update(DataEvent event) {
		byte[] sectorArray = event.getBank("ECAL::adc").getByte("sector");
		if (sectorArray != null) {
			// layers are 1..3 for PCAL and 4..9 for EC
			byte layerArray[] = event.getBank("ECAL::adc").getByte("layer");
			short componentArray[] = event.getBank("ECAL::adc").getShort("component");
			int adcArray[] = event.getBank("ECAL::adc").getInt("ADC");
			int tdcArray[] = event.getBank("ECAL::tdc").getInt("TDC");
			float timeArray[] = event.getBank("ECAL::adc").getFloat("time");

			for (int i = 0; i < sectorArray.length; i++) {

				if (layerArray[i] < 4) { // means it is PCAL, not ECAL
					byte layer = (byte) (layerArray[i] - 1); // 0..2
					byte view0 = (byte) (layer % 3); // 0, 1, 2 for U, V, W

					sector.add(sectorArray[i]);
					view.add(view0);
					strip.add(componentArray[i]);
					adc.add(adcArray[i]);
					tdc.add(tdcArray[i]);
					time.add(timeArray[i]);

				}

			} // end loop over sector array
		} // end sectorArray not null
		computeMaxADC();
	} // end update

}
