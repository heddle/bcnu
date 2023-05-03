package cnuphys.advisors.checklist.steps;

import java.util.List;

import cnuphys.advisors.Advisor;
import cnuphys.advisors.AdvisorFilter;
import cnuphys.advisors.Student;
import cnuphys.advisors.StudentFilter;
import cnuphys.advisors.checklist.IAlgorithmStep;
import cnuphys.advisors.model.AdvisorData;
import cnuphys.advisors.model.DataManager;
import cnuphys.advisors.model.StudentData;

public class PresScholarStep implements IAlgorithmStep {

	@Override
	public boolean run() {
		//get the pres scholar advisors and students
		AdvisorData advisorData = DataManager.getFilteredAdvisorData(AdvisorFilter.presScholarAdvisors);

		StudentData studentData = DataManager.getFilteredStudentData(StudentFilter.presScholars);

		System.err.println("Assigning pres scholars. Advisor count = " + advisorData.count() + "  student count = " + studentData.count());

		List<Advisor> advisors = advisorData.getAdvisors();
		List<Student> students = studentData.getStudents();

		int i = 0;
		int advCount = advisors.size();

		for (Student student : students) {
			int index = i % advCount;
			
			student.locked = false;
			
			advisors.get(index).addAdvisee(student, true);
			i++;
		}

		//lock down the pres scholar advisors
		for (Advisor advisor : advisorData.getAdvisors()) {
			advisor.locked = true;
		}

		return true;
	}

}
