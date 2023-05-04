package cnuphys.advisors.checklist.steps;

import java.util.List;

import cnuphys.advisors.Student;
import cnuphys.advisors.StudentFilter;
import cnuphys.advisors.checklist.IAlgorithmStep;
import cnuphys.advisors.model.DataManager;

/**
 * This assigns by major
 * @author heddle
 *
 */
public class ByMajorStep implements IAlgorithmStep  {

	@Override
	public boolean run() {
		//first, by pure major, nore preferred major
		//this step does not reassign so onlu used unassigned
		List<Student> students = DataManager.getFilteredStudentData(StudentFilter.unassignedStudents).getStudents();
		
		return true;

	}

}
