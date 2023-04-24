package cnuphys.advisors.enums;

import java.util.EnumMap;

import cnuphys.bCNU.component.EnumComboBox;


public enum StudentClass {
	FRESHMAN, SOPHOMORE, JUNIOR, SENIOR, GRADUATE;

	/**
	 * A map for the names of the student classes
	 */
	public static EnumMap<StudentClass, String> names = new EnumMap<>(StudentClass.class);

	static {
		names.put(FRESHMAN, "Freshman");
		names.put(SOPHOMORE, "Sophomore");
		names.put(JUNIOR, "Junior");
		names.put(SENIOR, "Senior");
		names.put(GRADUATE, "GradStudent");
	}


	/**
	 * Returns the enum value from the name.
	 *
	 * @param name the name to match.
	 * @return the <code>StudentClass</code> that corresponds to the name. Returns
	 *         <code>null</code> if no match is found. Note it will check (case
	 *         insensitive) both the map and the <code>name()</code> result.
	 */
	public static StudentClass getValue(String name) {
		if (name == null) {
			return null;
		}

		for (StudentClass val : values()) {
			// check the nice name
			// check the base name
			if (name.equalsIgnoreCase(val.toString()) || name.equalsIgnoreCase(val.name())) {
				return val;
			}
		}
		return null;
	}

	/**
	 * Obtain a combo box of choices.
	 *
	 * @param defaultChoice the default selection
	 * @return a conboBox selector
	 */
	public static EnumComboBox getComboBox(StudentClass defaultChoice) {
		return new EnumComboBox(names, defaultChoice);
	}


}
