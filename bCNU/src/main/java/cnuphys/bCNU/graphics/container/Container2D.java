package cnuphys.bCNU.graphics.container;

import java.awt.Component;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D.Double;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Vector;

import cnuphys.bCNU.drawable.DrawableList;
import cnuphys.bCNU.drawable.IDrawable;
import cnuphys.bCNU.feedback.FeedbackControl;
import cnuphys.bCNU.feedback.FeedbackPane;
import cnuphys.bCNU.graphics.toolbar.BaseToolBar;
import cnuphys.bCNU.graphics.toolbar.ToolBarToggleButton;
import cnuphys.bCNU.graphics.world.WorldPolygon;
import cnuphys.bCNU.item.AItem;
import cnuphys.bCNU.item.YouAreHereItem;
import cnuphys.bCNU.layer.LogicalLayer;
import cnuphys.bCNU.view.BaseView;
import cnuphys.bCNU.visible.VisibilityTableScrollPane;

public class Container2D implements IContainer {

	@Override
	public LogicalLayer addLogicalLayer(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addLogicalLayer(LogicalLayer layer) {
		// TODO Auto-generated method stub

	}

	@Override
	public LogicalLayer getAnnotationLayer() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public LogicalLayer getLogicalLayer(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void removeLogicalLayer(LogicalLayer layer) {
		// TODO Auto-generated method stub

	}

	@Override
	public void localToWorld(Point pp, Double wp) {
		// TODO Auto-generated method stub

	}

	@Override
	public void worldToLocal(Point pp, Double wp) {
		// TODO Auto-generated method stub

	}

	@Override
	public void worldToLocal(Rectangle r, java.awt.geom.Rectangle2D.Double wr) {
		// TODO Auto-generated method stub

	}

	@Override
	public void localToWorld(Rectangle r, java.awt.geom.Rectangle2D.Double wr) {
		// TODO Auto-generated method stub

	}

	@Override
	public void worldToLocal(Polygon polygon, WorldPolygon worldPolygon) {
		// TODO Auto-generated method stub

	}

	@Override
	public void localToWorld(Polygon polygon, WorldPolygon worldPolygon) {
		// TODO Auto-generated method stub

	}

	@Override
	public void worldToLocal(Point pp, double wx, double wy) {
		// TODO Auto-generated method stub

	}

	@Override
	public void pan(int dh, int dv) {
		// TODO Auto-generated method stub

	}

	@Override
	public void recenter(Point pp) {
		// TODO Auto-generated method stub

	}

	@Override
	public void prepareToZoom() {
		// TODO Auto-generated method stub

	}

	@Override
	public void restoreDefaultWorld() {
		// TODO Auto-generated method stub

	}

	@Override
	public void scale(double scaleFactor) {
		// TODO Auto-generated method stub

	}

	@Override
	public void undoLastZoom() {
		// TODO Auto-generated method stub

	}

	@Override
	public VisibilityTableScrollPane getVisibilityTableScrollPane() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void rubberBanded(Rectangle b) {
		// TODO Auto-generated method stub

	}

	@Override
	public AItem getItemAtPoint(Point lp) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Vector<AItem> getEnclosedItems(Rectangle rect) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Vector<AItem> getItemsAtPoint(Point lp) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean anySelectedItems() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void deleteSelectedItems(IContainer container) {
		// TODO Auto-generated method stub

	}

	@Override
	public void selectAllItems(boolean select) {
		// TODO Auto-generated method stub

	}

	@Override
	public void zoom(double xmin, double xmax, double ymin, double ymax) {
		// TODO Auto-generated method stub

	}

	@Override
	public void reworld(double xmin, double xmax, double ymin, double ymax) {
		// TODO Auto-generated method stub

	}

	@Override
	public BaseToolBar getToolBar() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setToolBar(BaseToolBar toolBar) {
		// TODO Auto-generated method stub

	}

	@Override
	public void activeToolBarButtonChanged(ToolBarToggleButton activeButton) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean handledPrint() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean handledCamera() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public ToolBarToggleButton getActiveButton() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void locationUpdate(MouseEvent mouseEvent, boolean dragging) {
		// TODO Auto-generated method stub

	}

	@Override
	public void redoFeedback() {
		// TODO Auto-generated method stub

	}

	@Override
	public BaseView getView() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setView(BaseView view) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setFeedbackPane(FeedbackPane feedbackPane) {
		// TODO Auto-generated method stub

	}

	@Override
	public FeedbackPane getFeedbackPane() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FeedbackControl getFeedbackControl() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public YouAreHereItem getYouAreHereItem() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setYouAreHereItem(YouAreHereItem youAreHereItem) {
		// TODO Auto-generated method stub

	}

	@Override
	public LogicalLayer getGlassLayer() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void handleFile(File file) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setDirty(boolean dirty) {
		// TODO Auto-generated method stub

	}

	@Override
	public void refresh() {
		// TODO Auto-generated method stub

	}

	@Override
	public Component getComponent() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BufferedImage getImage() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setAfterDraw(IDrawable afterDraw) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setBeforeDraw(IDrawable beforeDraw) {
		// TODO Auto-generated method stub

	}

	@Override
	public AItem createEllipseItem(LogicalLayer layer, Rectangle b) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AItem createRectangleItem(LogicalLayer layer, Rectangle b) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AItem createLineItem(LogicalLayer layer, Point p0, Point p1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AItem createPolygonItem(LogicalLayer layer, Point[] pp) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AItem createPolylineItem(LogicalLayer layer, Point[] pp) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AItem createRadArcItem(LogicalLayer layer, Point pc, Point p1, double arcAngle) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getLocationString(Double wp) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DrawableList getLogicalLayers() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Double getWorldPoint() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public java.awt.geom.Rectangle2D.Double getWorldSystem() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setWorldSystem(java.awt.geom.Rectangle2D.Double wr) {
		// TODO Auto-generated method stub

	}

	@Override
	public Rectangle getInsetRectangle() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setLeftMargin(int lMargin) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setTopMargin(int tMargin) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setRightMargin(int rMargin) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setBottomMargin(int bMargin) {
		// TODO Auto-generated method stub

	}

}
