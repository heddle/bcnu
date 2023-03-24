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
import cnuphys.ced.event.data.FTOF;
import cnuphys.ced.event.data.TdcAdcTOFHit;
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
		if (_view.isSingleEventMode() && _view.showClusters()) {
			DataEvent event = ClasIoEventManager.getInstance().getCurrentEvent();
			if (event == null) {
				return;
			}

			TdcAdcTOFHitList hitList = FTOF.getInstance().getTdcAdcHits();
			if (hitList != null) {
				Polygon poly = new Polygon();
				boolean isMono = _view.getControlPanel().isMonochrome();
				
				Color lc = isMono ? Color.red : Color.black;


				for (TdcAdcTOFHit hit : hitList) {
					int panel = hit.layer - 1;
					if (panel == _view.displayPanel()) {
						int adcL = hit.adcL;
						int adcR = hit.adcR;
						
						Color colorL = isMono ? _view.adcMonochromeColor(adcL, _view.getMaxADC()) : _view.adcColor(adcL, _view.getMaxADC());
						Color colorR = isMono ? _view.adcMonochromeColor(adcR, _view.getMaxADC()) : _view.adcColor(adcR, _view.getMaxADC());
						_view.getPaddlePolygon(container, hit.sector, panel, hit.component, poly);
						GradientPaint gpaint = new GradientPaint(poly.xpoints[0], poly.ypoints[0], colorL, poly.xpoints[2], poly.ypoints[2], colorR);
						

						((Graphics2D)g).setPaint(gpaint);
						((Graphics2D)g).fillPolygon(poly);
						
						g.setColor(lc);
						g.drawPolygon(poly);
					}
				}
			}
				


		} else {
			// drawAccumulatedHits(g, container);
		}
		
	}
	
	public void getFeedbackStrings(IContainer container, int sect, int layer, int paddleId, Point pp, Point2D.Double wp,
			List<String> feedbackStrings) {
		TdcAdcTOFHitList hitList = FTOF.getInstance().getTdcAdcHits();
		TdcAdcTOFHit hit = hitList.get(sect, layer, paddleId);
		if (hit != null) {
			feedbackStrings.add(String.format("adc left %d  adc right %d", hit.adcL, hit.adcR));
			feedbackStrings.add(String.format("tdc left %d  tdc right %d", hit.tdcL, hit.tdcR));
		}
	}


}
