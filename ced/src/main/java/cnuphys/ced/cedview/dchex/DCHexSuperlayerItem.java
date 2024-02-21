package cnuphys.ced.cedview.dchex;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.geom.Point2D;

import cnuphys.bCNU.graphics.container.IContainer;
import cnuphys.bCNU.item.ItemList;
import cnuphys.bCNU.item.PolygonItem;
import cnuphys.bCNU.util.X11Colors;
import cnuphys.ced.alldata.datacontainer.dc.DCTDCandDOCAData;
import cnuphys.ced.clasio.ClasIoEventManager;
import cnuphys.ced.frame.Ced;
import cnuphys.ced.frame.OrderColors;

public class DCHexSuperlayerItem extends PolygonItem {

	// the tangent of 30 degrees
	private static final double tan30 = 1.0 / Math.sqrt(3.0);
	
	// the radii and thicknesses of the superlayers
	private static final double rad[] = {124, 165, 211, 256, 310, 365};
	private static final double thk[] = {36, 36, 42, 42, 50, 50};

	// the colors for alternating layers
	public static final Color[] layerColors = { X11Colors.getX11Color("azure"), X11Colors.getX11Color("alice blue") };
	
	// line color
	public static final Color lineColor = X11Colors.getX11Color("misty rose");
	
	// 1-based sector
	private int _sector;
	
	// 1-based superlayer
	private int _superlayer;

	// the container sector view
	private DCHexView _view;
	
	//cache the superlayer polygon
	private Point2D.Double _superlayerPolygon[] = new Point2D.Double[4];
	
	//cache the layer polygons
	private Point2D.Double _layerPolygons[][] = new Point2D.Double[6][4];
	
	// data containers
	private DCTDCandDOCAData _dcData = DCTDCandDOCAData.getInstance();
	
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
	public DCHexSuperlayerItem(ItemList itemList, DCHexView view, int sector, int superlayer) {
		super(itemList, getShell((DCHexView) itemList.getContainer().getView(), sector, superlayer));

		_view = view;
		_sector = sector;
		_superlayer = superlayer;
		
		//cache the superlayer polygon
		_superlayerPolygon = getShell(_view, _sector, _superlayer);
		
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
			
			Point2D.Double wirePoly[] = new Point2D.Double[4];
			
			for (int j = 0; j < 4; j++) {
				wirePoly[j] = new Point2D.Double();
			}
			
			if (_view.isSingleEventMode()) {
				drawSingleEventData(g, container);
			}
			else {
				drawAccumulatedData(g, container);
			}
			
// 		for (int wire = 1; wire <= 112; wire += 1) {
//				getWirePolygon(layer, wire, wp, wirePoly);
//				poly.reset();
//				for (int i = 0; i < 4; i++) {
//					container.worldToLocal(pp, wirePoly[i]);
//					poly.addPoint(pp.x, pp.y);
//				}
//				g.setColor(Color.black);
//				g.drawPolygon(poly);
//			}
		}
	}
	
	private void drawSingleEventData(Graphics g, IContainer container) {
		// draw raw hits
		//if (_view.showRawHits()) {

			for (int i = 0; i < _dcData.count(); i++) {
				// draw the hit
				if ((_dcData.sector[i] == _sector) && (_dcData.superlayer[i] == _superlayer)) {
					drawDCRawHit(g, container, _dcData.layer6[i], _dcData.component[i], _dcData.noise[i], -1,
							_dcData.order[i]);
				}
			}
		//}

	}
	
	/**
	 * Draw a single dc hit
	 *
	 * @param g         the graphics context
	 * @param container the rendering container
	 * @param layer     a 1-based layer
	 * @param wire      the 1-based wire
	 * @param noise     is this marked as a noise hit
	 * @param pid       the gemc pid
	 * @param order     for optional coloring
	 */
	private void drawDCRawHit(Graphics g, IContainer container, int layer, int wire, boolean noise, int pid, int order) {
		Point2D.Double wp[] = _layerPolygons[layer - 1];
		getWirePolygon(layer, wire, wp, wirePoly);
		
		Color color = Ced.useOrderColoring() ? OrderColors.getOrderColor(order) : Color.red;
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
	
	private void drawAccumulatedData(Graphics g, IContainer container) {

	}

	
	public void getWirePolygon(int layer, int wire, Point2D.Double poly[], Point2D.Double wp[]) {
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


}
