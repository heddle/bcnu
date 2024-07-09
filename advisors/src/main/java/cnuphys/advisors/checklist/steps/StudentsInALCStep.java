package cnuphys.advisors.checklist.steps;

import java.util.List;

import cnuphys.advisors.Advisor;
import cnuphys.advisors.Person;
import cnuphys.advisors.Student;
import cnuphys.advisors.checklist.CheckListLaunchable;
import cnuphys.advisors.enums.EReason;
import cnuphys.advisors.frame.AdvisorAssign;
import cnuphys.advisors.model.ALCCourse;
import cnuphys.advisors.model.AdvisorData;
import cnuphys.advisors.model.Course;
import cnuphys.advisors.model.DataManager;

public class StudentsInALCStep extends CheckListLaunchable {

	public StudentsInALCStep(String info, boolean enabled) {
		super("Students in ALC", info, enabled);
	}

	@Override
	public void launch() {

		List<Student> students = DataManager.getUnassignedStudents();

		int target = AdvisorAssign.targetAverage();

		for (Student student : students) {

			// get the schedule
			for (Course course : student.schedule) {
				if (course.isALC) {
					ALCCourse alc = DataManager.getSchedule().getALCCourseFromCRN(course.crn);
					if (student.inALC_LC(alc)) {

						Advisor adv = DataManager.getAdvisorData().getAdvisorFromId(course.id);
						if (adv != null && adv.alc() && !adv.locked()) {
							System.out.println("  ** ALC BEST MATCH " + adv.name);
							adv.addAdvisee(student, true, EReason.ALC);

							if (adv.adviseeCount() >= target) {
								adv.setLocked();
								System.out.println("ALC Advisor " + adv.name + " is now locked.");
							}
						}
					}
				}
			}
		}

		
	}

}
