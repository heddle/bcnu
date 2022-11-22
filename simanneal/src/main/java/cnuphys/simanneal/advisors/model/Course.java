package cnuphys.simanneal.advisors.model;

import cnuphys.simanneal.advisors.io.ITabled;

public class Course implements ITabled {
	
	
	public String crn;
	public String course;
	public String section;
	public String title;
	public String hours;
	public String llc;
	public String type;
	public String days;
	public String time;
	public String location;
	public String instructor;

	
	
	public Course(String crn, String course, String section, String title, String hours, String llc, String type,
			String days, String time, String location, String instructor) {
		super();
		this.crn = crn.replace("\"", "");;
		this.course = course.replace("\"", "");;
		this.section = section.replace("\"", "");;
		this.title = title.replace("\"", "");;
		this.hours = hours.replace("\"", "");;
		this.llc = llc.replace("\"", "");;
		this.type = type.replace("\"", "");;
		this.days = days.replace("\"", "");;
		this.time = time.replace("\"", "");;
		this.location = location.replace("\"", "");;
		this.instructor = instructor.replace("\"", "");;
	}

	
	/**
	 * Get the value at a given colun, considering this as a row
	 */
	@Override
	public String getValueAt(int col) {
		if (col == 0) {
			return crn;
		}
		else if (col == 1) {
			return course;
		}
		else if (col == 2) {
			return section;
		}
		else if (col == 3) {
			return title;
		}
		else if (col == 4) {
			return hours;
		}
		else if (col == 5) {
			return llc;
		}
		else if (col == 6) {
			return type;
		}
		else if (col == 7) {
			return days;
		}
		else if (col == 8) {
			return time;
		}
		else if (col == 9) {
			return location;
		}
		else if (col == 10) {
			return instructor;
		}
		else {
			System.err.println("Bad column in Course getValueAt [" + col + "]");
			System.exit(0);
		}
		
		return null;
	}
}
