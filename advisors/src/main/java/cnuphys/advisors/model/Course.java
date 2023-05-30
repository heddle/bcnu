package cnuphys.advisors.model;

import cnuphys.advisors.io.ITabled;

public class Course implements ITabled {


	/** course CRN as a string */
	public String crn;

	/** subject e.g. BIOL */
	public String subject;

	/** course, e.g. 109L */
	public String course;

	/** course section */
	public String section;

	/** course title */
	public String title;

	/** full name of instructor */
	public String instructor;

	/** the instructor ID */
	public String id;

	/** is it an ILC? */
	public boolean isILC;

	public Course(String crn, String subject, String course, String section, String title, String instructor, String id) {
		super();
		this.crn = crn.replace("\"", "").trim();
		this.subject = subject.replace("\"", "").trim();
		this.course = course.replace("\"", "").trim();
		this.section = section.replace("\"", "").trim();
		this.title = title.replace("\"", "").trim();
		this.instructor = instructor.replace("\"", "").trim();
		this.id = DataManager.fixId(id);

		//add leading 0's
		while (this.id.length() < 8) {
			this.id = "0" + this.id;
		}

	}

	public boolean honors() {
		return subject.toUpperCase().equals("HONR");
	}

	/**
	 * An info string for this course
	 * @return an info string
	 */
	public String infoString() {
		return String.format("%s %s%s %s", crn, subject, course, title);
	}


	/**
	 * Get the value at a given column, considering this as a row
	 */
	@Override
	public String getValueAt(int col) {

		switch (col) {
		case 1:
			return crn;
		case 2:
			return subject;
		case 3:
			return course;
		case 4:
			return section;
		case 5:
			return title;
		case 6:
			return instructor;
		case 7:
			return id;
		default:
			System.err.println("\nERROR: Bad column in Course getValueAt [" + col + "]");
			System.exit(0);

		}

		return null;
	}


	@Override
	public boolean check(int bit) {
		return false;
	}
}
