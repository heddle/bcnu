package cnuphys.advisors.checklist.steps;

import cnuphys.advisors.AdvisorFilter;
import cnuphys.advisors.StudentFilter;
import cnuphys.advisors.checklist.IAlgorithmStep;
import cnuphys.advisors.model.AdvisorData;
import cnuphys.advisors.model.DataManager;
import cnuphys.advisors.model.StudentData;

public class PrelawStep implements IAlgorithmStep {

	/**
	 * Assign the bio tech and management advisees
	 */
	@Override
	public boolean run() {

		//get the community advisors and students
		AdvisorData advisorData = DataManager.getFilteredAdvisorData(AdvisorFilter.prelawAdvisors);
		StudentData studentData = DataManager.getFilteredStudentData(StudentFilter.prelawStudents);
		DataManager.roundRobinAssign(advisorData.getAdvisors(), studentData.getStudents(), "In Prelaw assign");

		return true;
	}


}
