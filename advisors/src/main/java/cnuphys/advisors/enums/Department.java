package cnuphys.advisors.enums;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;

import cnuphys.advisors.Advisor;
import cnuphys.advisors.model.DataManager;
import cnuphys.bCNU.component.EnumComboBox;

public enum Department {
COMM, ECON, ENGL, FAAH, HIST, LDSP, LUTR, MATH, MBCH, MCLL,
OENB, PCSE, PFAR, PHIL, POLS, PSYC, SOCL, NONE, INTERDIS;



	/**
	 * A map for the base names of the departments
	 */
	public static EnumMap<Department, String> baseNames = new EnumMap<>(Department.class);
	static {
		for (Department dept : values()) {
			baseNames.put(dept, dept.name());
		}
	}

	/**
	 * A map for the names and synonyms of the student classes
	 */
	public static EnumMap<Department, String[]> names = new EnumMap<>(Department.class);

	/**
	 * A map of counts for making bar plots
	 */
	public static EnumMap<Department, Integer> counts = new EnumMap<>(Department.class);

	static {
		names.put(COMM, new String[] {"comm", "communication"});
		names.put(ECON, new String[] {"econ", "economics"});
		names.put(ENGL, new String[] {"english"});
		names.put(FAAH, new String[] {"fine art & art hist", "fine art & art history"});
		names.put(HIST, new String[] {"history"});
		names.put(LDSP, new String[] {"leadership & as", "leadership & american studies"});
		names.put(LUTR, new String[] {"luter"});
		names.put(MATH, new String[] {"math", "mathematics"});
		names.put(MBCH, new String[] {"mbch", "molecular biology and chemistry"});
		names.put(MCLL, new String[] {"mod & class lang", "modern & classical languages & literatures"});
		names.put(OENB, new String[] {"o&e bio", "organismal and environmental biology"});
		names.put(PCSE, new String[] {"pcse", "physics, computer science & engineering"});
		names.put(PHIL, new String[] {"phil & religion", "philosophy & religion"});
		names.put(PFAR, new String[] {"musc, music", "thea", "theater & dance"});
		names.put(POLS, new String[] {"polysci", "political science"});
		names.put(PSYC, new String[] {"psychology", "psych"});
		names.put(SOCL, new String[] {"soc, soc & anth", "sociology, social work & anthropology"});
		names.put(INTERDIS, new String[] {"neuroscience"});
		names.put(NONE, new String[] {"undeclared"});
	}

	/**
	 * Get a list of majors for this department
	 * @return a list of majors
	 */
	public List<Major> getMajors() {
		List<Major> majors = new ArrayList<>();

		for (Major major : Major.values()) {
			if (major.getDepartment() == this) {
				majors.add(major);
			}
		}

		return majors;
	}

	/**
	 * Get the number of catalogs needed by this department
	 * @return the number of catalogs needed
	 */
	public int catalogsNeeded() {
	    List<Advisor> advisors = DataManager.getAdvisorsForDepartment(this);
	    int count = 0;
	    for (Advisor advisor : advisors) {
	    	count += advisor.adviseeCount();
	    }
	    return count;
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
