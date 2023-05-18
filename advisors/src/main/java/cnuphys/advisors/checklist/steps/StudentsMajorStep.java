package cnuphys.advisors.checklist.steps;

import java.util.List;

import cnuphys.advisors.Advisor;
import cnuphys.advisors.Student;
import cnuphys.advisors.checklist.IAlgorithmStep;
import cnuphys.advisors.enums.Major;
import cnuphys.advisors.model.DataManager;

public class StudentsMajorStep implements IAlgorithmStep {
	
	@Override
	public boolean run() {
		// first, assign by major
		// this does not reassign students so only used unassigned

		
		//first for the advisors "subject")
		for (Major major : Major.values()) {

			//get unassigned honors students and honors advisor
			List<Student> students = DataManager.getUnassignedStudentsForMajor(major);
			List<Advisor> advisors = DataManager.getAdvisorsForMajor(major);

			//remove locked students and advisors
			students.removeIf(x -> x.locked());
			advisors.removeIf(x -> x.locked());

			DataManager.roundRobinAssign(advisors, students, true, "Students by Major");

		}
		
//		//then for secondary major 
		for (Major major : Major.values()) {

			//get unassigned honors students and honors advisor
			List<Student> students = DataManager.getUnassignedStudentsForMajor(major);
			List<Advisor> advisors = DataManager.getAdvisorsForSecondaryMajor(major);

			//remove locked students and advisors
			students.removeIf(x -> x.locked());
			advisors.removeIf(x -> x.locked());

			DataManager.roundRobinAssign(advisors, students, true, "Students by Secondary FCA Major");

		}


		return true;
	}


}
