package cnuphys.ced.event.data.lists;

import java.awt.Color;
import java.util.Collections;
import java.util.Vector;

import cnuphys.ced.alldata.ColumnData;
import cnuphys.ced.event.data.AdcColorScale;
import cnuphys.ced.event.data.TdcAdcHit;

public class TdcAdcHitList extends Vector<TdcAdcHit> {

	// used to log erors
	private String _error;

	// for color scaling
	private int _maxADC;

	public TdcAdcHitList(String tdcBankName, String adcBankName) {
		super();

		/*
		 * 1) create basic list from the tdc bank and left tdc 2) sort 3) merge in right
		 * tdc 4) merge in left adc 5) merge in right adc
		 */

		// step 1: basic list from left tdc
		byte[] sector = ColumnData.getByteArray(tdcBankName + ".sector");
		if (sector != null) {

			int length = sector.length;

			byte[] layer = ColumnData.getByteArray(tdcBankName + ".layer");
			short[] component = ColumnData.getShortArray(tdcBankName + ".component");
			byte[] order = ColumnData.getByteArray(tdcBankName + ".order");
			int[] TDC = ColumnData.getIntArray(tdcBankName + ".TDC");


			// Step 1 build basic list
			for (int index = 0; index < length; index++) {
				if (order[index] != 3) { // left tdc
					modifyInsert(sector[index], layer[index], component[index], TDC[index], -1, -1, -1, -1, -1,
							Float.NaN, Float.NaN, order[index]);
				}
			}

			// step 2: sort
			if (size() > 1) {
				Collections.sort(this);
			}

			// step 3 merge in right tdc
			for (int index = 0; index < length; index++) {
				if (order[index] == 3) { // right
					modifyInsert(sector[index], layer[index], component[index], -1, TDC[index], -1, -1, -1, -1,
							Float.NaN, Float.NaN, order[index]);
				}
			}
		} // end sector not null tdc

		// on to the adcs
		sector = ColumnData.getByteArray(adcBankName + ".sector");

		if (sector != null) {

			int length = sector.length;
			byte[] layer = ColumnData.getByteArray(adcBankName + ".layer");
			short[] component = ColumnData.getShortArray(adcBankName + ".component");
			byte[] order = ColumnData.getByteArray(adcBankName + ".order");
			int[] ADC = ColumnData.getIntArray(adcBankName + ".ADC");
			short[] ped = ColumnData.getShortArray(adcBankName + ".ped");
			float[] time = ColumnData.getFloatArray(adcBankName + ".time");

			// Step 4 merge left adc, ped, and time
			for (int index = 0; index < length; index++) {
				if (order[index] == 0) { // left adc
					modifyInsert(sector[index], layer[index], component[index], -1, -1, ADC[index], -1, ped[index], -1,
							time[index], Float.NaN, order[index]);
				}
			}

			// step 5 merge in right adc, ped, and time
			for (int index = 0; index < length; index++) {
				if (order[index] == 1) { // right
					modifyInsert(sector[index], layer[index], component[index], -1, -1, -1, ADC[index], -1, ped[index],
							Float.NaN, time[index], order[index]);
				}
			}

			_maxADC = -1;
			for (TdcAdcHit hit : this) {
				_maxADC = Math.max(_maxADC, hit.averageADC());
			}
		}


	}

	/**
	 * Get the max average adc
	 *
	 * @return the max average adc
	 */
	public int maxADC() {
		return _maxADC;
	}

	public void modifyInsert(byte sector, byte layer, short component, int tdcL, int tdcR, int adcL, int adcR, int pedL,
			int pedR, float timeL, float timeR, byte order) {
		TdcAdcHit hit = new TdcAdcHit(sector, layer, component);
		int index = Collections.binarySearch(this, hit);
		if (index >= 0) {
			hit = this.elementAt(index);
		} else {
			index = -(index + 1); // now the insertion point.
			add(index, hit);
		}

		if (tdcL >= 0) {
			hit.tdcL = tdcL;
		}
		if (tdcR >= 0) {
			hit.tdcR = tdcR;
		}
		if (adcL >= 0) {
			hit.adcL = adcL;
		}
		if (adcR >= 0) {
			hit.adcR = adcR;
		}
		if (pedL >= 0) {
			hit.pedL = (short) pedL;
		}
		if (pedR >= 0) {
			hit.pedR = (short) pedR;
		}
		if (!Float.isNaN(timeL)) {
			hit.timeL = timeL;
		}
		if (!Float.isNaN(timeR)) {
			hit.timeR = timeR;
		}

		if (order >= 0) {
			hit.order = order;
		}

	}


	/**
	 * Find the index of a hit
	 *
	 * @param sector    the 1-based sector
	 * @param layer     the 1-based layer
	 * @param component the 1-based component
	 * @return the index, or -1 if not found
	 */
	public int getIndex(byte sector, byte layer, short component) {
		if (isEmpty()) {
			return -1;
		}
		TdcAdcHit hit = new TdcAdcHit(sector, layer, component);
		int index = Collections.binarySearch(this, hit);
		if (index >= 0) {
			return index;
		} else { // not found
			return -1;
		}
	}

	/**
	 * Find the hit
	 *
	 * @param sector    the 1-based sector
	 * @param layer     the 1-based layer 1..36
	 * @param component the 1-based component
	 * @return the hit, or null if not found
	 */
	public TdcAdcHit get(byte sector, byte layer, short component) {
		int index = getIndex(sector, layer, component);
		return (index < 0) ? null : elementAt(index);
	}

	/**
	 * Find the hit
	 *
	 * @param sector    the 1-based sector
	 * @param layer     the 1-based layer 1..36
	 * @param component the 1-based component
	 * @return the hit, or null if not found
	 */
	public TdcAdcHit get(int sector, int layer, int component) {
		return get((byte) sector, (byte) layer, (short) component);
	}

	/**
	 * Get a color with apha based of relative adc
	 *
	 * @param hit the hit
	 * @return a fill color for adc hits
	 */
	public Color adcColor(TdcAdcHit hit) {
		return adcColor(hit, _maxADC);
	}

	/**
	 * Get a color with apha based of relative adc
	 *
	 * @param hit    the hit
	 * @param maxAdc the max adc value
	 * @return a fill color for adc hits
	 */
	public Color adcColor(TdcAdcHit hit, int maxAdc) {
		if (hit == null) {
			return Color.white;
		}

		int avgADC = hit.averageADC();

		double maxadc = Math.max(1.0, maxAdc);

		double fract = (avgADC) / maxadc;
		fract = Math.max(0, Math.min(1.0, fract));

		int alpha = 128 + (int) (127 * fract);
		alpha = Math.min(255, alpha);

		return AdcColorScale.getInstance().getAlphaColor(fract, alpha);
	}
}
