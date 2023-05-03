package cnuphys.advisors.checklist.steps;

import java.util.List;

import cnuphys.advisors.Advisor;
import cnuphys.advisors.Student;
import cnuphys.advisors.StudentFilter;
import cnuphys.advisors.checklist.IAlgorithmStep;
import cnuphys.advisors.model.Course;
import cnuphys.advisors.model.DataManager;
import cnuphys.advisors.model.ILCCourse;
import cnuphys.advisors.model.ILCData;

public class ILCStep implements IAlgorithmStep {

	/**
	 * Assign the ILC advisees
	 */
	@Override
	public boolean run() {
		ILCData ilcData = DataManager.getILCData();

		List<Student> students = DataManager.getFilteredStudentData(StudentFilter.unassignedStudents).getStudents();

		for (Student student : students) {
			if (student.locked) {
				System.err.println("locked student in ilcStep should not have happened.");
				continue;
			}

			//student schedule only contains course taught by FCAs
			List<Course> courses = student.schedule;

			for (Course course : courses) {

				ILCCourse ilc = ilcData.getILCCourse(course.crn);

				if (ilc != null) {
					Advisor advisor = ilc.instructor;
					
					boolean assign = true;
					
					if (student.advisor != null) { //already have an advisor?
						
						//override if this is in student's LC
						if (student.courseInLC(course.crn)) {
			//				System.err.println("Before overwrite advisor " + student.advisor.name + "  count: " + student.advisor.adviseeCount());
							student.advisor.removeAdvisee(student);
			//				System.err.println("STUDENT: " + student.fullNameAndID()+ "   Overwrite with LC course " + advisor.name + "  replaces: " + student.advisor.name);
							student.locked = false;
						}
						else {  //no need to override
							assign = false;
						}
					}

					if (assign) {
						student.ilc = true;
						advisor.addAdvisee(student, true);
						ilc.count += 1;
					}
				}
			}
		}


		return true;
	}

}
