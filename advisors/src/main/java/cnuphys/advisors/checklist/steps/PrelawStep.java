package cnuphys.advisors.checklist.steps;

import cnuphys.advisors.Person;
import cnuphys.advisors.checklist.CheckListLaunchable;
import cnuphys.advisors.model.AdvisorData;
import cnuphys.advisors.model.DataManager;
import cnuphys.advisors.model.StudentData;

public class PrelawStep extends CheckListLaunchable {

	public PrelawStep(String info, boolean enabled) {
		super("Prelaw", info, enabled);
	}

	/**
	 * Assign the bio tech and management advisees
	 */
	@Override
	public void launch() {

		//get the community advisors and students
		AdvisorData advisorData = DataManager.getFilteredAdvisorData(Person.PRELAW);
		StudentData studentData = DataManager.getFilteredStudentData(Person.PRELAW);
		DataManager.roundRobinAssign(advisorData.getAdvisors(), studentData.getStudents(), true, "In Prelaw assign");
	}

}
