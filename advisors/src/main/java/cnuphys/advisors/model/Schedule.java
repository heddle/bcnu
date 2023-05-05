package cnuphys.advisors.model;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import javax.swing.event.ListSelectionEvent;

import cnuphys.advisors.Advisor;
import cnuphys.advisors.enums.Major;
import cnuphys.advisors.io.DataModel;
import cnuphys.advisors.io.ITabled;
import cnuphys.advisors.log.LogManager;
import cnuphys.advisors.table.CustomRenderer;
import cnuphys.advisors.table.InputOutput;

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


	public Schedule(String baseName) {
		super(baseName, scheduleAttributes);
		renderer = new CustomRenderer(this);

		for (int i = 0; i < getColumnCount(); i++) {
			_dataTable.getColumnModel().getColumn(i).setCellRenderer(renderer);
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
				if (major == null) {
					System.err.println("Could not match major to course subject [" + subject + "]");
				}
				else {
					advisor.subject = major;
				}

				_tableData.add(
						new Course(crn, subject, course, section, title, instructor, id));
			}
		}


		//mark the ILCs including instructors
		List<ILCCourse> ilcs = DataManager.getILCData().getILCs();
		for (ILCCourse ilc : ilcs) {
			Course course = getCourseFromCRN(ilc.crn);

			if (course == null) {
				System.err.println("Did not find course corresponding to ILC with crn: [" + ilc.crn + "]");
				System.exit(1);
			}

			course.isILC = true;

			//mark advisor as an ILC instructor
			Advisor advisor = DataManager.getAdvisorData().getAdvisorFromId(course.id);
			if (advisor == null) {
				System.err.println("Did not find advisor corresponding to course with advisor Id: [" + course.id + "]");
				System.exit(1);
			}

			ilc.instructor  = advisor;
			advisor.setILC();
		}



		//raw data not needed
		deleteRawData();

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
		return (course.isILC) ? Color.red : Color.black;
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
    		LogManager.error("null crn passed to getCourseFromCRN");
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

}
