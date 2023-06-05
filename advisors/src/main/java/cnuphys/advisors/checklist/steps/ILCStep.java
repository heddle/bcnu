package cnuphys.advisors.checklist.steps;

import java.util.List;

import cnuphys.advisors.Advisor;
import cnuphys.advisors.Person;
import cnuphys.advisors.Student;
import cnuphys.advisors.checklist.CheckListLaunchable;
import cnuphys.advisors.frame.AdvisorAssign;
import cnuphys.advisors.model.Course;
import cnuphys.advisors.model.DataManager;
import cnuphys.advisors.model.ILCCourse;
import cnuphys.advisors.model.ILCData;

public class ILCStep extends CheckListLaunchable {

	public ILCStep(String info, boolean enabled) {
		super("ILC", info, enabled);
	}

	/**
	 * Assign the ILC advisees
	 */
	@Override
	public void launch() {
		ILCData ilcData = DataManager.getILCData();

		List<Student> students = DataManager.getUnassignedStudents();

		for (Student student : students) {
			if (student.locked()) {
				System.err.println("\nERROR: Locked student in ilcStep should not have happened.\n");
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
							student.advisor.removeAdvisee(student);
							student.setLocked(false);
						}
						else {  //no need to override
							assign = false;
						}
					}

					if (assign) {
						student.setILC();
						advisor.addAdvisee(student, true);
						ilc.count += 1;
					}
				}
			}
		}

		//lock advisors that have reached or exceeded target

		int target = AdvisorAssign.targetAverage();
		List<Advisor> ilcAdvisors = DataManager.getFilteredAdvisorData(Person.ILC).getAdvisors();
		for (Advisor advisor : ilcAdvisors) {
			if (advisor.adviseeCount() >= target) {
				advisor.setLocked();
			}
		}
	}

}
