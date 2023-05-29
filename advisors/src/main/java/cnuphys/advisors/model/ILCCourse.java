package cnuphys.advisors.model;

import cnuphys.advisors.Advisor;
import cnuphys.advisors.io.ITabled;

public class ILCCourse implements ITabled {

	/** learning community */
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


	public ILCCourse(String learningCommunity, String crn, String subject, String course, String llc, String notes) {
		super();
		this.learningCommunity = learningCommunity.replace("\"", "").trim();
		this.crn = crn.replace("\"", "").trim();
		this.subject = subject.replace("\"", "").trim();
		this.course = course.replace("\"", "").trim();
		this.llc = llc.replace("\"", "").trim();
		this.notes = notes.replace("\"", "").trim();
	}

	/**
	 * Get the value at a given column, considering this as a row
	 */
	@Override
	public String getValueAt(int col) {

		switch (col) {
		case 1:
			return learningCommunity;
		case 2:
			return crn;
		case 3:
			return subject;
		case 4:
			return course;
		case 5:
			return llc;
		case 6:
			return notes;
		case 7:
			return (instructor == null) ? "---" : instructor.name;
		case 8:
			return "" + count;
		default:
			System.err.println("\nERROR: Bad column in ILC Course getValueAt [" + col + "]");
			System.exit(0);

		}

		return null;
	}


	@Override
	public boolean check(int bit) {
		return false;
	}


}
