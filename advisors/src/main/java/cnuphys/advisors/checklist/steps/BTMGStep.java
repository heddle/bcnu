package cnuphys.advisors.checklist.steps;

import cnuphys.advisors.AdvisorFilter;
import cnuphys.advisors.StudentFilter;
import cnuphys.advisors.checklist.IAlgorithmStep;
import cnuphys.advisors.model.AdvisorData;
import cnuphys.advisors.model.DataManager;
import cnuphys.advisors.model.StudentData;

public class BTMGStep implements IAlgorithmStep   {

	/**
	 * Assign the bio tech and management advisees
	 */
	@Override
	public boolean run() {

		//get the community advisors and students
		AdvisorData advisorData = DataManager.getFilteredAdvisorData(AdvisorFilter.btmgAdvisors);
		StudentData studentData = DataManager.getFilteredStudentData(StudentFilter.btmg);
		DataManager.roundRobinAssign(advisorData.getAdvisors(), studentData.getStudents(), "In BTMG assign");

		return true;
	}


}
