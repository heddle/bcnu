package cnuphys.simanneal;

import java.util.Random;

import javax.swing.event.EventListenerList;

public abstract class Simulation implements Runnable {

	// current state of the simulation
	protected SimulationState _simState = SimulationState.STOPPED;

	// current solution
	protected Solution _currentSolution;

	// current temperature
	protected double _temperature;

	// Listener list for solution updates.
	protected EventListenerList _listenerList;

	// random number generator
	protected Random _rand;

	// simulation attributes
	protected SimulationAttributes _attributes;

	// the initial solution. Saved to be available for reset.
	protected Solution _initialSolution;

	// the thread that runs the simulation
	protected Thread _thread;

	/**
	 * Create a Simulation
	 *
	 * @param props key-value properties of the simulation. Used for initialization.
	 */
	public Simulation() {

		// call the subclass to set up attributes and create the initial solution

		_attributes = new SimulationAttributes();
		setInitialAttributes();

		// create the random number generator
		_rand = _attributes.createRandomGenerator();

		// cache the initial solution and make a copy
		_initialSolution = setInitialSolution();
		_currentSolution = _initialSolution.copy();
	}

	/**
	 * Get the simulation state
	 *
	 * @return the simulation state
	 */
	public SimulationState getSimulationState() {
		return _simState;
	}
	
	/**
	 * Get the random number generator
	 * @return the random number generator
	 */
	public Random getRandom() {
		return _rand;
	}

	/**
	 * Set the simulation state
	 *
	 * @param simState the new simulation state
	 */
	public void setSimulationState(SimulationState simState) {
		if (_simState == simState) {
			return;
		}
		SimulationState oldState = _simState;
		_simState = simState;
//		System.err.println("STATE IS NOW " + _simState);
		notifyListeners(oldState, _simState);
	}

	/**
	 * Retrieve the initial solution
	 *
	 * @return the initial solution
	 */
	public Solution getInitialSolution() {
		return _initialSolution;
	}

	/**
	 * Get the initial attributes
	 *
	 * @return the initial attributes
	 */
	protected abstract void setInitialAttributes();

	/**
	 * Create the initial solution
	 *
	 * @return the initial solution
	 */
	protected abstract Solution setInitialSolution();

	/**
	 * Reset the simulation
	 */
	protected void reset() {
		_initialSolution = setInitialSolution();
		_currentSolution = _initialSolution.copy();
		notifyListeners();
	}

	/**
	 * Accessor for the attributes
	 *
	 * @return the attributes
	 */
	public SimulationAttributes getAttributes() {
		return _attributes;
	}

	/**
	 * Get the current solution
	 *
	 * @return the current solution
	 */
	public Solution currentSolution() {
		return _currentSolution;
	}

	// make a guess for an initial temperature
	private void setInitialTemperature() {
		// find a average energy step

		int n = 100;
		double e0 = _currentSolution.getEnergy();
		double sum = 0;

		for (int i = 0; i < n; i++) {
			double e1 = _currentSolution.getRearrangement().getEnergy();
			sum += Math.pow(e1 - e0, 2);
		}

//		_temperature = 10*Math.sqrt(sum/n);
		_temperature = 1.2 * Math.sqrt(sum / n);

//		System.out.println("Initial temperature: " + _temperature);
	}

	/**
	 * Get the temperature
	 *
	 * @return the temperature
	 */
	public double getTemperature() {
		return _temperature;
	}

	/**
	 * Start the simulation
	 */
	public void startSimulation() {

		if ((_thread != null) && _thread.isAlive()) {
			_simState = SimulationState.STOPPED;
			try {
				System.out.print("Waiting for current thread to die");
				_thread.join();
				System.out.println("died.");
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		setInitialTemperature();

		_thread = new Thread(this);
		_simState = SimulationState.RUNNING;
		_thread.start();
	}

	/**
	 * run the simulation
	 */
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

	// the Metropolis test
	protected boolean metrop(double ebest, double etest) {
		if (etest < ebest) {
			return true;
		}
		double delE = etest - ebest; // > 0
		double prob = Math.exp(-delE / _temperature);
		return (_rand.nextDouble() < prob);
	}

	/**
	 * Notify listeners that the solution was updated
	 */
	protected void notifyListeners(Solution newSolution, Solution oldSolution) {
		if (_listenerList == null) {
			return;
		}

		// Guaranteed to return a non-null array
		Object[] listeners = _listenerList.getListenerList();

		// Process the listeners last to first, notifying
		// those that are interested in this event
		for (int i = listeners.length - 2; i >= 0; i -= 2) {
			if (listeners[i] == IUpdateListener.class) {
				((IUpdateListener) listeners[i + 1]).updateSolution(this, newSolution, oldSolution);
			}
		}

	}

	/**
	 * Notify listeners that the simulation was reset
	 */
	protected void notifyListeners() {
		if (_listenerList == null) {
			return;
		}

		// Guaranteed to return a non-null array
		Object[] listeners = _listenerList.getListenerList();

		// Process the listeners last to first, notifying
		// those that are interested in this event
		for (int i = listeners.length - 2; i >= 0; i -= 2) {
			if (listeners[i] == IUpdateListener.class) {
				((IUpdateListener) listeners[i + 1]).reset(this);
			}
		}

	}

	/**
	 * Notify listeners that the state changed
	 */
	protected void notifyListeners(SimulationState oldState, SimulationState simState) {
		if (_listenerList == null) {
			return;
		}

		// Guaranteed to return a non-null array
		Object[] listeners = _listenerList.getListenerList();

		// Process the listeners last to first, notifying
		// those that are interested in this event
		for (int i = listeners.length - 2; i >= 0; i -= 2) {
			if (listeners[i] == IUpdateListener.class) {
				((IUpdateListener) listeners[i + 1]).stateChange(this, oldState, _simState);
			}
		}

	}

	/**
	 * Remove a solution update listener.
	 *
	 * @param listener the update listener to remove.
	 */
	public void removeUpdateListener(IUpdateListener listener) {

		if ((listener == null) || (_listenerList == null)) {
			return;
		}

		_listenerList.remove(IUpdateListener.class, listener);
	}

	/**
	 * Add a solution update listener.
	 *
	 * @param listener the update listener to add.
	 */
	public void addUpdateListener(IUpdateListener listener) {

		if (listener == null) {
			return;
		}

		if (_listenerList == null) {
			_listenerList = new EventListenerList();
		}

		_listenerList.add(IUpdateListener.class, listener);
	}

}
