package cnuphys.advisors.checklist.steps;

import java.util.ArrayList;
import java.util.List;

import cnuphys.advisors.Advisor;
import cnuphys.advisors.AdvisorFilter;
import cnuphys.advisors.Student;
import cnuphys.advisors.StudentFilter;
import cnuphys.advisors.checklist.IAlgorithmStep;
import cnuphys.advisors.enums.Major;
import cnuphys.advisors.frame.AdvisorAssign;
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

		roundRobinAssign(musicAdvisors, musicStudents, "In Music assign");
		roundRobinAssign(theaterAdvisors, theaterStudents, "In Theater assign");
	
		return true;
	}


	private void roundRobinAssign(List<Advisor> advisors, List<Student> students, String errPrompt) {
		
		int numAdvisor = advisors.size();
		int numStudent = students.size();
		if ((numAdvisor == 0) || (numStudent == 0)) {
			return;
		}
		
		//the max to assign to any one advisor
		int target = AdvisorAssign.targetAverage();
		
		int advisorIndex = 0;
		
		for (Student student : students) {
			
			if (student.locked) {
				continue;
			}
			
			//get the next advisor with room
			
			int nTry = 0;
			boolean found = false;
			
			while (!found && (nTry < numAdvisor)) {
				Advisor advisor = advisors.get(advisorIndex);
				if (!advisor.locked && (advisor.adviseeCount() < target)) {
					advisor.addAdvisee(student, true);
					found = true;
				}
				else {
					nTry++;
					if (nTry == numAdvisor) {
						System.err.println(errPrompt + "  (Target max reached) RoundRobin failed to assign student: " + student.fullNameAndID());
					}
				}
				
				advisorIndex  = (advisorIndex + 1) % numAdvisor;

			}
			
			
		}

	}
	
}
