package cnuphys.advisors.checklist.steps;

import java.util.List;

import cnuphys.advisors.Advisor;
import cnuphys.advisors.Student;
import cnuphys.advisors.checklist.IAlgorithmStep;
import cnuphys.advisors.frame.AdvisorAssign;
import cnuphys.advisors.model.AdvisorData;
import cnuphys.advisors.model.DataManager;
import cnuphys.advisors.simulation.AdvisorSimulation;

public class StudentsAlgorithmStep implements IAlgorithmStep {

	@Override
	public boolean run() {
		//now run the algorithm with all unassigned students and all
		// unlocked advisors
		AdvisorData advisorData = DataManager.getAdvisorData();

		List<Advisor> advisors = advisorData.getAdvisors();
		advisors.removeIf(x -> x.locked());

		List<Student> students = DataManager.getUnassignedStudents();
		
		System.err.println("Run Student algorithm");
		Algorithm.runAlgorithm(students, advisors);
		
		try {
			AdvisorSimulation.getInstance().getSimThread().join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		
		System.err.println("Student algorithm complete");
		
		//lock students
		for (Student student : students) {
			student.setLocked();
		}
		
		//lock down any advisors at target
		int target = AdvisorAssign.targetAverage();
		for (Advisor advisor : advisors) {
			if (advisor.adviseeCount() >= target) {
				advisor.setLocked();
			}
		}


		return true;
	}

}
