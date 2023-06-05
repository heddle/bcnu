package cnuphys.advisors.checklist.steps;

import java.util.List;

import cnuphys.advisors.Advisor;
import cnuphys.advisors.Student;
import cnuphys.advisors.checklist.CheckListLaunchable;
import cnuphys.advisors.enums.Major;
import cnuphys.advisors.enums.Specialty;
import cnuphys.advisors.model.DataManager;

public class StudentsMajorStep extends CheckListLaunchable {

	public StudentsMajorStep(String info, boolean enabled) {
		super("Students by  Major", info, enabled);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void launch() {
		
		//first for specialty
		for (Specialty specialty : Specialty.values()) {
			if (specialty != Specialty.NONE) {
				//get unassigned honors students and honors advisor
				List<Student> students = DataManager.getUnassignedStudentsForSpecialty(specialty);
				List<Advisor> advisors = DataManager.getAdvisorsForSpecialty(specialty);

				//remove locked students and advisors
				students.removeIf(x -> x.locked());
				advisors.removeIf(x -> x.locked());

				DataManager.roundRobinAssign(advisors, students, true, "Students by Specialty");
				
			}
		}

		// then by major
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

		//then for secondary major
		for (Major major : Major.values()) {

			//get unassigned honors students and honors advisor
			List<Student> students = DataManager.getUnassignedStudentsForMajor(major);
			List<Advisor> advisors = DataManager.getAdvisorsForSecondaryMajor(major);

			//remove locked students and advisors
			students.removeIf(x -> x.locked());
			advisors.removeIf(x -> x.locked());

			DataManager.roundRobinAssign(advisors, students, true, "Students by Secondary FCA Major");

		}
	}

}
