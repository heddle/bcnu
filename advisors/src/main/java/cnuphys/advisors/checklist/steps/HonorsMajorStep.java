package cnuphys.advisors.checklist.steps;

import java.util.List;

import cnuphys.advisors.Advisor;
import cnuphys.advisors.Student;
import cnuphys.advisors.checklist.CheckListLaunchable;
import cnuphys.advisors.enums.Major;
import cnuphys.advisors.enums.Specialty;
import cnuphys.advisors.model.DataManager;

/**
 * Assign the honors students
 * @author heddle
 *
 */
public class HonorsMajorStep extends CheckListLaunchable {

	public HonorsMajorStep(String info, boolean enabled) {
		super("Honors by Major", info, enabled);
	}

	@Override
	public void launch() {

		//first for specialty
		for (Specialty specialty : Specialty.values()) {
			if (specialty != Specialty.NONE) {
				//get unassigned honors students and honors advisor
				List<Student> students = DataManager.getUnassignedHonorsStudentsForSpecialty(specialty);
				List<Advisor> advisors = DataManager.getHonorsAdvisorsForSpecialty(specialty);

				// remove locked students and advisors
				students.removeIf(x -> x.locked());
				advisors.removeIf(x -> x.locked());

				DataManager.roundRobinAssign(advisors, students, true, "Students by Specialty");

			}

			// then by major
			// this does not reassign students so only used unassigned
			for (Major major : Major.values()) {

				// get unassigned honors students and honors advisor
				List<Student> students = DataManager.getUnassignedHonorsStudentsForMajor(major);
				List<Advisor> advisors = DataManager.getHonorsAdvisorsForMajor(major);

				// remove locked students and advisors
				students.removeIf(x -> x.locked());
				advisors.removeIf(x -> x.locked());

				DataManager.roundRobinAssign(advisors, students, true, "Honors by Major");

			}

		//then for secondary major
		for (Major major : Major.values()) {

			//get unassigned honors students and honors advisor
			List<Student> students = DataManager.getUnassignedHonorsStudentsForMajor(major);
			List<Advisor> advisors = DataManager.getHonorsAdvisorsForSecondaryMajor(major);

			//remove locked students and advisors
			students.removeIf(x -> x.locked());
			advisors.removeIf(x -> x.locked());

			DataManager.roundRobinAssign(advisors, students, true, "Honors by Secondary FCA Major");

		}
		
		}
	}


}
