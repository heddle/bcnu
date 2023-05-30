package cnuphys.advisors.enums;

import java.util.EnumMap;

import cnuphys.advisors.frame.AdvisorAssign;
import cnuphys.bCNU.component.EnumComboBox;

public enum Major {
	NONE, ACCT, AMSTD, BIOCHM, BIOL, BUSN, CAM, CELLMB, CHEM, CLST, COMM, COMENG, COMSCI, CRIM, CYBER, ECON, EE,
	ENGL, ENVSTD, FIN, FINART, FRENCH, GERMAN, HIST, INFSCI, KINES, MARKT, MATH, MGMT, MUSIC, NEURO,
	OEBIO, PHILO, PHYS, POLSCI, PSYCH, SOCIOL, SOWK, SPAN, STDART, THEA, UNDEC;

	/**
	 * A map for the base names of the student classes
	 */
	private static EnumMap<Major, String> comboNames = new EnumMap<>(Major.class);
	static {
		for (Major major : values()) {
			comboNames.put(major, major.name());
		}
	}


	/**
	 * A map for the names and synonyms of the majors
	 */
	public static EnumMap<Major, String[]> names = new EnumMap<>(Major.class);

	/**
	 * A map of counts for making bar plots
	 */
	public static EnumMap<Major, Integer> counts = new EnumMap<>(Major.class);

	/**
	 * Map major to department
	 */
	public static EnumMap<Major, Department> departments = new EnumMap<>(Major.class);

	static {
		names.put(NONE, new String[] {"none"});

		names.put(ACCT, new String[] {"accounting"});
		names.put(AMSTD, new String[] {"american studies", "amst", "ldsp"});
		names.put(BIOCHM, new String[] {"biochemistry", "mbch", "bchm"});
		names.put(BIOL, new String[] {"biology", "phys biology", "integrative biology", "biol", "pmed"});
		names.put(BUSN, new String[] {"business-undeclared", "business administration", "lutr", "btmg"});
		names.put(CAM, new String[] {"computational & applied math"});
		names.put(CELLMB, new String[] {"cell, molecular & phys biology", "biol"});
		names.put(CHEM, new String[] {"chemistry"});
		names.put(CLST, new String[] {"classical studies", "mcll", "latn", "mlan", "grek", "hbrw", "ital", "chin", "arab"});
		names.put(COMM, new String[] {"communication studies"});
		names.put(COMENG, new String[] {"computer engineering", "cpen", "engr"});
		names.put(COMSCI, new String[] {"computer science", "cs", "cpsc", "pcse"});
		names.put(CRIM, new String[] {"criminology"});
		names.put(CYBER, new String[] {"cybersecurity"});
		names.put(ECON, new String[] {"economics", "economics-ba"});
		names.put(EE, new String[] {"electrical engineering", "eeng"});
		names.put(ENGL, new String[] {"english"});
		names.put(ENVSTD, new String[] {"environmental studies"});
		names.put(FIN, new String[] {"finance", "finc"});
		names.put(FINART, new String[] {"fine arts", "fnar", "dance", "danc", "faah"});

		names.put(FRENCH, new String[] {"french", "fren"});
		names.put(GERMAN, new String[] {"german", "germ"});
		names.put(HIST, new String[] {"history"});
		names.put(INFSCI, new String[] {"information science"});
		names.put(KINES, new String[] {"kinesiology"});
		names.put(MARKT, new String[] {"marketing", "mktg"});
		names.put(MATH, new String[] {"mathematics"});
		names.put(MGMT, new String[] {"management"});
		names.put(MUSIC, new String[] {"music", "musc", "cond", "pfar"});
		names.put(NEURO, new String[] {"neuroscience", "neur"});
		names.put(OEBIO, new String[] {"organismal & environ biology", "oenb"});
		names.put(PHILO, new String[] {"philosophy", "phil", "religion", "rstd"});
		names.put(PHYS, new String[] {"physics", "applied physics", "pcse"});
		names.put(POLSCI, new String[] {"political science", "polysci", "pols"});
		names.put(PSYCH, new String[] {"psychology", "psyc"});
		names.put(SOCIOL, new String[] {"sociology", "socl"});
		names.put(SOWK, new String[] {"social work"});
		names.put(SPAN, new String[] {"spanish"});
		names.put(STDART, new String[] {"studio art"});
		names.put(THEA, new String[] {"theater", "pfar"});
		names.put(UNDEC, new String[] {"undeclared"});

		departments.put(NONE, Department.NONE);
		departments.put(ACCT, Department.LUTR);
		departments.put(AMSTD, Department.LDSP);
		departments.put(BIOCHM, Department.MBCH);
		departments.put(BIOL, Department.OENB);
		departments.put(BUSN, Department.LUTR);
		departments.put(CAM, Department.MATH);
		departments.put(CELLMB, Department.MBCH);
		departments.put(CHEM, Department.MBCH);
		departments.put(CLST, Department.MCLL);
		departments.put(COMM, Department.COMM);
		departments.put(COMENG, Department.PCSE);
		departments.put(COMSCI, Department.PCSE);
		departments.put(CRIM, Department.SOCL);
		departments.put(CYBER, Department.PCSE);
		departments.put(ECON, Department.ECON);
		departments.put(EE, Department.PCSE);
		departments.put(ENGL, Department.ENGL);
		departments.put(ENVSTD, Department.OENB);
		departments.put(FIN, Department.LUTR);
		departments.put(FINART, Department.FAAH);

		departments.put(FRENCH, Department.MCLL);
		departments.put(GERMAN, Department.MCLL);
		departments.put(HIST, Department.HIST);
		departments.put(INFSCI, Department.PCSE);
		departments.put(KINES, Department.MBCH);
		departments.put(MARKT, Department.LUTR);
		departments.put(MATH, Department.MATH);
		departments.put(MGMT, Department.LUTR);
		departments.put(MUSIC, Department.PFAR);
		departments.put(NEURO, Department.INTERDIS);
		departments.put(OEBIO, Department.OENB);
		departments.put(PHILO, Department.PHIL);
		departments.put(PHYS, Department.PCSE);
		departments.put(POLSCI, Department.POLS);
		departments.put(PSYCH, Department.PSYC);
		departments.put(SOCIOL, Department.SOCL);
		departments.put(SOWK, Department.SOCL);
		departments.put(SPAN, Department.MCLL);
		departments.put(STDART, Department.FAAH);
		departments.put(THEA, Department.PFAR);
		departments.put(UNDEC, Department.NONE);

	}

	public boolean isInMajorFamily(Major major) {
		if (this == major) {
			return true;
		}

		switch (this) {

		case ACCT:
		case BUSN:
		case FIN:
		case MARKT:
		case MGMT:
			if (AdvisorAssign.useBusinessFamily()) {
				return ((major == ACCT) || (major == BUSN) || (major == FIN) || (major == MARKT) || (major == MGMT));
			}
			break;

		case BIOCHM:
		case CHEM:
			if (AdvisorAssign.useChemFamily()) {
				return ((major == BIOCHM) || (major == CHEM));
			}
			break;

		case BIOL:
		case CELLMB:
		case KINES:
		case OEBIO:
			if (AdvisorAssign.useBioFamily()) {
				return ((major == BIOL) || (major == CELLMB) || (major == KINES) || (major == OEBIO));
			}
			break;


		default:
			return false;
		}

		return false;
	}

	/**
	 * Get the department from the major
	 * @return the department from the major
	 */
	public Department getDepartment() {
		return departments.get(this);
	}


	/**
	 * Returns the enum value from the name.
	 *
	 * @param name the name to match.
	 * @return the <code>StudentClass</code> that corresponds to the name. Returns
	 *         <code>null</code> if no match is found. Note it will check (case
	 *         insensitive) both the map and the <code>name()</code> result.
	 */
	public static Major getValue(String name) {
		if (name == null) {
			return null;
		}

		name = name.toLowerCase().trim();

		for (Major val : values()) {
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
		for (Major major : values()) {
			counts.put(major, 0);
		}
	}

	/**
	 * Increment a count
	 * @param major the enum value whose count will be incremented
	 */
	public static void incrementCount(Major major) {
		int val = counts.get(major) + 1;
		counts.put(major,val);
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

	/**
	 * Obtain a combo box of choices.
	 *
	 * @param defaultChoice the default selection
	 * @return a comboBox selector
	 */
	public static EnumComboBox getComboBox(Major defaultChoice) {
		return new EnumComboBox(comboNames, defaultChoice);
	}

}
