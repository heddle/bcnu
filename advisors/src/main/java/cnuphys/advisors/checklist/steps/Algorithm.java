package cnuphys.advisors.checklist.steps;

import java.util.List;

import cnuphys.advisors.Advisor;
import cnuphys.advisors.Student;
import cnuphys.advisors.frame.AdvisorAssign;
import cnuphys.advisors.simulation.AdvisorSimulation;

public class Algorithm {

	/**
	 * Run the simulated annealing algorithm
	 * @param students a list of unassigned students
	 * @param advisor a list of advisors
	 */
	public static boolean runAlgorithm(List<Student> students, List<Advisor> advisors) {

		//remove locked students and advisors
		students.removeIf(x -> x.locked());
		advisors.removeIf(x -> x.locked());

		if (!check(students, advisors)) {
			return false;
		}
		
		AdvisorSimulation sim = AdvisorAssign.

		return true;
	}

	//check that the initial lists are valid
	private static boolean check(List<Student> students, List<Advisor> advisors) {
		System.out.println("Algorithm start check");

		for (Student student : students) {
			if (student.locked()) {

				String message = "LOCKED student passed to Algorithm "
						+ student.fullNameAndID() + "  advisor: " + student.advisor.name;

				System.err.println(message);

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
		System.out.println(s);
		return true;
	}
}
