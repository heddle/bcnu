package cnuphys.simanneal.advisors.enums;

import java.util.EnumMap;

import cnuphys.bCNU.component.EnumComboBox;

public enum Department {
COMM, ECON, ENGL, FAAH, HIST, LDSPAS, LUTER, MATH, MBCH, MCL, 
MUSIC, OEBIO, PCSE, PHIREL, POLSCI, PSYCH, SOCANT, THEADN;
	
	
	
	/**
	 * A map for the base names of the student classes
	 */
	public static EnumMap<Department, String> baseNames = new EnumMap<Department, String>(Department.class);
	static {
		for (Department dept : values()) {
			baseNames.put(dept, dept.name());
		}
	}

	/**
	 * A map for the names and synonyms of the student classes
	 */
	public static EnumMap<Department, String[]> names = new EnumMap<Department, String[]>(Department.class);
	
	/**
	 * A map of counts for making bar plots
	 */
	public static EnumMap<Department, Integer> counts = new EnumMap<Department, Integer>(Department.class);

	static {
		names.put(COMM, new String[] {"comm"});
		names.put(ECON, new String[] {"econ"});
		names.put(ENGL, new String[] {"english"});
		names.put(FAAH, new String[] {"fine art & art hist"});
		names.put(HIST, new String[] {"history"});
		names.put(LDSPAS, new String[] {"leadership & as"});
		names.put(LUTER, new String[] {"luter"});
		names.put(MATH, new String[] {"math"});
		names.put(MBCH, new String[] {"mbch"});
		names.put(MCL, new String[] {"mod & class lang"});
		names.put(MUSIC, new String[] {"music"});
		names.put(OEBIO, new String[] {"o&e bio"});
		names.put(PCSE, new String[] {"pcse"});
		names.put(PHIREL, new String[] {"phil & religion"});
		names.put(POLSCI, new String[] {"polysci"});
		names.put(PSYCH, new String[] {"psychology"});
		names.put(SOCANT, new String[] {"soc, soc & anth"});
		names.put(THEADN, new String[] {"theater & dance"});
	}
	
	
	
	/**
	 * Returns the enum value from the name.
	 * 
	 * @param name the name to match.
	 * @return the <code>StudentClass</code> that corresponds to the name. Returns
	 *         <code>null</code> if no match is found. Note it will check (case
	 *         insensitive) both the map and the <code>name()</code> result.
	 */
	public static Department getValue(String name) {
		if (name == null) {
			return null;
		}
		
		name = name.toLowerCase();

		for (Department val : values()) {
			// check the nice name
			if (name.equalsIgnoreCase(val.name())) {
				return val;
			}
			
			String[] syn = names.get(val);
			for (String s : syn) {
				if (name.equals(s.toLowerCase())) {
					return val;
				}
			}
		}
		return null;
	}
	
	/**
	 * Zero all the counts
	 */
	public static void clearCounts() {
		for (Department dept : values()) {
			counts.put(dept, 0);
		}
	}
	
	/**
	 * Increment a count
	 * @param dept the enum value whose count will be incremented
	 */
	public static void incrementCount(Department dept) {
		int val = counts.get(dept) + 1;
		counts.put(dept,val);
	}
	
	/**
	 * Obtain a combo box of choices.
	 * 
	 * @param defaultChoice the default selection
	 * @return a comboBox selector
	 */
	public static EnumComboBox getComboBox(Department defaultChoice) {
		return new EnumComboBox(baseNames, defaultChoice);
	}
	
	/**
	 * Get all the basenames in an array 
	 * @return the basenames
	 */
	public static String[] getBaseNames() {
		int len = values().length;
		String bn[] = new String[len];
		
		for (int i = 0; i < len; i++) {
			bn[i] = values()[i].name();
		}
		
		return bn;
	}
	
	/**
	 * Get all the counts in an array 
	 * @return the counts
	 */
	public static int[] getCounts() {
		int len = values().length;
		int count[] = new int[len];
		
		for (int i = 0; i < len; i++) {
			count[i] = counts.get(values()[i]);
		}
		
		return count;
	}
}
