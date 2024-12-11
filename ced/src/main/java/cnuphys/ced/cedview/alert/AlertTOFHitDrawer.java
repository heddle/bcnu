package cnuphys.ced.cedview.alert;

import java.awt.Graphics;

import org.jlab.io.base.DataEvent;

import cnuphys.bCNU.graphics.container.IContainer;
import cnuphys.ced.alldata.DataWarehouse;
import cnuphys.ced.clasio.ClasIoEventManager;

public class AlertTOFHitDrawer {
	
	//data warehouse
	private DataWarehouse _dataWarehouse = DataWarehouse.getInstance();

	// the alert view
	private AlertXYView _view;

	/**
	 * Create a TOF hit drawer for the alert view.
	 *
	 * @param view the alert view.
	 */
	public AlertTOFHitDrawer(AlertXYView view) {
		_view = view;
    }
	
	/**
	 * Draw the hits
	 * @param g
	 * @param container
	 */
	public void drawHits(Graphics g, IContainer container) {
		DataEvent dataEvent = ClasIoEventManager.getInstance().getCurrentEvent();
		if (dataEvent == null) {
			return;
		}
		
		drawTOFHits(g, container, dataEvent);

	}
	//draw the TOF hits
	private void drawTOFHits(Graphics g, IContainer container, DataEvent dataEvent) {
		
	}

}
