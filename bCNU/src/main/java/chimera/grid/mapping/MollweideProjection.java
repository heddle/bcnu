package chimera.grid.mapping;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;

import chimera.frame.Chimera;
import chimera.grid.ChimeraGrid;
import chimera.grid.SphericalGrid;
import cnuphys.bCNU.graphics.container.IContainer;

public class MollweideProjection implements IMapProjection {

    private double _radius = 1.0; // Sphere radius, normalized to 1 for simplicity
    
    private static final double MAXLAT = Math.toRadians(89.999); // Maximum latitude in radians
    private static final double MINLAT = -MAXLAT; // Minimum latitude in raduians
    
    
	public MollweideProjection(double radius) {
		_radius = radius;
	}

	public MollweideProjection() {
	}

    @Override
    public void latLonToXY(Point2D.Double latLon, Point2D.Double xy) {
        double lon = latLon.x; // Longitude in radians
        double lat = latLon.y;    // Latitude in radians
        
        // Solve for theta iteratively
        double theta = solveTheta(lat);

        // Compute x and y
        xy.x = 2 * _radius * lon / Math.PI * Math.cos(theta);
        xy.y = _radius * Math.sin(theta);
    }

    @Override
    public void latLonFromXY(Point2D.Double latLon, Point2D.Double xy) {
        double theta = Math.asin(xy.y / _radius); // Compute theta from y

        // Compute latitude (phi) from theta
        double lat = Math.asin((2 * theta + Math.sin(2 * theta)) / Math.PI);

        // Compute longitude (lambda) from x
        double lon = xy.x * Math.PI / (2 * _radius * Math.cos(theta));

        latLon.x = lon; // Longitude
        latLon.y = lat;    // Latitude
    }

    /**
     * Solve for theta using an iterative method for the Mollweide equation:
     * 2 * theta + sin(2 * theta) = PI * sin(phi)
     * 
     * @param phi Latitude in radians
     * @return theta in radians
     */
    private double solveTheta(double phi) {
        double theta = phi; // Initial guess
        double delta;
        do {
            double f = 2 * theta + Math.sin(2 * theta) - Math.PI * Math.sin(phi);
            double fPrime = 2 + 2 * Math.cos(2 * theta);
            delta = -f / fPrime;
            theta += delta;
        } while (Math.abs(delta) > 1e-10); // Convergence tolerance
        return theta;
    }
    
 
	@Override
	public void drawMapOutline(Graphics g, IContainer container) {
	       Graphics2D g2 = (Graphics2D) g;
	       ChimeraGrid grid = Chimera.getInstance().getChimeraGrid();
	       SphericalGrid sgrid = grid.getSphericalGrid();
	       
	        // Define ranges and step sizes for sampling
	        double latStep = sgrid.getThetaDel();  // Step size for latitude (radians)
	        double lonStep = sgrid.getPhiDel();    // Step size for longitude (radians)
	        int numLat = sgrid.getNumTheta();       // Number of latitude samples
	        int numLon = sgrid.getNumPhi();         // Number of longitude samples
	        
			for (int i = 0; i < numLat; i++) {
				double lat = Math.PI / 2 - i * latStep;
				drawLatitudeLine(g2, container, lat);
			}

	        
			for (int i = 0; i < numLon; i++) {
				double lon = -Math.PI + i * lonStep;
	//			System.out.println("lon = " + Math.toDegrees(lon));
				drawLongitudeLine(g2, container, lon);
			}
	}
	
	private void drawLatitudeLine(Graphics2D g2, IContainer container, double latitude) {
		//latitude lines are straight lines in Mollweide projection
		Point2D.Double xy1 = new Point2D.Double();
		Point2D.Double xy2 = new Point2D.Double();
		Point2D.Double latLon1 = new Point2D.Double(-Math.PI, latitude);
		Point2D.Double latLon2 = new Point2D.Double(Math.PI, latitude);
		latLonToXY(latLon1, xy1);
		latLonToXY(latLon2, xy2);
		Point screenPoint1 = new Point();
		Point screenPoint2 = new Point();
		container.worldToLocal(screenPoint1, xy1);
		container.worldToLocal(screenPoint2, xy2);
		g2.setColor(Color.black);
		g2.drawLine(screenPoint1.x, screenPoint1.y, screenPoint2.x, screenPoint2.y);
	}
	
	private void drawLongitudeLine(Graphics2D g2, IContainer container, double longitude) {
		GeneralPath path = new GeneralPath();
		int numPoints = 50;
		Point2D.Double xy = new Point2D.Double();
		Point2D.Double latLon = new Point2D.Double();
		Point screenPoint = new Point();

		latLon.x = longitude;
		
		double step = Math.PI / numPoints;
		for (int i = 0; i <= numPoints; i++) {
			double lat = -Math.PI / 2 + i * step;
			lat = Math.max(MINLAT, Math.min(MAXLAT, lat));
			
			latLon.y = lat;
			latLonToXY(latLon, xy);
			container.worldToLocal(screenPoint, xy);
			
            if (path.getCurrentPoint() == null) {
                path.moveTo(screenPoint.x, screenPoint.y);
            } else {
                path.lineTo(screenPoint.x, screenPoint.y);
            }
		}
		g2.setColor(Color.black);
		g2.draw(path);
		
	}

	@Override
	/**
     * Check if a given (x, y) point is on the Mollweide projection map.
     *
     * @param x The x-coordinate of the point.
     * @param y The y-coordinate of the point.
     * @return true if the point is on the map, false otherwise.
     */
    public boolean isPointOnMap(Point2D.Double xy) {
        // Semi-axes of the ellipse
        double a = 2 * _radius;           // Semi-major axis (horizontal)
        double b = 1.3265 * _radius;      // Semi-minor axis (vertical)

        // Check the ellipse equation
        return (xy.x * xy.x) / (a * a) + (xy.y * xy.y) / (b * b) <= 1.0;
    }
	
}
