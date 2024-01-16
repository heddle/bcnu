package cnuphys.ced.event.data.arrays;

import java.awt.Color;
import java.util.List;

import cnuphys.ced.event.data.AdcColorScale;

public final class TOF_ADC_Arrays extends ADC_Arrays {

	//color used for feedback
	private static final String _fbColor = "$Orange Red$";
	
	/** the left-right array */
	private static final String leftRight[] = {"L", "R"};

	//max adc for current event
	private int _maxADC = -1;
	
	/**
	 * Create the data arrays
	 * 
	 * @param bankName the bank name, either "CTOF::adc" or "FTOF::adc"
	 */
	public TOF_ADC_Arrays(String bankName) {
		super(bankName);
	}

	@Override
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
	
	//compute and cache the max ADC
	private void computeMaxADC() {
		if (_maxADC < 0) {
			_maxADC = 0;
			for (int i = 0; i < ADC.length; i++) {
				if (ADC[i] > _maxADC) {
					_maxADC = ADC[i];
				}
			}
		}
		
	}
	
	//gets the average ADC for a given sector, layer, component
	private int getComponentAverageADC(byte sector, byte layer, short component) {
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
	
	@Override
	public void addFeedback(byte sector, byte layer, short component, List<String> feedback) {
		if (hasData()) {
			for (int i = 0; i < this.sector.length; i++) {
				if ((this.sector[i] == sector) && (this.layer[i] == layer) && (this.component[i] == component)) {
					String s = String.format("%s adc: %d, time: %8.3f, ped: %d", 
							leftRight[order[i]], ADC[i], time[i], ped[i]);
					
					feedback.add(_fbColor + s);
				}
			}
		} else {
			feedback.add(_fbColor + "no adc data");
		}
	}

	
}
