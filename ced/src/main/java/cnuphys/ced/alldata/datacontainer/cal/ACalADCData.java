package cnuphys.ced.alldata.datacontainer.cal;

import java.awt.Color;
import java.util.ArrayList;

import cnuphys.ced.event.data.AdcColorScale;

public abstract class ACalADCData extends ACalData {

	/** 1-based strips */
	public ArrayList<Short> strip = new ArrayList<>();

	/** adc values */
	public ArrayList<Integer> adc = new ArrayList<>();

	/** time values */
	public ArrayList<Float> time = new ArrayList<>();


	/** max adc value */
	public int maxADC;
	
	@Override
	public void clear() {
		super.clear();
		strip.clear();
		adc.clear();
		time.clear();
	}

		/**
	 * Get the color for a given adc value
	 * @param adc the adc value
	 * @return the color
	 */
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
	
	//compute the max adc
	protected void computeMaxADC() {
		// get the max adc
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
		
	}



}
