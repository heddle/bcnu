package cnuphys.advisors.enums;

import java.util.EnumMap;

import cnuphys.bCNU.component.EnumComboBox;

public enum Semester {
	None, Fall2022, Fall2023, Fall2024, Fall2025;

	/**
	 * A map for the base names of the student classes
	 */
	public static EnumMap<Semester, String> baseNames = new EnumMap<>(Semester.class);
	static {
		for (Semester semester : values()) {
			baseNames.put(semester, semester.name());
		}
	}


	/**
	 * Obtain a combo box of choices.
	 *
	 * @param defaultChoice the default selection
	 * @return a comboBox selector
	 */
	public static EnumComboBox getComboBox(Semester defaultChoice) {
		return new EnumComboBox(baseNames, defaultChoice);
	}

}
