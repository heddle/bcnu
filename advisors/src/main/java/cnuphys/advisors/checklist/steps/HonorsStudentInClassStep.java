package cnuphys.advisors.checklist.steps;

import java.util.ArrayList;
import java.util.List;

import cnuphys.advisors.Advisor;
import cnuphys.advisors.Person;
import cnuphys.advisors.Student;
import cnuphys.advisors.checklist.IAlgorithmStep;
import cnuphys.advisors.frame.AdvisorAssign;
import cnuphys.advisors.model.AdvisorData;
import cnuphys.advisors.model.Course;
import cnuphys.advisors.model.DataManager;

public class HonorsStudentInClassStep implements IAlgorithmStep {
	
	@Override
	public boolean run() {
		
		List<Student> students = DataManager.getUnassignedHonorsStudents();
		
		int target = AdvisorAssign.targetAverage();

		for (Student student : students) {
			Advisor adv = bestInClassAdvisor(student);
			if (adv != null) {
				System.out.println("  ** HONR BEST MATCH " + adv.name);
				adv.addAdvisee(student, true);

				if (adv.adviseeCount() >= target) {
					adv.setLocked();
					System.out.println("Honors Advisor " + adv.name + " is now locked.");
				}
			}
		}

		
		return true;
	}
	
	private Advisor bestInClassAdvisor(Student student) {
		
		int target = AdvisorAssign.targetAverage();

		
		ArrayList<Advisor> potAdvisors = new ArrayList<>(); 
		List<Course> schedule = student.schedule;
		
		for (Course course : schedule) {
			Advisor adv = DataManager.getAdvisorData().getAdvisorFromId(course.id);
			if ((adv != null) && (adv != DataManager.honorsDirector) && adv.honors() && !adv.locked() && (adv.adviseeCount() <= target)) {
				potAdvisors.add(adv);
			}
		}
		
		if (potAdvisors.isEmpty()) {
			return null;
		}
		
		else if (potAdvisors.size() == 1) {
			return potAdvisors.get(0);
		}

		else {
			for (Advisor adv : potAdvisors) {
				System.out.println(
						"  ** HONR POTENTIAL IN CLASS MATCH STUDENT: " + student.fullNameAndID() + "  ADV: " + adv.name);
			}
			
			for (Advisor adv : potAdvisors) {
				if (student.major == adv.subject) {
					System.out.println("  ** HONR MAJOR MATCH");
					return adv;
				}
			}
			
			for (Advisor adv : potAdvisors) {
				if (student.major == adv.preferred2ndMajor) {
					System.out.println("  ** HONR PREF 2ND MAJOR MATCH");
					return adv;
				}
			}
			
			for (Advisor adv : potAdvisors) {
				if (student.major.isInMajorFamily(adv.subject)) {
					System.out.println("  ** HONR FAMILY MATCH");
					return adv;
				}
			}
			
			//give to least num advisees
			
			int min = 10000;
			Advisor minAdv = null;
			for (Advisor adv : potAdvisors) {
				if (adv.adviseeCount() < min) {
					min = adv.adviseeCount();
					minAdv = adv;
				}
			}
			return minAdv;


		}

	}	

}
