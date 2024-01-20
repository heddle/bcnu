package cnuphys.ced.event.data.arrays.adc;

import java.awt.Color;
import java.util.List;

import cnuphys.ced.event.data.AdcColorScale;
import cnuphys.ced.event.data.arrays.BaseArrays;

public class ADCArrays extends BaseArrays {

	/** the ADC array */
	public int ADC[];

	/** the ped array */
	public short ped[];

	/** the time array */
	public float time[];
	
	/** the order array */
	public byte order[];
	
	//max adc for current event
	protected int _maxADC = -1;

	//color used for feedback
	protected static final String _fbColor = "$cyan$";


	/**
	 * Create the ADC data arrays
	 *
	 * @param bankName the bank name, "____::adc" where ____ is the detector name
	 */
	public ADCArrays(String bankName) {
		super(bankName);

		if (hasData()) {
 			ped = bank.getShort("ped");
			time = bank.getFloat("time");
			ADC = bank.getInt("ADC");
			order = bank.getByte("order");
		}
	}
	
	/**
	 * Get the  adc arrays for a given bank name
	 * 
	 * @param bankName the bank name
	 * @return the arrays, either created or from cache
	 */
	public static ADCArrays getArrays(String bankName) {
		//try to get from cache
		BaseArrays arrays = dataWarehouse.getArrays(bankName);
		if (arrays != null) {
			return (ADCArrays) arrays;
		}
		
		ADCArrays adcArrays = new ADCArrays(bankName);
		dataWarehouse.putArrays(bankName, adcArrays);
		return adcArrays;
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
		return Color.lightGray;
	}
	
	/**
	 * Add to the feedback strings assuming the mouse is pointing to the given sector, layer, component.
	 * @param sector the 1-based sector
	 * @param layer the 1-based layer
	 * @param component the 1-based component
	 * @param feedback the List of feedback strings to add to.
	 */
	@Override
	public void addFeedback(byte sector, byte layer, short component, List<String> feedback) {
		if (hasData()) {
			for (int i = 0; i < this.sector.length; i++) {
				if ((this.sector[i] == sector) && (this.layer[i] == layer) && (this.component[i] == component)) {
					String s = String.format("%s adc: %d time: %8.3f, ped: %d",
							detectorName, ADC[i], time[i], ped[i]);

					feedback.add(_fbColor + s);
				}
			}
		} else {
			feedback.add(_fbColor + "no adc data");
		}
		
	}
	
	/**
	 * Add to the feedback strings assuming the mouse is pointing to the given
	 * sector, layer, component.
	 * 
	 * @param sector    the 1-based sector
	 * @param layer     the 1-based layer
	 * @param component the 1-based component
	 * @param order     the order
	 * @param feedback  the List of feedback strings to add to.
	 */
	public void addFeedback(byte sector, byte layer, short component, byte order, List<String> feedback) {
		if (hasData()) {
			for (int i = 0; i < this.sector.length; i++) {
				if ((this.sector[i] == sector) && (this.layer[i] == layer) && (this.component[i] == component)
						&& (this.order[i] == order)) {
					String s = String.format("%s adc: %d time: %8.3f, ped: %d", detectorName, ADC[i], time[i], ped[i]);

					feedback.add(_fbColor + s);
				}
			}
		} else {
			feedback.add(_fbColor + "no adc data");
		}

	}


}
