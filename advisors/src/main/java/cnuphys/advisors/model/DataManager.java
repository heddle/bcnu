package cnuphys.advisors.model;

import java.util.ArrayList;
import java.util.List;

import cnuphys.advisors.Advisor;
import cnuphys.advisors.AdvisorFilter;
import cnuphys.advisors.Student;
import cnuphys.advisors.StudentFilter;
import cnuphys.advisors.enums.Major;

public class DataManager {

	//base file name for advisors csv file in data dir
    private static final String _advisorBaseName = "advisors.csv";
    private static final String _scheduleBaseName = "scheduleofclasses.csv";
    private static final String _studentsBaseName = "students.csv";
    private static final String _ilcsBaseName = "ilcs.csv";
    private static final String _honAdvBaseName = "honorsadvisors.csv";
    private static final String _presScholarAdvBaseName = "presscholaradvisors.csv";
    private static final String _studentSchedulesBaseName = "studentschedules.csv";

    //the advisor data
	private static AdvisorData _advisorData;

	//the class schedule
	private static Schedule _schedule;

	//the student data
	private static StudentData _studentData;

	//ILCs
	private static ILCData _ilcData;

	//synonym lists

	public static final DataAttribute rowAtt = new DataAttribute(" ", 40);


	public static final DataAttribute idAtt = new DataAttribute("ID NUMBER", 74, "id", "cnuid");
	public static final DataAttribute advisorAtt = new DataAttribute("ADVISOR", 200);
	public static final DataAttribute lastNameAtt = new DataAttribute("LAST", 110, "last name", "lastname", "last_name", "lname");
	public static final DataAttribute firstNameAtt = new DataAttribute("FIRST", 90, "first name", "firstname", "lfirst_name", "fname");
	public static final DataAttribute departmentNameAtt = new DataAttribute("DEPT", 70, "department", "adv1_dept");
	public static final DataAttribute numAdviseeAtt = new DataAttribute("#ADV", 50);


	public static final DataAttribute crnAtt = new DataAttribute("CRN", 40);
	public static final DataAttribute subjectAtt = new DataAttribute("SUBJ", 70, "subject");
	public static final DataAttribute courseAtt = new DataAttribute("CRSE", 60, "course", "course #");
	public static final DataAttribute sectionAtt = new DataAttribute("SECT", 30, "section");
	public static final DataAttribute titleAtt = new DataAttribute("TITLE", 170);
	public static final DataAttribute hoursAtt = new DataAttribute("HOURS", 38);
	public static final DataAttribute llcAtt= new DataAttribute("LLC", 40, "Area of LLC", "Liberal Learning Attribute");
	public static final DataAttribute lcAtt= new DataAttribute("Learning Community", 240, "Learning Community Title");
	public static final DataAttribute notesAtt = new DataAttribute("NOTES", 140);
	public static final DataAttribute typeAtt = new DataAttribute("TYPE", 30);
	public static final DataAttribute daysAtt = new DataAttribute("DAYS", 34);
	public static final DataAttribute timeAtt = new DataAttribute("TIME", 90);
	public static final DataAttribute locationAtt = new DataAttribute("LOC", 70, "location");
	public static final DataAttribute instructorAtt = new DataAttribute("INSTRUCTOR", 150);

	public static final DataAttribute ilcAtt = new DataAttribute("ILC", 40);
	public static final DataAttribute prscAtt = new DataAttribute("PRSC", 40);
	public static final DataAttribute windAtt = new DataAttribute("WIND", 40);
	public static final DataAttribute ccapAtt = new DataAttribute("CCAP", 40);
	public static final DataAttribute plpAtt = new DataAttribute("PLP", 40);
	public static final DataAttribute honrAtt = new DataAttribute("HONR", 40);
	public static final DataAttribute pspAtt = new DataAttribute("PSP", 40);
	public static final DataAttribute prelawAtt = new DataAttribute("PRELAW", 46);
	public static final DataAttribute majorAtt = new DataAttribute("MAJOR", 70, "Major_1st");


	/**
	 * Initialize all the data
	 */
	public static void init() {
		_advisorData = new AdvisorData(_advisorBaseName);
		_ilcData = new ILCData(_ilcsBaseName);

		_schedule = new Schedule(_scheduleBaseName);
		_studentData = new StudentData(_studentsBaseName);

		new HonorsAdvisors(_honAdvBaseName);
		new PresScholarAdvisors(_presScholarAdvBaseName);
		new StudentSchedules(_studentSchedulesBaseName);
	}

	/**
	 * Standardize an is to include leading 0's
	 * @param id the id to standardize
	 * @return the fixed id
	 */
	public static String fixId(String id) {
		String fixedId = id.replace("\"", "").trim();

		//add leading 0's
		while (fixedId.length() < 8) {
			fixedId = "0" + fixedId;
		}

		return fixedId;
	}

	/**
	 * Get the data for all core advisors
	 * @return the advisor data
	 */
	public static ILCData getILCData() {
		return _ilcData;
	}


	/**
	 * Get the data for all core advisors
	 * @return the advisor data
	 */
	public static AdvisorData getAdvisorData() {
		return _advisorData;
	}

	public static AdvisorData getFilteredAdvisorData(AdvisorFilter filter) {
		return _advisorData.subModel(filter);
	}

	/**
	 * Get a list of advisors whose subject matches a major
	 * @param major the major to match
	 * @return the list
	 */
	public static List<Advisor> getAdvisorsForMajor(Major major) {
		ArrayList<Advisor> advisors = new ArrayList<>();

		for (Advisor advisor : _advisorData.getAdvisors()) {
			if (major == advisor.subject) {
				advisors.add(advisor);
			}
		}

		return advisors;
	}
	
	/**
	 * Get a list of students whose major matches a given major
	 * @param major the major to match
	 * @return the list
	 */
	public static List<Student> getStudentsForMajor(Major major) {
		ArrayList<Student> students = new ArrayList<>();

		for (Student student : _studentData.getStudents()) {
			if (major == student.major) {
				students.add(student);
			}
		}

		return students;
	}


	public static void fileDataChange() {

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

	public static StudentData getFilteredStudentData(StudentFilter filter) {
		return _studentData.subModel(filter);
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
