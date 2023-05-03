package cnuphys.advisors.model;

import cnuphys.advisors.Advisor;
import cnuphys.advisors.io.ITabled;

public class LearningCommunityCourse implements ITabled {
	
	
	/** the learning community number */
	public int lcNum;

	/** the learning community title */
	public String learningCommunity;
	
	/** course CRN */
	public String crn;
	
	/** subject e.g. BIOL */
	public String subject;

	/** course, e.g. 109L */
	public String course;

	/** llc if any */
	public String llc;
	
	/** notes */
	public String notes;

	/** the instructor */
	public Advisor instructor;
	
	/** number of students enrolled */
	public int count;
	
	/** is an ilc */
	public boolean ilc;
	
	//LearningCommunityCourse(lcNum, lcTitle, crn, subject, course, llc, notes)
	
	/**
	 * @param lcNum
	 * @param learningCommunity
	 * @param crn
	 * @param subject
	 * @param course
	 * @param llc
	 * @param notes
	 */
	public LearningCommunityCourse(int lcNum, String learningCommunity, String crn, String subject, String course, String llc, String notes) {
		super();
		this.lcNum = lcNum;
		this.learningCommunity = learningCommunity.replace("\"", "").trim();
		this.crn = crn.replace("\"", "").trim();
		this.subject = subject.replace("\"", "").trim();
		this.course = course.replace("\"", "").trim();
		this.llc = llc.replace("\"", "").trim();
		this.notes = notes.replace("\"", "").trim();
		
		ILCCourse  ilcCourse =  DataManager.getILCData().getILCCourse(this.crn);
		if (ilcCourse != null) {
			ilc = true;
			instructor = ilcCourse.instructor;
		}
	}
	
	/**
	 * Get the value at a given column, considering this as a row
	 */
	@Override
	public String getValueAt(int col) {

		switch (col) {
		case 1:
			return "" + lcNum;
		case 2:
			return learningCommunity;
		case 3:
			return "" + count;
		case 4:
			return crn;
		case 5:
			return subject;
		case 6:
			return course;
		case 7:
			return llc;
		case 8:
			return notes;
		case 9:
			return (instructor == null) ? "---" : instructor.name;
		default:
			System.err.println("Bad column in LC Course getValueAt [" + col + "]");
			System.exit(0);

		}

		return null;
	}


	
}
