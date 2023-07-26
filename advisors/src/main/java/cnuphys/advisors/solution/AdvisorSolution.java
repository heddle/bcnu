package cnuphys.advisors.solution;

import java.util.List;

import cnuphys.advisors.Advisor;
import cnuphys.advisors.Student;
import cnuphys.advisors.dialogs.OptionsDialog;
import cnuphys.advisors.enums.EAlgorithm;
import cnuphys.advisors.enums.EReason;
import cnuphys.advisors.model.DataManager;
import cnuphys.advisors.simulation.AdvisorSimulation;
import cnuphys.advisors.table.InputOutput;
import cnuphys.advisors.util.Utilities;
import cnuphys.simanneal.Solution;

public class AdvisorSolution extends Solution {

	/** one student exchanged for this solution */
	public Student studentA;

	/** another student exchanged for this solution */
	public Student studentB;


	//the simulation owner
	private AdvisorSimulation _simulation;

	public AdvisorSolution(AdvisorSimulation simulation) {
		_simulation = simulation;
	}

	/**
	 * Copy constructor
	 * @param as the solution to copy
	 */
	public AdvisorSolution(AdvisorSolution as) {
		_simulation = as.getSimulation();
	}

	/**
	 * Accessor for the simulation owner
	 * @return the simulation
	 */
	public AdvisorSimulation getSimulation() {
		return _simulation;
	}


	@Override
	public double getEnergy() {

		List<Advisor> advisors = _simulation.advisors;
		List<Student> students = _simulation.students;
		double sum = 0;

		//good things should produce negative contributions
		//bad things should produce positive contributions
		sum += manyMajorsEnergy(advisors);

		if (OptionsDialog.currentAlgorithm == EAlgorithm.OptInFCAAndNumMaj) {
			sum += manyInAdvisorsClassEnergy(students);
		}

		return sum;
	}

	/**
	 * Compute the positive energy (penalty) associated with having to many majors.
	 * @param advisors
	 * @return the positive (bad) energy associate with multiple majors
	 */
	private double manyMajorsEnergy(List<Advisor> advisors) {
		int sum = 0;

		for (Advisor advisor : advisors) {
			int numMajor = advisor.numDiffMajorsAdvising();
			if (numMajor > 0) {
				//penalty is num different majors raised to the 1.5 power
				double penalty = Math.pow((numMajor-1), 1.5);
				sum += penalty;
			}
		}

		return sum;
	}

	/**
	 * Compute the negative energy (reward) associated with students being in an advisors class
	 * @param advisors
	 * @return
	 */
	private double manyInAdvisorsClassEnergy(List<Student> students) {
	//	return 275*(595-DataManager.studentsHavingAdvisorAsInstructorCount());
		return -2*DataManager.studentsHavingAdvisorAsInstructorCount();
	}

	@Override
	public Solution getRearrangement() {
		AdvisorSolution rearrangement = (AdvisorSolution) copy();

		List<Student> students = _simulation.students;
		if (students.isEmpty()) {
			System.err.println("Empty student collection in GetRearrangement");
		} else {
			// get two random students to exchange
			int ranNum = Utilities.randomInt(0, students.size() - 1);

			studentA = students.get(ranNum);
			studentB = studentA;

			while (studentA.advisor == studentB.advisor) {
				ranNum = Utilities.randomInt(0, students.size() - 1);
				studentB = students.get(ranNum);
			}

			// now exchange the students
			_simulation.exchangeStudents(studentA, studentB);
		}
		return rearrangement;
	}

	@Override
	public Solution copy() {
		return new AdvisorSolution(this);
	}

	@Override
	public double getPlotY() {
		return getEnergy();
	}

	/**
	 * Create the initial solution
	 * @return the initial solution
	 */
	public static AdvisorSolution initialSolution(AdvisorSimulation simulation) {


		InputOutput.debugPrintln("Creating the initial solution.");
		AdvisorSolution solution = new AdvisorSolution(simulation);

		// assign the students round robin style to acheive equitability in numbers
		if ((simulation.advisors != null) && (simulation.students != null)) {
			String s = String.format("Num Advisors: %d  Num Students: %d",
					simulation.advisors.size(), simulation.students.size());
			InputOutput.debugPrintln(s);
			DataManager.roundRobinAssign(simulation.advisors, simulation.students, false,
					"Error in Solution initalSolution", EReason.ALG);

			InputOutput.debugPrintln("Initial solution energy: " + solution.getEnergy());
		}

		return solution;
	}

}
