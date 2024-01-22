package cnuphys.ced.event.data.arrays.adc;

import java.util.List;

import cnuphys.ced.event.data.arrays.BaseArrays;

public class CC_ADCArrays extends ADCArrays {

	//the HTCC adc array
	private CC_ADCArrays(String bankName) {
		super(bankName);
	}


	/**
	 * Get the  adc arrays for a given bank name
	 *
	 * @param bankName the bank name
	 * @return the arrays, either created or from cache
	 */
	public static CC_ADCArrays getArrays(String bankName) {
		//try to get from cache
		BaseArrays arrays = dataWarehouse.getArrays(bankName);
		if (arrays != null) {
			return (CC_ADCArrays) arrays;
		}

		CC_ADCArrays adcArrays = new CC_ADCArrays(bankName);
		dataWarehouse.putArrays(bankName, adcArrays);
		return adcArrays;
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


}
