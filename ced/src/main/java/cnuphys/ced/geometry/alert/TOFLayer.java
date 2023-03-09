package cnuphys.ced.geometry.alert;

import java.awt.Color;
import java.awt.Graphics;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.awt.geom.Point2D;
import java.util.Hashtable;
import java.util.List;

import org.jlab.geom.component.ScintillatorPaddle;
import org.jlab.geom.detector.alert.ATOF.AlertTOFLayer;
import org.jlab.geom.prim.Point3D;

import cnuphys.bCNU.graphics.container.IContainer;
import cnuphys.lund.X11Colors;

public class TOFLayer {
	
	private static Color[] fillColors = {X11Colors.getX11Color("Cornsilk"), X11Colors.getX11Color("Blanched Almond")};
	
	//work points
	private Point2D.Double wp[] = new Point2D.Double[4];
	private Point pp = new Point();
	

	/** the 0-based sector of this layer */
	public final int sector;

	/** the 0-based superlayer of this layer */
	public final int superlayer;

	/** the 0-based layer ID of this layer */
	public final int layer;

	/** the number of paddles */
	public final int numPaddles;
	
	//radii are only geo accurate for superlayer 0
	//for superlayer 1 they are modified to show all 10 layers
	private double _innerRad;
	private double _outerRad;
	
	private static double _deltaR = Double.NaN;

	
	public List<ScintillatorPaddle> paddles;
	
	Hashtable<ScintillatorPaddle, Polygon> polyhash = new Hashtable<>();


	public TOFLayer(AlertTOFLayer geoAlertTOFLayer) {
		sector = geoAlertTOFLayer.getSectorId();
		superlayer = geoAlertTOFLayer.getSuperlayerId();
		layer = geoAlertTOFLayer.getLayerId();
		numPaddles = geoAlertTOFLayer.getNumComponents();

		paddles = geoAlertTOFLayer.getAllComponents();

		setLimitValues();
	}
	
	/**
	 * Get a paddle from this layer
	 * @param paddleId 0-based id
	 * @return the paddle
	 */
	public ScintillatorPaddle getPaddle(int paddleId) {
		return paddles.get(paddleId);
	}
	

	public void drawAllPaddles(Graphics g, IContainer container) {
		polyhash.clear();
		for (ScintillatorPaddle paddle : paddles) {
			
			Color fc;
			if (superlayer == 0) {
				fc = Color.white;
			}
			else {
				fc = fillColors[layer%2];
			}
			drawPaddle(g, container, paddle, fc, Color.black);
		}
	}
	
	private void setLimitValues()  {
		ScintillatorPaddle paddle = paddles.get(0);
		
		Point3D p3d = paddle.getVolumePoint(0);
		_innerRad = Math.hypot(p3d.x(), p3d.y());
		
		p3d = paddle.getVolumePoint(1);
		_outerRad = Math.hypot(p3d.x(), p3d.y());

		if (superlayer == 1) {
			_deltaR = (_outerRad - _innerRad)/10;
			
			if (Double.isNaN(_deltaR)) {
				_deltaR = (_outerRad - _innerRad)/10;
			}
		}

	}

	
	public  void drawPaddle(Graphics g, IContainer container, ScintillatorPaddle paddle, Color fillColor, Color lineColor) {
		
		
		Graphics2D g2 = (Graphics2D)g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		Polygon poly = new Polygon();
		
		for (int i = 0; i < 4; i++) {
			
			double x = paddle.getVolumePoint(i).x();
			double y = paddle.getVolumePoint(i).y();						
			wp[i] = new Point2D.Double(x, y);
			
			//shift the point if not superlayer 0
			
			if (superlayer  == 1) {
				if ((i == 0) || (i == 3)) {
					double dR = layer*_deltaR;
				    shiftPoint(wp[i], dR);
				}
				else { //outer
					double dR = -(9 - layer)*_deltaR;
				    shiftPoint(wp[i], dR);
				}
			}
			
			container.worldToLocal(pp, wp[i]);
			poly.addPoint(pp.x, pp.y);
		}
		
		if (fillColor != null) {
			g.setColor(fillColor);
			g.fillPolygon(poly);
		}
		g.setColor(lineColor);
		g.drawPolygon(poly);

		polyhash.put(paddle, poly);
	}
	
	
	private static void shiftPoint(Point2D.Double wp, double dR) {
		double r = Math.hypot(wp.x, wp.y);
		double theta = Math.atan2(wp.y, wp.x);
		
		r += dR;
		wp.x = r*Math.cos(theta);
		wp.y = r*Math.sin(theta);
	}

	/**
	 * Basic fb string for XY view
	 * @param feedbackStrings list to add to
	 */
	public boolean feedbackXYString(Point pp, Point2D.Double wp, List<String> feedbackStrings) {
		
		double rad = Math.hypot(wp.x, wp.y);
		if ((rad < _innerRad) || (rad > _outerRad)) {
			return false;
		}
		
		for (ScintillatorPaddle paddle : paddles) {
			Polygon poly = polyhash.get(paddle);
			
			if ((poly != null) && poly.contains(pp)) {
				feedbackStrings.add(String.format("AlertTOF sector: %d superlayer: %d layer: %d", sector + 1, superlayer + 1, layer + 1));
				feedbackStrings.add(String.format("AlertTOF paddle: %d", paddle.getComponentId()+1));
				return true;
			}
		}
		
		return false;

	}
}
