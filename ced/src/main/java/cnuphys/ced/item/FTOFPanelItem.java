package cnuphys.ced.item;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.util.List;

import cnuphys.bCNU.graphics.container.IContainer;
import cnuphys.bCNU.graphics.style.LineStyle;
import cnuphys.bCNU.graphics.world.WorldGraphicsUtilities;
import cnuphys.bCNU.item.PolygonItem;
import cnuphys.bCNU.layer.LogicalLayer;
import cnuphys.ced.cedview.sectorview.SectorView;
import cnuphys.ced.clasio.ClasIoEventManager;
import cnuphys.ced.event.AccumulationManager;
import cnuphys.ced.event.data.DataDrawSupport;
import cnuphys.ced.event.data.arrays.TOF_ADC_Arrays;
import cnuphys.ced.event.data.arrays.XYZ_Hit_Arrays;
import cnuphys.ced.geometry.GeometryManager;
import cnuphys.ced.geometry.ftof.FTOFGeometry;
import cnuphys.ced.geometry.ftof.FTOFPanel;

public class FTOFPanelItem extends PolygonItem {

	private FTOFPanel _ftofPanel;

	// 1-based sector
	private int _sector;

	// the container sector view
	private SectorView _view;

	/**
	 * Create a FTOFPanelItem
	 *
	 * @param logLayer the Layer this item is on.
	 */
	public FTOFPanelItem(LogicalLayer logLayer, FTOFPanel panel, int sector) {
		super(logLayer, getShell((SectorView) logLayer.getContainer().getView(), panel, sector));

		_ftofPanel = panel;
		_sector = sector;

		_name = (panel != null) ? DataDrawSupport.panelName((byte)panel.getPanelType()) : "??";

		// _style.setFillColor(X11Colors.getX11Color("Wheat", 128));
		_style.setFillColor(Color.white);
		_style.setLineWidth(0);
		_view = (SectorView) getLayer().getContainer().getView();
	}

	/**
	 * Custom drawer for the item.
	 *
	 * @param g         the graphics context.
	 * @param container the graphical container being rendered.
	 */
	@Override
	public void drawItem(Graphics g, IContainer container) {
		// TODO use dirty. If the item is not dirty, should be able to draw
		// the _lastDrawnPolygon directly;
		if (ClasIoEventManager.getInstance().isAccumulating()) {
			return;
		}

		Point2D.Double path[] = getShell(_view, _ftofPanel, _sector);
		if (path == null) {
			return;
		}

		setPath(path);
		super.drawItem(g, container);

		// hits
		drawHits(g, container);

		Point2D.Double wp[] = GeometryManager.allocate(4);

		// draw a line marking paddle boundary
		for (int i = 0; i < _ftofPanel.getCount(); i++) {
			boolean isects = _ftofPanel.getPaddle(i, _view.getProjectionPlane(), wp);
			if (isects) {
				if (_sector > 3) {
					wp[0].y = -wp[0].y;
					wp[3].y = -wp[3].y;
				}

				WorldGraphicsUtilities.drawWorldLine(g, container, wp[0], wp[3], _style);
			}
		}

	}

	// draw any hits
	private void drawHits(Graphics g, IContainer container) {

		if (_view.isSingleEventMode()) {
			drawSingleModeADCBased(g, container);
		} else {
			drawAccumulatedADCBased(g, container);
		}
	}

	private void drawAccumulatedADCBased(Graphics g, IContainer container) {
		int hits[][] = null;

		int medianHit = 0;

		int panelType = _ftofPanel.getPanelType();
		switch (panelType) {
		case DataDrawSupport.PANEL_1A:
			medianHit = AccumulationManager.getInstance().getMedianFTOF1ACount();
			hits = AccumulationManager.getInstance().getAccumulatedFTOF1AData();
			break;
		case DataDrawSupport.PANEL_1B:
			medianHit = AccumulationManager.getInstance().getMedianFTOF1BCount();
			hits = AccumulationManager.getInstance().getAccumulatedFTOF1BData();
			break;
		case DataDrawSupport.PANEL_2:
			medianHit = AccumulationManager.getInstance().getMedianFTOF2Count();
			hits = AccumulationManager.getInstance().getAccumulatedFTOF2Data();
			break;
		}

		if (hits != null) {
			int sect0 = _sector - 1;
			for (int paddle0 = 0; paddle0 < hits[sect0].length; paddle0++) {

				int hitCount = hits[sect0][paddle0];
				double fract = _view.getMedianSetting() * (((double) hitCount) / (1 + medianHit));

				Color fc = AccumulationManager.getInstance().getColor(_view.getColorScaleModel(), fract);
				Point2D.Double wp[] = getPaddle(_view, paddle0, _ftofPanel, _sector);

				if (wp != null) {
					Path2D.Double path = WorldGraphicsUtilities.worldPolygonToPath(wp);
					WorldGraphicsUtilities.drawPath2D(g, container, path, fc, _style.getLineColor(), 0, LineStyle.SOLID,
							true);
				}
			}
		}

	}

	//draw based on data in ADC bank
	//hits drawn by recondrawer
	private void drawSingleModeADCBased(Graphics g, IContainer container) {
		
		TOF_ADC_Arrays arrays = new TOF_ADC_Arrays("FTOF::adc");
		if (!arrays.hasData()) {
			return;
		}

		byte sect = (byte) _sector; //1-based
		byte layer = (byte) (_ftofPanel.getPanelType() + 1); //(now) 1-based
		
		for (int i = 0; i < arrays.sector.length; i++) {
			if ((arrays.sector[i] == sect) && (arrays.layer[i] == layer)) {
				Point2D.Double wp[] = getPaddle(_view, arrays.component[i] - 1, _ftofPanel, _sector);

				
				if (wp != null) {
					Color fc = arrays.getColor(sect, layer, arrays.component[i]);
					Path2D.Double path = WorldGraphicsUtilities.worldPolygonToPath(wp);
					WorldGraphicsUtilities.drawPath2D(g, container, path, fc, _style.getLineColor(), 0,
							LineStyle.SOLID, true);
				}
			}
		}
	}

	/**
	 * Get the FTOFPanel which contains the geometry
	 *
	 * @return the ftofPanel
	 */
	public FTOFPanel getFtofPanel() {
		return _ftofPanel;
	}

	/**
	 * Get a paddle outline of the tof panel.
	 *
	 * @param view   the view being rendered.
	 * @param index  the zero-based paddle index.
	 * @param panel  the panel holding the geometry data
	 * @param sector the 1-based sector 1..6
	 * @return
	 */
	private static Point2D.Double[] getPaddle(SectorView view, int index, FTOFPanel panel, int sector) {

		// hide if don't fully intersect, which happens as phi moves away from midplane
		if (!doesPaddleFullIntersectPlane(view, index, panel)) {
			return null;
		}

		Point2D.Double wp[] = GeometryManager.allocate(4);

		panel.getPaddle(index, view.getProjectionPlane(), wp);

		// lower sectors (4, 5, 6) (need sign flip
		if (sector > 3) {
			for (Point2D.Double twp : wp) {
				twp.y = -twp.y;
			}
		}

		return wp;
	}

	// does paddle fully intersect projection plane?
	private static boolean doesPaddleFullIntersectPlane(SectorView view, int index, FTOFPanel panel) {

		return panel.paddleFullyIntersects(index, view.getProjectionPlane());
	}

	/**
	 * Get the shell of the tof panel.
	 *
	 * @param view   the view being rendered.
	 * @param panel  the panel holding the geometry data
	 * @param sector the 1-based sector 1..6
	 * @return
	 */
	private static Point2D.Double[] getShell(SectorView view, FTOFPanel panel, int sector) {
		if (panel == null) {
			return null;
		}

		Point2D.Double wp[] = panel.getShell(view.getProjectionPlane());

		// lower sectors (4, 5, 6) (need sign flip
		if (sector > 3) {
			for (Point2D.Double twp : wp) {
				twp.y = -twp.y;
			}
		}

		return wp;
	}

	/**
	 * Add any appropriate feedback strings panel. Default implementation returns
	 * the item's name.
	 *
	 * @param container       the Base container.
	 * @param screenPoint     the mouse location.
	 * @param worldPoint      the corresponding world point.
	 * @param feedbackStrings the List of feedback strings to add to.
	 */
	@Override
	public void getFeedbackStrings(IContainer container, Point screenPoint, Point2D.Double worldPoint,
			List<String> feedbackStrings) {

		// which paddle?

		for (int index = 0; index < _ftofPanel.getCount(); index++) {

			Point2D.Double wp[] = getPaddle(_view, index, _ftofPanel, _sector);

			if (wp != null) {
				Path2D.Double path = WorldGraphicsUtilities.worldPolygonToPath(wp);

				if (path.contains(worldPoint)) {
					
					byte sect = (byte) _sector; //1-based
					byte layer = (byte) (_ftofPanel.getPanelType() + 1); //1-based
					short paddle = (short) (index + 1); //1-based

					feedbackStrings.add("$Orange Red$" + getName() + "  sector " + _sector + " paddle " + (index + 1));
					feedbackStrings.add("$Orange Red$paddle length "
							+ FTOFGeometry.getLength(_ftofPanel.getPanelType(), index) + " cm");
					
					//any adc data?
					TOF_ADC_Arrays adcArrays = new TOF_ADC_Arrays("FTOF::adc");
					adcArrays.addFeedback(sect, layer, paddle, feedbackStrings);
					
					//any hit data?
					XYZ_Hit_Arrays hitArrays = new XYZ_Hit_Arrays("FTOF::hits");
					hitArrays.addFeedback(sect, layer, paddle, feedbackStrings);
					break;
				} // path contains wp
			} // end wp != null
		} // end which paddle
	}

}
