package cnuphys.ced.cedview.alert;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.Collection;
import java.util.List;

import cnuphys.bCNU.drawable.DrawableAdapter;
import cnuphys.bCNU.drawable.IDrawable;
import cnuphys.bCNU.graphics.GraphicsUtilities;
import cnuphys.bCNU.graphics.container.IContainer;
import cnuphys.bCNU.item.YouAreHereItem;
import cnuphys.bCNU.util.Fonts;
import cnuphys.bCNU.util.PropertySupport;
import cnuphys.ced.cedview.CedView;
import cnuphys.ced.cedview.CedXYView;
import cnuphys.ced.component.ControlPanel;
import cnuphys.ced.component.DisplayBits;
import cnuphys.ced.geometry.alert.AlertGeometry;
import cnuphys.ced.geometry.alert.DCLayer;
import cnuphys.ced.geometry.alert.TOFLayer;

public class AlertXYView extends CedXYView {


	// for naming clones
	private static int CLONE_COUNT = 0;

	// base title
	private static final String _baseTitle = "ALERT XY";

	// units are mm
	private static Rectangle2D.Double _defaultWorldRectangle = new Rectangle2D.Double(120, -120, -240, 240);

	/**
	 * Create a Alert detector XY View
	 *
	 * @param keyVals
	 */
	private AlertXYView(Object... keyVals) {
		super(keyVals);
	}

	public static AlertXYView createAlertXYView() {
		// set to a fraction of screen
		Dimension d = GraphicsUtilities.screenFraction(0.35);

		// make it square
		int width = d.width;
		int height = width;

		String title = _baseTitle + ((CLONE_COUNT == 0) ? "" : ("_(" + CLONE_COUNT + ")"));

		final AlertXYView view = new AlertXYView(PropertySupport.WORLDSYSTEM, _defaultWorldRectangle,
				PropertySupport.WIDTH, width, PropertySupport.HEIGHT, height, PropertySupport.LEFTMARGIN, LMARGIN,
				PropertySupport.TOPMARGIN, TMARGIN, PropertySupport.RIGHTMARGIN, RMARGIN, PropertySupport.BOTTOMMARGIN,
				BMARGIN, PropertySupport.TOOLBAR, true, PropertySupport.TOOLBARBITS, CedView.TOOLBARBITS,
				PropertySupport.VISIBLE, true, PropertySupport.TITLE, title, PropertySupport.PROPNAME, "AlertXY",
				PropertySupport.STANDARDVIEWDECORATIONS,
				true);

		view._controlPanel = new ControlPanel(view,
				ControlPanel.DISPLAYARRAY + ControlPanel.FEEDBACK + ControlPanel.ACCUMULATIONLEGEND +
				ControlPanel.MATCHINGBANKSPANEL,
				DisplayBits.ACCUMULATION + DisplayBits.CROSSES + DisplayBits.MCTRUTH + DisplayBits.RECONHITS
						+ DisplayBits.ADC_HITS,
				3, 5);

		view.add(view._controlPanel, BorderLayout.EAST);
		view.pack();
		return view;
	}

	@Override
	protected void setBeforeDraw() {
		IDrawable beforeDraw = new DrawableAdapter() {

			@Override
			public void draw(Graphics g, IContainer container) {
				drawLayerShells(g, container);
				drawWires(g, container);
				drawPaddles(g, container);
				
				AlertGeometry.drawTOFSectorOutlines(g, container);
			}

		};

		getContainer().setBeforeDraw(beforeDraw);
	}

	@Override
	protected void setAfterDraw() {
		IDrawable beforeDraw = new DrawableAdapter() {

			@Override
			public void draw(Graphics g, IContainer container) {

				Rectangle screenRect = getActiveScreenRectangle(container);
				drawAxes(g, container, screenRect, false);
			}

		};

		getContainer().setAfterDraw(beforeDraw);
	}

	@Override
	protected void addItems() {
	}

	//draw the dc wires
	private void drawWires(Graphics g, IContainer container) {
		
		Collection<DCLayer> dcLayers = AlertGeometry.getAllDCLayers();
		
		for (DCLayer dcl : dcLayers) {
			if (dcl.numWires >  0) {
				dcl.drawXYWires(g, container);
			}
		}
	}


	//draw all the layers shells
	private void drawLayerShells(Graphics g, IContainer container) {
		
		Collection<DCLayer> dcLayers = AlertGeometry.getAllDCLayers();
		
		for (DCLayer dcl : dcLayers) {
			if (dcl.numWires >  0) {
				dcl.drawXYDonut(g, container);
			}
			
		}
	}
	
	private void drawPaddles(Graphics g, IContainer container) {
		Collection<TOFLayer> tofLayers = AlertGeometry.getAllTOFLayers();
		
		for (TOFLayer tof : tofLayers) {
			if (tof.numPaddles >  0) {
				tof.drawAllPaddles(g, container);
			}
			
		}
		
	}

	/**
	 * Some view specific feedback. Should always call super.getFeedbackStrings
	 * first.
	 *
	 * @param container   the base container for the view.
	 * @param pp the pixel point
	 * @param wp  the corresponding world location.
	 */
	@Override
	public void getFeedbackStrings(IContainer container, Point pp, Point2D.Double wp,
			List<String> feedbackStrings) {

		basicFeedback(container, pp, wp, "mm", feedbackStrings);

		Collection<DCLayer> dcLayers = AlertGeometry.getAllDCLayers();

		// anchor (urhere) feedback?
		YouAreHereItem item = getContainer().getYouAreHereItem();
		if (item != null) {
			Point2D.Double anchor = item.getFocus();
			String dstr = String.format("$khaki$Dist from ref. point: %5.2f mm", anchor.distance(wp));
			feedbackStrings.add(dstr);
		}

		for (DCLayer dcl : dcLayers) {
			if (dcl.containsXY(wp)) {
				dcl.feedbackXYString(pp, wp, feedbackStrings);
				return;
			}
		}

		Collection<TOFLayer> tofLayers = AlertGeometry.getAllTOFLayers();

		for (TOFLayer tof : tofLayers) {
			if (tof.feedbackXYString(pp, wp, feedbackStrings)) {
				return;
			}

		}
	}

}
