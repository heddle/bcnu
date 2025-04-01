package cnuphys.advisors.model;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import javax.swing.event.ListSelectionEvent;

import cnuphys.advisors.Advisor;
import cnuphys.advisors.Person;
import cnuphys.advisors.enums.Major;
import cnuphys.advisors.io.DataModel;
import cnuphys.advisors.io.ITabled;
import cnuphys.advisors.table.CustomRenderer;
import cnuphys.advisors.table.InputOutput;
import cnuphys.bCNU.util.X11Colors;

/**
 * Holds all the courses taught by core advisors
 * @author heddle
 *
 */
public class Schedule extends DataModel {

	//attributes for schedule data
	private static final DataAttribute scheduleAttributes[] = {DataManager.rowAtt, DataManager.crnAtt,
			DataManager.subjectAtt, DataManager.courseAtt, DataManager.sectionAtt,
			DataManager.titleAtt, DataManager.instructorAtt, DataManager.idAtt};


	/**
	 * The course schedule
	 * @param baseName
	 */
	public Schedule(String baseName) {
		super(baseName, scheduleAttributes);
		renderer = new CustomRenderer(this);

		for (int i = 0; i < getColumnCount(); i++) {
			_dataTable.getColumnModel().getColumn(i).setCellRenderer(renderer);
		}

		//get the advisor schedules

		for (Course course : getCourses()) {
			Advisor advisor = DataManager.getAdvisorData().getAdvisorFromId(course.id);

			if (advisor == null) {
				System.err.println("\nERROR: Null advisor encountered when setting advisor schedule");
				System.exit(1);
			}

			advisor.schedule.add(course);
		}

	}



	@Override
	protected void processData() {
		int colCount = _header.length;
		InputOutput.debugPrintln("SCHEDULE row count: " + _data.size());
		InputOutput.debugPrintln("SCHEDULE col count: " + colCount);

		int crnIndex = getColumnIndex(DataManager.crnAtt);
		int subjectIndex = getColumnIndex(DataManager.subjectAtt);
		int courseIndex = getColumnIndex(DataManager.courseAtt);
		int sectionIndex = getColumnIndex(DataManager.sectionAtt);
		int titleIndex = getColumnIndex(DataManager.titleAtt);
		int instructorIndex = getColumnIndex(DataManager.instructorAtt);
		int idIndex = getColumnIndex(DataManager.idAtt);


		for (String s[] : _data) {

			String id = s[idIndex];
			id = id.replace("\"", "");

			//add leading 0's
			while (id.length() < 8) {
				id = "0" + id;
			}


			//only keep courses taught by core advisors
			Advisor advisor = DataManager.getAdvisorData().isCoreAdvisor(id);
			if (advisor != null) {
				String crn = s[crnIndex];
				String instructor = s[instructorIndex];
				String subject = s[subjectIndex];
				String course = s[courseIndex];
				String section = s[sectionIndex];
				String title = s[titleIndex];

				//try to set instructor subject (major) bases on course taught

				Major major = Major.getValue(subject);

				String trimSubj = subject.trim().toLowerCase();

				if (major == null) {
					if (!(trimSubj.contains("honr") || trimSubj.contains("idst"))) {
						System.out.println("Could not match major to course subject [" + subject + "]");
					}
				}
				else {
					advisor.subject = major;

					//recheck the major based on class taught
					advisor.set(Person.PREBUS, major.isPreBusiness());
			     	advisor.set(Person.ENGR, major.isEngineering());

				}

				_tableData.add(
						new Course(crn, subject, course, section, title, instructor, id));
			}
		}

		//raw data not needed
		deleteRawData();

	}

	public Advisor getAdvisorFromCRN(String crn) {

		for (Advisor advisor : DataManager.getAdvisorData().getAdvisors()) {
			if (advisor.hasCRN(crn)) {
				return advisor;
			}
		}
		return null;
	}



	/**
	 * Get all the courses in a list
	 * @return all the students
	 */
    public List<Course> getCourses() {

    	ArrayList<Course> list = new ArrayList<>();
		for (ITabled itabled : _tableData) {
			Course course = (Course) itabled;
			list.add(course);
		}

		return list;
    }

	/**
	 * Get a highlight text color for a given row and column
	 * @param row the 0-based row
	 * @param column the 0-based column
	 * @return the hightlight color, if null use default (black)
	 */
    @Override
	public Color getHighlightTextColor(int row, int column) {
  		Course course = getCourseFromRow(row);
		return (course.isALC) ? Color.red : Color.black;
	}


	/**
	 * Get a highlight background color for a given row and column
	 * @param row the 0-based row
	 * @param column the 0-based column
	 * @return the highlight backgroundcolor, if null use default (black)
	 */
    @Override
	public Color getHighlightBackgroundColor(int row, int column) {
 		Course course = getCourseFromRow(row);
		return (course.honors()) ? X11Colors.getX11Color("alice blue") : Color.white;
	}


	/**
	 * Get the course at the given 0-based row
	 * @param row the row
	 * @return the course at the given row
	 */
    public Course getCourseFromRow(int row) {
    	return (Course)getFromRow(row);
    }

    /**
     * Get a course from a crn
     * @param crn the crn
     * @return the matching course or null
     */
    public Course getCourseFromCRN(String crn) {
    	if (crn == null) {
    		System.err.println("\nERROR: null crn passed to getCourseFromCRN");
    		return null;
    	}
    	crn = crn.trim();

    	 List<Course> courses = getCourses();
    	 for (Course course : courses) {
    		 if (crn.equals(course.crn)) {
    			 return course;
    		 }
    	 }

    	return null;
    }

    /**
     * Get a alc course from a crn
     * @param crn the crn
     * @return the matching course or null
     */
    public ALCCourse getALCCourseFromCRN(String crn) {
    	if (crn == null) {
    		System.err.println("\nERROR: null crn passed to getALCCourseFromCRN");
    		return null;
    	}
    	crn = crn.trim();

    	 List<ALCCourse> courses = DataManager.getALCData().getALCs();
    	 for (ALCCourse course : courses) {
    		 if (crn.equals(course.crn)) {
    			 return course;
    		 }
    	 }

    	return null;
    }


	@Override
	public void valueChanged(ListSelectionEvent e) {
	}

	/**
	 * Double clicked on a row
	 * @param row the 0-based row
	 * @param o the object at that location
	 */
	@Override
	protected void doubleClicked(int row, ITabled o) {

	}

	/**
	 * Get the list of courses (in CRNs) for an advisor
	 * @return he list of courses (in CRNs) for an advisor
	 */
	public List<String> getAdvisorSchedule(Advisor advisor) {
		List<String> crns = new ArrayList<>();

		for (Course course : getCourses()) {
			if (advisor.id.equals(course.id)) {
				crns.add(course.crn);
			}
		}

		return crns;
	}

}
