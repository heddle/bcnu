package cnuphys.ced.cedview.dchex;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import cnuphys.bCNU.geometry.Line;
import cnuphys.bCNU.graphics.container.IContainer;
import cnuphys.bCNU.graphics.style.LineStyle;
import cnuphys.bCNU.graphics.world.WorldGraphicsUtilities;
import cnuphys.bCNU.item.ItemList;
import cnuphys.bCNU.item.PolygonItem;
import cnuphys.bCNU.util.X11Colors;
import cnuphys.ced.alldata.datacontainer.dc.DCTDCandDOCAData;
import cnuphys.ced.alldata.datacontainer.dc.HBTrkgAIHitData;
import cnuphys.ced.alldata.datacontainer.dc.HBTrkgHitData;
import cnuphys.ced.alldata.datacontainer.dc.TBTrkgAIHitData;
import cnuphys.ced.alldata.datacontainer.dc.TBTrkgHitData;
import cnuphys.ced.clasio.ClasIoEventManager;
import cnuphys.ced.event.AccumulationManager;
import cnuphys.ced.frame.Ced;
import cnuphys.ced.frame.CedColors;
import cnuphys.ced.frame.OrderColors;
import cnuphys.ced.geometry.GeoConstants;

public class DCHexSuperLayer extends PolygonItem {

	// the tangent of 30 degrees
	private static final double tan30 = 1.0 / Math.sqrt(3.0);
	
	// the radii and thicknesses of the superlayers
	private static final double rad[] = {128, 169, 214, 260, 311, 365};
	private static final double thk[] = {36, 36, 42, 42, 50, 50};

	// the colors for alternating layers
	public static final Color[] layerColors = { X11Colors.getX11Color("azure"), X11Colors.getX11Color("alice blue") };
	
	// line color
	public static final Color lineColor = X11Colors.getX11Color("misty rose");
	
	// 1-based sector
	private int _sector;
	
	// 1-based superlayer
	private int _superLayer;

	// the container sector view
	private DCHexView _view;
	
	//cache the superlayer polygon
	private Point2D.Double _superlayerPolygon[] = new Point2D.Double[4];
	
	//cache the layer polygons
	private Point2D.Double _layerPolygons[][] = new Point2D.Double[6][4];
	
	// data containers
	private DCTDCandDOCAData _dcData = DCTDCandDOCAData.getInstance();
	private HBTrkgHitData _hbData = HBTrkgHitData.getInstance();
	private TBTrkgHitData _tbData = TBTrkgHitData.getInstance();
	private HBTrkgAIHitData _hbAIData = HBTrkgAIHitData.getInstance();
	private TBTrkgAIHitData _tbAIData = TBTrkgAIHitData.getInstance();
	
	//workspace
	private Point2D.Double wirePoly[] = new Point2D.Double[4];
	private Polygon ppoly = new Polygon();
	


	
	/**
	 * Create a superlayer item
	 * @param itemList the list this item is on.
	 * @param view the view being rendered
	 * @param sector the 1-based sector
	 * @param superlayer the 1-based superlayer
	 */
	public DCHexSuperLayer(ItemList itemList, DCHexView view, int sector, int superlayer) {
		super(itemList, getShell((DCHexView) itemList.getContainer().getView(), sector, superlayer));

		_view = view;
		_sector = sector;
		_superLayer = superlayer;
		
		//cache the superlayer polygon
		_superlayerPolygon = getShell(_view, _sector, _superLayer);
		
		//cache the layer polygons
		for (int layer = 1; layer <= 6; layer++) {
			_layerPolygons[layer - 1] = getLayerPolygon(layer);
		}
		
		//workspace
		for (int i = 0; i < 4; i++) {
			wirePoly[i] = new Point2D.Double();
		}
		
		this.getStyle().setFillColor(Color.white);
		this.getStyle().setLineColor(lineColor);
	}
	
	private Point2D.Double[] getLayerPolygon(int layer) {
		Point2D.Double wp[] = new Point2D.Double[4];

		for (int i = 0; i < 4; i++) {
			wp[i] = new Point2D.Double();
		}

		getLayerPolygon(layer, wp);
		return wp;
	}
	
	/**
	 * Custom drawer for the item.
	 *
	 * @param g         the graphics context.
	 * @param container the graphical container being rendered.
	 */
	@Override
	public void drawItem(Graphics g, IContainer container) {
		if (ClasIoEventManager.getInstance().isAccumulating()) {
			return;
		}

		super.drawItem(g, container);
		
		//draw the layers
		Point pp = new Point();
				
		Polygon poly = new Polygon();

		for (int layer = 1; layer <= 6; layer++) {
			Point2D.Double wp[] = _layerPolygons[layer - 1];
			poly.reset();
			for (int i = 0; i < 4; i++) {
				container.worldToLocal(pp, wp[i]);
				poly.addPoint(pp.x, pp.y);
			}
			
			g.setColor(layerColors[layer % 2]);
			g.fillPolygon(poly);
			
			g.setColor(lineColor);
			g.drawPolygon(poly);
			
			
			if (_view.isSingleEventMode()) {
				drawSingleEventData(g, container);
			}
			else {
				drawAccumulatedData(g, container);
			}
			
		}
	}
	
	/**
	 * Get the cached superlayer polygon
	 * @return the cached superlayer polygon
	 */
	public Point2D.Double[] getPolygon() {
		return _superlayerPolygon;
	}
	
	private void drawSingleEventData(Graphics g, IContainer container) {
		// draw raw hits
		if (_view.showRawHits()) {

			for (int i = 0; i < _dcData.count(); i++) {
				// draw the hit
				
				Color color = Ced.useOrderColoring() ? OrderColors.getOrderColor(_dcData.order[i]) : Color.red;

				if ((_dcData.sector[i] == _sector) && (_dcData.superlayer[i] == _superLayer)) {
					drawDCHit(g, container, _dcData.layer6[i], _dcData.component[i], 
							color);
				}
			}
		}
		
		// draw regular HB Hits
		if (_view.showHBHits()) {
			for (int i = 0; i < _hbData.count(); i++) {
				// draw the hit
				if ((_hbData.sector[i] == _sector) && (_hbData.superlayer[i] == _superLayer)) {
					drawDCHit(g, container, _hbData.layer[i], _hbData.wire[i], CedColors.HB_COLOR);
				}
			}
		}

		// draw regular TB Hits
		if (_view.showTBHits()) {
			for (int i = 0; i < _tbData.count(); i++) {
				// draw the hit
				if ((_tbData.sector[i] == _sector) && (_tbData.superlayer[i] == _superLayer)) {
					drawDCHit(g, container, _tbData.layer[i], _tbData.wire[i], CedColors.TB_COLOR);
				}
			}
		}

		// draw AI HB Hits
		if (_view.showAIHBHits()) {
			for (int i = 0; i < _hbAIData.count(); i++) {
				// draw the hit
				if ((_hbAIData.sector[i] == _sector) && (_hbAIData.superlayer[i] == _superLayer)) {
					drawDCHit(g, container, _hbAIData.layer[i], _hbAIData.wire[i], CedColors.AIHB_COLOR);
				}
			}
		}

		// draw AI TB Hits
		if (_view.showAITBHits()) {
			for (int i = 0; i < _tbAIData.count(); i++) {
				// draw the hit
				if ((_tbAIData.sector[i] == _sector) && (_tbAIData.superlayer[i] == _superLayer)) {
					drawDCHit(g, container, _tbAIData.layer[i], _tbAIData.wire[i], CedColors.AITB_COLOR);
				}
			}
		}

	}
	
	
	/**
	 * Draw a single dc hit
	 *
	 * @param g         the graphics context
	 * @param container the rendering container
	 * @param layer     a 1-based layer
	 * @param wire      the 1-based wire
	 * @param color     for fill colo
	 */
	private void drawDCHit(Graphics g, IContainer container, int layer, int wire, Color color) {
		getWirePolygon(layer, wire, wirePoly);
		
		ppoly.reset();
		Point pp = new Point();
		for (int i = 0; i < 4; i++) {
			container.worldToLocal(pp, wirePoly[i]);
			ppoly.addPoint(pp.x, pp.y);
		}
		g.setColor(color);
		g.fillPolygon(ppoly);
		g.drawPolygon(ppoly);
	}

	//draw results of accumulation
	private void drawAccumulatedData(Graphics g, IContainer container) {

		Rectangle2D.Double wr = new Rectangle2D.Double(); // used over and over
		int dcAccumulatedData[][][][] = AccumulationManager.getInstance().getAccumulatedDCData();

		int maxHit = AccumulationManager.getInstance().getMaxDCCount(_superLayer - 1);

		for (int layer = 1; layer <= 6; layer++) {

			for (int wire = 1; wire <= 112; wire++) {
				int hitCount = dcAccumulatedData[_sector - 1][_superLayer - 1][layer-1][wire-1];
				getWirePolygon(layer, wire, wirePoly);
				
				ppoly.reset();
				Point pp = new Point();
				for (int i = 0; i < 4; i++) {
					container.worldToLocal(pp, wirePoly[i]);
					ppoly.addPoint(pp.x, pp.y);
				}
			

				double fract = (maxHit == 0) ? 0 : (((double) hitCount) / maxHit);
				AccumulationManager.getInstance();
				Color color = AccumulationManager.getInstance().getColor(_view.getColorScaleModel(), fract);

				drawDCHit(g, container, layer, wire, color);

			}
		}

	}

	/**
	 * Get the polygon for a wire
	 * @param layer the 1-based layer
	 * @param wire the 1-based wire
	 * @param wp will hold the world points of the wire
	 */
	public void getWirePolygon(int layer, int wire, Point2D.Double wp[]) {

		Point2D.Double poly[] = _layerPolygons[layer - 1];

		double fract1 = (double) (wire - 1) / 112.;
		double fract2 = (double) (wire) / 112.;

		cut(poly[1], poly[2], fract1, wp[0]);
		cut(poly[1], poly[2], fract2, wp[1]);
		cut(poly[0], poly[3], fract2, wp[2]);
		cut(poly[0], poly[3], fract1, wp[3]);
	}
	
	/**
	 * Get the polygon for a layer
	 * 
	 * @param layer the 1-based layer
	 * @param wp    the world points
	 */
	public void getLayerPolygon(int layer, Point2D.Double wp[]) {
			
		double fract1 = (double)(layer - 1) / 6.;
		double fract2 = (double)(layer) / 6.;
		
		Point2D.Double poly[] = _superlayerPolygon;
		
		cut(poly[0], poly[1], fract1, wp[0]);
		cut(poly[0], poly[1], fract2, wp[1]);
		cut(poly[3], poly[2], fract2, wp[2]);
		cut(poly[3], poly[2], fract1, wp[3]);
	
	}
	
	private void cut(Point2D.Double wp1, Point2D.Double wp2, double fract, Point2D.Double wp) {
		double dx = wp2.x - wp1.x;
		double dy = wp2.y - wp1.y;
		wp.x = wp1.x + fract * dx;
		wp.y = wp1.y + fract * dy;
	}
	
	/**
	 * Get the shell of the tof panel.
	 *
	 * @param view   the view being rendered.
	 * @param panel  the panel holding the geometry data
	 * @param sector the 1-based sector 1..6
	 * @return
	 */
	
	private static Point2D.Double[] getShell(DCHexView view, int sector, int superlayer) {
		Point2D.Double wp[] = new Point2D.Double[4];
		
		double r = rad[superlayer-1];
		double t = thk[superlayer-1];
		double rho = r + t;
		
		wp[0] = new Point2D.Double(-tan30*r, r);
		wp[1] = new Point2D.Double(-tan30*rho, rho);
		wp[2] = new Point2D.Double(tan30*rho, rho);
		wp[3] = new Point2D.Double(tan30*r, r);
		
		double phi = Math.toRadians(-90 + 60 * (sector - 1));
		double cosPhi = Math.cos(phi);
		double sinPhi = Math.sin(phi);
		
		for (int i = 0; i < 4; i++) {
			rotatePoint(wp[i], wp[i], cosPhi, sinPhi);
		}
		
		return wp;
	}
	
	/**
	 * Rotate a point around the z axis 
	 *
	 * @param wp0  the point being rotated
	 * @param wp1  the rotated point
	 * @param cosPhi the cosine of the rotation angle
	 * @param sinPhi the sine of the rotation angle
	 */
	private static void rotatePoint(Point2D.Double wp0, Point2D.Double wp1, double cosPhi, double sinPhi) {
		double x = cosPhi * wp0.x + -sinPhi * wp0.y;
		double y = sinPhi * wp0.x + cosPhi * wp0.y;
		wp1.setLocation(x, y);
	}

	/**
	 * Get the 1-based layer containing the point.
	 * @param container the container
	 * @param screenPoint the point in screen coordinates
	 * @return the 1-based layer, or -1 if none
	 */
	public int whichLayer(IContainer container, Point screenPoint) {
		if (!contains(_view.getContainer(), screenPoint)) {
			return -1;
		}

		Polygon poly = new Polygon();
		Point pp = new Point();

		for (int layer = 1; layer <= 6; layer++) {
			Point2D.Double wp[] = _layerPolygons[layer - 1];
			poly.reset();
			for (int i = 0; i < 4; i++) {
				container.worldToLocal(pp, wp[i]);
				poly.addPoint(pp.x, pp.y);
			}
			if (poly.contains(screenPoint)) {
				return layer;
			}
		}
		return -1;
	}
	
	/**
	 * Get the 1-based wire containing the point.
	 * @param container the container
	 * @param layer the 1-based layer
	 * @param screenPoint the point in screen coordinates
	 * @return the 1-based wire, or -1 if none
	 */
	public int whichWire(IContainer container, int layer, Point screenPoint) {
		if (!contains(_view.getContainer(), screenPoint)) {
			return -1;
		}

		Polygon poly = new Polygon();
		Point pp = new Point();
		
		for (int wire = 1; wire <= 112; wire++) {
            getWirePolygon(layer, wire, wirePoly);
            poly.reset();
            for (int i = 0; i < 4; i++) {
                container.worldToLocal(pp, wirePoly[i]);
                poly.addPoint(pp.x, pp.y);
            }
            if (poly.contains(screenPoint)) {
                return wire;
            }
        }
		
		return -1;
	}

}
