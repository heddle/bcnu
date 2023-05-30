package cnuphys.advisors.checklist.steps;

import java.util.ArrayList;
import java.util.List;

import cnuphys.advisors.Advisor;
import cnuphys.advisors.Student;
import cnuphys.advisors.checklist.IAlgorithmStep;
import cnuphys.advisors.frame.AdvisorAssign;
import cnuphys.advisors.model.Course;
import cnuphys.advisors.model.DataManager;
import cnuphys.advisors.model.StudentData;

public class StudentInClassStep  implements IAlgorithmStep{


	@Override
	public boolean run() {
		StudentData studentData = DataManager.getStudentData();

		List<Student> students = studentData.getStudents();

		students.removeIf(x -> x.locked());

		int target = AdvisorAssign.targetAverage();

		for (Student student : students) {
			Advisor adv = bestInClassAdvisor(student);
			if (adv != null) {
				System.out.println("  ** BEST MATCH " + adv.name);
				adv.addAdvisee(student, true);

				if (adv.adviseeCount() >= target) {
					adv.setLocked();
					System.out.println("Advisor " + adv.name + " is now locked.");
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
			if ((adv != null) && !adv.locked() && (adv.adviseeCount() <= target)) {
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
						"  ** POTENTIAL IN CLASS MATCH STUDENT: " + student.fullNameAndID() + "  ADV: " + adv.name);
			}

			for (Advisor adv : potAdvisors) {
				if (student.major == adv.subject) {
					System.out.println("  ** MAJOR MATCH");
					return adv;
				}
			}

			for (Advisor adv : potAdvisors) {
				if (student.major == adv.preferred2ndMajor) {
					System.out.println("  ** PREF 2ND MAJOR MATCH");
					return adv;
				}
			}

			for (Advisor adv : potAdvisors) {
				if (student.major.isInMajorFamily(adv.subject)) {
					System.out.println("  ** FAMILY MATCH");
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
