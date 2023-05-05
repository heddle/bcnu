package cnuphys.advisors.model;

import javax.swing.event.ListSelectionEvent;

import cnuphys.advisors.Student;
import cnuphys.advisors.io.DataModel;
import cnuphys.advisors.io.ITabled;
import cnuphys.advisors.table.InputOutput;

public class StudentSchedules extends DataModel {


	//attributes for student schedules data
	private static final DataAttribute studentScheduleAtts[] = {DataManager.rowAtt, DataManager.idAtt, DataManager.crnAtt,
			DataManager.firstNameAtt};


	public StudentSchedules(String baseName) {
		super(baseName, studentScheduleAtts);
	}

	@Override
	protected void processData() {
		InputOutput.debugPrintln("STUDENT SCHEDULE row count: " + _data.size());

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


//	private void assignCourse(Student student, Course course) {
//		student.addCourse(course);
//
//		if (course.isILC) {
//			ILCCourse ilcCourse= DataManager.getILCData().getILCCourse(course.crn);
//			Advisor advisor = ilcCourse.instructor;
//
//			if (student.advisor != null) { //already have an advisor?
//
//				//override if this is in student's LC
//				if (student.courseInLC(course.crn)) {
//	//				System.err.println("Before overwrite advisor " + student.advisor.name + "  count: " + student.advisor.adviseeCount());
//					student.advisor.removeAdvisee(student);
//	//				System.err.println("STUDENT: " + student.fullNameAndID()+ "   Overwrite with LC course " + advisor.name + "  replaces: " + student.advisor.name);
//					student.locked = false;
//				}
//				else {  //no need to override
//					return;
//				}
//			}
//
//			student.ilc = true;
//			advisor.addAdvisee(student, true);
//			ilcCourse.count += 1;
//
//		}
//	}
//

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
