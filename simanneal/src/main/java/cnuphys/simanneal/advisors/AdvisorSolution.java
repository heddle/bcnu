package cnuphys.simanneal.advisors;

import cnuphys.simanneal.Solution;
import cnuphys.simanneal.advisors.table.InputOutput;

public class AdvisorSolution extends Solution {

	@Override
	public double getEnergy() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Solution getRearrangement() {
		// TODO Auto-generated method stub
		return null;
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
	public static AdvisorSolution initialSolution() {
		
		InputOutput.debugPrintln("Creating the initial solution");
		AdvisorSolution solution = new AdvisorSolution();
		
		return solution;
	}

}
