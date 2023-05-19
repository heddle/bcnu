package cnuphys.advisors.checklist.steps;

import java.util.List;

import cnuphys.advisors.Advisor;
import cnuphys.advisors.Person;
import cnuphys.advisors.Student;
import cnuphys.advisors.checklist.IAlgorithmStep;
import cnuphys.advisors.frame.AdvisorAssign;
import cnuphys.advisors.model.AdvisorData;
import cnuphys.advisors.model.Course;
import cnuphys.advisors.model.DataManager;
import cnuphys.advisors.model.StudentData;

public class StudentInClassStep  implements IAlgorithmStep{


	@Override
	public boolean run() {
		AdvisorData advisorData = DataManager.getAdvisorData();
		StudentData studentData = DataManager.getStudentData();

		List<Advisor> advisors = advisorData.getAdvisors();
		List<Student> students = studentData.getStudents();

		students.removeIf(x -> x.locked());

		int target = AdvisorAssign.targetAverage();

		for (Student student : students) {
			advisors.removeIf(x -> x.locked());
			for (Advisor advisor : advisors) {

				Course course = student.courseWithThisAdvisor(advisor);

				if (course != null) {
					System.out
							.println("STUDENT " + student.fullNameAndID() + " has class with advisor " + advisor.name);

					if (advisor.adviseeCount() <= target) {
						advisor.addAdvisee(student, true);
						
						if (advisor.adviseeCount() >= target) {
							advisor.setLocked();
							System.out.println("Advisor " + advisor.name + " is now locked.");
						}
						break;
					} else {
						advisor.setLocked();
						System.out.println("Advisor " + advisor.name + " is now locked.");
						continue;
					}
				}
			}
		}

		return true;

	}

}
