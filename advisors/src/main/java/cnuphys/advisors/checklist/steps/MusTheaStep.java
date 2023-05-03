package cnuphys.advisors.checklist.steps;

import java.util.List;

import cnuphys.advisors.Advisor;
import cnuphys.advisors.Student;
import cnuphys.advisors.checklist.IAlgorithmStep;
import cnuphys.advisors.enums.Major;
import cnuphys.advisors.model.DataManager;

public class MusTheaStep implements IAlgorithmStep  {

	/**
	 * Assign the Music and Theater advisees
	 */
	@Override
	public boolean run() {
		List<Student> musicStudents = DataManager.getStudentsForMajor(Major.MUSIC);
		List<Student> theaterStudents = DataManager.getStudentsForMajor(Major.THEA);

		List<Advisor> musicAdvisors = DataManager.getAdvisorsForMajor(Major.MUSIC);
		List<Advisor> theaterAdvisors = DataManager.getAdvisorsForMajor(Major.THEA);

		DataManager.roundRobinAssign(musicAdvisors, musicStudents, "In Music assign");
		DataManager.roundRobinAssign(theaterAdvisors, theaterStudents, "In Theater assign");

		return true;
	}



}
