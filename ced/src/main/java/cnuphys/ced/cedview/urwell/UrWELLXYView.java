package cnuphys.ced.cedview.urwell;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Stroke;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.List;
import java.util.Properties;

import org.jlab.geom.prim.Line3D;

import cnuphys.bCNU.drawable.DrawableAdapter;
import cnuphys.bCNU.drawable.IDrawable;
import cnuphys.bCNU.graphics.GraphicsUtilities;
import cnuphys.bCNU.graphics.container.IContainer;
import cnuphys.bCNU.graphics.style.LineStyle;
import cnuphys.bCNU.layer.LogicalLayer;
import cnuphys.bCNU.log.Log;
import cnuphys.bCNU.util.Fonts;
import cnuphys.bCNU.util.PropertySupport;
import cnuphys.bCNU.util.X11Colors;
import cnuphys.bCNU.view.BaseView;
import cnuphys.ced.cedview.CedView;
import cnuphys.ced.cedview.HexView;
import cnuphys.ced.component.ControlPanel;
import cnuphys.ced.component.DisplayBits;
import cnuphys.ced.geometry.GeometryManager;
import cnuphys.ced.geometry.urwell.UrWELLGeometry;
import cnuphys.ced.item.HexSectorItem;

public class UrWELLXYView extends HexView {

	// for naming clones
	private static int CLONE_COUNT = 0;

	// sector items
	private UrWELLHexSectorItem _hexItems[];
	
	// chamber outline items
	private UrWELLChamberItem _chamberItems[][];

	// font for label text
	private static final Font labelFont = Fonts.commonFont(Font.PLAIN, 11);
	private static final Color TRANS = new Color(192, 192, 192, 128);
	private static final Color TRANSTEXT = new Color(64, 64, 192, 40);
	private static final Font _font = Fonts.commonFont(Font.BOLD, 60);

	private static Stroke stroke = GraphicsUtilities.getStroke(0.5f, LineStyle.SOLID);

	protected static Rectangle2D.Double _defaultWorld;

	// the z location of the projection plane
	private double _zplane = 10;

	static {
		double _xsize = 160;
		double _ysize = 160 * 1.154734;

		_defaultWorld = new Rectangle2D.Double(_xsize, -_ysize, -2 * _xsize, 2 * _ysize);

	}

	private UrWELLXYView(String title) {
		super(getAttributes(title));

		// projection plane
		projectionPlane = GeometryManager.xyPlane(_zplane);

		setBeforeDraw();
		setAfterDraw();
		getContainer().getComponent().setBackground(Color.gray);


	}

	// add the control panel
	@Override
	protected void addControls() {

		_controlPanel = new ControlPanel(this,
				ControlPanel.DISPLAYARRAY + ControlPanel.FEEDBACK + ControlPanel.ACCUMULATIONLEGEND
						+ ControlPanel.DRAWLEGEND,
				DisplayBits.ACCUMULATION + DisplayBits.CROSSES + DisplayBits.FMTCROSSES + DisplayBits.RECPART
						+ DisplayBits.GLOBAL_HB + DisplayBits.GLOBAL_TB + DisplayBits.GLOBAL_AIHB
						+ DisplayBits.GLOBAL_AITB + DisplayBits.CVTRECTRACKS + DisplayBits.CVTP1TRACKS
						+ DisplayBits.MCTRUTH + DisplayBits.SECTORCHANGE,
				3, 5);

		add(_controlPanel, BorderLayout.EAST);
		pack();
	}

	/**
	 * Used to create a UrWELLView
	 *
	 * @return the new view
	 */
	public static UrWELLXYView createUrWELLView() {
		String title = UrWELLGeometry.NAME + "XY" + ((CLONE_COUNT == 0) ? "" : ("_(" + CLONE_COUNT + ")"));

		UrWELLXYView view = new UrWELLXYView(title);
		return view;
	}

	// add items to the view
	@Override
	protected void addItems() {
		LogicalLayer detectorLayer = getContainer().getLogicalLayer(_detectorLayerName);
		
		

		_hexItems = new UrWELLHexSectorItem[6];

		for (int sector = 0; sector < 6; sector++) {
			_hexItems[sector] = new UrWELLHexSectorItem(detectorLayer, this, sector + 1);
			_hexItems[sector].getStyle().setFillColor(Color.lightGray);
		}
		
		//chamber outline items
		_chamberItems = new UrWELLChamberItem[6][3];
		for (int sector = 0; sector < 1; sector++) {
			for (int chamber = 0; chamber < 3; chamber++) {
				_chamberItems[sector][chamber] = UrWELLChamberItem.createUrWELLChamberItem(detectorLayer, sector+1, chamber+1); 
			}
		}		

		
	}
	
	/**
	 * Draw the strips
	 * @param g the graphics context
	 * @param container the container
	 * @param sector sector [1..6]
	 * @param chamber chamber [1..3]
	 * @param layer [1..2]
	 * @param color strip color
	 */
	public void drawStrips(Graphics g, IContainer container, int sector, int chamber, int layer, Color color) {
		Point pp1 = new Point();
		Point pp2 = new Point();
		Point2D.Double wp1 = new Point2D.Double();
		Point2D.Double wp2 = new Point2D.Double();

		g.setColor(color);

		for (int chamberStrip = 1; chamberStrip < UrWELLGeometry.numStripsByChamber[chamber-1]; chamberStrip++) {
			projectStrip(g, container, sector, chamber, layer, chamberStrip, wp1, wp2, pp1, pp2);
			g.drawLine(pp1.x, pp1.y, pp2.x, pp2.y);
			
	//		System.err.println("wp1: " + wp1 + "  wp2: " + wp2 + "  pp1: " + pp1 + "   pp2: " + pp2);

		}
	}

	/**
	 * Create the view's before drawer.
	 */
	private void setBeforeDraw() {
		// use a before-drawer to sector dividers and labels
		IDrawable beforeDraw = new DrawableAdapter() {

			@Override
			public void draw(Graphics g, IContainer container) {
				
			}

		};

		getContainer().setBeforeDraw(beforeDraw);
	}

	private void setAfterDraw() {
		// use a after-drawer to sector dividers and labels
		IDrawable afterDraw = new DrawableAdapter() {

			@Override
			public void draw(Graphics g, IContainer container) {

				if (!_eventManager.isAccumulating()) {

					drawCoordinateSystem(g, container);
					drawSectorNumbers(g, container);
				} // not acumulating
			}

		};

		getContainer().setAfterDraw(afterDraw);
	}


	private void drawHits(Graphics g, IContainer container, int sector) {

		if (isSingleEventMode()) {

		}
	}

	/**
	 * 
	 * @param g the graphics context
	 * @param container the container
	 * @param sector 1-based sector [1..6]
	 * @param chamber 1-based sector [1..3]
	 * @param layer 1-based layer [1..2]
	 * @param chamberStrip 1-based strip
	 * @param wp1
	 * @param wp2
	 * @param p1
	 * @param p2
	 */
	private void projectStrip(Graphics g, IContainer container, int sector, int chamber, int layer, int chamberStrip, 
			Point2D.Double wp1, Point2D.Double wp2, Point p1, Point p2) {

		
		Line3D line = UrWELLGeometry.getStrip(sector, chamber, layer, chamberStrip);
		projectClasToWorld(line.origin(), projectionPlane, wp1);
		projectClasToWorld(line.end(), projectionPlane, wp2);

		container.worldToLocal(p1, wp1);
		container.worldToLocal(p2, wp2);

	}


	// draw the sector numbers
	private void drawSectorNumbers(Graphics g, IContainer container) {
		double r3over2 = Math.sqrt(3) / 2;

		double x = 100;
		double y = 0;
		FontMetrics fm = getFontMetrics(_font);
		g.setFont(_font);
		g.setColor(TRANSTEXT);
		Point pp = new Point();

		for (int sect = 1; sect <= 6; sect++) {
			container.worldToLocal(pp, x, y);

			String s = "" + sect;
			int sw = fm.stringWidth(s);

			g.drawString(s, pp.x - sw / 2, pp.y + fm.getHeight() / 2);

			if (sect != 6) {
				double tx = x;
				double ty = y;
				x = 0.5 * tx - r3over2 * ty;
				y = r3over2 * tx + 0.5 * ty;
			}
		}
	}

	// draw the coordinate system
	private void drawCoordinateSystem(Graphics g, IContainer container) {
		// draw coordinate system
		Component component = container.getComponent();
		Rectangle sr = component.getBounds();

		int left = 25;
		int right = left + 50;
		int bottom = sr.height - 20;
		int top = bottom - 50;
		g.setFont(labelFont);
		FontMetrics fm = getFontMetrics(labelFont);

		Rectangle r = new Rectangle(left - fm.stringWidth("x") - 4, top - fm.getHeight() / 2 + 1,
				(right - left + fm.stringWidth("x") + fm.stringWidth("y") + 9), (bottom - top) + fm.getHeight() + 2);

		g.setColor(TRANS);
		g.fillRect(r.x, r.y, r.width, r.height);

		g.setColor(X11Colors.getX11Color("dark red"));
		g.drawLine(left, bottom, right, bottom);
		g.drawLine(right, bottom, right, top);

		g.drawString("y", right + 3, top + fm.getHeight() / 2 - 1);
		g.drawString("x", left - fm.stringWidth("x") - 2, bottom + fm.getHeight() / 2);

	}

	private void drawAccumulatedHits(Graphics g, IContainer container) {
	}

	// get the attributes to pass to the super constructor
	private static Object[] getAttributes(String title) {

		Properties props = new Properties();
		props.put(PropertySupport.TITLE, title);

		// set to a fraction of screen
		Dimension d = GraphicsUtilities.screenFraction(0.65);

		props.put(PropertySupport.WORLDSYSTEM, _defaultWorld);
		props.put(PropertySupport.WIDTH, (int) (0.866 * d.height));
		props.put(PropertySupport.HEIGHT, d.height);

		props.put(PropertySupport.TOOLBAR, true);
		props.put(PropertySupport.TOOLBARBITS, CedView.TOOLBARBITS);
		props.put(PropertySupport.VISIBLE, true);

		props.put(PropertySupport.BACKGROUND, X11Colors.getX11Color("Alice Blue"));
		props.put(PropertySupport.STANDARDVIEWDECORATIONS, true);

		return PropertySupport.toObjectArray(props);
	}

	@Override
	public void getFeedbackStrings(IContainer container, Point pp, Point2D.Double wp, List<String> feedbackStrings) {

		container.worldToLocal(pp, wp);

		super.getFeedbackStrings(container, pp, wp, feedbackStrings);
	}

	/**
	 * Lab (CLAS) xy coordinates to local screen coordinates.
	 *
	 * @param container the drawing container
	 * @param pp        will hold the graphical world coordinates
	 * @param lab       the lab coordinates
	 */
	public static void labToLocal(IContainer container, Point pp, Point2D.Double lab) {
		container.worldToLocal(pp, lab);
	}

	/**
	 * Get the hex item for the given 1-based sector
	 *
	 * @param sector the 1-based sector
	 * @return the corresponding item
	 */
	public HexSectorItem getHexSectorItem(int sector) {
		if ((sector < 1) || (sector > 6)) {
			Log.getInstance().warning("Bad sector in DCXYView getHexSectorItem, sector = " + sector);
			return null;
		}
		return _hexItems[sector - 1];
	}

	/**
	 * Clone the view.
	 *
	 * @return the cloned view
	 */
	@Override
	public BaseView cloneView() {
		super.cloneView();
		CLONE_COUNT++;

		// limit
		if (CLONE_COUNT > 2) {
			return null;
		}

		Rectangle vr = getBounds();
		vr.x += 40;
		vr.y += 40;

		UrWELLXYView view = createUrWELLView();
		view.setBounds(vr);
		return view;

	}

}
