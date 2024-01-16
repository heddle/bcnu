package cnuphys.ced.cedview.ftof;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.geom.Point2D;
import java.util.List;

import org.jlab.io.base.DataEvent;

import cnuphys.bCNU.graphics.container.IContainer;
import cnuphys.ced.clasio.ClasIoEventManager;
import cnuphys.ced.event.data.TdcAdcTOFHit;
import cnuphys.ced.event.data.arrays.TOF_ADC_Arrays;
import cnuphys.ced.event.data.lists.TdcAdcTOFHitList;

/**
 * Handles drawing and feedback for the adc (and tdc) bank
 * @author heddle
 *
 */
public class FTOFAdcHandler {

	private static final String fbcolor = "$medium spring green$";


	// the parent view
	private FTOFView _view;

	public FTOFAdcHandler(FTOFView view) {
		_view = view;
	}


	// draw the adc data
	public void draw(Graphics g, IContainer container) {
		if (_view.isSingleEventMode()) {
			
			TOF_ADC_Arrays arrays = new TOF_ADC_Arrays("FTOF::adc");
			if (!arrays.hasData()) {
				return;
			}

			DataEvent event = ClasIoEventManager.getInstance().getCurrentEvent();
			if (event == null) {
				return;
			}
			
			Polygon poly = new Polygon();
			for (int i = 0; i < arrays.sector.length; i++) {
				int panel = arrays.layer[i] - 1; //nasty -- this is zero based
				_view.getPaddlePolygon(container, arrays.sector[i], panel, arrays.component[i], poly);
				Color fc = arrays.getColor(arrays.sector[i], arrays.layer[i], arrays.component[i]);
				g.setColor(fc);
				g.fillPolygon(poly);
				g.setColor(Color.red);
				g.drawPolygon(poly);

			}
			


		} else {
			// drawAccumulatedHits(g, container);
		}

	}

	public void getFeedbackStrings(IContainer container, int sect, int layer, int paddleId, Point pp, Point2D.Double wp,
			List<String> feedbackStrings) {
		
		TOF_ADC_Arrays arrays = new TOF_ADC_Arrays("FTOF::adc");
		arrays.addFeedback((byte) sect, (byte) layer, (short) paddleId, feedbackStrings);
	}


}
