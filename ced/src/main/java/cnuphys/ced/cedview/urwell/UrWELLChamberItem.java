package cnuphys.ced.cedview.urwell;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Shape;
import java.awt.geom.Point2D;
import java.util.List;

import cnuphys.bCNU.graphics.container.IContainer;
import cnuphys.bCNU.item.PolygonItem;
import cnuphys.bCNU.layer.LogicalLayer;
import cnuphys.bCNU.util.X11Colors;
import cnuphys.ced.clasio.ClasIoEventManager;
import cnuphys.ced.geometry.urwell.UrWELLGeometry;

public class UrWELLChamberItem extends PolygonItem {
	
	//1-based sector
	private int _sector;
	
	//1-based chamber
	private int _chamber;
	
	//chamber colors
	private static Color _fillColors[] = {
			X11Colors.getX11Color("Dark Blue", 10),
			X11Colors.getX11Color("Dark Green", 10),
			X11Colors.getX11Color("Dark Red", 10),

	};

	public UrWELLChamberItem(LogicalLayer layer, Point2D.Double points[], int sector, int chamber) {
		super(layer, points);
		_sector = sector;
		_chamber = chamber;
	}
	
	/**
	 * Create a chamber outline
	 * @param sector the sector [1..6]
	 * @param chamber the chamber [1..3]
	 * @return the chamber outline item
	 */
	public static  UrWELLChamberItem createUrWELLChamberItem(LogicalLayer layer, int sector, int chamber) {
		Point2D.Double points[] = new Point2D.Double[4];
		
		int cm1 = chamber-1;
		points[0] = new Point2D.Double(UrWELLGeometry.minX[cm1], -UrWELLGeometry.minY[cm1]);
		points[1] = new Point2D.Double(UrWELLGeometry.maxX[cm1], -UrWELLGeometry.maxY[cm1]);
		points[2] = new Point2D.Double(UrWELLGeometry.maxX[cm1], UrWELLGeometry.maxY[cm1]);
		points[3] = new Point2D.Double(UrWELLGeometry.minX[cm1], UrWELLGeometry.minY[cm1]);
		
		//rotate if not sector 1

		if (sector > 1) {
			double midPhi = (Math.PI * (sector - 1)) / 3;
			for (int i = 0; i < 4; i++) {
				rotatePoint(points[i], midPhi);
			}
		}

		
		UrWELLChamberItem item = new UrWELLChamberItem(layer, points, sector, chamber);
		item.getStyle().setFillColor(_fillColors[cm1]);
		return item;
	}
	
	/**
	 * Rotate a point around the z axis
	 *
	 * @param wp  the point being rotated
	 * @param phi rotation angle in radians
	 */
	private static void rotatePoint(Point2D.Double wp, double phi) {
		double cosPhi = Math.cos(phi);
		double sinPhi = Math.sin(phi);
		double x = cosPhi * wp.x + -sinPhi * wp.y;
		double y = sinPhi * wp.x + cosPhi * wp.y;
		wp.setLocation(x, y);
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

	}

	@Override
	public void getFeedbackStrings(IContainer container, Point pp, Point2D.Double wp, List<String> feedbackStrings) {

		if (contains(container, pp)) {
			String chamberStr = "$yellow$chamber: " + _chamber;
			feedbackStrings.add(chamberStr);
		}
	}

}
