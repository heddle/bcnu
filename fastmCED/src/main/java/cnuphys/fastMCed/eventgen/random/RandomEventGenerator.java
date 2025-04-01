package cnuphys.fastMCed.eventgen.random;

import org.jlab.clas.physics.PhysicsEvent;

import cnuphys.bCNU.dialog.DialogUtilities;
import cnuphys.fastMCed.eventgen.AEventGenerator;
import cnuphys.fastMCed.eventgen.IEventSource;

public class RandomEventGenerator extends AEventGenerator {

	// dialog used to generate random event event after it is closed
	private IEventSource _eventSource;

	// event number
	private int _eventNumber = 0;

	// most recent event
	private PhysicsEvent _currentEvent;
	
	//the dialog
	private static RandomEvGenDialog _dialog = null;

	private RandomEventGenerator(IEventSource source) {
		_eventSource = source;
	}

	/**
	 * A Generator for random events
	 * 
	 * @return a random event generator
	 */
	public static RandomEventGenerator createRandomGenerator(boolean useDialog) {
		if (_dialog == null) {
			_dialog = new RandomEvGenDialog(null, 5);
		}

		if (useDialog) {
			_dialog.setVisible(true);

			if (_dialog.getReason() == DialogUtilities.OK_RESPONSE) {
				return new RandomEventGenerator(_dialog);
			}
		}
		return new RandomEventGenerator(_dialog);
	}


	@Override
	public String generatorDescription() {
		return "Random Generator";
	}

	@Override
	public PhysicsEvent nextEvent() {
		_currentEvent = _eventSource.getEvent();
		_eventNumber++;
		return _currentEvent;
	}

	@Override
	public int eventNumber() {
		return _eventNumber;
	}

	@Override
	public int eventCount() {
		return Integer.MAX_VALUE;
	}

	@Override
	public PhysicsEvent getCurrentEvent() {
		return _currentEvent;
	}

}
