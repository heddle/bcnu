package cnuphys.advisors.checklist.steps;

import cnuphys.advisors.Person;
import cnuphys.advisors.checklist.CheckListLaunchable;
import cnuphys.advisors.model.AdvisorData;
import cnuphys.advisors.model.DataManager;
import cnuphys.advisors.model.StudentData;

public class CommunityCaptainStep extends CheckListLaunchable  {

	public CommunityCaptainStep(String info, boolean enabled) {
		super("CCPT", info, enabled);
	}

	/**
	 * Assign the community captain advisees
	 */
	@Override
	public void launch() {
		//get the community advisors and students
		AdvisorData advisorData = DataManager.getFilteredAdvisorData(Person.CCPT);
		StudentData studentData = DataManager.getFilteredStudentData(Person.CCPT);
		DataManager.roundRobinAssign(advisorData.getAdvisors(), studentData.getStudents(), true, "In Community Captain assign");
	}
}
