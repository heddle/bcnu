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
	
	/** the isntructor */
	public Advisor instructor;
	

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
		case 0:
			return learningCommunity;
		case 1:
			return crn;
		case 2:
			return subject;
		case 3:
			return course;
		case 4:
			return llc;
		case 5:
			return notes;
		case 6:
			return (instructor == null) ? "---" : instructor.name;
		default:
			System.err.println("Bad column in Course getValueAt [" + col + "]");
			System.exit(0);

		}

		return null;
	}

}
