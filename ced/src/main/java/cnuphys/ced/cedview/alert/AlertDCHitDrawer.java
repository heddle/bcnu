package cnuphys.ced.cedview.alert;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.util.List;

import org.jlab.io.base.DataEvent;

import cnuphys.bCNU.graphics.container.IContainer;
import cnuphys.ced.alldata.DataWarehouse;
import cnuphys.ced.clasio.ClasIoEventManager;
import cnuphys.ced.event.AccumulationManager;
import cnuphys.ced.geometry.alert.AlertGeometry;
import cnuphys.ced.geometry.alert.DCLayer;

public class AlertDCHitDrawer {

	// data warehouse
	private DataWarehouse _dataWarehouse = DataWarehouse.getInstance();

	// the alert view
	private AlertXYView _view;

	/**
	 * Create a DC hit drawer for the alert view.
	 *
	 * @param view
	 */
	public AlertDCHitDrawer(AlertXYView view) {
		_view = view;
	}

	/**
	 * Draw the hits
	 *
	 * @param g
	 * @param container
	 */
	public void drawHits(Graphics g, IContainer container) {
		DataEvent dataEvent = ClasIoEventManager.getInstance().getCurrentEvent();
		if (dataEvent == null) {
			return;
		}

		drawDCHits(g, container, dataEvent);

	}

	// draw the DC hits
	private void drawDCHits(Graphics g, IContainer container, DataEvent dataEvent) {

		if (ClasIoEventManager.getInstance().isAccumulating()) {
			return;
		}

		int minADC = _view.getADCThreshold();
			
		if (dataEvent.hasBank("AHDC::adc") && _view.showADCHits()) {

			short component[] = _dataWarehouse.getShort("AHDC::adc", "component");
			if (component != null) {
				int count = component.length;
				if (count > 0) {
					int maxADC = _view.getMaxADCThisEvent();
					int adc[] = _dataWarehouse.getInt("AHDC::adc", "ADC");
					byte sector[] = _dataWarehouse.getByte("AHDC::adc", "sector");
					byte compLayer[] = _dataWarehouse.getByte("AHDC::adc", "layer");
					byte order[] = _dataWarehouse.getByte("AHDC::adc", "order");

					AlertDCGeometryNumbering adcGeom = new AlertDCGeometryNumbering();

					for (int i = 0; i < count; i++) {
						if (adc[i] < minADC) {
							continue;
						}
						adcGeom.fromDataNumbering(sector[i], compLayer[i], component[i], order[i]);
						DCLayer dcl = AlertGeometry.getDCLayer(adcGeom.sector, adcGeom.superlayer, adcGeom.layer);
						if (dcl == null) {
							System.err.println("DC layer not found for sector " + adcGeom.sector + ", superlayer "
									+ adcGeom.superlayer + ", layer " + adcGeom.layer);
							continue;
						}
						
						double fract = ((double) adc[i]) / maxADC;
						Color color = _view.getColorScaleModel().getColor(fract);
						dcl.drawXYWire(g, container, adcGeom.component, color, color.darker(),  _view.getFixedZ(), true);
					}
				}
			}
		}
	}

	/**
	 * Draw the highlighted hit from the adc bank
	 *
	 * @param g         the graphics context
	 * @param container the container
	 * @param index     the 0-based index of the hit
	 */
	public void drawHighlightHit(Graphics g, IContainer container, DataEvent dataEvent, int index) {
		if (dataEvent.hasBank("AHDC::adc") && _view.showADCHits()) {
			short component[] = _dataWarehouse.getShort("AHDC::adc", "component");
			if (component != null) {
				int count = component.length;
				if (count > index) {
					byte sector[] = _dataWarehouse.getByte("AHDC::adc", "sector");
					byte compLayer[] = _dataWarehouse.getByte("AHDC::adc", "layer");
					byte order[] = _dataWarehouse.getByte("AHDC::adc", "order");

					AlertDCGeometryNumbering adcGeom = new AlertDCGeometryNumbering();
					adcGeom.fromDataNumbering(sector[index], compLayer[index], component[index], order[index]);
					DCLayer dcl = AlertGeometry.getDCLayer(adcGeom.sector, adcGeom.superlayer, adcGeom.layer);
					if (dcl != null) {
						dcl.drawXYWire(g, container, adcGeom.component, Color.orange, Color.black, _view.getFixedZ());
					}
				}
			}
		}
	}


	/**
	 * Draw the accumulated hits
	 * @param g
	 * @param container
	 */
	public void drawAccumulatedHits(Graphics g, IContainer container) {

		for (int superlayer = 0; superlayer < 5; superlayer++) {
			int maxHit = AccumulationManager.getInstance().getMaxAlertDCCount(superlayer);
			int counts[][][] = AccumulationManager.getInstance().getAccumulatedAlertDCData(superlayer);
			if (counts == null) {
				break;
			}

			for (int sector = 0; sector < counts.length; sector++) {
				for (int layer = 0; layer < counts[sector].length; layer++) {
					DCLayer dcl = AlertGeometry.getDCLayer(sector, superlayer, layer);
					if (dcl != null) {
						for (int wire = 0; wire < dcl.numWires; wire++) {
							double count = counts[sector][layer][wire];
							double fract = (maxHit == 0) ? 0 : (count / maxHit);
							Color color = AccumulationManager.getInstance().getColor(_view.getColorScaleModel(), fract);
							dcl.drawXYWire(g, container, wire, color, Color.black, _view.getFixedZ());
						}
					}
				}
			}
		}
	}

	/**
	 * Get feedback strings for a hit
	 * @param container the container
	 * @param pp the pixel point
	 * @param wp the world point
	 * @param dcl the DC layer
	 * @param feedbackStrings the list of feedback strings to add to
	 */
	public void getHitFeedbackStrings(IContainer container, Point pp, Point2D.Double wp, DCLayer dcl,
			List<String> feedbackStrings) {

		if (ClasIoEventManager.getInstance().isAccumulating() || _view.isAccumulatedMode()) {
			return;
		}


		DataEvent dataEvent = ClasIoEventManager.getInstance().getCurrentEvent();
		if ((dataEvent == null) || !dataEvent.hasBank("AHDC::adc")) {
			return;
		}

		short component[] = _dataWarehouse.getShort("AHDC::adc", "component");
		if (component != null) {
			int count = component.length;
			if (count > 0) {
				
				int maxADC = _view.getMaxADCThisEvent();
				feedbackStrings.add(String.format("max AHDC ADC in this event %d", maxADC));
				byte sector[] = _dataWarehouse.getByte("AHDC::adc", "sector");
				byte compLayer[] = _dataWarehouse.getByte("AHDC::adc", "layer");
				byte order[] = _dataWarehouse.getByte("AHDC::adc", "order");

				AlertDCGeometryNumbering adcGeom = new AlertDCGeometryNumbering();

				for (int i = 0; i < count; i++) {

					adcGeom.fromDataNumbering(sector[i], compLayer[i], component[i], order[i]);

					if (adcGeom.match(dcl)) {
						if (dcl.wireContainsXY(component[i] - 1, wp)) {
							String bankName = "AHDC::adc";
							AlertFeedbackSupport.handleInt(bankName, "ADC", i, "$orange$", feedbackStrings);
							AlertFeedbackSupport.handleInt(bankName, "integral", i, "$orange$", feedbackStrings);
							AlertFeedbackSupport.handleByte(bankName, "order", i, "$orange$", feedbackStrings);

							AlertFeedbackSupport.handleShort(bankName, "ped", i, "$orange$", feedbackStrings);
							AlertFeedbackSupport.handleFloat(bankName, "time", i, "$orange$", feedbackStrings);
							AlertFeedbackSupport.handleFloat(bankName, "timeOverThreshold", i, "$orange$", feedbackStrings);

							return;
						}
					}

				}
			}
		} // component != null

	}
	


}
