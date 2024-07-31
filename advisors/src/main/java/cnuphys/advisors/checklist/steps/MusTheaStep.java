package cnuphys.advisors.checklist.steps;

import java.util.List;

import cnuphys.advisors.Advisor;
import cnuphys.advisors.Student;
import cnuphys.advisors.checklist.CheckListLaunchable;
import cnuphys.advisors.enums.EReason;
import cnuphys.advisors.enums.Major;
import cnuphys.advisors.model.DataManager;

public class MusTheaStep extends CheckListLaunchable  {

	public MusTheaStep(String info, boolean enabled) {
		super("MUSC/THEA", info, enabled);
	}

	/**
	 * Assign the Music and Theater advisees
	 */
	@Override
	public void launch() {
		List<Student> musicStudents = DataManager.getStudentsForMajor(Major.MUSIC);
		List<Student> theaterStudents = DataManager.getStudentsForMajor(Major.THEA);

		List<Advisor> musicAdvisors = DataManager.getAdvisorsForMajorALC(Major.MUSIC);
		List<Advisor> theaterAdvisors = DataManager.getAdvisorsForMajorALC(Major.THEA);

		DataManager.roundRobinAssign(musicAdvisors, musicStudents, true, "In Music assign", EReason.ALC);
		DataManager.roundRobinAssign(theaterAdvisors, theaterStudents, true, "In Theater assign", EReason.ALC);
	
		
		musicAdvisors = DataManager.getAdvisorsForMajorNotALC(Major.MUSIC);
		theaterAdvisors = DataManager.getAdvisorsForMajorNotALC(Major.THEA);

		DataManager.roundRobinAssign(musicAdvisors, musicStudents, true, "In Music assign", EReason.MUSC);
		DataManager.roundRobinAssign(theaterAdvisors, theaterStudents, true, "In Theater assign", EReason.THEA);
	}

}
