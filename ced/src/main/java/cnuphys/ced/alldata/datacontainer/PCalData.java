package cnuphys.ced.alldata.datacontainer;

import java.awt.Color;
import java.util.ArrayList;

import org.jlab.io.base.DataEvent;

import cnuphys.ced.alldata.DataWarehouse;
import cnuphys.ced.clasio.ClasIoEventManager;
import cnuphys.ced.event.data.AdcColorScale;

public class PCalData implements IDataContainer {

	// the singleton
	private static volatile PCalData _instance;

	// the data warehouse
	private static DataWarehouse _dataWarehouse = DataWarehouse.getInstance();

	// event manager
	private static ClasIoEventManager _eventManager = ClasIoEventManager.getInstance();

	// the data in arrays lists due to the way the data is stored in the banks

	/** 1-based sectors */
	public ArrayList<Byte> sector = new ArrayList<>();

	/** 0-based views (0, 1, 2) for (u, v, w) */
	public ArrayList<Byte> view = new ArrayList<>();

	/** 1-based strips */
	public ArrayList<Short> strip = new ArrayList<>();

	/** adc values */
	public ArrayList<Integer> adc = new ArrayList<>();

	/** tdc values */
	public ArrayList<Integer> tdc = new ArrayList<>();
	
	/** max adc value */
	public int maxADC;


	// private constructor for singleton
	private PCalData() {
		_dataWarehouse.addDataContainerListener(this);
	}

	/**
	 * Public access to the singleton
	 * 
	 * @return the singleton
	 */
	public static PCalData getInstance() {
		if (_instance == null) {
			synchronized (PCalData.class) {
				if (_instance == null) {
					_instance = new PCalData();
				}
			}
		}
		return _instance;
	}

	@Override
	public void clear() {
		sector.clear();
		view.clear();
		strip.clear();
		adc.clear();
		tdc.clear();
		
		maxADC = 0;
	}

	@Override
	public void update(DataEvent event) {
		if (!_eventManager.isAccumulating()) {
			if (event != null) {
				byte[] sectorArray = event.getBank("ECAL::adc").getByte("sector");
				if (sectorArray != null) {
					// layers are 1..3 for PCAL and 4..9 for EC
					byte layerArray[] = event.getBank("ECAL::adc").getByte("layer");
					short componentArray[] = event.getBank("ECAL::adc").getShort("component");
					int adcArray[] = event.getBank("ECAL::adc").getInt("ADC");
					int tdcArray[] = event.getBank("ECAL::tdc").getInt("TDC");

					for (int i = 0; i < sectorArray.length; i++) {

						if (layerArray[i] < 4) { // means it is PCAL, not ECAL
							byte layer = (byte) (layerArray[i] - 1); // 0..2
							byte view0 = (byte) (layer % 3); // 0, 1, 2 for U, V, W

							sector.add(sectorArray[i]);
							view.add(view0);
							strip.add(componentArray[i]);
							adc.add(adcArray[i]);
							tdc.add(tdcArray[i]);

						}

					} // end loop over sector array
				} // end sectorArray not null
			} // end event not null
			
			//get the max adc
			int n = adc.size();
			maxADC = 0;

			if (n > 0) {
				for (int i = 0; i < n; i++) {
					int a = adc.get(i);
					if (a > maxADC) {
						maxADC = a;
					}
				}
			}

		} // end not accumulating
	}

	@Override
	public int rawCount() {
		return sector.size();
	}

	@Override
	public Color getADCColor(int adc) {
		if (adc > 0) {
			double fract = ((double) adc) / maxADC;
			fract = Math.max(0, Math.min(1.0, fract));
			int alpha = 128 + (int) (127 * fract);
			alpha = Math.min(255, alpha);

			return AdcColorScale.getInstance().getAlphaColor(fract, alpha);
		}
		return ADCZERO;
	}

}
