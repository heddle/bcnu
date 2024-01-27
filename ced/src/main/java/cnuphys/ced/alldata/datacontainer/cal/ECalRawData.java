package cnuphys.ced.alldata.datacontainer.cal;

import java.util.ArrayList;

import org.jlab.io.base.DataEvent;

public class ECalRawData extends ACalRawDataContainer {

	// singleton
	private static volatile ECalRawData _instance;
	

	/** 0-based planes (0, 1) for (inner, outer) */
	public ArrayList<Byte> plane = new ArrayList<>();

	/**
	 * Public access to the singleton
	 * 
	 * @return the singleton
	 */
	public static ECalRawData getInstance() {
		if (_instance == null) {
			synchronized (ECalRawData.class) {
				if (_instance == null) {
					_instance = new ECalRawData();
				}
			}
		}
		return _instance;
	}

	@Override
	public void clear() {
		super.clear();
		plane.clear();
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
			float timeArray[] = event.getBank("ECAL::tdc").getFloat("time");
			

			for (int i = 0; i < sectorArray.length; i++) {

				if (layerArray[i] > 3) { // means it is ECAL, not PCAL
					byte layer = (byte) (layerArray[i] - 4); // 0..5
					byte plane0 = (byte) (layer / 3); // 0, 1, for inner, outer
					byte view0 = (byte) (layer % 3); // 0, 1, 2 for U, V, W

					sector.add(sectorArray[i]);
					plane.add(plane0);
					view.add(view0);
					strip.add(componentArray[i]);
					adc.add(adcArray[i]);
					tdc.add(tdcArray[i]);
					time.add(timeArray[i]);

				}

			} // end loop over sectorArray
		} // end if sectorArray not null
		computeMaxADC();
	} // end update

}
