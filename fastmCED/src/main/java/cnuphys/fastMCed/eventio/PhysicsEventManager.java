package cnuphys.fastMCed.eventio;

import java.util.ArrayList;
import java.util.List;

import javax.swing.event.EventListenerList;

import org.jlab.clas.physics.PhysicsEvent;
import org.jlab.geom.prim.Path3D;

import cnuphys.bCNU.magneticfield.swim.ISwimAll;
import cnuphys.fastMCed.eventgen.AEventGenerator;
import cnuphys.fastMCed.fastmc.ParticleHits;
import cnuphys.fastMCed.geometry.GeometryManager;
import cnuphys.fastMCed.snr.SNRManager;
import cnuphys.fastMCed.streaming.StreamManager;
import cnuphys.lund.LundId;
import cnuphys.swim.SwimTrajectory;
import cnuphys.swim.Swimming;

/**
 * Manager class for the PhysicsEvent data provided by the FastMC engine
 *
 * @author heddle
 *
 */
public class PhysicsEventManager {

	// particle hits corresponding to the current event.
	// these are the results from the FastMC engine given the tracks
	// found in Lund file event and swum by Swimmer
	private ArrayList<ParticleHits> _currentParticleHits = new ArrayList<>();

	// the current event generator
	private AEventGenerator _eventGenerator;

	/** possible lund file extensions */
	public static String extensions[] = { "dat", "DAT", "lund" };

	// Unique lund ids in the event (if any)
	private ArrayList<LundId> _uniqueLundIds = new ArrayList<>();

	// manager singleton
	private static PhysicsEventManager instance;

	// list of listeners. There are three lists. Those in index 0 are
	// notified first. Then those in index 1. Finally those in index 2. The
	private EventListenerList _listeners[] = new EventListenerList[3];

	// someone who can swim all particles in the current event
	private ISwimAll _allSwimmer;

	// private constructor for manager
	private PhysicsEventManager() {
		_allSwimmer = new SwimAll();
	}

	/**
	 * Public access to the singleton
	 *
	 * @return
	 */
	public static PhysicsEventManager getInstance() {
		if (instance == null) {
			instance = new PhysicsEventManager();
		}
		return instance;
	}


	/**
	 * Set the event generator
	 *
	 * @param generator the new event generator
	 */
	public void setEventGenerator(AEventGenerator generator) {

		// close the current
		if (_eventGenerator != null) {
			_eventGenerator.close();
		}

		_eventGenerator = generator;
		reset();
	}

	/**
	 * Accessor for the all swimmer
	 *
	 * @return the all swimmer
	 */
	public ISwimAll getAllSwimmer() {
		return _allSwimmer;
	}

	/**
	 * Get a collection of unique LundIds in the current event
	 *
	 * @return a collection of unique LundIds
	 */
	public ArrayList<LundId> uniqueLundIds() {
		_uniqueLundIds.clear();
		if (_eventGenerator != null) {
			_eventGenerator.uniqueLundIds(_uniqueLundIds);
		}
		return _uniqueLundIds;
	}

	/**
	 * Reload the current event
	 */
	public void reloadCurrentEvent() {
		if (_eventGenerator != null) {
			PhysicsEvent event = _eventGenerator.getCurrentEvent();
			if (event != null) {
				parseEvent(event);
			}
		}
	}

	/**
	 * Get the next event from the active generator.
	 *
	 * @return the next event from the generator
	 */
	public PhysicsEvent nextEvent() {

		PhysicsEvent event = null;

		if (_eventGenerator != null) {
			event = _eventGenerator.nextEvent();
			if (event != null) {
				parseEvent(event);
			} else {
				// Toolkit.getDefaultToolkit().beep();
			}
		}

		return event;
	}

	// Parse the event, which will convert the PhysicsEvent into
	// detector hits and load them into _currentParticleHits
	private void parseEvent(PhysicsEvent event) {

		_currentParticleHits.clear();
		Swimming.setNotifyOn(false); // prevent refreshes
		Swimming.clearAllTrajectories();
		Swimming.setNotifyOn(true); // prevent refreshes

		if ((event == null) || (event.count() < 1)) {
			return;
		}

		// the event has to be swum to get the hits
		_allSwimmer.swimAll();

		// how many trajectories?
		List<SwimTrajectory> trajectories = Swimming.getMCTrajectories();

		// get DC hits for charged particles

		if (trajectories != null) {
			for (SwimTrajectory traj : trajectories) {
				if (traj.getLundId() != null) {
					Path3D path3D = GeometryManager.fromSwimTrajectory(traj);
					_currentParticleHits
							.add(new ParticleHits(traj.getLundId(), traj.getGeneratedParticleRecord(), path3D));
				}
			}
		}

		// Add random noise if requested
		if (RandomNoiseGenerator.getInstance().isGenerateNoise()) {
			System.err.println("Generating noise");

			RandomNoiseGenerator.getInstance().generateNoise(_currentParticleHits);
		}

		// do the SNR analysis
		SNRManager.getInstance().analyzeSNR(_currentParticleHits);

		// notify all listeners of the event

		if (StreamManager.getInstance().isStarted()) {
			StreamManager.getInstance().notifyStreamListeners(event, _currentParticleHits);
		} else {
			Runnable runnable = new Runnable() {

				@Override
				public void run() {
					notifyPhysicsListeners(event);
				}

			};
			(new Thread(runnable)).start();
			// notifyPhysicsListeners(event);
		}
	}

	/**
	 * Get the hits for all particles in the current event These are the results
	 * from the FastMC engine given the tracks found in Lund file event and swum by
	 * Swimmer
	 *
	 * @return the detector hits for the current event
	 */
	public ArrayList<ParticleHits> getParticleHits() {
		return _currentParticleHits;
	}

	/**
	 * Get the current event number from the active generator
	 *
	 * @return the current event number
	 */
	public int eventNumber() {
		return (_eventGenerator == null) ? 0 : _eventGenerator.eventNumber();
	}

	/**
	 * Get the event count from the active generator
	 *
	 * @return the current event count
	 */
	public int getEventCount() {
		return (_eventGenerator == null) ? 0 : _eventGenerator.eventCount();
	}

	/**
	 * Are there any more events?
	 *
	 * @return <code>true</code> if we have not reached the end of the stream
	 */
	public boolean moreEvents() {
		return (getNumRemainingEvents() > 0);
	}

	/**
	 * Get the number of remaining events
	 *
	 * @return the number of remaining events
	 */
	public int getNumRemainingEvents() {
		return (_eventGenerator == null) ? 0 : _eventGenerator.eventsRemaining();
	}

	/**
	 * Get a description of the active event generator
	 *
	 * @return a description of the active event generator
	 */
	public String getGeneratorDescription() {
		return (_eventGenerator == null) ? "none" : _eventGenerator.generatorDescription();
	}

	/**
	 * Get the current eventfrom the active generator
	 *
	 * @return the current generated event
	 */
	public PhysicsEvent getCurrentEvent() {
		return (_eventGenerator == null) ? null : _eventGenerator.getCurrentEvent();
	}

	/**
	 * Reset to the no data state
	 */
	public void reset() {
		_currentParticleHits.clear();
		SNRManager.getInstance().clear();
	}

	/**
	 * Determines whether any next event control should be enabled.
	 *
	 * @return <code>true</code> if any next event control should be enabled.
	 */
	public boolean isNextOK() {
		return (moreEvents());
	}


	// notify the listeners
	private void notifyPhysicsListeners(PhysicsEvent event) {

		_uniqueLundIds.clear();

		for (int index = 0; index < 3; index++) {
			if (_listeners[index] != null) {
				// Guaranteed to return a non-null array
				Object[] listeners = _listeners[index].getListenerList();

				// This weird loop is the bullet proof way of notifying all
				// listeners.
				for (int i = listeners.length - 2; i >= 0; i -= 2) {
					IPhysicsEventListener listener = (IPhysicsEventListener) listeners[i + 1];
					if (listeners[i] == IPhysicsEventListener.class) {
						listener.newPhysicsEvent(event, _currentParticleHits);
					}
				}
			}
		} // index loop
	}

	/**
	 * Remove a IPhysicsEventListener. IPhysicsEventListener listeners listen for
	 * new physics events.
	 *
	 * @param listener the IPhysicsEventListener listener to remove.
	 */
	public void removePhysicsListener(IPhysicsEventListener listener) {

		if (listener == null) {
			return;
		}

		for (int i = 0; i < 3; i++) {
			if (_listeners[i] != null) {
				_listeners[i].remove(IPhysicsEventListener.class, listener);
			}
		}
	}

	/**
	 * Add a IPhysicsEventListener. IPhysicsEventListener listeners listen for new
	 * events.
	 *
	 * @param listener the IPhysicsEventListener listener to add.
	 * @param index    Determines gross notification order. Those in index 0 are
	 *                 notified first. Then those in index 1. Finally those in index
	 *                 2. The Data containers should be in index 0. The trajectory
	 *                 and noise in index 1, and the regular views in index 2 (they
	 *                 are notified last)
	 */
	public void addPhysicsListener(IPhysicsEventListener listener, int index) {

		if (listener == null) {
			return;
		}

		if (_listeners[index] == null) {
			_listeners[index] = new EventListenerList();
		}

		_listeners[index].add(IPhysicsEventListener.class, listener);
	}

}
