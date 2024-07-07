package cnuphys.advisors.checklist.steps;

import java.util.List;

import cnuphys.advisors.Advisor;
import cnuphys.advisors.Student;
import cnuphys.advisors.checklist.CheckListLaunchable;
import cnuphys.advisors.frame.AdvisorAssign;
import cnuphys.advisors.model.AdvisorData;
import cnuphys.advisors.model.DataManager;
import cnuphys.advisors.simulation.AdvisorSimulation;

public class StudentsAlgorithmStep extends CheckListLaunchable {

	public StudentsAlgorithmStep(String info, boolean enabled) {
		super("Students Algorithm", info, enabled);
	}

	@Override
	public void launch() {
		//now run the algorithm with all unassigned students and all
		// unlocked advisors
		AdvisorData advisorData = DataManager.getAdvisorData();

		List<Advisor> advisors = advisorData.getAdvisors();
		advisors.removeIf(x -> x.locked());
		
		int target= AdvisorAssign.targetAverage();
		
		//what is out availability of unlocked slots?
		int available = 0;
		for (Advisor advisor : advisors) {
			available += target - advisor.adviseeCount();
		}
		

		List<Student> students = DataManager.getUnassignedStudents();
		
		System.out.println("advisor slots: " + available + " unassigned students: " + students.size());

		Algorithm.runAlgorithm(students, advisors);

		try {
			AdvisorSimulation.getInstance().getSimThread().join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}


		System.out.println("Student algorithm complete");

		//lock students
		for (Student student : students) {
			student.setLocked();
		}

		//lock down any advisors at target
		target = AdvisorAssign.targetAverage();
		for (Advisor advisor : advisors) {
			if (advisor.adviseeCount() >= target) {
				advisor.setLocked();
			}
		}
	}
}
