package cnuphys.ced.event.data.arrays;

import java.util.List;

/**
 * Base array for hits with x,y,z locations
 * Detectors that extend this are: FTOF, CTOF
 * 
 * @param bankName
 */
public class XYZ_Hit_Arrays extends Base_Arrays {
	
	//color used for feedback
	private final static String _fbColor = "$Orange Red$";
	
	/** the energy array */
	public float energy[];
	
	/** the id array */
	public short id[];
	
	/** the status array */
	public short status[];
	
	/** the x array */
	public float x[];
	
	/** the y array */
	public float y[];
	
	/** the z array */
	public float z[];
	
	/**
	 * Base array for hits
	 * @param bankName
	 */
	public XYZ_Hit_Arrays(String bankName) {
		super(bankName);

		if (hasData()) {
			energy = bank.getFloat("energy");
			id = bank.getShort("id");
			status = bank.getShort("status");
			x = bank.getFloat("x");
			y = bank.getFloat("y");
			z = bank.getFloat("z");
		}
	}

	@Override
	public void addFeedback(byte sector, byte layer, short component, List<String> feedback) {
		if (hasData()) {
			int hitIndex = find(sector, layer, component);
			
			if (hitIndex >= 0) {
				feedback.add(_fbColor + "hit index " + hitIndex);
				feedback.add(_fbColor + "energy " + energy[hitIndex]);
				feedback.add(_fbColor + "hit x " + x[hitIndex] + " cm");
				feedback.add(_fbColor + "hit y " + y[hitIndex] + " cm");
				feedback.add(_fbColor + "hit z " + z[hitIndex] + " cm");

			} else {
				feedback.add(_fbColor + "no hit");
			}
					
					
		} else {
			feedback.add(_fbColor + "no hit");
		}
	}
	

}
