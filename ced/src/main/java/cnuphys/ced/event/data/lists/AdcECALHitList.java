package cnuphys.ced.event.data.lists;

import java.awt.Color;
import java.util.Collections;
import java.util.Hashtable;
import java.util.Vector;

import cnuphys.ced.alldata.ColumnData;
import cnuphys.ced.event.data.AdcColorScale;
import cnuphys.ced.event.data.AdcECALHit;
import cnuphys.lund.X11Colors;

public class AdcECALHitList extends Vector<AdcECALHit> {

	// for color scaling
	private int _maxADC;

	//for tracking duplicates
	private Hashtable<String, AdcECALHit> occHash = new Hashtable<>();

	//for 0 adc values
	private static final Color ASDZERO1 = new Color(0, 0, 0, 64);
	private static final Color ASDZERO2 = X11Colors.getX11Color("Light Sky Blue", 80);

	public String adcBankName;

	public AdcECALHitList(String adcBankName) {
		super();

		this.adcBankName = adcBankName;

		byte[] sector = ColumnData.getByteArray(adcBankName + ".sector");
		if (sector != null) {


			int length = sector.length;
			byte[] layer = ColumnData.getByteArray(adcBankName + ".layer");
			short[] component = ColumnData.getShortArray(adcBankName + ".component");
			byte[] order = ColumnData.getByteArray(adcBankName + ".order");
			int[] ADC = ColumnData.getIntArray(adcBankName + ".ADC");
			short[] ped = ColumnData.getShortArray(adcBankName + ".ped");
			float[] time = ColumnData.getFloatArray(adcBankName + ".time");

			for (int index = 0; index < length; index++) {
				AdcECALHit hit = new AdcECALHit(sector[index], layer[index], component[index], order[index], ADC[index], time[index], ped[index]);

			    //duplicate
				String hash = hit.hash();
				AdcECALHit oldHit = occHash.get(hash);
				if (oldHit != null) { //duplicate
					oldHit.occurances += 1;
					if (hit.adc > oldHit.adc) {
						oldHit.swapValues(hit);
					}
				}
				else { //unique
					occHash.put(hash,  hit);
					add(hit);
				}

			}
			
			Collections.sort(this);

			//get max adc
			_maxADC = -1;
			for (AdcECALHit hit : this) {
				_maxADC = Math.max(_maxADC, hit.adc);
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
		AdcECALHit hit = new AdcECALHit(sector, layer, component);
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
	public AdcECALHit get(byte sector, byte layer, short component) {
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
	public AdcECALHit get(int sector, int layer, int component) {
		return get((byte) sector, (byte) layer, (short) component);
	}

	/**
	 * Get a gray scale with alpha based of relative adc
	 *
	 * @param hit the hit
	 * @return a fill color for adc hits
	 */
	public Color adcMonochromeColor(AdcECALHit hit) {
		return adcMonochromeColor(hit, _maxADC);
	}


	/**
	 * Get a color with alpha based of relative adc
	 *
	 * @param hit the hit
	 * @return a fill color for adc hits
	 */
	public Color adcColor(AdcECALHit hit) {
		return adcColor(hit, _maxADC);
	}


	/**
	 * Get a monochrome color with alpha based of relative adc
	 *
	 * @param hit    the hit
	 * @param maxAdc the max adc value
	 * @return a fill color for adc hits
	 */
	public Color adcMonochromeColor(AdcECALHit hit, int maxAdc) {
		if (hit == null) {
			return Color.white;
		}

		int avgADC = hit.averageADC();

		if(avgADC < 1) {
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
	 * @param hit    the hit
	 * @param maxAdc the max adc value
	 * @return a fill color for adc hits
	 */
	public Color adcColor(AdcECALHit hit, int maxAdc) {
		if (hit == null) {
			return Color.white;
		}

		int avgADC = hit.averageADC();

		if(avgADC < 1) {
			return ASDZERO1;
		}

		double maxadc = Math.max(1.0, maxAdc);

		double fract = (avgADC) / maxadc;
		fract = Math.max(0, Math.min(1.0, fract));

		int alpha = 128 + (int) (127 * fract);
		alpha = Math.min(255, alpha);

		return AdcColorScale.getInstance().getAlphaColor(fract, alpha);
	}
}
