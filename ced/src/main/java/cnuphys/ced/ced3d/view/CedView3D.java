package cnuphys.ced.ced3d.view;

import org.jlab.io.base.DataEvent;

import cnuphys.ced.ced3d.CedPanel3D;
import cnuphys.ced.clasio.ClasIoEventManager;
import cnuphys.ced.clasio.IClasIoEventListener;
import cnuphys.ced.event.AccumulationManager;
import cnuphys.ced.event.IAccumulationListener;
import cnuphys.lund.SwimTrajectoryListener;
import cnuphys.swim.Swimming;

public abstract class CedView3D extends PlainView3D
		implements IClasIoEventListener, SwimTrajectoryListener, IAccumulationListener {

	// the event manager
	private final ClasIoEventManager _eventManager = ClasIoEventManager.getInstance();

	//for appending event number to the titile
	private static final String evnumAppend = "  (Event# ";

	/**
	 * Create a 3D view
	 *
	 * @param title
	 * @param angleX
	 * @param angleY
	 * @param angleZ
	 * @param xDist
	 * @param yDist
	 * @param zDist
	 */
	public CedView3D(String title, float angleX, float angleY, float angleZ, float xDist, float yDist, float zDist) {
		super(title, angleX, angleY, angleZ, xDist, yDist, zDist);

		_eventManager.addClasIoEventListener(this, 2);

		// listen for trajectory changes
		Swimming.addSwimTrajectoryListener(this);

		AccumulationManager.getInstance().addAccumulationListener(this);
	}

	// make the 3d panel
	@Override
	protected abstract CedPanel3D make3DPanel(float angleX, float angleY, float angleZ, float xDist, float yDist,
			float zDist);


	@Override
	public void newClasIoEvent(DataEvent event) {
		if (!_eventManager.isAccumulating()) {
			fixTitle(event);
			_panel3D.refreshQueued();
		}
	}

	@Override
	public void openedNewEventFile(String path) {
		_panel3D.refreshQueued();
	}

	/**
	 * Change the event source type
	 *
	 * @param source the new source: File, ET
	 */
	@Override
	public void changedEventSource(ClasIoEventManager.EventSourceType source) {
	}

	@Override
	public void accumulationEvent(int reason) {
		switch (reason) {
		case AccumulationManager.ACCUMULATION_STARTED:
			break;

		case AccumulationManager.ACCUMULATION_CANCELLED:
			fixTitle(_eventManager.getCurrentEvent());
			_panel3D.refresh();
			break;

		case AccumulationManager.ACCUMULATION_FINISHED:
			fixTitle(_eventManager.getCurrentEvent());
			_panel3D.refresh();
			break;
		}
	}

	@Override
	public void trajectoriesChanged() {
		if (!_eventManager.isAccumulating()) {
			_panel3D.refreshQueued();
		}
	}


	/**
	 * Fix the title of the view after an event arrives. The default is to append
	 * the event number.
	 *
	 * @param event the new event
	 */
	protected void fixTitle(DataEvent event) {
		String title = getTitle();
		int index = title.indexOf(evnumAppend);
		if (index > 0) {
			title = title.substring(0, index);
		}

		int seqNum = _eventManager.getSequentialEventNumber();
		int trueNum = _eventManager.getTrueEventNumber();
		if (seqNum > 0) {
			if (trueNum > 0) {
				setTitle(title + evnumAppend + seqNum + "  True event# " + trueNum + ")");
			}
			else {
			    setTitle(title + evnumAppend + seqNum + ")");
			}
		}
	}

}
