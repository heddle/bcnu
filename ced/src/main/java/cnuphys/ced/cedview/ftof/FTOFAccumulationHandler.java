package cnuphys.ced.cedview.ftof;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Polygon;

import cnuphys.bCNU.graphics.container.IContainer;
import cnuphys.ced.event.AccumulationManager;
import cnuphys.ced.event.data.FTOF;

public class FTOFAccumulationHandler {
	
	private FTOFView _view;
	
	public FTOFAccumulationHandler(FTOFView view) {
		_view = view;
	}
	
	public void draw(Graphics g, IContainer container) {
		
		
		Polygon poly = new Polygon();
		int hits[][] = null;

		int medianHit = 0;

		switch (_view.displayPanel()) {
		case FTOF.PANEL_1A:
			medianHit = AccumulationManager.getInstance().getMedianFTOF1ACount();
			hits = AccumulationManager.getInstance().getAccumulatedFTOF1AData();
			break;
		case FTOF.PANEL_1B:
			medianHit = AccumulationManager.getInstance().getMedianFTOF1BCount();
			hits = AccumulationManager.getInstance().getAccumulatedFTOF1BData();
			break;
		case FTOF.PANEL_2:
			medianHit = AccumulationManager.getInstance().getMedianFTOF2Count();
			hits = AccumulationManager.getInstance().getAccumulatedFTOF2Data();
			break;
		}

		if (hits != null) {
			
			for (int sect0 = 0; sect0 < 6; sect0++) {
				
				for (int paddle0 = 0; paddle0 < hits[sect0].length; paddle0++) {
					int hitCount = hits[sect0][paddle0];
					
					double fract = _view.getMedianSetting() * (((double) hitCount) / (1 + medianHit));
					Color fc = AccumulationManager.getInstance().getColor(_view.getColorScaleModel(), fract);
					
					_view.getPaddlePolygon(container, sect0+1, _view.displayPanel(), paddle0+1, poly);
					
					g.setColor(fc);
					g.fillPolygon(poly);
					g.setColor(Color.LIGHT_GRAY);
					g.drawPolygon(poly);

				}
			}

		}

		
	}

}
