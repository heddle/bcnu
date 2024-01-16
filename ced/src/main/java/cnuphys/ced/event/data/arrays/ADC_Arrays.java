package cnuphys.ced.event.data.arrays;

import java.awt.Color;

public abstract class ADC_Arrays extends Base_Arrays {
	
	/** the order array */
	public byte order[];
	
	/** the ADC array */
	public int ADC[];
	
	/** the ped array */
	public short ped[];
	
	/** the time array */
	public float time[];


	/**
	 * Create the data arrays
	 * 
	 * @param bankName the bank name, "____::adc" where ____ is the detector name
	 */
	public ADC_Arrays(String bankName) {
		super(bankName);
		
		if (hasData()) {
 			order = bank.getByte("order");
			ped = bank.getShort("ped");
			time = bank.getFloat("time");
			ADC = bank.getInt("ADC");
		}

	}
	
	/**
	 * Get the color for the given sector, layer, component using the common
	 * ADC color map
	 * @param sector the 1-based sector
	 * @param layer the 1-based layer
	 * @param component the 1-based component
	 * @return the color
	 */
	public abstract Color getColor(byte sector, byte layer, short component);

}
