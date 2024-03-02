package cnuphys.fastMCed.view.alldcsnr;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.List;

import cnuphys.bCNU.graphics.container.IContainer;
import cnuphys.bCNU.graphics.world.WorldGraphicsUtilities;
import cnuphys.bCNU.item.ItemList;
import cnuphys.bCNU.item.RectangleItem;
import cnuphys.bCNU.util.Fonts;
import cnuphys.bCNU.util.X11Colors;
import cnuphys.fastMCed.eventio.PhysicsEventManager;
import cnuphys.fastMCed.streaming.StreamManager;
import cnuphys.fastMCed.view.alldc.AllDCView;

public class AllDCSNRSuperLayer extends RectangleItem {

	public static final int RIGHT = 0;
	public static final int LEFT = 1;
	public static final String[] leanStrings = { "R", "L" };
	public static final Color[] leanColors = {X11Colors.getX11Color("ghost white"),
			X11Colors.getX11Color("honeydew") };

	// convenient access to the event manager
	private PhysicsEventManager _eventManager = PhysicsEventManager.getInstance();

	// font for label text
	private static final Font labelFont = Fonts.commonFont(Font.PLAIN, 11);

	// the sector [1..6]
	private int _sector;

	// the super layer [1..6]
	private int _superLayer;
	
	// cell overlay transparent color
	private static final Color cellOverlayColor = new Color(180, 180, 180, 64);

	
	// the view that owns this superlayer
	private AllDCSNRView _view;
	
	// this is the world rectangle that defines the super layer
	private Rectangle2D.Double _worldRectangle;
	
	// result rects two per superlayer (L and R(
	private Rectangle2D.Double _resultWorldRects[] = new Rectangle2D.Double[2];
	
	// cache the "position" rects which span the superlayer. 
	//That is, a wire is the intersection of the layer rect and the position rect
	private Rectangle2D.Double _positionWorldRects[];


	
	/**
	 * Constructor for a geometrically unfaithful "all dc snr" superlayer.
	 * 
	 * @param layer          the Layer this item is on.
	 * @param view           the AllDCView parent
	 * @param worldRectangle the boundaries which are not the real boundaries.
	 * @param sector         the sector [0..5]
	 * @param superLayer     the superLayer [0..5]
	 */
	public AllDCSNRSuperLayer(ItemList layer, AllDCSNRView view, Rectangle2D.Double worldRectangle, int sector,
			int superLayer) {
		super(layer, worldRectangle);
		_worldRectangle = worldRectangle;
		_view = view;

		_style.setFillColor(Color.white);
		_style.setLineColor(Color.black);
		_sector = sector + 1; // convert to 1-based
		
		setResultRects();
		setPositionRects();


		_superLayer = superLayer + 1; // convert to 1-based
		_name = "Sector: " + _sector + " SuperLayer: " + _superLayer;

	}
	
	// cache the result outline rectangles
	private void setResultRects() {
		double dy = _worldRectangle.height / 2;
		double x = _worldRectangle.x;
		double y = _worldRectangle.y;
		double w = _worldRectangle.width;

		for (int i = 0; i < 2; i++) {
			// trick to invert layers in lower sector
			int recIndex = (_sector < 4) ? i : (1 - i);
			_resultWorldRects[recIndex] = new Rectangle2D.Double(x, y, w, dy);
			y += dy;
		}
	}

	// cache the position rectangles
	private void setPositionRects() {
		_positionWorldRects = new Rectangle2D.Double[112];

		double dx = _worldRectangle.width / 112;
		double x = _worldRectangle.x;
		double y = _worldRectangle.y;
		double h = _worldRectangle.height;

		// note counting right to left
		for (int i = 0; i < 112; i++) {
			_positionWorldRects[112 - i - 1] = new Rectangle2D.Double(x, y, dx, h);
			x += dx;
		}
	}

	/**
	 * Custom drawer for the item.
	 * 
	 * @param g         the graphics context.
	 * @param container the graphical container being rendered.
	 */
	@Override
	public void drawItem(Graphics g, IContainer container) {

		if (StreamManager.getInstance().isStarted()) {
			return;
		}

		super.drawItem(g, container); // draws rectangular shell
		

		// shade the result rectangles
		g.setFont(labelFont);
		for (int i = 0; i < 2; i++) {
			Rectangle2D.Double wr = _resultWorldRects[i];
			WorldGraphicsUtilities.drawWorldRectangle(g, container, wr, leanColors[i], Color.black);
			g.setColor(Color.cyan);
			WorldGraphicsUtilities.drawWorldText(g, container, wr.x, wr.y, 
					"" + _superLayer + leanStrings[i], -15, -9);
		}
		
		// causes cell shading
		for (int i = 0; i < 112; i += 2) {
			WorldGraphicsUtilities.drawWorldRectangle(g, container, _positionWorldRects[i], cellOverlayColor, null);

		}

		
		// just to make clean
		g.setColor(_style.getLineColor());
		g.drawPolygon(_lastDrawnPolygon);

	}
	
	/**
	 * For the given world point return the 1-based wire.
	 * 
	 * @param worldPoint the point in question
	 * @return the wire [1..]
	 */
	private int getWire(Point2D.Double worldPoint) {
		if (_worldRectangle.contains(worldPoint)) {
			for (int i = 0; i < 112; i++) {
				if (_positionWorldRects[i].contains(worldPoint)) {
					return i + 1; // convert to 1-based
				}
			}
		}

		return -1;
	}
	
	/**
	 * Get the world rectangle for a given cell (the wire is in the center)
	 * 
	 * @param lean the 1-based layer [0 or 1 for R, L]
	 * @param wire  the 1-based wire [1..] return the world rectangle cell for this
	 *              layer, wire
	 */
	public void getCell(int lean, int wire, Rectangle2D.Double wr) {

		int wm1 = wire - 1;

		Rectangle2D.Double leanRect = _resultWorldRects[lean];
		Rectangle2D.Double positionRect = _positionWorldRects[wm1];
		wr.setFrame(positionRect.x, leanRect.y, positionRect.width, leanRect.height);

	}


	
	/**
	 * Add any appropriate feedback strings panel.
	 * 
	 * @param container       the Base container.
	 * @param screenPoint     the mouse location.
	 * @param worldPoint      the corresponding world point.
	 * @param feedbackStrings the List of feedback strings to add to.
	 */
	@Override
	public void getFeedbackStrings(IContainer container, Point screenPoint, Point2D.Double worldPoint,
			List<String> feedbackStrings) {

		if (StreamManager.getInstance().isStarted()) {
			return;
		}

		if (_worldRectangle.contains(worldPoint)) {
			feedbackStrings.add("superlayer " + _superLayer);

			int lean = getLean(worldPoint); // 0: R 1: L
			int wire = getWire(worldPoint); // 1-based

			if ((wire > 0) && (wire <= 112)) {
				feedbackStrings.add("lean " + leanStrings[lean] + " wire " + wire);
			}
		}
	}
	
	/**
	 * For the given world point return the 1-based layer.
	 * 
	 * @param worldPoint the point in question
	 * @return the layer [1..6]
	 */
	private int getLean(Point2D.Double worldPoint) {
		if (_worldRectangle.contains(worldPoint)) {
			for (int i = 0; i < 2; i++) {
				if (_resultWorldRects[i].contains(worldPoint)) {
					return i;
				}
			}
		}

		return -1;
	}



}
