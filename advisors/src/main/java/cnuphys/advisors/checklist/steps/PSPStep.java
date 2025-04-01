package cnuphys.advisors.checklist.steps;

import java.util.List;

import cnuphys.advisors.Advisor;
import cnuphys.advisors.Person;
import cnuphys.advisors.Student;
import cnuphys.advisors.checklist.CheckListLaunchable;
import cnuphys.advisors.enums.EReason;
import cnuphys.advisors.model.DataManager;

public class PSPStep extends CheckListLaunchable {

	public PSPStep(String info, boolean enabled) {
		super("Premed Scholars", info, enabled);
	}

	@Override
	public void launch() {

		List<Advisor> advisors = DataManager.getFilteredAdvisorData(Person.PREMEDSCHOLAR).getAdvisors();
		advisors.removeIf(x -> x.locked());

		List<Student> students = DataManager.getUnassignedPSPStudents();

		DataManager.roundRobinAssign(advisors, students, true, "PSP students", EReason.PSP);
	}

}
