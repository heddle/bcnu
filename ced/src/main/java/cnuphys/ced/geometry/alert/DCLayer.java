package cnuphys.ced.geometry.alert;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.List;

import org.jlab.geom.detector.alert.AHDC.AlertDCLayer;
import org.jlab.geom.detector.alert.AHDC.AlertDCWire;
import org.jlab.geom.prim.Line3D;
import org.jlab.geom.prim.Point3D;

import cnuphys.bCNU.graphics.container.IContainer;
import cnuphys.bCNU.graphics.world.WorldGraphicsUtilities;
import cnuphys.lund.X11Colors;

public class DCLayer {
	//LENGTH units are mm

	//layer shell
	private static Color[] shellColors = {X11Colors.getX11Color("Cornsilk"), X11Colors.getX11Color("Blanched Almond")};
	
	//wire fill
	private static Color wireFill = Color.white;
	
	/** assumed half radial width of gap */
	public static final double LAYERDR = 1.6; // mm;
	
	/** assumed half radial width of gap */
	public static final double WIRERAD = 0.85*LAYERDR; // mm;

	
	// for approximating arcs
	private static final int NUMCIRCSTEP = 200;

	
	/** the 0-based sector of this layer */
	public final int sector;
	
	/** the 0-based superlayer of this layer */
	public final int superlayer;

	/** the 0-based layer ID of this layer */
	public final int layer;
	
	/** the number of wires */
	public final int numWires;
	
	/** the array of wires */
	public final Line3D wires[];
	
	/** the midpoints of the wires */
	public final Point2D.Double midpoints[];
	
	/** XY radius at midpoint in mm */
	public final double midPointRadius;
	
	/** XY radius at midpoint in mm */
	public final double innerMidPointRadius;

	/** XY radius at midpoint in mm */
	public final double outerMidPointRadius;
	
	//for world xy shell
	private double _x[];
	private double _y[];
	
	//for wire outline
	private Rectangle2D.Double _wr[];
	
	//workspace 
	
	
	public DCLayer(AlertDCLayer geoAlertDCLayer) {
		sector = geoAlertDCLayer.getSectorId();
		superlayer = geoAlertDCLayer.getSuperlayerId();
		layer = geoAlertDCLayer.getLayerId();
		numWires = geoAlertDCLayer.getNumComponents();
		
		
		List<AlertDCWire> wireList = geoAlertDCLayer.getAllComponents();
		
		wires = new Line3D[numWires];
		midpoints = new Point2D.Double[numWires];
		
		int index = 0;
		for (AlertDCWire aw : wireList) {
			wires[index] = aw.getLine();
			
			Point3D p3d = aw.getMidpoint();
			midpoints[index] = new Point2D.Double(p3d.x(), p3d.y());
			
//			double theta = Math.atan2(midpoints[index].y, midpoints[index].x);
//			System.out.println("wire " + index + "   theta: " + Math.toDegrees(theta));
			
			index++;
		}

		if (numWires == 0) {
			midPointRadius = Double.NaN;
			innerMidPointRadius = Double.NaN;
			outerMidPointRadius = Double.NaN;

		} else {
			midPointRadius = Math.hypot(midpoints[0].x, midpoints[0].y);
			innerMidPointRadius = midPointRadius - LAYERDR;
			outerMidPointRadius = midPointRadius + LAYERDR;
			shellWorldPoly();
		}

		System.out.println(String.format("sect: %d    supl: %d    lay: %d   nw:  %d rad: %8.4f", sector, superlayer, layer, numWires, midPointRadius));
	}
	
	/**
	 * Get a wire as a 3D line
	 * @param wire the 0-based wire id
	 * @return the 3D line for the wire, or null
	 */
	public Line3D getLine(int wire) {
		if ((wire < 0) || (wire >= numWires)) {
			return null;
		}
		return wires[wire];
	}
	
	private void shellWorldPoly() {
		
		int N2 = 2 * NUMCIRCSTEP;
		_x = new double[N2];
		_y = new double[N2];
		
		double delAng = (2 * Math.PI) / (NUMCIRCSTEP - 1);

		
		for (int i = 0; i < NUMCIRCSTEP; i++) {
			int j = i + NUMCIRCSTEP;
			double theta = i * delAng;

			double cos = Math.cos(theta);
			double sin = Math.sin(theta);

			_x[i] = innerMidPointRadius * cos;
			_y[i] = innerMidPointRadius * sin;

			_x[j] = outerMidPointRadius * cos;
			_y[j] = outerMidPointRadius * sin;
		}
		
		_wr = new Rectangle2D.Double[numWires];
		
		double r2 = 2*WIRERAD;
		for (int wire = 0; wire < numWires; wire++) {
			double xx = midpoints[wire].x;
			double yy = midpoints[wire].y;
			_wr[wire] = new Rectangle2D.Double(xx-WIRERAD, yy-WIRERAD, r2, r2);
		}
		

	}

	
	/**
	 * Get midpoint of wire as a 2D point
	 * @param wire the 0-based wire id
	 * @return midpoint of wire, or null
	 */
	public Point2D.Double getMidpoint(int wire) {
		if ((wire < 0) || (wire >= numWires)) {
			return null;
		}
		return midpoints[wire];
	}

	/**
	 * Contained in the XY view?
	 * @param wp the world point
	 * @return true if contained in this layer
	 */
	public boolean containsXY(Point2D.Double wp) {
		if (numWires == 0) {
			return false;
		}
		
		double rad = Math.hypot(wp.x, wp.y);
		return (rad > innerMidPointRadius) && (rad < outerMidPointRadius);
	}
	
	/**
	 * Basic fb string for XY view
	 * @param feedbackStrings list to add to
	 */
	public void feedbackXYString(Point pp, Point2D.Double wp, List<String> feedbackStrings) {
		feedbackStrings.add(
				String.format("AlertDC sector: %d superlayer: %d layer: %d", sector + 1, superlayer + 1, layer + 1));

		if (numWires > 0) {
			for (int wire = 0; wire < numWires; wire++) {
				if (_wr[wire].contains(wp)) {
					feedbackStrings.add(String.format("AlertDC wire: %d", wire + 1));
				}
			}
		}
	}
	
	/**
	 * Draw the layer shell as a donut
	 * @param g the graphics object
	 * @param container the drawing container
	 */
	public void drawXYDonut(Graphics g, IContainer container) {
		
		if (numWires < 1) {
			return;
		}
		
		Point pp = new Point();
		
		//to erase connecting line
		Point pp0 = new Point();
		Point pp1 = new Point();
		
		Polygon poly = new Polygon();
		
		for (int i = 0; i < _x.length; i++) {
			container.worldToLocal(pp, _x[i], _y[i]);
			poly.addPoint(pp.x, pp.y);
			
			if (i == 0) {
				pp0.setLocation(pp);
			}
			else if (i == NUMCIRCSTEP) {
				pp1.setLocation(pp);
			}
		}


		Color fc = shellColors[layer];
		g.setColor(fc);
		g.fillPolygon(poly);
		g.setColor(Color.gray);
		g.drawPolygon(poly);
		
		g.setColor(fc);
		g.drawLine(pp0.x-1, pp0.y, pp1.x+1, pp1.y);
		

	}
	
	public void drawXYWires(Graphics g, IContainer container) {
		
		for (int wire = 0; wire < numWires; wire++) {
			drawXYWire(g, container, wire);
		}
	}
	
	public void drawXYWire(Graphics g, IContainer container, int wire) {
		if (numWires < 1) {
			return;
		}
		
		WorldGraphicsUtilities.drawWorldOval(g, container, _wr[wire], wireFill, Color.darkGray);
	}

	
	
}
