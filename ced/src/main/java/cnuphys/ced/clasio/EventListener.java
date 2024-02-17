package cnuphys.ced.clasio;

import org.jlab.io.base.DataEvent;

import cnuphys.bCNU.threading.IEventListener;

public class EventListener<Object> implements IEventListener<Object> {
	
	
	private IClasIoEventListener listener;
	
	public EventListener(IClasIoEventListener listener) {
		this.listener = listener;
	}

	@Override
	public void newEvent(Object data) {
		if (data instanceof DataEvent) {
			listener.newClasIoEvent((DataEvent) data);
		} else if (data instanceof String) {
			listener.openedNewEventFile((String) data);
		} else if (data instanceof ClasIoEventManager.EventSourceType) {
			listener.changedEventSource((ClasIoEventManager.EventSourceType) data);
		} else {
			System.err.println("Unknown event type: " + data);
		}
	}
	

}
