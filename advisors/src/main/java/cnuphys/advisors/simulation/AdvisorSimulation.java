package cnuphys.advisors.simulation;

import cnuphys.advisors.model.DataManager;
import cnuphys.advisors.solution.AdvisorSolution;
import cnuphys.advisors.table.InputOutput;
import cnuphys.simanneal.Simulation;
import cnuphys.simanneal.SimulationAttributes;
import cnuphys.simanneal.Solution;

public class AdvisorSimulation extends Simulation {


	/**
	 * Create an advisor simulation
	 */
	public AdvisorSimulation() {

	}

	@Override
	protected void setInitialAttributes() {
		_attributes.removeAttribute(SimulationAttributes.USELOGTEMP);
		_attributes.setPlotTitle("Assignment Quality");
		_attributes.setYAxisLabel("1/Quality");

	}

	@Override
	protected Solution setInitialSolution() {
		finalPrep();
		return AdvisorSolution.initialSolution();
	}

	//last minute preparation
	private void finalPrep() {
		DataManager.init();
		InputOutput.init();
	}


}
