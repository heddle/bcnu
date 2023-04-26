package cnuphys.advisors.model;

import cnuphys.advisors.io.ITabled;

public class Course implements ITabled {


	/** course CRN */
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


	/**
	 * Get the value at a given column, considering this as a row
	 */
	@Override
	public String getValueAt(int col) {
		
		switch (col) {
		case 0:
			return crn;
		case 1:
			return subject;
		case 2:
			return course;
		case 3:
			return section;
		case 4:
			return title;
		case 5:
			return instructor;
		case 6:
			return id;
		default:
			System.err.println("Bad column in Course getValueAt [" + col + "]");
			System.exit(0);

		}

		return null;
	}
}
