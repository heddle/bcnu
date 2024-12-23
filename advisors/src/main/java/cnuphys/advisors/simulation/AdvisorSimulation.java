package cnuphys.advisors.simulation;

import java.util.List;

import cnuphys.advisors.Advisor;
import cnuphys.advisors.Student;
import cnuphys.advisors.enums.EReason;
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

	private int _step;


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
		_attributes.removeAttribute(SimulationAttributes.MAXSTEPS);
		_attributes.removeAttribute(SimulationAttributes.SUCCESSCOUNT);
		_attributes.setPlotTitle("Assignment Energy");
		_attributes.setYAxisLabel("Energy");
		_attributes.setMinTemp(0.005);
		_attributes.setCoolRate(0.0015);
	//	_attributes.setMaxSteps(20000);
		_attributes.setThermalizationCount(1400);
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

		//this will cause all data to be read in
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



	//override the run method so we can customize the rearrangement
	@Override
	public void run() {

		double factor = 1. - _attributes.getCoolRate();
		Solution oldSolution = _currentSolution.copy();

		_step = 0;

		//compute max steps
		int maxSteps = -(int) (Math.log(_temperature/_attributes.getMinTemp())/Math.log(factor));

		while ((_simState != SimulationState.STOPPED) && (_step < maxSteps)) {

			if (_simState == SimulationState.PAUSED) {
				// sleep for a second
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

			} else if (_simState == SimulationState.RUNNING) { // running
//				int succ = 0;

				double eCurrent = _currentSolution.getEnergy();

				for (int i = 0; i < _attributes.getThermalizationCount(); i++) {
					Solution rearrangement = _currentSolution.getRearrangement();
					double eTest = rearrangement.getEnergy();

					if (metrop(eCurrent, eTest)) {  //accept rearrangement
						_currentSolution = rearrangement;
						eCurrent = eTest;
//						succ++;
//						if (succ > _attributes.getSuccessCount()) {
//							System.out.println("Breaking on success count");
//							break;
//						}
					} else {
						AdvisorSolution advSol = (AdvisorSolution)_currentSolution;
						exchangeStudents(advSol.studentA, advSol.studentB);
					}

				}

				// reduce the temperature
				_temperature *= factor;

				// System.out.println("Current temp: " + _temperature);
				_step++;
				notifyListeners(_currentSolution, oldSolution);
			} // running
		} // while

		System.out.println("Cooldown steps taken: " + _step);
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

		if ((advisorA == null) || (advisorB == null)) {
			System.err.println("Null advisor in exchangeStudents");
			System.exit(0);
		}

		if (advisorA.locked() || advisorB.locked()) {
			return;
		}

		advisorA.removeAdvisee(studentA);
		advisorB.removeAdvisee(studentB);

		advisorA.addAdvisee(studentB, false, EReason.ALG);
		advisorB.addAdvisee(studentA, false, EReason.ALG);

		studentA.advisor = advisorB;
		studentB.advisor = advisorA;
	}

	@Override
	public void updateSolution(Simulation simulation, Solution newSolution, Solution oldSolution) {

		if ((_step % 50) == 0) {
			String s = String.format("SOLUTION UPDATE. Step = %d  Temp = %12.8f  Energy = %7.3f", _step, simulation.getTemperature(),
					newSolution.getEnergy());
			System.out.println(s);

		}
	}

	@Override
	public void reset(Simulation simulation) {
		System.out.println("SIMULATION RESET");
	}

	@Override
	public void stateChange(Simulation simulation, SimulationState oldState, SimulationState newState) {

		if (newState == SimulationState.RUNNING) {

		}
		else if (newState == SimulationState.STOPPED) {

		}

	}

}
