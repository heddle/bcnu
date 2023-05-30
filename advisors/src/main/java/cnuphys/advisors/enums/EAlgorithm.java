package cnuphys.advisors.enums;

import java.util.EnumMap;

public enum EAlgorithm {
	PutInFCAOptNumMaj, OptNumMajOnly, OptInFCAAndNumMaj;

	public static EnumMap<EAlgorithm, String> descriptions = new EnumMap(EAlgorithm.class);
	static {
		descriptions.put(PutInFCAOptNumMaj, "If a student has an FCA as an instructor, assign that FCA. Only after that, minimize the number of unique majors per FCA.");
		descriptions.put(OptNumMajOnly, "Do not auto-assign the student if in an FCA's class. Simply minimize the number of unique majors per FCA.");
		descriptions.put(OptInFCAAndNumMaj, "Optimize the number of students with their FCA as an instructor and the number of unique majors. This is a \"tradeoff\".");
	}

	public String description() {
		return descriptions.get(this);
	}
}
