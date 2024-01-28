package cnuphys.ced.alldata.datacontainer.cal;

import java.util.ArrayList;

import org.jlab.io.base.DataBank;
import org.jlab.io.base.DataEvent;

public class ECalADCData extends ACalADCData {

	// singleton
	private static volatile ECalADCData _instance;
	

	/** 0-based planes (0, 1) for (inner, outer) */
	public ArrayList<Byte> plane = new ArrayList<>();

	/**
	 * Public access to the singleton
	 * 
	 * @return the singleton
	 */
	public static ECalADCData getInstance() {
		if (_instance == null) {
			synchronized (ECalADCData.class) {
				if (_instance == null) {
					_instance = new ECalADCData();
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

				if (layerArray[i] > 3) { // means it is ECAL, not PCAL
					byte layer = (byte) (layerArray[i] - 4); // 0..5
					byte plane0 = (byte) (layer / 3); // 0, 1, for inner, outer
					byte view0 = (byte) (layer % 3); // 0, 1, 2 for U, V, W

					sector.add(sectorArray[i]);
					plane.add(plane0);
					view.add(view0);
					strip.add(componentArray[i]);
					adc.add(adcArray[i]);
					time.add(timeArray[i]);

				}

			} // end loop over sectorArray
		} // end if sectorArray not null
		computeMaxADC();
	} // end update

}
