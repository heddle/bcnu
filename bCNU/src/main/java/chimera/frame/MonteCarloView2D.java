package chimera.frame;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.List;

import javax.swing.JTextArea;

import chimera.grid.CartesianGrid;
import chimera.grid.ChimeraGrid;
import chimera.grid.SphericalGrid;
import chimera.grid.mapping.IMapProjection;
import chimera.grid.mapping.MercatorProjection;
import chimera.grid.mapping.MollweideProjection;
import chimera.grid.mapping.OrthographicProjection;
import chimera.monteCarlo.MonteCarloPoint;
import chimera.util.Point3D;
import chimera.util.ThetaPhi;
import cnuphys.bCNU.drawable.DrawableAdapter;
import cnuphys.bCNU.drawable.IDrawable;
import cnuphys.bCNU.graphics.container.IContainer;
import cnuphys.bCNU.util.PropertySupport;
import cnuphys.bCNU.view.BaseView;

public class MonteCarloView2D extends BaseView implements MouseMotionListener {
	
	private static final int WIDTH = 1200;
	
	//the map projection
	private IMapProjection _projection;
	
	/**
	 * For a status line feedback string.
	 */
	private JTextArea _status;
	

	/**
	 * Create a 2D view for Monte Carlo
	 */
	public MonteCarloView2D() {
		super(PropertySupport.TITLE, "MonteCarlo 2D", 
				PropertySupport.WORLDSYSTEM, getWorldSystem(),
				PropertySupport.ICONIFIABLE, true, 
				PropertySupport.MAXIMIZABLE, true,
				PropertySupport.CLOSABLE, true, 
				PropertySupport.RESIZABLE, true,
				PropertySupport.PROPNAME, "MonteCarlo 2D",
				PropertySupport.BACKGROUND, Color.white,
				PropertySupport.WIDTH, WIDTH,
				PropertySupport.HEIGHT, (int)(0.66325 * WIDTH),
				PropertySupport.TOOLBAR, true,
				PropertySupport.VISIBLE, false);
	
	
		//the map projection
	//	_projection = new MollweideProjection(getRadius());
	//	_projection = new OrthographicProjection(getRadius(), Math.toRadians(-15), Math.toRadians(10));
		_projection = new MercatorProjection();
		
		_status = new JTextArea(1, 200);
		_status.setBackground(Color.black);
		_status.setForeground(Color.cyan);
		_status.setFont(new Font("SanSerif", Font.BOLD, 12));
		_status.setEditable(false);

		add(_status, BorderLayout.SOUTH);

		getContainer().getComponent().addMouseMotionListener(this);
		setAfterDraw();
	}
		
	private static double getRadius() {
		return SphericalGrid.R;
	}

	private static Rectangle2D.Double getWorldSystem() {
		double radius = getRadius();
		double xlim = 2.1 * radius;
		double ylim = 1.4 * radius;
		return new Rectangle2D.Double(-xlim, -ylim, 2 * xlim, 2 * ylim);
	}
	
	/**
	 * Get the map projection
	 * @return the map projection
	 */
	public IMapProjection getProjection() {
		return _projection;
	}
	
	/**
	 * Set the views before draw
	 */
	private void setBeforeDraw() {
		IDrawable afterDraw = new DrawableAdapter() {
			@Override
			public void draw(Graphics g, IContainer container) {
			}
		};
		
		getContainer().setBeforeDraw(afterDraw);
	}
	
	/**
	 * Set the views after draw
	 */
	private void setAfterDraw() {
		
		
		IDrawable afterDraw = new DrawableAdapter() {
			@Override
			public void draw(Graphics g, IContainer container) {
				drawMonteCarloPoints(g, container);
				_projection.drawMapOutline(g, container);
			}
		};
		
		getContainer().setAfterDraw(afterDraw);
	}
	
	private void drawMonteCarloPoints(Graphics g, IContainer container) {
		List<MonteCarloPoint> points = Chimera.getInstance().getMonteCarloPoints();
		
		Point2D.Double xy = new Point2D.Double();
		Point2D.Double latLon = new Point2D.Double();
		Point pp = new Point();
		
		for (MonteCarloPoint mcp : points) {
			ThetaPhi thetaPhi = mcp.thetaPhi;
			latLon.x = thetaPhi.getPhi();
			latLon.y = thetaPhi.getLatitude();
			if (!_projection.isPointVisible(latLon)) {
				continue;
			}

			_projection.latLonToXY(latLon, xy);
			container.worldToLocal(pp, xy);
			g.setColor(mcp.getColor());
			g.fillRect(pp.x - 1, pp.y - 1, 2, 2);
		}
	}
	
	private void updateStatus(Point pp) {
		Point2D.Double xy = new Point2D.Double();
		Point2D.Double latLon = new Point2D.Double();
		
		getContainer().localToWorld(pp, xy);
		
		if (!_projection.isPointOnMap(xy)) {
			_status.setText("Off Map");
			return;
		}
		
		
		_projection.latLonFromXY(latLon, xy);
		
		double lat = Math.toDegrees(latLon.y);
		double lon = Math.toDegrees(latLon.x);
		
		if ((Double.isNaN(lat)) || (Math.abs(lon) > 180)) {
			_status.setText(" Off Map");
			return;
		}
		
		
		double theta = 90 - lat;
		double phi = lon;
		ThetaPhi tp = new ThetaPhi(Math.PI/2 - latLon.y, latLon.x);
		Point3D.Double cartesian = tp.toCartesian();
		
		ChimeraGrid grid = Chimera.getInstance().getChimeraGrid();
		CartesianGrid cgrid = grid.getCartesianGrid();
		SphericalGrid sgrid = grid.getSphericalGrid();
		int cindices[] = new int[3];
		cgrid.getIndices(cartesian, cindices);
		int sindices[] = new int[2];
		sgrid.getIndices(tp, sindices);
		
		String s = String.format( "Lat: %.2f Lon: %.2f   %s: %.2f %s: %.2f  (%d, %d, %d) (%d, %d)", 
				lat, lon, ThetaPhi.SMALL_THETA,theta, ThetaPhi.SMALL_PHI, phi, 
				cindices[0], cindices[1], cindices[2], sindices[0], sindices[1]);
		
		_status.setText(s);
	}
	

	@Override
	public void mouseDragged(MouseEvent e) {
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		Point pp = e.getPoint();
		updateStatus(pp);
		
	}
	
}
