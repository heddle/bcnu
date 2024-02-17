package cnuphys.ced.alldata;

import org.jlab.io.base.DataEvent;

import cnuphys.bCNU.threading.IEventListener;
import cnuphys.ced.alldata.datacontainer.IDataContainer;

public class DataListener implements IEventListener<Object> {
	
	private IDataContainer container;
	
	public DataListener(IDataContainer container) {
		this.container = container;
	}
	
	@Override
	public void newEvent(Object data) {
		if (data == null) {
			container.clear();
		}
		else if (data instanceof DataEvent) {
			container.update((DataEvent) data);
		}
		else {
			System.err.println("Unknown event type: " + data);
		}
	}

}
