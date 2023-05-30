package cnuphys.advisors.checklist.steps;

import java.util.List;

import cnuphys.advisors.Advisor;
import cnuphys.advisors.Person;
import cnuphys.advisors.Student;
import cnuphys.advisors.checklist.IAlgorithmStep;
import cnuphys.advisors.model.DataManager;

public class PSPStep implements IAlgorithmStep {

	@Override
	public boolean run() {
		
		List<Advisor> advisors = DataManager.getFilteredAdvisorData(Person.PREMEDSCHOLAR).getAdvisors();
		advisors.removeIf(x -> x.locked());

		List<Student> students = DataManager.getUnassignedPSPStudents();

		DataManager.roundRobinAssign(advisors, students, true, "PSP students");

		return true;
	}

}
