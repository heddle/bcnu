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

public class HonorsStudentInClassStep implements IAlgorithmStep {
	
	@Override
	public boolean run() {
		
		AdvisorData advisorData = DataManager.getFilteredAdvisorData(Person.HONOR);
		List<Advisor> advisors = advisorData.getAdvisors();

		List<Student> students = DataManager.getUnassignedHonorsStudents();
		
		int target = AdvisorAssign.targetAverage();

		for (Student student : students) {
			advisors.removeIf(x -> x.locked());

			for (Advisor advisor : advisors) {

				// do not assign to j paul because all have a class with him
				if (advisor == DataManager.honorsDirector) {
					continue;
				}
				Course course = student.courseWithThisAdvisor(advisor);

				if (course != null) {
					System.out
							.println("STUDENT " + student.fullNameAndID() + " has class with advisor " + advisor.name);
					
					if (advisor.adviseeCount() <= target) {
						advisor.addAdvisee(student, true);
						
						if (advisor.adviseeCount() >= target) {
							advisor.setLocked();
							System.out.println("Honors Advisor " + advisor.name + " is now locked.");
						}

						break;
					}

				}
			}
		}

		
		return true;
	}
	

}
