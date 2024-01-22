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
import cnuphys.ced.event.data.arrays.adc.LR_ADCArrays;

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

			LR_ADCArrays arrays = LR_ADCArrays.getArrays("FTOF::adc");
			if (!arrays.hasData()) {
				return;
			}

			DataEvent event = ClasIoEventManager.getInstance().getCurrentEvent();
			if (event == null) {
				return;
			}

			Polygon poly = new Polygon();
			for (int i = 0; i < arrays.sector.length; i++) {
				int panel = arrays.layer[i] - 1; // nasty -- this is zero based
				if (panel == _view.displayPanel()) {

					_view.getPaddlePolygon(container, arrays.sector[i], panel, arrays.component[i], poly);
					Color colorL = arrays.getColor(arrays.sector[i], arrays.layer[i], arrays.component[i], (byte) 0);
					Color colorR = arrays.getColor(arrays.sector[i], arrays.layer[i], arrays.component[i], (byte) 1);

					if (colorL == null) {
						colorL = Color.white;
					}
					if (colorR == null) {
						colorR = Color.white;
					}

//					if ((colorL == null) && (colorR == null)) {
//						continue;
//					}


					GradientPaint gpaint = new GradientPaint(poly.xpoints[0], poly.ypoints[0], colorL, poly.xpoints[2],
							poly.ypoints[2], colorR);

					((Graphics2D) g).setPaint(gpaint);
					((Graphics2D) g).fillPolygon(poly);

					g.fillPolygon(poly);
				}
			}

		} else {
			// drawAccumulatedHits(g, container);
		}

	}

	public void getFeedbackStrings(IContainer container, int sect, int layer, int paddleId, Point pp, Point2D.Double wp,
			List<String> feedbackStrings) {

		LR_ADCArrays arrays = LR_ADCArrays.getArrays("FTOF::adc");
		arrays.addFeedback((byte) sect, (byte) layer, (short) paddleId, feedbackStrings);
	}


}
