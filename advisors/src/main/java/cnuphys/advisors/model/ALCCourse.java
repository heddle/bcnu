package cnuphys.advisors.model;

import cnuphys.advisors.Advisor;
import cnuphys.advisors.io.ITabled;

public class ALCCourse implements ITabled {

	/** learning community number */
	public String lcNum;

	/** learning community title */
	public String learningCommunity;

	/** course CRN */
	public String crn;

	/** subject e.g. BIOL */
	public String subject;

	/** course, e.g. 109L */
	public String course;

	/** the instructor */
	public Advisor instructor;


	public ALCCourse(String lcNum, String learningCommunity, String crn, String subject, String course) {
		super();

		this.lcNum = lcNum.replace("\"", "");
		this.learningCommunity = learningCommunity.replace("\"", "").trim();
		this.crn = crn.replace("\"", "").trim();
		this.subject = subject.replace("\"", "").trim();
		this.course = course.replace("\"", "").trim();

		instructor = DataManager.getSchedule().getAdvisorFromCRN(crn);
	}

	/**
	 * Get the value at a given column, considering this as a row
	 */
	@Override
	public String getValueAt(int col) {

		switch (col) {
		case 1:
			return lcNum;
		case 2:
			return learningCommunity;
		case 3:
			return crn;
		case 4:
			return subject;
		case 5:
			return course;
		case 6:
			return (instructor == null) ? "---" : instructor.name;
		default:
			System.err.println("\nERROR: Bad column in ALC Course getValueAt [" + col + "]");
			System.exit(0);

		}

		return null;
	}

	/**
	 * get an info string
	 * @return full name (last, first) and department is a single string
	 */
	public String infoString() {
		String iname = (instructor == null) ? "---" : instructor.name;
		return String.format("%s  [%s] [%s] [%s]", learningCommunity, crn, subject + course, iname);
	}



	@Override
	public boolean check(int bit) {
		return false;
	}


}
