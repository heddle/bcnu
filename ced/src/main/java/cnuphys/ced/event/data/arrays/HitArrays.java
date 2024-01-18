package cnuphys.ced.event.data.arrays;

import java.util.List;

/**
 * Base array for hits with x,y,z locations
 * Detectors that extend this are: FTOF, CTOF
 *
 * @param bankName
 */
public abstract class HitArrays extends BaseArrays {

	//color used for feedback
	protected final static String _fbColor = "$Orange Red$";

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
	public HitArrays(String bankName) {
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
			addFeedback(hitIndex, feedback);
		}
	}

	/**
	 * Add feedback for the given hit index
	 *
	 * @param hitIndex the hit index
	 * @param feedback the feedback list
	 */
	public void addFeedback(int hitIndex, List<String> feedback) {
		if (hitIndex >= 0) {

			if (hitIndex >= 0) {
				feedback.add(_fbColor + detectorIdentityString(hitIndex));
				feedback.add(_fbColor + "hit index " + hitIndex);
				feedback.add(_fbColor + "hit energy " + energy[hitIndex]);

				String locStr = String.format("hit location (%6.4f, %6.4f, %6.4f) cm", x[hitIndex], y[hitIndex], z[hitIndex]);

				feedback.add(_fbColor + locStr);

			} else {
				feedback.add(_fbColor + "no hit");
			}


		} else {
			feedback.add(_fbColor + "no hit");
		}
	}

	/**
	 * Return a detector specific identity string
	 * @param hitIndex the hit index
	 * @return a detector specific identity string
	 */
	protected abstract String detectorIdentityString(int hitIndex);

}
