package cnuphys.ced.event;

import java.util.ArrayList;

import org.jlab.io.base.DataEvent;

import cnuphys.ced.clasio.ClasIoEventManager;
import cnuphys.ced.clasio.ClasIoEventManager.EventSourceType;
import cnuphys.ced.clasio.IClasIoEventListener;

public class AllECData implements IClasIoEventListener {
	
	public ArrayList<Byte> sector = new ArrayList<>();
	public ArrayList<Byte> plane = new ArrayList<>();
	public ArrayList<Byte> view = new ArrayList<>();
	public ArrayList<Short> strip = new ArrayList<>();
	public ArrayList<Integer> adc = new ArrayList<>();
	public ArrayList<Integer> tdc = new ArrayList<>();

	
	// singleton
	private static volatile AllECData _instance;
	
	// event manager
	private static ClasIoEventManager _eventManager = ClasIoEventManager.getInstance();
	
	// private constructor for singleton
	private AllECData() {
	}
	
	/**
	 * Public access to the AllECData
	 *
	 * @return the AllECData singleton
	 */
	public static AllECData getInstance() {
		if (_instance == null) {
			synchronized (AllECData.class) {
				if (_instance == null) {
					_instance = new AllECData();
				}
			}
		}
		return _instance;
	}
	
	// clear the data
	private void clear() {
		sector.clear();
		plane.clear();
		view.clear();
		strip.clear();
		adc.clear();
		tdc.clear();
	}

	@Override
	public void newClasIoEvent(DataEvent event) {
		if (!_eventManager.isAccumulating()) {
			System.err.println("ECView.prepareDataArrays");
			if (event != null) {
				byte[] sectorArray = event.getBank("ECAL::adc").getByte("sector");
				if (sectorArray != null) {
					//layers are 1..3 for PCAL and 4..9 for EC
					byte layerArray[] = event.getBank("ECAL::adc").getByte("layer");
					short componentArray[] = event.getBank("ECAL::adc").getShort("component");
					int adcArray[] = event.getBank("ECAL::adc").getInt("ADC");
					int tdcArray[] = event.getBank("ECAL::tdc").getInt("TDC");

					for (int i = 0; i < sectorArray.length; i++) {
						
						if (layerArray[i] > 3) { //means it is EC, not PCAL
							byte layer = (byte) (layerArray[i] - 4);
							byte plane0 = (byte) (layer/3); //0, 1, for inner, outer
							byte view0 = (byte) (layer % 3); //0, 1, 2 for U, V, W
							
							sector.add(sectorArray[i]);
							plane.add(plane0);
							view.add(view0);
							strip.add(componentArray[i]);
							adc.add(adcArray[i]);
							tdc.add(tdcArray[i]);
							
						}
						
						
					} //end loop over sector array
				} //end sectorArray not null
			} //end event not null
			
			System.err.println("DONE");
		} //end not accumulating
	}

	@Override
	public void openedNewEventFile(String path) {
		clear();
	}

	@Override
	public void changedEventSource(EventSourceType source) {
		clear();
	}

}
