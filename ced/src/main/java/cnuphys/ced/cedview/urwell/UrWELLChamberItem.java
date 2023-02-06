package cnuphys.ced.cedview.urwell;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Point2D;

import cnuphys.bCNU.graphics.container.IContainer;
import cnuphys.bCNU.item.PolygonItem;
import cnuphys.bCNU.layer.LogicalLayer;
import cnuphys.ced.clasio.ClasIoEventManager;
import cnuphys.ced.geometry.urwell.UrWELLGeometry;

public class UrWELLChamberItem extends PolygonItem {

	public UrWELLChamberItem(LogicalLayer layer, Point2D.Double points[]) {
		super(layer, points);
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
			
		}
		
		return new UrWELLChamberItem(layer, points);
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



}
