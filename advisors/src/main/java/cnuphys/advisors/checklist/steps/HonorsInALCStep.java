package cnuphys.advisors.checklist.steps;

import java.util.List;

import cnuphys.advisors.Advisor;
import cnuphys.advisors.Person;
import cnuphys.advisors.Student;
import cnuphys.advisors.checklist.CheckListLaunchable;
import cnuphys.advisors.enums.EReason;
import cnuphys.advisors.frame.AdvisorAssign;
import cnuphys.advisors.model.AdvisorData;
import cnuphys.advisors.model.Course;
import cnuphys.advisors.model.DataManager;

public class HonorsInALCStep extends CheckListLaunchable {

	public HonorsInALCStep(String info, boolean enabled) {
		super("Hon Student in HONR ALC", info, enabled);
	}

	@Override
	public void launch() {

		List<Student> students = DataManager.getUnassignedHonorsStudents();

		for (Student student : students) {
			
			//get the schedule
			for (Course course : student.schedule) {
				if (course.isALC && course.honors()) {
					Advisor adv = DataManager.getAdvisorData().getAdvisorFromId(course.id);
                    if (adv != null && adv.honors()) {
                        System.out.println("  ** HONR ALC BEST MATCH " + adv.name);
                        adv.addAdvisee(student, true, EReason.HONR);
                        
                        //do not lock the advisor
                    }
				}
			}
		}
		
		//now lock down if full cohort
		
		int target = AdvisorAssign.targetAverage();
		AdvisorData advisorData = DataManager.getFilteredAdvisorData(Person.HONOR);

		for (Advisor advisor : advisorData.getAdvisors()) {
			if (advisor.adviseeCount() >= target) {
				advisor.setLocked();
				System.out.println("Honors Advisor " + advisor.name + " is now locked.");
			}
		}
		
	}

}
