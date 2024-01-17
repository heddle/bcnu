package cnuphys.ced.event.data.arrays;

import java.awt.Color;
import java.util.List;

import cnuphys.ced.event.data.AdcColorScale;

public final class LR_ADCArrays extends ADCArrays {

	//color used for feedback
	private static final String _fbColor = "$Orange Red$";

	/** the left-right array */
	private static final String leftRight[] = {"L", "R"};

	/** the order array */
	public byte order[];
	
	
	/**
	 * Create the data arrays
	 *
	 * @param bankName the bank name, either "CTOF::adc" or "FTOF::adc"
	 */
	public LR_ADCArrays(String bankName) {
		super(bankName);
		if (hasData()) {
			order = bank.getByte("order");
		}
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
	private int getComponentAverageADC(byte sector, byte layer, short component, short order) {
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
					String s = String.format("%s adc: %d time: %8.3f, ped: %d",
							leftRight[order[i]], ADC[i], time[i], ped[i]);

					feedback.add(_fbColor + s);
				}
			}
		} else {
			feedback.add(_fbColor + "no adc data");
		}
	}


}
