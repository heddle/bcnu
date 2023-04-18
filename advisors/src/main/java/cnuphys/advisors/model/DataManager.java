package cnuphys.advisors.model;

import java.util.ArrayList;
import java.util.List;

import cnuphys.advisors.Advisor;
import cnuphys.advisors.io.ITabled;

public class DataManager {
	
	//base file name for advisors csv file in data dir
    private static final String _advisorBaseName = "advisors.csv";
    private static final String _scheduleBaseName = "ScheduleOfClasses.csv";
    private static final String _studentsBaseName = "students.csv";
	
    //the advisor data
	private static AdvisorData _advisorData;
	
	//the class schedule
	private static Schedule _schedule;
	
	//the student data
	private static StudentData _studentData;
	
	//synonym lists
	public static final DataAttribute idAtt = new DataAttribute("ID", 30, "");
	public static final DataAttribute lastNameAtt = new DataAttribute("LAST", 160, "last name", "lastname", "last_name");
	public static final DataAttribute firstNameAtt = new DataAttribute("FIRST", 110, "first name", "firstname", "lfirst_name");
	public static final DataAttribute departmentNameAtt = new DataAttribute("DEPT", 100, "department");
	public static final DataAttribute numAdviseeAtt = new DataAttribute("#ADV", 60, "");
	

	public static final DataAttribute crnAtt = new DataAttribute("CRN", 40, "");
	public static final DataAttribute courseAtt = new DataAttribute("COURSE", 60, "");
	public static final DataAttribute sectionAtt = new DataAttribute("SECT", 30, "section");
	public static final DataAttribute titleAtt = new DataAttribute("TITLE", 120, "");
	public static final DataAttribute hoursAtt = new DataAttribute("HOURS", 38, "");
	public static final DataAttribute llcAtt= new DataAttribute("LLC", 40, "Area of LLC");
	public static final DataAttribute typeAtt = new DataAttribute("TYPE", 30, "");
	public static final DataAttribute daysAtt = new DataAttribute("DAYS", 34, "");
	public static final DataAttribute timeAtt = new DataAttribute("TIME", 90, "");
	public static final DataAttribute locationAtt = new DataAttribute("LOC", 70, "location");
	public static final DataAttribute instructorAtt = new DataAttribute("INSTRUCTOR", 120, "");
	
	public static final DataAttribute plpAtt = new DataAttribute("PLP", 40, "");
	public static final DataAttribute honrAtt = new DataAttribute("HONR", 40, "");
	public static final DataAttribute pspAtt = new DataAttribute("PSP", 40, "");
	public static final DataAttribute prelawAtt = new DataAttribute("PRELAW", 40, "");
	public static final DataAttribute majorAtt = new DataAttribute("MAJOR", 120, "Major_1st");
	

	/**
	 * Initialize all the data
	 */
	public static void init() {
		_advisorData = new AdvisorData(_advisorBaseName);
		_schedule = new Schedule(_scheduleBaseName);
		_studentData = new StudentData(_studentsBaseName);
		
		//now extract the courses taught by FCAs
		
		for (Advisor advisor : _advisorData.getAdvisors()) {
			for (Course course : _schedule.getCourses()) {
				
			}
		}
	}
	

	
	/**
	 * Get the data for all core advisors
	 * @return the advisor data
	 */
	public static AdvisorData getAdvisorData() {
		return _advisorData;
	}
	
	/**
	 * Get the schedule of classes data
	 * @return the schedule of classes
	 */
	public static Schedule getSchedule() {
		return _schedule;
	}
	/**
	 * Get the data for all students
	 * @return the student data
	 */
	public static StudentData getStudentData() {
		return _studentData;
	}
	
	/**
	 * Get the target cohort size
	 * @return the target cohort size
	 */
	public static double targetCohort() {
		double na = getAdvisorData().count();
		double ns = getStudentData().count();
		return ns/na;
	}

}
