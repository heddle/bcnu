package cnuphys.advisors.simulation;

import java.util.List;

import cnuphys.advisors.Advisor;
import cnuphys.advisors.Student;
import cnuphys.advisors.model.DataManager;
import cnuphys.advisors.solution.AdvisorSolution;
import cnuphys.advisors.table.InputOutput;
import cnuphys.simanneal.IUpdateListener;
import cnuphys.simanneal.Simulation;
import cnuphys.simanneal.SimulationAttributes;
import cnuphys.simanneal.SimulationState;
import cnuphys.simanneal.Solution;

public class AdvisorSimulation extends Simulation implements IUpdateListener {
	
	private boolean _inited;
	
	/** 
	 * The list of available advisors
	 */
	public List<Advisor> advisors;
	
	/**
	 * The list of students who need an assignment
	 */
	public List<Student> students;
	
	//singleton
	private static AdvisorSimulation _instance;


	/**
	 * Create an advisor simulation
	 */
	private AdvisorSimulation() {
		addUpdateListener(this);
	}
	
	/**
	 * public access to the singleton
	 * @return the simulation object
	 */
	public static AdvisorSimulation getInstance() {
		if (_instance == null) {
			_instance = new AdvisorSimulation();
		}
		return _instance;
	}

	@Override
	protected void setInitialAttributes() {
		_attributes.removeAttribute(SimulationAttributes.USELOGTEMP);
		_attributes.setPlotTitle("Assignment Energy");
		_attributes.setYAxisLabel("Energy");
		_attributes.setMinTemp(0.01);
		_attributes.setCoolRate(0.003);
		_attributes.setMaxSteps(2000);
		_attributes.setThermalizationCount(400);
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
		System.out.println("resetting the simulation");
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

					if (metrop(eCurrent, eTest)) {  //accept rearrangement
						_currentSolution = rearrangement;
						eCurrent = eTest;
						succ++;
						if (succ > _attributes.getSuccessCount()) {
							break;
						}
					} else {
						AdvisorSolution advSol = (AdvisorSolution)_currentSolution;
						exchangeStudents(advSol.studentA, advSol.studentB);
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

	/**
	 * Exchange two students
	 * @param studentA one student
	 * @param studentB other student
	 */
	public void exchangeStudents(Student studentA, Student studentB) {
		Advisor advisorA = studentA.advisor;
		Advisor advisorB = studentB.advisor;
		
		advisorA.removeAdvisee(studentA);
		advisorB.removeAdvisee(studentB);
		
		advisorA.addAdvisee(studentB, false);
		advisorB.addAdvisee(studentA, false);
		
		studentA.advisor = advisorB;
		studentB.advisor = advisorA;
	}

	@Override
	public void updateSolution(Simulation simulation, Solution newSolution, Solution oldSolution) {
		String s = String.format("SOLUTION UPDATE. Temp = %12.8f  Energy = %7.3f", simulation.getTemperature(), newSolution.getEnergy());
		System.err.println(s);
	}

	@Override
	public void reset(Simulation simulation) {
		System.err.println("SIMULATION RESET");
	}

	@Override
	public void stateChange(Simulation simulation, SimulationState oldState, SimulationState newState) {
		System.err.println("SIMULATION STATE CHANGE TO " + newState.name());
		
		
		
	}

}
