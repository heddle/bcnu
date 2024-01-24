package cnuphys.ced.event.data;

import org.jlab.io.base.DataEvent;

import cnuphys.ced.event.data.lists.AdcECALHitList;
import cnuphys.ced.event.data.lists.ClusterList;

public class AllEC extends DetectorData {


//	bank name: [ECAL::adc] column name: [ADC] full name: [ECAL::adc.ADC] data type: int
//	bank name: [ECAL::adc] column name: [component] full name: [ECAL::adc.component] data type: short
//	bank name: [ECAL::adc] column name: [layer] full name: [ECAL::adc.layer] data type: byte
//	bank name: [ECAL::adc] column name: [order] full name: [ECAL::adc.order] data type: byte
//	bank name: [ECAL::adc] column name: [ped] full name: [ECAL::adc.ped] data type: short
//	bank name: [ECAL::adc] column name: [sector] full name: [ECAL::adc.sector] data type: byte
//	bank name: [ECAL::adc] column name: [time] full name: [ECAL::adc.time] data type: float
//	bank name: [ECAL::tdc] column name: [TDC] full name: [ECAL::tdc.TDC] data type: int
//	bank name: [ECAL::tdc] column name: [component] full name: [ECAL::tdc.component] data type: short
//	bank name: [ECAL::tdc] column name: [layer] full name: [ECAL::tdc.layer] data type: byte
//	bank name: [ECAL::tdc] column name: [order] full name: [ECAL::tdc.order] data type: byte
//	bank name: [ECAL::tdc] column name: [sector] full name: [ECAL::tdc.sector] data type: byte

	// tdc adc data
	private AdcECALHitList _adcHits = new AdcECALHitList("ECAL::adc");

	// clusters
	private ClusterList _clusters = new ClusterList("ECAL::clusters");

	private int _maxPCALAdc;
	private int _maxECALAdc;

	// singleton
	private static AllEC _instance;

	/**
	 * Public access to the singleton
	 *
	 * @return the FECAL singleton
	 */
	public static AllEC getInstance() {
		if (_instance == null) {
			_instance = new AllEC();
		}
		return _instance;
	}

	@Override
	public void newClasIoEvent(DataEvent event) {
		_clusters.update();
		_adcHits = new AdcECALHitList("ECAL::adc");
		computeADCMax();
	}

	// compte the max for PCAL and ECAL separately. Fugly.
	private void computeADCMax() {
		_maxPCALAdc = 0;
		_maxECALAdc = 0;

		if ((_adcHits != null) && !_adcHits.isEmpty()) {
			for (AdcECALHit hit : _adcHits) {
				if (hit != null) {
					if (hit.layer < 4) {
						_maxPCALAdc = Math.max(_maxPCALAdc, hit.averageADC());
					} else {
						_maxECALAdc = Math.max(_maxECALAdc, hit.averageADC());
					}
				}
			}
		}
	}

	/**
	 * Update the list. This is probably needed only during accumulation
	 *
	 * @return the updated list
	 */
	public AdcECALHitList updateAdcList() {
		_adcHits = new AdcECALHitList("ECAL::adc");
		return _adcHits;
	}

	/**
	 * Get the max adc for just the PCAL
	 *
	 * @return the max adc for just the PCAL
	 */
	public int getMaxPCALAdc() {
		return _maxPCALAdc;
	}

	/**
	 * Get the max adc for just the ECAL
	 *
	 * @return the max adc for just the ECAL
	 */
	public int getMaxECALAdc() {
		return _maxECALAdc;
	}

	/**
	 * Get the tdc and adc hit list
	 *
	 * @return the tdc adc hit list
	 */
	public AdcECALHitList getHits() {
		return _adcHits;
	}

	/**
	 * Get the reconstructed cluster list
	 *
	 * @return reconstructed list
	 */
	public ClusterList getClusters() {
		return _clusters;
	}

}
