package cnuphys.advisors.graphics;

import java.awt.Rectangle;

public class ValueRect extends Rectangle {

	public double value;

	public String label;


	/**
	 * Used on bar plots
	 * @param value value of the bar
	 * @param label label on the bar
	 */
	public ValueRect(double value, String label) {
		super();
		this.value = value;
		this.label = label;
	}

	/**
	 * A mouse over string
	 * @return a description
	 */
	public String fbString() {
		return label + "  " + value;
	}

}
