package cnuphys.ced.event.data;

import org.jlab.io.base.DataEvent;

import cnuphys.ced.clasio.ClasIoEventManager;

public class HTCC2 extends DetectorData {

	AdcList _adcHits = new AdcList("HTCC::adc");

	private static HTCC2 _instance;

	/**
	 * Public access to the singleton
	 *
	 * @return the FTOF singleton
	 */
	public static HTCC2 getInstance() {
		if (_instance == null) {
			_instance = new HTCC2();
		}
		return _instance;
	}

	@Override
	public void newClasIoEvent(DataEvent event) {
		_adcHits = new AdcList("HTCC::adc");
	}

	/**
	 * Update the list. This is probably needed only during accumulation
	 *
	 * @return the updated list
	 */
	public AdcList updateAdcList() {
		try {
			_adcHits = new AdcList("HTCC::adc");
			return _adcHits;
		} catch (Exception e) {
			System.err.println("HTCC2 data error for event: " + ClasIoEventManager.getInstance().getSequentialEventNumber());
			return null;
		}
	}

	/**
	 * Get the adc hit list
	 *
	 * @return the adc hit list
	 */
	public AdcList getHits() {
		return _adcHits;
	}
}
