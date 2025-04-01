package cnuphys.bCNU.graphics.container;

import java.awt.Component;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import javax.swing.JComponent;

import cnuphys.bCNU.feedback.FeedbackControl;
import cnuphys.bCNU.feedback.FeedbackPane;
import cnuphys.bCNU.graphics.GraphicsUtilities;
import cnuphys.bCNU.graphics.toolbar.BaseToolBar;
import cnuphys.bCNU.graphics.toolbar.ToolBarToggleButton;
import cnuphys.bCNU.graphics.world.WorldPolygon;
import cnuphys.bCNU.item.YouAreHereItem;
import cnuphys.bCNU.util.Point2DSupport;
import cnuphys.bCNU.view.BaseView;

public abstract class AContainer<T extends JComponent> implements IContainer, MouseListener, MouseMotionListener, MouseWheelListener {

	protected T _component;

	/**
	 * Keeps track of current mouse position
	 */
	protected Point _currentMousePoint;

	/**
	 * Each container may or may not have a tool bar.
	 */
	protected BaseToolBar _toolBar;

	/**
	 * The optional feedback pane.
	 */
	protected FeedbackPane _feedbackPane;

	// location of last mouse event
	protected MouseEvent _lastLocationMouseEvent;

	/**
	 * The view that holds this container (might be null for viewless container).
	 */
	protected BaseView _view;

	/**
	 * The world coordinate system,
	 */
	protected Rectangle2D.Double _worldSystem;

	/**
	 * Original, default world system.
	 */
	protected Rectangle2D.Double _defaultWorldSystem;

	/**
	 * Previous world system, for undoing the last zoom.
	 */
	protected Rectangle2D.Double _previousWorldSystem;

	// for world to local transformations (and vice versa)
	protected int _lMargin = 0;
	protected int _tMargin = 0;
	protected int _rMargin = 0;
	protected int _bMargin = 0;
	protected AffineTransform localToWorld;
	protected AffineTransform worldToLocal;

	/**
	 * Controls the feedback for the container. You can add and remove feedback
	 * providers to this object.
	 */
	protected FeedbackControl _feedbackControl;

	/**
	 * Optional anchor item.
	 */
	protected YouAreHereItem _youAreHereItem;


	public AContainer(T component, BaseView view, Rectangle2D.Double worldSystem) {
		_component = component;
		_view = view;
		_worldSystem = worldSystem;
		_feedbackControl = new FeedbackControl(this);

		_defaultWorldSystem = copy(worldSystem);
		_previousWorldSystem = copy(worldSystem);



		ComponentAdapter componentAdapter = new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent ce) {
				setDirty(true);
				_component.repaint();
				setAffineTransforms();
			}
		};

		_component.addComponentListener(componentAdapter);
		_component.addMouseListener(this);
		_component.addMouseMotionListener(this);

	}

	/**
	 * This converts a screen or pixel point to a world point.
	 *
	 * @param pp contains the local (screen-pixel) point.
	 * @param wp will hold the resultant world point.
	 */
	@Override
	public void localToWorld(Point pp, Point2D.Double wp) {
		if (localToWorld != null) {
			localToWorld.transform(pp, wp);
		}
	}

	/**
	 * This converts a world point to a screen or pixel point.
	 *
	 * @param pp will hold the resultant local (screen-pixel) point.
	 * @param wp contains world point.
	 */
	@Override
	public void worldToLocal(Point pp, Point2D.Double wp) {
		if (worldToLocal != null) {
			try {
				worldToLocal.transform(wp, pp);
			} catch (NullPointerException npe) {

				System.err.println("Null pointer exception in BaseContainer worldToLocal pp = " + pp + "  wp = " + wp);
				npe.printStackTrace();
			}
		}
	}

	/**
	 * This converts a world rectangle to a screen or pixel rectangle.
	 *
	 * @param r  will hold the resultant local (screen-pixel) rectangle.
	 * @param wr contains the world rectangle.
	 */
	@Override
	public void worldToLocal(Rectangle r, Rectangle.Double wr) {
		// New version to accommodate world with x decreasing right
		Point2D.Double wp0 = new Point2D.Double(wr.getMinX(), wr.getMinY());
		Point2D.Double wp1 = new Point2D.Double(wr.getMaxX(), wr.getMaxY());
		Point p0 = new Point();
		Point p1 = new Point();
		worldToLocal(p0, wp0);
		worldToLocal(p1, wp1);

		int x = Math.min(p0.x, p1.x);
		int y = Math.min(p0.y, p1.y);
		int w = Math.abs(p1.x - p0.x);
		int h = Math.abs(p1.y - p0.y);
		r.setBounds(x, y, w, h);
	}

	/**
	 * This converts a screen or local rectangle to a world rectangle.
	 *
	 * @param r  contains the local (screen-pixel) rectangle.
	 * @param wr will hold the resultant world rectangle.
	 */
	@Override
	public void localToWorld(Rectangle r, Rectangle.Double wr) {
		Point p0 = new Point(r.x, r.y);
		Point p1 = new Point(r.x + r.width, r.y + r.height);
		Point2D.Double wp0 = new Point2D.Double();
		Point2D.Double wp1 = new Point2D.Double();
		localToWorld(p0, wp0);
		localToWorld(p1, wp1);

		// New version to accommodate world with x decreasing right
		double x = wp0.x;
		double y = wp1.y;
		double w = wp1.x - wp0.x;
		double h = wp0.y - wp1.y;
		wr.setFrame(x, y, w, h);

	}

	/**
	 * This converts a world point to a screen or pixel point.
	 *
	 * @param pp will hold the resultant local (screen-pixel) point.
	 * @param wx the world x coordinate.
	 * @param wy the world y coordinate.
	 */
	@Override
	public void worldToLocal(Point pp, double wx, double wy) {
		worldToLocal(pp, new Point2D.Double(wx, wy));
	}


	/**
	 * Pan the container.
	 *
	 * @param dh the horizontal step in pixels.
	 * @param dv the vertical step in pixels.
	 */
	@Override
	public void pan(int dh, int dv) {

		Rectangle r = _component.getBounds();
		int xc = r.width / 2;
		int yc = r.height / 2;

		xc -= dh;
		yc -= dv;

		Point p = new Point(xc, yc);
		recenter(p);
	}


	/**
	 * Recenter the container at the point of a click.
	 *
	 * @param pp the point in question. It will be the new center.
	 */
	@Override
	public void recenter(Point pp) {
		Point2D.Double wp = new Point2D.Double();
		localToWorld(pp, wp);
		recenter(_worldSystem, wp);
		setDirty(true);
		refresh();
	}

	/**
	 * Recenter the world rectangle.
	 *
	 * @param wr        the affected rectangle
	 * @param newCenter the new center.
	 */
	private void recenter(Rectangle2D.Double wr, Point2D.Double newCenter) {
		wr.x = newCenter.x - wr.width / 2.0;
		wr.y = newCenter.y - wr.height / 2.0;
	}

	/**
	 * Begin preparations for a zoom.
	 */
	@Override
	public void prepareToZoom() {
		_previousWorldSystem = copy(_worldSystem);
	}

	/**
	 * Restore the default world. This gets us back to the original zoom level.
	 */
	@Override
	public void restoreDefaultWorld() {
		_worldSystem = copy(_defaultWorldSystem);
		setDirty(true);
		refresh();
	}

	/**
	 * Refresh the container.
	 */
	@Override
	public void refresh() {
		if (getView().isViewVisible()) {

			_component.repaint();

			if (getToolBar() != null) {
				if (getToolBar().getUserComponent() != null) {
					getToolBar().getUserComponent().repaint();
				}
			}
		}
	}

	/**
	 * Convenience routine to scale the container.
	 *
	 * @param scaleFactor the scale factor.
	 */
	@Override
	public void scale(double scaleFactor) {
		prepareToZoom();
		scale(_worldSystem, scaleFactor);
		setDirty(true);
		refresh();
	}

	/**
	 * Scale the world rectangle, keeping the center fixed.
	 *
	 * @param wr    the affected rectangle
	 * @param scale the factor to scale by.
	 */
	private void scale(Rectangle2D.Double wr, double scale) {
		double xc = wr.getCenterX();
		double yc = wr.getCenterY();
		wr.width *= scale;
		wr.height *= scale;
		wr.x = xc - wr.width / 2.0;
		wr.y = yc - wr.height / 2.0;
	}


	/**
	 * Undo that last zoom.
	 */
	@Override
	public void undoLastZoom() {
		Rectangle2D.Double temp = _worldSystem;
		_worldSystem = copy(_previousWorldSystem);
		_previousWorldSystem = temp;
		setDirty(true);
		refresh();
	}


	/**
	 * This is called when we have completed a rubber banding. pane.
	 *
	 * @param b The rubber band bounds.
	 */

	@Override
	public void rubberBanded(Rectangle b) {
		// if too small, don't zoom
		if ((b.width < 10) || (b.height < 10)) {
			return;
		}
		localToWorld(b, _worldSystem);
		setDirty(true);
		refresh();
	}



	/**
	 * Zooms to the specified area.
	 *
	 * @param xmin minimum x coordinate.
	 * @param xmax maximum x coordinate.
	 * @param ymin minimum y coordinate.
	 * @param ymax maximum y coordinate.
	 */
	@Override
	public void zoom(final double xmin, final double xmax, final double ymin, final double ymax) {
		prepareToZoom();
		_worldSystem = new Rectangle2D.Double(xmin, ymin, xmax - xmin, ymax - ymin);
		setDirty(true);
		refresh();
	}


	/**
	 * Get this container's tool bar.
	 *
	 * @return this container's tool bar, or <code>null</code>.
	 */
	@Override
	public BaseToolBar getToolBar() {
		return _toolBar;
	}

	/**
	 * Set this container's tool bar.
	 *
	 * @param toolBar the new toolbar.
	 */
	@Override
	public void setToolBar(BaseToolBar toolBar) {
		_toolBar = toolBar;
	}

	/**
	 * The mouse has been clicked.
	 *
	 * @param mouseEvent the causal event.
	 */
	@Override
	public void mouseClicked(MouseEvent mouseEvent) {
		if (!_component.isEnabled()) {
			Toolkit.getDefaultToolkit().beep();
			return;
		}
	}

	/**
	 * The mouse has entered the container.
	 *
	 * @param mouseEvent the causal event.
	 */
	@Override
	public void mouseEntered(MouseEvent mouseEvent) {
		_currentMousePoint = mouseEvent.getPoint();

		ToolBarToggleButton mtb = getActiveButton();
		if (mtb != null) {
			_component.setCursor(mtb.canvasCursor());
		}
	}

	/**
	 * The mouse has exited the container.
	 *
	 * @param mouseEvent the causal event.
	 */
	@Override
	public void mouseExited(MouseEvent mouseEvent) {
		_currentMousePoint = null;
	}

	/**
	 * The mouse was pressed in the container.
	 *
	 * @param mouseEvent the causal event.
	 */
	@Override
	public void mousePressed(MouseEvent mouseEvent) {
	}

	/**
	 * The mouse was released in the container.
	 *
	 * @param mouseEvent the causal event.
	 */
	@Override
	public void mouseReleased(MouseEvent mouseEvent) {
		if (_toolBar != null) {
			_toolBar.checkButtonState();
		}
	}

	/**
	 * The mouse was dragged in the container.
	 *
	 * @param mouseEvent the causal event.
	 */
	@Override
	public void mouseDragged(MouseEvent mouseEvent) {
		if (!_component.isEnabled()) {
			return;
		}
		locationUpdate(mouseEvent, true);
	}

	/**
	 * The mouse has moved in the container.
	 *
	 * @param mouseEvent the causal event.
	 */
	@Override
	public void mouseMoved(MouseEvent mouseEvent) {
		if (!_component.isEnabled()) {
			return;
		}
		_currentMousePoint = mouseEvent.getPoint();
		locationUpdate(mouseEvent, false);
	}

	/**
	 * Convert the mouse event location to a world point.
	 *
	 * @param me the mouse event
	 * @return the world location of the mouse click
	 */
	protected Point2D.Double getLocation(MouseEvent me) {
		if (me == null) {
			return null;
		}

		Point2D.Double wp = new Point2D.Double();
		localToWorld(me.getPoint(), wp);
		return wp;
	}

	/**
	 * Gets the current mouse position.
	 *
	 * @return the current mouse position.
	 */
	public Point getCurrentMousePoint() {
		return _currentMousePoint;
	}

	/**
	 * Get the active button on the toolbar, if there is a toolbar.
	 *
	 * @return the active toggle button.
	 */
	@Override
	public ToolBarToggleButton getActiveButton() {
		BaseToolBar tb = getToolBar();
		if (tb == null) {
			return null;
		} else {
			return tb.getActiveButton();
		}
	}

	/**
	 * Convenience method to update the location string in the toolbar.
	 *
	 * @param mouseEvent the causal event.
	 * @param dragging   <code>true</code> if we are dragging
	 */
	@Override
	public void locationUpdate(MouseEvent mouseEvent, boolean dragging) {

		_lastLocationMouseEvent = mouseEvent;
		ToolBarToggleButton mtb = getActiveButton();
		Point2D.Double wp = null;
		wp = getLocation(mouseEvent);

		if (mtb == null) {
			if (_feedbackControl != null) {
				_feedbackControl.updateFeedback(mouseEvent, wp, dragging);
			}
			return;
		}

		if (mtb == _toolBar.getPointerButton()) { // pointer active
			getToolBar().setText(Point2DSupport.toString(wp));
			if (_feedbackControl != null) {
				_feedbackControl.updateFeedback(mouseEvent, wp, dragging);
			}
		} else if (mtb == _toolBar.getPanButton()) { // pan active
			// do nothing
		} else { // default case
			wp = getLocation(mouseEvent);
			getToolBar().setText(Point2DSupport.toString(wp));
			if (_feedbackControl != null) {
				_feedbackControl.updateFeedback(mouseEvent, wp, dragging);
			}
		}
	}

	/**
	 * Force a redo of the feedback even though the mouse didn't move. This is
	 * useful, for example, when control-N'ing events.
	 */
	@Override
	public void redoFeedback() {
		if (_lastLocationMouseEvent != null) {
			locationUpdate(_lastLocationMouseEvent, false);
		}
	}

	/**
	 * Get the view (internal frame) that holds this container.
	 *
	 * @return the view (internal frame) that holds this container.
	 */
	@Override
	public BaseView getView() {
		return _view;
	}

	/**
	 * Sets the feedback pane. This is an optional alternative to a HUD.
	 *
	 * @param feedbackPane the feedback pane.
	 */
	@Override
	public void setFeedbackPane(FeedbackPane feedbackPane) {
		_feedbackPane = feedbackPane;
	}

	/**
	 * Get the optional feedback pane.
	 *
	 * @return the feedbackPane
	 */
	@Override
	public FeedbackPane getFeedbackPane() {
		return _feedbackPane;
	}

	/**
	 * Return the object that controls the container's feedback. You can and and
	 * remove feedback providers using this object.
	 *
	 * @return the object that controls the container's feedback.
	 */
	@Override
	public FeedbackControl getFeedbackControl() {
		return _feedbackControl;
	}

	/**
	 * Get the optional YouAreHereItem
	 *
	 * @return the youAreHereItem
	 */
	@Override
	public YouAreHereItem getYouAreHereItem() {
		return _youAreHereItem;
	}

	/**
	 * Set the optional YouAreHereItem.
	 *
	 * @param youAreHereItem the youAreHereItem to set
	 */
	@Override
	public void setYouAreHereItem(YouAreHereItem youAreHereItem) {
		_youAreHereItem = youAreHereItem;
	}

	/**
	 * Get the underlying component, which is me.
	 *
	 * @return the underlying component, which is me.
	 */
	@Override
	public Component getComponent() {
		return _component;
	}

	/**
	 * Get a location string for a point
	 *
	 * @param wp the world point in question
	 * @return a location string for a point
	 */
	@Override
	public String getLocationString(Point2D.Double wp) {
		return Point2DSupport.toString(wp);
	}


	/**
	 * Create a Point2D.Double or subclass thereof that is appropriate for this
	 * container.
	 *
	 * @return a Point2D.Double or subclass thereof that is appropriate for this
	 *         container.
	 */
	@Override
	public Point2D.Double getWorldPoint() {
		return new Point2D.Double();
	}

	/**
	 * Get the current world system
	 *
	 * @return the world system
	 */
	@Override
	public Rectangle2D.Double getWorldSystem() {
		return _worldSystem;
	}

	/**
	 * Set the world system (does not cause redraw)
	 *
	 * @param wr the new world system
	 */
	@Override
	public void setWorldSystem(Rectangle2D.Double wr) {
		_worldSystem = new Rectangle2D.Double(wr.x, wr.y, wr.width, wr.height);
	}


	// Get the transforms for world to local and vice versa
	protected void setAffineTransforms() {
		Rectangle bounds = getInsetRectangle();

		if ((bounds == null) || (bounds.width < 1) || (bounds.height < 1)) {
			localToWorld = null;
			worldToLocal = null;
			return;
		}

		if ((_worldSystem == null) || (Math.abs(_worldSystem.width) < 1.0e-12)
				|| (Math.abs(_worldSystem.height) < 1.0e-12)) {
			localToWorld = null;
			worldToLocal = null;
			return;
		}

		double scaleX = _worldSystem.width / bounds.width;
		double scaleY = _worldSystem.height / bounds.height;

		localToWorld = AffineTransform.getTranslateInstance(_worldSystem.getMinX(), _worldSystem.getMaxY());
		localToWorld.concatenate(AffineTransform.getScaleInstance(scaleX, -scaleY));
		localToWorld.concatenate(AffineTransform.getTranslateInstance(-bounds.x, -bounds.y));

		try {
			worldToLocal = localToWorld.createInverse();
		} catch (NoninvertibleTransformException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Convert a pixel based polygon to a world based polygon.
	 *
	 * @param polygon      the pixel based polygon
	 * @param worldPolygon the world based polygon
	 */
	@Override
	public void localToWorld(Polygon polygon, WorldPolygon worldPolygon) {
		Point2D.Double wp = new Point2D.Double();
		Point pp = new Point();
		for (int i = 0; i < polygon.npoints; ++i) {
			pp.setLocation(polygon.xpoints[i], polygon.ypoints[i]);
			localToWorld(pp, wp);
			worldPolygon.addPoint(wp.x, wp.y);
		}
	}

	/**
	 * Convert a world based polygon to a pixel based polygon.
	 *
	 * @param polygon      the pixel based polygon
	 * @param worldPolygon the world based polygon
	 */
	@Override
	public void worldToLocal(Polygon polygon, WorldPolygon worldPolygon) {
		Point pp = new Point();
		for (int i = 0; i < worldPolygon.npoints; ++i) {
			worldToLocal(pp, worldPolygon.xpoints[i], worldPolygon.ypoints[i]);
			polygon.addPoint(pp.x, pp.y);
		}
	}

	@Override
	public void setView(BaseView view) {
		_view = view;
	}

	/**
	 * The mouse scroll wheel has been moved.
	 *
	 * @param mouseEvent the causal event.
	 */
	@Override
	public void mouseWheelMoved(MouseWheelEvent mouseEvent) {
	}

	/**
	 * Obtain the inset rectangle. Insets are the inert region around the
	 * container's active area. Often there are no insets. Sometimes they are used
	 * so that text can be written in the inset area, such as for plot view.
	 *
	 * @return the inset rectangle.
	 */
	@Override
	public Rectangle getInsetRectangle() {
		Rectangle b = getComponent().getBounds();
		if (b == null) {
			return null;
		}

		// ignore b.x and b.y as usual
		int left = _lMargin;
		int top = _tMargin;
		int right = b.width - _rMargin;
		int bottom = b.height - _bMargin;

		Rectangle screenRect = new Rectangle(left, top, right - left, bottom - top);
		return screenRect;

	}

	/**
	 * Set the left margin
	 *
	 * @param lMargin the left margin
	 */
	@Override
	public void setLeftMargin(int lMargin) {
		_lMargin = lMargin;
	}

	/**
	 * Set the top margin
	 *
	 * @param tMargin the top margin
	 */
	@Override
	public void setTopMargin(int tMargin) {
		_tMargin = tMargin;
	}

	/**
	 * Set the right margin
	 *
	 * @param rMargin the right margin
	 */
	@Override
	public void setRightMargin(int rMargin) {
		_rMargin = rMargin;
	}

	/**
	 * Set the bottom margin
	 *
	 * @param bMargin the bottom margin
	 */
	@Override
	public void setBottomMargin(int bMargin) {
		_bMargin = bMargin;
	}

	/**
	 * The active toolbar button changed.
	 *
	 * @param activeButton the new active button.
	 */
	@Override
	public void activeToolBarButtonChanged(ToolBarToggleButton activeButton) {
	}

	/**
	 * Get the background image.
	 *
	 * @return the fully painted background image.
	 */
	@Override
	public BufferedImage getImage() {
		BufferedImage image = GraphicsUtilities.getComponentImageBuffer(_component);
		GraphicsUtilities.paintComponentOnImage(_component, image);
		return image;

	}

	// copier
	protected Rectangle2D.Double copy(Rectangle2D.Double wr) {
		return new Rectangle2D.Double(wr.x, wr.y, wr.width, wr.height);
	}


}
