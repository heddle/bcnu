package cnuphys.ced.cedview.ft;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.List;

import cnuphys.bCNU.drawable.DrawableAdapter;
import cnuphys.bCNU.drawable.IDrawable;
import cnuphys.bCNU.graphics.GraphicsUtilities;
import cnuphys.bCNU.graphics.container.IContainer;
import cnuphys.bCNU.util.PropertySupport;
import cnuphys.bCNU.view.BaseView;
import cnuphys.ced.cedview.CedView;
import cnuphys.ced.cedview.CedXYView;
import cnuphys.ced.component.ControlPanel;
import cnuphys.ced.component.DisplayBits;
import cnuphys.ced.event.AccumulationManager;
import cnuphys.ced.event.data.DataDrawSupport;
import cnuphys.ced.event.data.arrays.ADCArrays;
import cnuphys.ced.event.data.arrays.HitArrays;
import cnuphys.ced.geometry.FTCALGeometry;

public class FTCalXYView extends CedXYView {

	// for naming clones
	private static int CLONE_COUNT = 0;

	// base title
	private static final String _baseTitle = "FTCal XY";

	// units are cm
	private static Rectangle2D.Double _defaultWorldRectangle = new Rectangle2D.Double(20., -20., -40., 40.);

	// the CND xy polygons
	FTCalXYPolygon ftCalPoly[] = new FTCalXYPolygon[332];

	//the reverse mappings
	private short[] componentToIndex = new short[476];
	
	//bank matches
	private static String _defMatches[] = {"FTCAL"};
	
	//good ids are the 332 component ids
	private static final short[] goodIds = FTCALGeometry.getGoodIds();


	/**
	 * Create a FTCalXYView View
	 *
	 */
	public FTCalXYView(Object... keyVals) {
		super(keyVals);

		for (int i = 0; i < componentToIndex.length; i++) {
			componentToIndex[i] = -1;
		}

		for (int i = 0; i < 332; i++) {
			int id = goodIds[i];
			componentToIndex[id] = (short) i; // reverse mapping
			ftCalPoly[i] = new FTCalXYPolygon(id);
		}

	}

	/**
	 * Create a FTCalXYView view
	 *
	 * @return a FTCalXYView View
	 */
	public static FTCalXYView createFTCalXYView() {
		FTCalXYView view = null;

		// set to a fraction of screen
		Dimension d = GraphicsUtilities.screenFraction(0.35);

		// make it square
		int width = d.width;
		int height = width;

		String title = _baseTitle + ((CLONE_COUNT == 0) ? "" : ("_(" + CLONE_COUNT + ")"));

		// create the view
		view = new FTCalXYView(PropertySupport.WORLDSYSTEM, _defaultWorldRectangle, PropertySupport.WIDTH, width,
				PropertySupport.HEIGHT, height, PropertySupport.LEFTMARGIN, LMARGIN, PropertySupport.TOPMARGIN, TMARGIN,
				PropertySupport.RIGHTMARGIN, RMARGIN, PropertySupport.BOTTOMMARGIN, BMARGIN, PropertySupport.TOOLBAR,
				true, PropertySupport.TOOLBARBITS, CedView.TOOLBARBITS, PropertySupport.VISIBLE, true,
				PropertySupport.TITLE, title, PropertySupport.PROPNAME, "FTCalXY",
				PropertySupport.STANDARDVIEWDECORATIONS, true);

		view._controlPanel = new ControlPanel(view,
				ControlPanel.DISPLAYARRAY + ControlPanel.FEEDBACK + ControlPanel.MATCHINGBANKSPANEL + ControlPanel.ACCUMULATIONLEGEND,
				DisplayBits.ACCUMULATION + DisplayBits.MCTRUTH + DisplayBits.RECONHITS, 3, 5);

		view.add(view._controlPanel, BorderLayout.EAST);
		view.pack();
		
		//i.e. if none were in the properties
		if (view.hasNoBankMatches()) {
			view.setBankMatches(_defMatches);
		}
		view._controlPanel.getMatchedBankPanel().update();


		return view;
	}

	/**
	 * Create the view's before drawer.
	 */
	@Override
	protected void setBeforeDraw() {
		// use a before-drawer to sector dividers and labels
		IDrawable beforeDraw = new DrawableAdapter() {

			@Override
			public void draw(Graphics g, IContainer container) {

				Component component = container.getComponent();
				Rectangle b = component.getBounds();

				// ignore b.x and b.y as usual

				b.x = 0;
				b.y = 0;

				Rectangle screenRect = container.getInsetRectangle();
				g.setColor(Color.white);
				g.fillRect(screenRect.x, screenRect.y, screenRect.width, screenRect.height);

				drawGrid(g, container);

				for (FTCalXYPolygon poly : ftCalPoly) {
					poly.draw(g, container);
				}

			}

			// draw the ftcal grid
			private void drawGrid(Graphics g, IContainer container) {

				g.setColor(Color.lightGray);

				double range[] = new double[2];
				Point p0 = new Point();
				Point p1 = new Point();
				double vmax = FTCALGeometry.getMaxAbsXYExtent();

				for (int ix = -11; ix <= 11; ix++) {
					if ((ix < -3) || (ix > 4)) {
						FTCALGeometry.indexToRange(ix, range);
						container.worldToLocal(p0, range[0], -vmax);
						container.worldToLocal(p1, range[0], vmax);
						g.drawLine(p0.x, p0.y, p1.x, p1.y);

						if (ix == 11) {
							container.worldToLocal(p0, range[1], -vmax);
							container.worldToLocal(p1, range[1], vmax);
							g.drawLine(p0.x, p0.y, p1.x, p1.y);
						}

						for (int iy = -11; iy <= 11; iy++) {
							if ((iy < -3) || (iy > 4)) {
								FTCALGeometry.indexToRange(iy, range);
								container.worldToLocal(p0, -vmax, range[0]);
								container.worldToLocal(p1, vmax, range[0]);
								g.drawLine(p0.x, p0.y, p1.x, p1.y);

								if (iy == 11) {
									container.worldToLocal(p0, -vmax, range[1]);
									container.worldToLocal(p1, vmax, range[1]);
									g.drawLine(p0.x, p0.y, p1.x, p1.y);
								}
							}
						}
					}
				}
			}

		};

		getContainer().setBeforeDraw(beforeDraw);
	}

	/**
	 * Set the view's after draw
	 */
	@Override
	protected void setAfterDraw() {
		IDrawable afterDraw = new DrawableAdapter() {

			@Override
			public void draw(Graphics g, IContainer container) {
				if (isSingleEventMode()) {
					drawSingleEventHits(g, container);
				} else {
					drawAccumulatedHits(g, container);
				}
				Rectangle screenRect = getActiveScreenRectangle(container);
				drawAxes(g, container, screenRect, false);
			}

		};
		getContainer().setAfterDraw(afterDraw);
	}

	// single event drawer
	private void drawSingleEventHits(Graphics g, IContainer container) {

		// draw based on adc values
		ADCArrays adcArrays = ADCArrays.getArrays("FTCAL::adc");
		if (adcArrays.hasData()) {
			for (int i = 0; i < adcArrays.sector.length; i++) {
				short component = adcArrays.component[i];
				short index = componentToIndex[component];
				if (index >= 0) {
					FTCalXYPolygon poly = ftCalPoly[index];
					Color color = adcArrays.getColor(adcArrays.sector[i], adcArrays.layer[i], adcArrays.component[i]);
					g.setColor(color);
					g.fillPolygon(poly);
					g.setColor(Color.black);
					g.drawPolygon(poly);
				} else {
					System.err.println("indexing problem in FT");
				}
			}
		}

		// draw based on hits, but the FTCal hit data doesn't map to
		// one of out hit arrays because therer is no sector, layer, component
		// so go directly to the bank

		if (this.showReconHits()) {
			int hitCount = _dataWarehouse.rowCount("FTCAL::hits");
			if (hitCount > 0) {
				float x[] = _dataWarehouse.getFloat("FTCAL::hits", "x");
				float y[] = _dataWarehouse.getFloat("FTCAL::hits", "y");
				Point pp = new Point();
				Point2D.Double wp = new Point2D.Double();

				for (int i = 0; i < hitCount; i++) {
					wp.setLocation(x[i], y[i]);
					container.worldToLocal(pp, wp);
					DataDrawSupport.drawReconHit(g, pp);
				}
			}
		}
	}

	// accumulated hits drawer
	private void drawAccumulatedHits(Graphics g, IContainer container) {

		int medianHit = AccumulationManager.getInstance().getMedianFTCALCount();

		int acchits[] = AccumulationManager.getInstance().getAccumulatedFTCALData();
		
		//loop will be 0 to 475
		for (int i = 0; i < acchits.length; i++) {
			if (acchits[i] > 0) {
				int index = componentToIndex[i];
				if (index >= 0) {
					FTCalXYPolygon poly = ftCalPoly[index];
					double fract = getMedianSetting() * (((double) acchits[i]) / (1 + medianHit));

					Color color = AccumulationManager.getInstance().getColor(getColorScaleModel(), fract);
					g.setColor(color);
					g.fillPolygon(poly);
					g.setColor(Color.black);
					g.drawPolygon(poly);
				} else {
					System.err.println("indexing problem in FTCAL");
				}
			}
		}

	}

	/**
	 * This adds the detector items. The AllDC view is not faithful to geometry. All
	 * we really uses in the number of superlayers, number of layers, and number of
	 * wires.
	 */
	@Override
	protected void addItems() {
	}

	/**
	 * Some view specific feedback. Should always call super.getFeedbackStrings
	 * first.
	 *
	 * @param container   the base container for the view.
	 * @param screenPoint the pixel point
	 * @param worldPoint  the corresponding world location.
	 */
	@Override
	public void getFeedbackStrings(IContainer container, Point screenPoint, Point2D.Double worldPoint,
			List<String> feedbackStrings) {

		basicFeedback(container, screenPoint, worldPoint, "cm", feedbackStrings);
		
		int xindex = FTCALGeometry.valToIndex(worldPoint.x);
		if (xindex != 0) {
			int yindex = FTCALGeometry.valToIndex(worldPoint.y);
			if (yindex != 0) {

				boolean found = false;

				// loop of the polygons 1-332
				for (int index = 0; index < ftCalPoly.length; index++) {
					FTCalXYPolygon poly = ftCalPoly[index];
					found = poly.contains(screenPoint);
					if (found) {
						short component = goodIds[index];
						feedbackStrings.add("FTCal index: " + component);

						ADCArrays arrays = ADCArrays.getArrays("FTCAL::adc");
						if (arrays.hasData()) {
							arrays.addFeedback((byte) 1, (byte) 1, component, feedbackStrings);
						}
					}

					if (found) {
						break;
					}

				}
			} // end for index

		}
		

		// draw based on hits, but the FTCal hit data doesn't map to
		// one of out hit arrays because therer is no sector, layer, component
		// so go directly to the bank

		if (this.showReconHits()) {
			int hitCount = _dataWarehouse.rowCount("FTCAL::hits");
			if (hitCount > 0) {
				Point pp = new Point();
				Point2D.Double wp = new Point2D.Double();
				Rectangle r = new Rectangle();

				for (int i = 0; i < hitCount; i++) {
					float x = _dataWarehouse.getFloat("FTCAL::hits", "x")[i];
					float y = _dataWarehouse.getFloat("FTCAL::hits", "y")[i];
					wp.setLocation(x, y);
					container.worldToLocal(pp, wp);
					r.setBounds(pp.x - 4, pp.y - 4, 8, 8);
					if (r.contains(screenPoint)) {
						
						byte xid = _dataWarehouse.getByte("FTCAL::hits", "idx")[i];
						byte yid = _dataWarehouse.getByte("FTCAL::hits", "idy")[i];
						float z = _dataWarehouse.getFloat("FTCAL::hits", "z")[i];
						float energy = _dataWarehouse.getFloat("FTCAL::hits", "energy")[i];
						float time = _dataWarehouse.getFloat("FTCAL::hits", "time")[i];
						
						
						String s = String.format("FTCal hit idXY: (%d, %d)", xid, yid);
						feedbackStrings.add("$cyan$" + s);
						s = String.format("FTCal hit loc: (%5.3f, %5.3f, %5.3f)", x, y, z);
						feedbackStrings.add("$cyan$" + s);
						s = String.format("FTCal energy: %6.4f time: %6.4f", energy, time);
						feedbackStrings.add("$cyan$" + s);
						break;
					}
				}
			}
		}
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

		FTCalXYView view = createFTCalXYView();
		view.setBounds(vr);
		return view;

	}

}
