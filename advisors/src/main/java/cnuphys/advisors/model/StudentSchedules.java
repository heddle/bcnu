package cnuphys.advisors.model;

import javax.swing.event.ListSelectionEvent;

import cnuphys.advisors.Student;
import cnuphys.advisors.io.DataModel;
import cnuphys.advisors.io.ITabled;

public class StudentSchedules extends DataModel {


	//attributes for student schedules data
	private static final DataAttribute studentScheduleAtts[] = {DataManager.rowAtt, DataManager.idAtt, DataManager.crnAtt,
			DataManager.firstNameAtt};


	public StudentSchedules(String baseName) {
		super(baseName, studentScheduleAtts);
	}

	@Override
	protected void processData() {
		int idIndex = getColumnIndex(DataManager.idAtt);
		int crnIndex = getColumnIndex(DataManager.crnAtt);

		// dont create an actual model, just add classes to the student schedule
		for (String s[] : _data) {
			String id = DataManager.fixId(s[idIndex]);
			String crn = s[crnIndex];

			Course course = DataManager.getSchedule().getCourseFromCRN(crn);
			if (course != null) {
				Student student = DataManager.getStudentData().getStudentFromId(id);

				if (student == null) {
					System.err.println("Did not find student with ID: [" + id + "] in StudentSchedule");
				} else {
					student.addCourse(course);
					//assignCourse(student, course);
				}
			}

		}

		//raw data not needed
		deleteRawData();

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
