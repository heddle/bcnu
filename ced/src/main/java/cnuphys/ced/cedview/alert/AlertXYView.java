package cnuphys.ced.cedview.alert;

import java.awt.BorderLayout;
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
import cnuphys.bCNU.util.PropertySupport;
import cnuphys.bCNU.view.BaseView;
import cnuphys.ced.cedview.CedView;
import cnuphys.ced.cedview.CedXYView;
import cnuphys.ced.cedview.ILabCoordinates;
import cnuphys.ced.component.ControlPanel;
import cnuphys.ced.component.DisplayBits;
import cnuphys.ced.geometry.alert.AlertGeometry;
import cnuphys.ced.geometry.alert.DCLayer;
import cnuphys.ced.geometry.alert.TOFLayer;

public class AlertXYView extends CedXYView implements ILabCoordinates {



	// for naming clones
	private static int CLONE_COUNT = 0;

	// base title
	private static final String _baseTitle = "ALERT XY";

	// units are mm
	private static Rectangle2D.Double _defaultWorldRectangle = new Rectangle2D.Double(-120, -120, 240, 240);

	// bank matches
	private static String _defMatches[] = { "AHDC", "ATOF" };

	// used to draw swum trajectories (if any) in the after drawer
	private SwimTrajectoryDrawer _swimTrajectoryDrawer;

	//draw hits in the DC
	private AlertDCHitDrawer _dcHitDrawer;

	//draw hits in the TOF
	private AlertTOFHitDrawer _tofHitDrawer;

	//wire projection
	private DCWireProjectionPanel _dcPanel;

	/**
	 * Create a Alert detector XY View
	 *
	 * @param keyVals
	 */
	private AlertXYView(Object... keyVals) {
		super(keyVals);
		// draws any swum trajectories (in the after draw)
		_swimTrajectoryDrawer = new SwimTrajectoryDrawer(this);
		_dcHitDrawer = new AlertDCHitDrawer(this);
		_tofHitDrawer = new AlertTOFHitDrawer(this);
	}

	/**
	 * Create a Alert detector XY view
	 * @return a Alert detector XY view
	 */
	public static AlertXYView createAlertXYView() {
		// set to a fraction of screen
		Dimension d = GraphicsUtilities.screenFraction(0.4);

		// make it square
		int width = d.width;
		int height = width-100;

		String title = _baseTitle + ((CLONE_COUNT == 0) ? "" : ("_(" + CLONE_COUNT + ")"));

		final AlertXYView view = new AlertXYView(PropertySupport.WORLDSYSTEM, _defaultWorldRectangle,
				PropertySupport.WIDTH, width, 
				PropertySupport.HEIGHT, height, 
				PropertySupport.LEFTMARGIN, LMARGIN,
				PropertySupport.TOPMARGIN, TMARGIN,
				PropertySupport.RIGHTMARGIN, RMARGIN, 
				PropertySupport.BOTTOMMARGIN, BMARGIN, 
				PropertySupport.TOOLBAR, true, 
				PropertySupport.TOOLBARBITS, CedView.TOOLBARBITS,
				PropertySupport.VISIBLE, true, 
				PropertySupport.TITLE, title, 
				PropertySupport.PROPNAME, "AlertXY",
				PropertySupport.STANDARDVIEWDECORATIONS, true);

		view._controlPanel = new ControlPanel(view,
				ControlPanel.DISPLAYARRAY + ControlPanel.FEEDBACK + ControlPanel.ACCUMULATIONLEGEND +
				ControlPanel.MATCHINGBANKSPANEL,
				DisplayBits.ACCUMULATION + DisplayBits.CROSSES + DisplayBits.RECONHITS
						+ DisplayBits.ADCDATA + DisplayBits.MCTRUTH,
				3, 5);

		view.add(view._controlPanel, BorderLayout.EAST);
		view.pack();

		// i.e. if none were in the properties
		if (view.hasNoBankMatches()) {
			view.setBankMatches(_defMatches);
		}

		view._controlPanel.getMatchedBankPanel().update();
		
		//add dc projection panel
		view._dcPanel = new DCWireProjectionPanel(view);
		view._controlPanel.addComponent(view._dcPanel);

		return view;
	}

	@Override
	protected void setBeforeDraw() {
		IDrawable beforeDraw = new DrawableAdapter() {

			@Override
			public void draw(Graphics g, IContainer container) {

				if (!_eventManager.isAccumulating()) {
					drawWires(g, container);
					drawPaddles(g, container);

					AlertGeometry.drawAlertTOFSectorOutlines(g, container);
				}
			}

		};

		getContainer().setBeforeDraw(beforeDraw);
	}

	@Override
	protected void setAfterDraw() {
		final AlertXYView view = this;

		IDrawable afterDraw = new DrawableAdapter() {

			@Override
			public void draw(Graphics g, IContainer container) {

				if (!_eventManager.isAccumulating()) {

					if (view.isSingleEventMode()) {
						drawSingleModeHits(g, container);
					}

					else {
						drawAccumulatedHits(g, container);
					}
				}

				Rectangle screenRect = getActiveScreenRectangle(container);
				drawAxes(g, container, screenRect, false);

			}

		};

		getContainer().setAfterDraw(afterDraw);
	}

	private void drawSingleModeHits(Graphics g, IContainer container) {
		_dcHitDrawer.drawHits(g, container);
		_tofHitDrawer.drawHits(g, container);

		//overlay trajectories
		_swimTrajectoryDrawer.draw(g, container);

	}

	private void drawAccumulatedHits(Graphics g, IContainer container) {
		_dcHitDrawer.drawAccumulatedHits(g, container);
		_tofHitDrawer.drawAccumulatedHits(g, container);

	}


	@Override
	protected void addItems() {
	}


	//draw the dc wires
	private void drawWires(Graphics g, IContainer container) {

		Collection<DCLayer> dcLayers = AlertGeometry.getAllDCLayers();

		for (DCLayer dcl : dcLayers) {
			if (dcl.numWires >  0) {
				dcl.drawXYWires(g, container, useWireMidpoint(), getFixedZ());
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
	 * Convert lab coordinates (CLAS x,y,z) to world coordinates (2D world system of
	 * the view)
	 *
	 * @param x  the CLAS12 x coordinate
	 * @param y  the CLAS12 y coordinate
	 * @param z  the CLAS12 z coordinate
	 * @param wp holds the world point
	 */
	@Override
	public void labToWorld(double x, double y, double z, Point2D.Double wp) {
		wp.x = x;
		wp.y = y;
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

		AlertXYView view = createAlertXYView();
		view.setBounds(vr);
		return view;

	}

	/**
	 * Are we using the wire midpoints?
	 * 
	 * @return <code>true</code> if we are using the wire midpoints.
	 */
	public boolean useWireMidpoint() {
		return _dcPanel.useWireMidpoint();
	}
	
	/**
	 * Get the fixed Z value if we are not using midpoints
	 * 
	 * @return the fixed Z value.
	 */
	public double getFixedZ() {
		return _dcPanel.getFixedZ();
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
				_dcHitDrawer.getHitFeedbackStrings(container, pp, wp, dcl, feedbackStrings);
				return;
			}
		}

		Collection<TOFLayer> tofLayers = AlertGeometry.getAllTOFLayers();

		for (TOFLayer tof : tofLayers) {
			if (tof.feedbackXYString(pp, wp, feedbackStrings)) {
				_tofHitDrawer.getHitFeedbackStrings(container, pp, wp, tof, feedbackStrings);
				return;
			}

		}

	}

}
