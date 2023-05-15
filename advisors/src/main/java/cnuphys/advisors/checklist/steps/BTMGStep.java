package cnuphys.advisors.checklist.steps;

import cnuphys.advisors.Person;
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
		AdvisorData advisorData = DataManager.getFilteredAdvisorData(Person.BTMG);
		StudentData studentData = DataManager.getFilteredStudentData(Person.BTMG);
		DataManager.roundRobinAssign(advisorData.getAdvisors(), studentData.getStudents(), true, "In BTMG assign");

		return true;
	}


}
