package cnuphys.simanneal.advisors.enums;

import java.util.EnumMap;

public enum Major {
	ACCT, AMSTD, BIOCHM, BIOL, BUSN, CAM, CELLMB, CHEM, COMM, COMENG, COMSCI, CRIM, CYBER, ECON, EE, 
	ENGL, ENVSTD, FIN, FINART, FRENCH, GERMAN, HIST, INFSCI, KINES, MARKT, MATH, MGMT, MUSIC, NEURO, 
	OEBIO, PHILO, PHYS, POLSCI, PSYCH, SOCIOL, SOWK, SPAN, STDART, THEA, UNDEC;
	
	/**
	 * A map for the base names of the student classes
	 */
	public static EnumMap<Major, String> baseNames = new EnumMap<Major, String>(Major.class);
	static {
		for (Major major : values()) {
			baseNames.put(major, major.name());
		}
	}

	
	/**
	 * A map for the names and synonyms of the majors
	 */
	public static EnumMap<Major, String[]> names = new EnumMap<Major, String[]>(Major.class);
	
	/**
	 * A map of counts for making bar plots
	 */
	public static EnumMap<Major, Integer> counts = new EnumMap<Major, Integer>(Major.class);

	static {
		names.put(ACCT, new String[] {"accounting"});
		names.put(AMSTD, new String[] {"american studies"});
		names.put(BIOCHM, new String[] {"biochemistry"});
		names.put(BIOL, new String[] {"biology", "phys biology", "integrative biology"});
		names.put(BUSN, new String[] {"business-undeclared", "business administration"});
		names.put(CAM, new String[] {"computational & applied math"});
		names.put(CELLMB, new String[] {"cell, molecular & phys biology"});
		names.put(CHEM, new String[] {"chemistry"});
		names.put(COMM, new String[] {"communication studies"});
		names.put(COMENG, new String[] {"computer engineering"});
		names.put(COMSCI, new String[] {"computer science", "cs"});
		names.put(CRIM, new String[] {"criminology"});
		names.put(CYBER, new String[] {"cybersecurity"});
		names.put(ECON, new String[] {"economics", "economics-ba"});
		names.put(EE, new String[] {"electrical engineering"});
		names.put(ENGL, new String[] {"english"});
		names.put(ENVSTD, new String[] {"environmental studies"});
		names.put(FIN, new String[] {"finance"});
		names.put(FINART, new String[] {"fine arts"});
		
		names.put(FRENCH, new String[] {"french"});
		names.put(GERMAN, new String[] {"german"});
		names.put(HIST, new String[] {"history"});
		names.put(INFSCI, new String[] {"information science"});
		names.put(KINES, new String[] {"kinesiology"});
		names.put(MARKT, new String[] {"marketing"});
		names.put(MATH, new String[] {"mathematics"});
		names.put(MGMT, new String[] {"management"});
		names.put(MUSIC, new String[] {"music"});
		names.put(NEURO, new String[] {"neuroscience"});
		names.put(OEBIO, new String[] {"organismal & environ biology"});
		names.put(PHILO, new String[] {"philosophy"});
		names.put(PHYS, new String[] {"physics", "applied physics"});
		names.put(POLSCI, new String[] {"political science", "polysci"});
		names.put(PSYCH, new String[] {"psychology"});
		names.put(SOCIOL, new String[] {"sociology"});
		names.put(SOWK, new String[] {"social work"});
		names.put(SPAN, new String[] {"spanish"});
		names.put(STDART, new String[] {"studio art"});
		names.put(THEA, new String[] {"theater"});
		names.put(UNDEC, new String[] {"undeclared"});

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
		
		name = name.toLowerCase();

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
}
