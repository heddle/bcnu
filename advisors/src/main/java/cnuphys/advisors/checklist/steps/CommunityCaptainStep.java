package cnuphys.advisors.checklist.steps;

import cnuphys.advisors.Person;
import cnuphys.advisors.checklist.IAlgorithmStep;
import cnuphys.advisors.model.AdvisorData;
import cnuphys.advisors.model.DataManager;
import cnuphys.advisors.model.StudentData;

public class CommunityCaptainStep implements IAlgorithmStep   {

	/**
	 * Assign the community captain advisees
	 */
	@Override
	public boolean run() {

		//get the community advisors and students
		AdvisorData advisorData = DataManager.getFilteredAdvisorData(Person.CCPT);
		StudentData studentData = DataManager.getFilteredStudentData(Person.CCPT);
		DataManager.roundRobinAssign(advisorData.getAdvisors(), studentData.getStudents(), "In Community Captain assign");

		return true;
	}


}
