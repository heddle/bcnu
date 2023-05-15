package cnuphys.advisors.solution;

import java.util.Random;

import cnuphys.advisors.Student;
import cnuphys.advisors.simulation.AdvisorSimulation;
import cnuphys.advisors.table.InputOutput;
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
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Solution getRearrangement() {
		AdvisorSolution rearrangement = (AdvisorSolution)copy();
		
		
		
		return rearrangement;
	}

	@Override
	public Solution copy() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public double getPlotY() {
		// TODO Auto-generated method stub
		return 0;
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
