package cnuphys.ced.event.data.arrays.tdc;

import java.util.List;

import cnuphys.ced.event.data.arrays.BaseArrays;

public class TDCArrays extends BaseArrays {

	/** the TDC array */
	public int TDC[];

	/** the order array */
	public byte order[];

	//color used for feedback
	protected static final String _fbColor = "$cyan$";


	/**
	 * Create the TDC data arrays
	 *
	 * @param bankName the bank name, "____::tdc" where ____ is the detector name
	 */
	public TDCArrays(String bankName) {
		super(bankName);

		if (hasData()) {
 			TDC = bank.getInt("TDC");
			order = bank.getByte("order");
		}
	}

	/**
	 * Get the tdc arrays for a given bank name
	 *
	 * @param bankName the bank name
	 * @return the arrays, either created or from cache
	 */
	public static TDCArrays getArrays(String bankName) {
		//try to get from cache
		BaseArrays arrays = dataWarehouse.getArrays(bankName);
		if (arrays != null) {
			return (TDCArrays) arrays;
		}

		TDCArrays tdcArrays = new TDCArrays(bankName);
		dataWarehouse.putArrays(bankName, tdcArrays);
		return tdcArrays;
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
					String s = String.format("%s tdc: %d", detectorName, TDC[i]);

					feedback.add(_fbColor + s);
				}
			}
		} else {
			feedback.add(_fbColor + "no adc data");
		}

	}

	/**
	 * Add to the feedback strings assuming the mouse is pointing to the given sector, layer, component.
	 * @param sector the 1-based sector
	 * @param layer the 1-based layer
	 * @param component the 1-based component
	 * @oaram order the order
	 * @param feedback the List of feedback strings to add to.
	 */
	public void addFeedback(byte sector, byte layer, short component, byte order, List<String> feedback) {
		if (hasData()) {
			for (int i = 0; i < this.sector.length; i++) {
				if ((this.sector[i] == sector) && (this.layer[i] == layer) && (this.component[i] == component)
						&& (this.order[i] == order)) {
					String s = String.format("%s tdc: %d", detectorName, TDC[i]);

					feedback.add(_fbColor + s);
				}
			}
		} else {
			feedback.add(_fbColor + "no adc data");
		}

	}



}
