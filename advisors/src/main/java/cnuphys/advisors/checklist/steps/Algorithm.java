package cnuphys.advisors.checklist.steps;

import java.util.List;

import cnuphys.advisors.Advisor;
import cnuphys.advisors.Student;
import cnuphys.advisors.frame.AdvisorAssign;

public class Algorithm {

	/**
	 * Run the simulated annealing algorithm
	 * @param students a list of unassigned students
	 * @param advisor a list of advisors
	 */
	public static boolean runAlgorithm(List<Student> students, List<Advisor> advisors) {
		if (!check(students, advisors)) {
			return false;
		}
		
		return true;
	}
	
	private static boolean check(List<Student> students, List<Advisor> advisors) {
		
		for (Student student : students) {
			if (student.locked) {
				System.err.println("LOCKED student passed to Algorithm " 
			+ student.fullNameAndID() + "  advisor: " + student.advisor.name);
				return false;
			}
		}
		
		int target = AdvisorAssign.targetAverage();
		
		int space = 0;
		//count advisor space
		for (Advisor advisor : advisors) {
			int avail = target - advisor.adviseeCount();
			if (avail < 0) {
				avail = 0;
			}
			
			space += avail;
		}
		
		String s = String.format("Num students: %d   space: %d   target: %d", students.size(), space, target);
		System.err.println(s);
		return true;
	}
}
