package cnuphys.advisors.checklist.steps;

import java.util.List;

import cnuphys.advisors.Advisor;
import cnuphys.advisors.Person;
import cnuphys.advisors.Student;
import cnuphys.advisors.checklist.IAlgorithmStep;
import cnuphys.advisors.frame.AdvisorAssign;
import cnuphys.advisors.model.AdvisorData;
import cnuphys.advisors.model.DataManager;
import cnuphys.advisors.simulation.AdvisorSimulation;

public class HonorsAlgorithmStep implements IAlgorithmStep {

	@Override
	public boolean run() {
				
		//now run the algorithm with unassigned honors students and unlocked
		//honors advisors
		AdvisorData advisorData = DataManager.getFilteredAdvisorData(Person.HONOR);
		List<Advisor> advisors = advisorData.getAdvisors();
		advisors.removeIf(x -> x.locked());

		List<Student> students = DataManager.getUnassignedHonorsStudents();

		Algorithm.runAlgorithm(students, advisors);

		try {
			AdvisorSimulation.getInstance().getSimThread().join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}


		System.out.println("Honors algorithm complete");

		//lock students
		for (Student student : students) {
			student.setLocked();
		}

		//lock down any advisors at target and lock down honors director
		int target = AdvisorAssign.targetAverage();
		for (Advisor advisor : advisors) {
			if ((advisor.adviseeCount() >= target) || (advisor == DataManager.honorsDirector)) {
				advisor.setLocked();
			}
		}



		return true;
	}

}
