package cnuphys.advisors.checklist.steps;

import java.util.List;

import cnuphys.advisors.Advisor;
import cnuphys.advisors.Person;
import cnuphys.advisors.Student;
import cnuphys.advisors.checklist.IAlgorithmStep;
import cnuphys.advisors.model.AdvisorData;
import cnuphys.advisors.model.DataManager;

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
		return true;
	}

} 
