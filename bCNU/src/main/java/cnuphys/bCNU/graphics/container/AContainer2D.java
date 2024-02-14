package cnuphys.bCNU.graphics.container;

import java.awt.Component;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelListener;
import java.awt.geom.Point2D.Double;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Hashtable;
import java.util.Vector;

import cnuphys.bCNU.drawable.DrawableList;
import cnuphys.bCNU.drawable.IDrawable;
import cnuphys.bCNU.drawable.IDrawableListener;
import cnuphys.bCNU.feedback.FeedbackControl;
import cnuphys.bCNU.feedback.FeedbackPane;
import cnuphys.bCNU.graphics.toolbar.BaseToolBar;
import cnuphys.bCNU.graphics.toolbar.ToolBarToggleButton;
import cnuphys.bCNU.graphics.world.WorldPolygon;
import cnuphys.bCNU.item.AItem;
import cnuphys.bCNU.item.YouAreHereItem;
import cnuphys.bCNU.layer.LogicalLayer;
import cnuphys.bCNU.log.Log;
import cnuphys.bCNU.view.BaseView;
import cnuphys.bCNU.visible.VisibilityTableScrollPane;

public abstract class AContainer2D implements IContainer, MouseListener, MouseMotionListener, MouseWheelListener, IDrawableListener {

	/**
	 * A collection of layers. This is the container's model.
	 */
	protected DrawableList _layers = new DrawableList("Layers");


	/**
	 * The annotation layer. Every container has one.
	 */
	protected LogicalLayer _annotationLayer;

	// A map of layers added by users.
	protected Hashtable<String, LogicalLayer> _userLayers = new Hashtable<>(47);


	/**
	 * Add a layer to this container.
	 *
	 * @param layer the layer to add.
	 */
	@Override
	public void addLogicalLayer(LogicalLayer layer) {

		_layers.add(layer);
		if (layer != _annotationLayer) {
			_layers.sendToFront(_annotationLayer);
		}
		layer.addDrawableListener(this);
	}

	/**
	 * Get the annotation layer for this obtainer.
	 *
	 * @return the annotation layer for this obtainer. All drawing tools draw on the
	 *         annotation layer, which is kept on top.
	 */
	@Override
	public LogicalLayer getAnnotationLayer() {
		return _annotationLayer;
	}

	/**
	 * Gets a user layer by name. Do not use for the annotation layer-- for that use
	 * getAnnotationLayer().
	 *
	 * @param name the name of the user layer.
	 * @return the layer, or <code>null</code>.
	 */
	@Override
	public LogicalLayer getLogicalLayer(String name) {
		LogicalLayer layer = _userLayers.get(name);
		if (layer == null) {
			Log.getInstance().warning("Requested nonexistant layer: " + name);
		}
		return layer;
	}

	@Override
	public void removeLogicalLayer(LogicalLayer layer) {
	}

	@Override
	public void localToWorld(Point pp, Double wp) {
	}

	@Override
	public void worldToLocal(Point pp, Double wp) {
	}

	@Override
	public void worldToLocal(Rectangle r, java.awt.geom.Rectangle2D.Double wr) {
	}

	@Override
	public void localToWorld(Rectangle r, java.awt.geom.Rectangle2D.Double wr) {
	}

	@Override
	public void worldToLocal(Polygon polygon, WorldPolygon worldPolygon) {
	}

	@Override
	public void localToWorld(Polygon polygon, WorldPolygon worldPolygon) {
	}

	@Override
	public void worldToLocal(Point pp, double wx, double wy) {
	}

	@Override
	public void pan(int dh, int dv) {
	}

	@Override
	public void recenter(Point pp) {
	}

	@Override
	public void prepareToZoom() {
	}

	@Override
	public void restoreDefaultWorld() {
	}

	@Override
	public void scale(double scaleFactor) {
	}

	@Override
	public void undoLastZoom() {
	}

	@Override
	public VisibilityTableScrollPane getVisibilityTableScrollPane() {
		return null;
	}

	@Override
	public void rubberBanded(Rectangle b) {
	}

	@Override
	public AItem getItemAtPoint(Point lp) {
		return null;
	}

	@Override
	public Vector<AItem> getEnclosedItems(Rectangle rect) {
		return null;
	}

	@Override
	public Vector<AItem> getItemsAtPoint(Point lp) {
		return null;
	}

	@Override
	public boolean anySelectedItems() {
		return false;
	}

	@Override
	public void deleteSelectedItems(IContainer container) {
	}

	@Override
	public void selectAllItems(boolean select) {
	}

	@Override
	public void zoom(double xmin, double xmax, double ymin, double ymax) {
	}

	@Override
	public void reworld(double xmin, double xmax, double ymin, double ymax) {
	}

	@Override
	public BaseToolBar getToolBar() {
		return null;
	}

	@Override
	public void setToolBar(BaseToolBar toolBar) {
	}

	@Override
	public void activeToolBarButtonChanged(ToolBarToggleButton activeButton) {
	}

	@Override
	public ToolBarToggleButton getActiveButton() {
		return null;
	}

	@Override
	public void locationUpdate(MouseEvent mouseEvent, boolean dragging) {
	}

	@Override
	public void redoFeedback() {
	}

	@Override
	public BaseView getView() {
		return null;
	}

	@Override
	public void setView(BaseView view) {
	}

	@Override
	public void setFeedbackPane(FeedbackPane feedbackPane) {
		}

	@Override
	public FeedbackPane getFeedbackPane() {
		return null;
	}

	@Override
	public FeedbackControl getFeedbackControl() {
		return null;
	}

	@Override
	public YouAreHereItem getYouAreHereItem() {
		return null;
	}

	@Override
	public void setYouAreHereItem(YouAreHereItem youAreHereItem) {
	}

	@Override
	public LogicalLayer getGlassLayer() {
		return null;
	}

	@Override
	public void handleFile(File file) {
	}

	@Override
	public void setDirty(boolean dirty) {
	}

	@Override
	public void refresh() {
	}

	@Override
	public Component getComponent() {
		return null;
	}

	@Override
	public BufferedImage getImage() {
		return null;
	}

	@Override
	public void setAfterDraw(IDrawable afterDraw) {
	}

	@Override
	public void setBeforeDraw(IDrawable beforeDraw) {
	}

	@Override
	public AItem createEllipseItem(LogicalLayer layer, Rectangle b) {
		return null;
	}

	@Override
	public AItem createRectangleItem(LogicalLayer layer, Rectangle b) {
		return null;
	}

	@Override
	public AItem createLineItem(LogicalLayer layer, Point p0, Point p1) {
		return null;
	}

	@Override
	public AItem createPolygonItem(LogicalLayer layer, Point[] pp) {
		return null;
	}

	@Override
	public AItem createPolylineItem(LogicalLayer layer, Point[] pp) {
		return null;
	}

	@Override
	public AItem createRadArcItem(LogicalLayer layer, Point pc, Point p1, double arcAngle) {
		return null;
	}

	@Override
	public String getLocationString(Double wp) {
		return null;
	}

	@Override
	public DrawableList getLogicalLayers() {
		return null;
	}

	@Override
	public Double getWorldPoint() {
		return null;
	}

	@Override
	public java.awt.geom.Rectangle2D.Double getWorldSystem() {
		return null;
	}

	@Override
	public void setWorldSystem(java.awt.geom.Rectangle2D.Double wr) {
	}

	@Override
	public Rectangle getInsetRectangle() {
		return null;
	}

	@Override
	public void setLeftMargin(int lMargin) {
	}

	@Override
	public void setTopMargin(int tMargin) {
	}

	@Override
	public void setRightMargin(int rMargin) {
	}

	@Override
	public void setBottomMargin(int bMargin) {
	}

}
