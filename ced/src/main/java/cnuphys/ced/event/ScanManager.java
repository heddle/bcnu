package cnuphys.ced.event;

import java.util.ArrayList;

import org.jlab.io.base.DataEvent;

import cnuphys.ced.clasio.ClasIoEventManager;
import cnuphys.ced.clasio.ClasIoEventManager.EventSourceType;
import cnuphys.ced.clasio.IClasIoEventListener;

/**
 * This class manages the rapid scanning of events
 * for making as map of event index to true event number
 */
public class ScanManager implements IClasIoEventListener {
	
	// event manager
	private ClasIoEventManager _eventManager = ClasIoEventManager.getInstance();

	
	//the map 
	ArrayList<Integer> _eventIndexToEventNumber = new ArrayList<Integer>();
	
	public ScanManager() {
		_eventManager.addClasIoEventListener(this, 1);
	}

	@Override
	public void newClasIoEvent(DataEvent event) {
	}

	@Override
	public void openedNewEventFile(String path) {
		_eventIndexToEventNumber.clear();
	}

	@Override
	public void changedEventSource(EventSourceType source) {
		_eventIndexToEventNumber.clear();
	}
	
	

}
