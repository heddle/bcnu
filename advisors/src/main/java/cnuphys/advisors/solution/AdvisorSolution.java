package cnuphys.advisors.solution;

import java.util.List;

import cnuphys.advisors.Advisor;
import cnuphys.advisors.Student;
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
		
		//good things should produce positive contributions
		//bad things should produce negative contributions
		
		sum += manyMajorsEnergy(advisors);
		
		return -sum;
	}
	
	private final int manyMajorsStrength = 2;

	/**
	 * Compute the negative energy (penalty) associated with having to many majors.
	 * @param advisors
	 * @return
	 */
	private double manyMajorsEnergy(List<Advisor> advisors) {
		int sum = 0;
		
		for (Advisor advisor : advisors) {
			sum += advisor.numMajorsAdvising();
		}
		
		return -manyMajorsStrength * sum;
	}

	@Override
	public Solution getRearrangement() {
		AdvisorSolution rearrangement = (AdvisorSolution)copy();
		
		List<Student> students = _simulation.students;
		
		//get two random students to exchange
		
		int ranNum = Utilities.randomInt(0, students.size()-1);
		
		studentA = students.get(ranNum);
		studentB = studentA;
		
		while (studentB.advisor == studentB.advisor) {
			ranNum = Utilities.randomInt(0, students.size()-1);
			studentB = students.get(ranNum);
		}
		
		//now exchange the students
		_simulation.exchangeStudents(studentA, studentB);
		
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

		InputOutput.debugPrintln("Creating the initial solution");
		AdvisorSolution solution = new AdvisorSolution(simulation);

		return solution;
	}

}
