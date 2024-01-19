package cnuphys.ced.event.data.arrays;

import java.awt.Color;
import java.util.List;

import cnuphys.ced.event.data.AdcColorScale;

public final class LR_ADCArrays extends ADCArrays {

	/** the left-right array */
	private static final String leftRight[] = {"L", "R"};
	
	/**
	 * Create the data arrays
	 *
	 * @param bankName the bank name, either "CTOF::adc" or "FTOF::adc"
	 */
	protected LR_ADCArrays(String bankName) {
		super(bankName);
	}
	
	/**
	 * Get the left-right adc arrays for a given bank name
	 * 
	 * @param bankName the bank name, either "CTOF::adc" or "FTOF::adc"
	 * @return the arrays, either created or from cache
	 */
	public static LR_ADCArrays getArrays(String bankName) {
		//try to get from cache
		BaseArrays arrays = dataWarehouse.getArrays(bankName);
		if (arrays != null) {
			return (LR_ADCArrays) arrays;
		}
		
		LR_ADCArrays lrArrays = new LR_ADCArrays(bankName);
		dataWarehouse.putArrays(bankName, lrArrays);
		return lrArrays;
	}
	
	/**
	 * Get the adc color for a given sector, layer, component, and order
	 * @param sector the 1-based sector	
	 * @param layer the 1-based layer
	 * @param component the 1-based component
	 * @param order the order, 0 or 1 for left/right
	 * @return the color, or null if no data
	 */
	public Color getColor(byte sector, byte layer, short component, byte order) {
		computeMaxADC();

		int adc = getComponentAverageADC(sector, layer, component, order);
		if (adc > 0) {
			double fract = ((double) adc) / _maxADC;
			fract = Math.max(0, Math.min(1.0, fract));
			return AdcColorScale.getInstance().getAlphaColor(fract, 255);
		}
		return null;
	}

	//gets the average ADC for a given sector, layer, component, and order
	private int getComponentAverageADC(byte sector, byte layer, short component, byte order) {
		int count = 0;
		int sum = 0;
		for (int i = 0; i < this.sector.length; i++) {
			if ((this.sector[i] == sector) && (this.layer[i] == layer) 
					&& (this.component[i] == component) && (this.order[i] == order)) {
				sum += ADC[i];
				count++;
			}
		}

		return (count > 0) ? sum / count : 0;
	}


	@Override
	public void addFeedback(byte sector, byte layer, short component, List<String> feedback) {
		if (hasData()) {
			for (int i = 0; i < this.sector.length; i++) {
				if ((this.sector[i] == sector) && (this.layer[i] == layer) && (this.component[i] == component)) {
					String s = String.format("%s %s adc: %d time: %8.3f, ped: %d",
							detectorName, leftRight[order[i]], ADC[i], time[i], ped[i]);

					feedback.add(_fbColor + s);
				}
			}
		} else {
			feedback.add(_fbColor + "no adc data");
		}
	}


}
