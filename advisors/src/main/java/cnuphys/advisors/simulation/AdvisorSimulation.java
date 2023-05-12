package cnuphys.advisors.simulation;

import java.util.List;

import cnuphys.advisors.Advisor;
import cnuphys.advisors.Student;
import cnuphys.advisors.model.DataManager;
import cnuphys.advisors.solution.AdvisorSolution;
import cnuphys.advisors.table.InputOutput;
import cnuphys.simanneal.Simulation;
import cnuphys.simanneal.SimulationAttributes;
import cnuphys.simanneal.SimulationState;
import cnuphys.simanneal.Solution;

public class AdvisorSimulation extends Simulation {
	
	private boolean _inited;
	
	/** 
	 * The list of available advisors
	 */
	public List<Advisor> advisors;
	
	/**
	 * The list of students who need an asignment
	 */
	public List<Student> students;


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
		return AdvisorSolution.initialSolution(this);
	}

	//last minute preparation
	private void finalPrep() {
		if (_inited) {
			return;
		}
		DataManager.init();
		InputOutput.init();
		_inited = true;
	}

	/**
	 * Reset the simulation
	 * @param advisors the list of available advisors
	 * @param students the list of available students
	 */
	public void reset(List<Advisor> advisors, List<Student> students) {
		this.advisors = advisors;
		this.students = students;
		super.reset();

	}
	
	//override the run method so we can customize the rearranhement
	@Override
	public void run() {

		double factor = 1. - _attributes.getCoolRate();
		Solution oldSolution = _currentSolution.copy();

		int step = 0;

		while ((_simState != SimulationState.STOPPED) && (step < _attributes.getMaxSteps())
				&& (_temperature > _attributes.getMinTemp())) {

			if (_simState == SimulationState.PAUSED) {
				// sleep for a second
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

			} else if (_simState == SimulationState.RUNNING) { // running
				int succ = 0;

				double eCurrent = _currentSolution.getEnergy();

				for (int i = 0; i < _attributes.getThermalizationCount(); i++) {
					Solution rearrangement = _currentSolution.getRearrangement();
					double eTest = rearrangement.getEnergy();

					if (metrop(eCurrent, eTest)) {
						_currentSolution = rearrangement;
						eCurrent = eTest;
						succ++;
						if (succ > _attributes.getSuccessCount()) {
							break;
						}
					}

				}

				// reduce the temperature
				_temperature *= factor;

				// System.err.println("Current temp: " + _temperature);
				step++;
				notifyListeners(_currentSolution, oldSolution);
			} // running
		} // while

		setSimulationState(SimulationState.STOPPED);
	}


}
