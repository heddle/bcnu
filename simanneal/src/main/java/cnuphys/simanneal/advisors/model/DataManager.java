package cnuphys.simanneal.advisors.model;

public class DataManager {
	
	//base file name for advisors csv file in data dir
    private static final String _advisorBaseName = "advisors.csv";
    private static final String _scheduleBaseName = "ScheduleOfClasses.csv";
	
    //the advisor data
	private static AdvisorData _advisorData;
	
	//the schedue
	private static Schedule _schedule;
	
	//synonym lists
	public static final DataAttribute idAtt = new DataAttribute("ID", 30, "");
	public static final DataAttribute lastNameAtt = new DataAttribute("LAST", 160, "last name", "lastname", "last_name");
	public static final DataAttribute firstNameAtt = new DataAttribute("FIRST", 110, "first name", "firstname", "lfirst_name");
	public static final DataAttribute departmentNameAtt = new DataAttribute("DEPT", 100, "department");

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

	/**
	 * Initialize all the data
	 */
	public static void init() {
		_advisorData = new AdvisorData(_advisorBaseName);
		_schedule = new Schedule(_scheduleBaseName);
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

}
