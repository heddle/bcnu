package cnuphys.advisors.simulation;

import cnuphys.advisors.model.DataManager;
import cnuphys.advisors.solution.AdvisorSolution;
import cnuphys.advisors.table.InputOutput;
import cnuphys.simanneal.Simulation;
import cnuphys.simanneal.SimulationAttributes;
import cnuphys.simanneal.Solution;

public class AdvisorSimulation extends Simulation {
	
	/**
	 * A custom attribute for the number of advisors
	 */
	public static final String NUMADVISOR = "advisor count";
	
	/**
	 * A custom attribute for the number of students
	 */
	public static final String NUMSTUDENT = "student count";
	
	/**
	 * A custom attribute for the avg number of advisees
	 */
	public static final String AVGCOHORT = "target cohort size";


	/**
	 * Create an advisor simulation
	 */
	public AdvisorSimulation() {
		
	}

	@Override
	protected void setInitialAttributes() {
		_attributes.add(NUMADVISOR, 0, false, false);
		_attributes.add(NUMSTUDENT, 0, false, false);
		_attributes.add(AVGCOHORT, 0, false, false);
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
		
		_attributes.setValue(NUMADVISOR, DataManager.getAdvisorData().count());		
		_attributes.setValue(NUMSTUDENT, DataManager.getStudentData().count());	
		_attributes.setValue(AVGCOHORT, String.format("%4.1f", DataManager.targetCohort()));	
	}


}
