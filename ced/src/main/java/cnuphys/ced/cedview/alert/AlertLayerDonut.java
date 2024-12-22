package cnuphys.ced.cedview.alert;

import java.awt.Point;
import java.awt.geom.Area;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import cnuphys.bCNU.graphics.container.IContainer;
import cnuphys.ced.geometry.alert.DCLayer;

public class AlertLayerDonut extends Area {
	
	/** assumed half radial width of gap */
	public static final double LAYERDR = 1.6; // mm;

	
	public Area area;
	
	public AlertLayerDonut(IContainer container, DCLayer layer, double z) {
		int numWires = layer.numWires;
		
		ArrayList<Point> innerShell = new ArrayList<Point>();
		ArrayList<Point> outerShell = new ArrayList<Point>();
		
		Point2D.Double zp = new Point2D.Double();
		Point2D.Double wp = new Point2D.Double();

		for (int wire = 0; wire < numWires; wire++) {
			double t = layer.getWireXYatZ(wire, z, zp);
			if ((t >= 0) && (t <= 1)) {
				double radius = Math.hypot(zp.x, zp.y);
				double thet = Math.atan2(zp.y, zp.x);
				double cos = Math.cos(thet);
				double sin = Math.sin(thet);
				
				double innerRadius = radius - LAYERDR;
				double outerRadius = radius + LAYERDR;
				
				Point innerP = new Point();
				Point outerP = new Point();
				
				wp.setLocation(innerRadius * cos, innerRadius * sin);
				container.worldToLocal(innerP, wp);
				innerShell.add(innerP);
				
				wp.setLocation(outerRadius * cos, outerRadius * sin);
				container.worldToLocal(outerP, wp);
				outerShell.add(outerP);
			}
		}

		area =  createDonutArea(innerShell, outerShell);
	}
	
	
    private static Area createDonutArea(List<Point> innerShell, List<Point> outerShell) {
        // Convert shells into `Path2D`
        Path2D outerPath = createPathFromShell(outerShell);
        Path2D innerPath = createPathFromShell(innerShell);

        // Create areas from the paths
        Area outerArea = new Area(outerPath);
        Area innerArea = new Area(innerPath);

        // Subtract the inner area from the outer area to create the donut
        outerArea.subtract(innerArea);

        return outerArea;
    }
	
    private static Path2D createPathFromShell(List<Point> shell) {
        Path2D path = new Path2D.Double();
        if (!shell.isEmpty()) {
            Point start = shell.get(0);
            path.moveTo(start.getX(), start.getY());

            for (int i = 1; i < shell.size(); i++) {
                Point point = shell.get(i);
                path.lineTo(point.getX(), point.getY());
            }
            path.closePath();
        }
        return path;
    }

}
