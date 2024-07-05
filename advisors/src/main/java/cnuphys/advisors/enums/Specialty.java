package cnuphys.advisors.enums;

import java.util.EnumMap;

import cnuphys.advisors.Person;
import cnuphys.bCNU.component.EnumComboBox;

public enum Specialty {
	NONE, CCPT, PREMEDSCHOLAR, PLP;
	
	/**
	 * A map for the nice names of the specialties
	 */
	public static EnumMap<Specialty, String> names = new EnumMap<>(Specialty.class);
	
	static {
		names.put(PLP, "PLP (Pres. Leadership Program)");
		names.put(CCPT, "CCPT (Commumity Captains)");
		names.put(PREMEDSCHOLAR, "PSP (Premed Scholars)");
		names.put(NONE, "NONE");

	}
	
	/**
	 * A map for the bits for the specialties
	 */
	public static EnumMap<Specialty, Integer> bits = new EnumMap<>(Specialty.class);

	static {
		bits.put(PLP, Person.PLP);
		bits.put(CCPT, Person.CCPT);
		bits.put(PREMEDSCHOLAR, Person.PREMEDSCHOLAR);
		bits.put(NONE, 0);
	}
	
	
	/**
	 * Obtain a combo box of choices.
	 *
	 * @param defaultChoice the default selection
	 * @return a comboBox selector
	 */
	public static EnumComboBox getComboBox(Specialty defaultChoice) {
		return new EnumComboBox(names, defaultChoice);
	}
	
	public int getBit() {
		return bits.get(this);
	}


}
