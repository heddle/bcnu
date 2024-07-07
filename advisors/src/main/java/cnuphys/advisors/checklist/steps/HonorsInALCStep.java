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
import cnuphys.advisors.model.Schedule;

public class HonorsInALCStep extends CheckListLaunchable {

	public HonorsInALCStep(String info, boolean enabled) {
		super("Hon Students in HONR ALC", info, enabled);
	}

	@Override
	public void launch() {

		List<Student> students = DataManager.getUnassignedHonorsStudents();

		for (Student student : students) {

			// get the schedule
			for (Course course : student.schedule) {
				if (course.isALC && course.honors()) {
					ALCCourse alc = DataManager.getSchedule().getALCCourseFromCRN(course.crn);
					if (student.inALC_LC(alc)) {
						Advisor adv = DataManager.getAdvisorData().getAdvisorFromId(course.id);
						if (adv != null && adv.honors()) {
							System.out.println("  ** HONR ALC BEST MATCH " + adv.name);
							adv.addAdvisee(student, true, EReason.ALC);

							// do not lock the advisor
						}
					}
				}
			}
		}

		//now lock down if full cohort
		
		int target = AdvisorAssign.targetAverage();

		for (Advisor advisor : DataManager.getAdvisorData().getAdvisors()) {
			if (!advisor.locked() && advisor.adviseeCount() >= target) {
				advisor.setLocked();
				System.out.println("Advisor " + advisor.name + " is now locked.");
			}
		}
		
	}

}
