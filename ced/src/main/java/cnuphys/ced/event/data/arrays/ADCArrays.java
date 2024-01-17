package cnuphys.ced.event.data.arrays;

import java.awt.Color;

import cnuphys.ced.event.data.AdcColorScale;

public abstract class ADCArrays extends BaseArrays {

	/** the ADC array */
	public int ADC[];

	/** the ped array */
	public short ped[];

	/** the time array */
	public float time[];
	
	//max adc for current event
	protected int _maxADC = -1;


	/**
	 * Create the data arrays
	 *
	 * @param bankName the bank name, "____::adc" where ____ is the detector name
	 */
	public ADCArrays(String bankName) {
		super(bankName);

		if (hasData()) {
 			ped = bank.getShort("ped");
			time = bank.getFloat("time");
			ADC = bank.getInt("ADC");
		}

	}
	
	//compute and cache the max ADC
	protected void computeMaxADC() {
		if (_maxADC < 0) {
			_maxADC = 0;
			for (int val : ADC) {
				if (val > _maxADC) {
					_maxADC = val;
				}
			}
		}

	}

	//gets the average ADC for a given sector, layer, component
	protected int getComponentAverageADC(byte sector, byte layer, short component) {
		int count = 0;
		int sum = 0;
		for (int i = 0; i < this.sector.length; i++) {
			if ((this.sector[i] == sector) && (this.layer[i] == layer) && (this.component[i] == component)) {
				sum += ADC[i];
				count++;
			}
		}

		return (count > 0) ? sum / count : 0;
	}


	/**
	 * Get the color for the given sector, layer, and component
	 * @param sector the 1-based sector
	 * @param layer the 1-based layer
	 * @param component the 1-based component
	 * @return the color, or null if no data
	 */
	public Color getColor(byte sector, byte layer, short component) {
		computeMaxADC();

		int adc = getComponentAverageADC(sector, layer, component);
		if (adc > 0) {
			double fract = ((double) adc) / _maxADC;
			fract = Math.max(0, Math.min(1.0, fract));
			return AdcColorScale.getInstance().getAlphaColor(fract, 255);
		}
		return null;
	}

}
