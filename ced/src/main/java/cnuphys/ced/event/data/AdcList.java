package cnuphys.ced.event.data;

import java.awt.Color;
import java.util.Collections;
import java.util.Hashtable;
import java.util.Vector;

import cnuphys.ced.alldata.ColumnData;
import cnuphys.ced.clasio.ClasIoEventManager;
import cnuphys.lund.X11Colors;

public class AdcList extends Vector<AdcHit> {
	// for color scaling
	private int _maxADC;

	// for 0 adc values
	private static final Color ASDZERO1 = new Color(0, 0, 0, 64);
	private static final Color ASDZERO2 = X11Colors.getX11Color("Light Sky Blue", 80);

	public AdcList(String adcBankName) {

		Hashtable<String, AdcHit> hitHash = new Hashtable<>();

		byte[] sector = ColumnData.getByteArray(adcBankName + ".sector");
		int count = (sector == null) ? 0 : sector.length;

		if (count > 0) {
			byte[] layer = ColumnData.getByteArray(adcBankName + ".layer");
			short[] component = ColumnData.getShortArray(adcBankName + ".component");
			byte[] order = ColumnData.getByteArray(adcBankName + ".order");
			int[] ADC = ColumnData.getIntArray(adcBankName + ".ADC");
			short[] ped = ColumnData.getShortArray(adcBankName + ".ped");
			float[] time = ColumnData.getFloatArray(adcBankName + ".time");

			for (int i = 0; i < count; i++) {
				String hash = hash(sector[i], layer[i], component[i]);
				AdcHit hit = hitHash.get(hash);

				if (hit == null) {
					hit = new AdcHit(sector[i], layer[i], component[i]);

					hit.adc = ADC[i];
					hit.ped = ped[i];
					hit.time = time[i];
					hit.order = order[i];

					hitHash.put(hash, hit);
					add(hit);
				}
				else {
					String s1 = String.format("duplicate [%s] event num %d ", adcBankName, ClasIoEventManager.getInstance().getSequentialEventNumber());
					String s2 = String.format(" [sector: %d, layer: %d, component: %d]", hit.sector, hit.layer, hit.component);
					System.err.println(s1 + s2);
				}
			}

			if (size() > 1) {
				Collections.sort(this);
			}

		}

		_maxADC = -1;
		for (AdcHit hit : this) {
			_maxADC = Math.max(_maxADC, hit.adc);
		}

	}

	// string for hashtable
	private String hash(byte sector, byte layer, short component) {
		return "" + sector + "|" + layer + component;
	}

	/**
	 * Get the max average adc
	 *
	 * @return the max average adc
	 */
	public int maxADC() {
		return _maxADC;
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
		AdcHit hit = new AdcHit(sector, layer, component);
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
	public AdcHit get(byte sector, byte layer, short component) {
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
	public AdcHit get(int sector, int layer, int component) {
		return get((byte) sector, (byte) layer, (short) component);
	}

	/**
	 * Get a monochrome color with alpha based of relative adc
	 *
	 * @param hit    the hit
	 * @param maxAdc the max adc value
	 * @return a fill color for adc hits
	 */
	public Color adcMonochromeColor(TdcAdcTOFHit hit, int maxAdc) {
		if (hit == null) {
			return Color.white;
		}

		int avgADC = hit.averageADC();

		if (avgADC < 1) {
			return ASDZERO2;
		}

		double maxadc = Math.max(1.0, maxAdc);

		double fract = (avgADC) / maxadc;
		fract = Math.max(0, Math.min(1.0, fract));

		int alpha = 128 + (int) (127 * fract);
		alpha = Math.min(255, alpha);

		return AdcColorScale.getInstance().getMonochromeAlphaColor(fract, alpha);
	}

	/**
	 * Get a color with alpha based of relative adc
	 *
	 * @param hit the hit
	 * @return a fill color for adc hits
	 */
	public Color adcColor(AdcHit hit) {
		if (hit == null) {
			return Color.white;
		}


		if (hit.adc < 1) {
			return ASDZERO1;
		}

		double maxadc = Math.max(1.0, _maxADC);

		double fract = (hit.adc) / maxadc;
		fract = Math.max(0, Math.min(1.0, fract));

		int alpha = 128 + (int) (127 * fract);
		alpha = Math.min(255, alpha);

		return AdcColorScale.getInstance().getAlphaColor(fract, alpha);
	}

}
