package cnuphys.ced.cedview.dchex;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.List;
import java.util.Properties;

import cnuphys.bCNU.drawable.DrawableAdapter;
import cnuphys.bCNU.drawable.IDrawable;
import cnuphys.bCNU.graphics.GraphicsUtilities;
import cnuphys.bCNU.graphics.container.IContainer;
import cnuphys.bCNU.item.ItemList;
import cnuphys.bCNU.util.PropertySupport;
import cnuphys.bCNU.util.X11Colors;
import cnuphys.bCNU.view.BaseView;
import cnuphys.ced.alldata.datacontainer.dc.DCTDCandDOCAData;
import cnuphys.ced.cedview.CedView;
import cnuphys.ced.cedview.HexView;
import cnuphys.ced.component.ControlPanel;
import cnuphys.ced.component.DisplayBits;
import cnuphys.ced.geometry.DCGeometry;

public class DCHexView extends HexView  {

	// for naming clones
	private static int CLONE_COUNT = 0;

	// base title
	private static final String _baseTitle = "DC Hex";
	
	// sector items
	private DCHexSectorItem _hexItems[];

	//bank matches
	private static String _defMatches[] = {"DC:"};

	// data containers
	private static DCTDCandDOCAData _dcData = DCTDCandDOCAData.getInstance();


	protected static Rectangle2D.Double _defaultWorld;

	static {
		double _xsize = 1.02 * DCGeometry.getAbsMaxWireX();
		double _ysize = 1.02 * _xsize * 1.154734;

		_defaultWorld = new Rectangle2D.Double(_xsize, -_ysize, -2 * _xsize, 2 * _ysize);

	}

	/**
	 * Create an allDCView
	 *
	 * @param keyVals variable set of arguments.
	 */
	private DCHexView(String title) {
		super(getAttributes(title));


		setBeforeDraw();
		setAfterDraw();
		getContainer().getComponent().setBackground(Color.gray);


	}

	// add the control panel
	@Override
	protected void addControls() {

		_controlPanel = new ControlPanel(this,
				ControlPanel.DISPLAYARRAY + ControlPanel.FEEDBACK + ControlPanel.ACCUMULATIONLEGEND
						+ ControlPanel.MATCHINGBANKSPANEL,
				DisplayBits.ACCUMULATION + DisplayBits.CROSSES + DisplayBits.FMTCROSSES + DisplayBits.RECPART
						+ DisplayBits.GLOBAL_HB + DisplayBits.GLOBAL_TB + DisplayBits.GLOBAL_AIHB
						+ DisplayBits.GLOBAL_AITB + DisplayBits.CVTRECTRACKS + DisplayBits.MCTRUTH
						+ DisplayBits.SECTORCHANGE + DisplayBits.CVTP1TRACKS,
				3, 5);

		add(_controlPanel, BorderLayout.EAST);

		//i.e. if none were in the properties
		if (hasNoBankMatches()) {
			setBankMatches(_defMatches);
		}
		_controlPanel.getMatchedBankPanel().update();


		pack();
	}

	/**
	 * Used to create the DCXY view
	 *
	 * @return the view
	 */
	public static DCHexView createDCHexView() {
		String title = _baseTitle + ((CLONE_COUNT == 0) ? "" : ("_(" + CLONE_COUNT + ")"));
		DCHexView view = new DCHexView(title);

		return view;
	}

	// add items to the view
	@Override
	protected void addItems() {
		ItemList detectorLayer = getContainer().getItemList(_detectorLayerName);

		_hexItems = new DCHexSectorItem[6];

		for (int sector = 0; sector < 6; sector++) {
			_hexItems[sector] = new DCHexSectorItem(detectorLayer, this, sector + 1);
			_hexItems[sector].getStyle().setFillColor(Color.lightGray);
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
				Rectangle b = container.getComponent().getBounds();
				g.setColor(X11Colors.getX11Color("steel blue"));
				g.fillRect(0, 0, b.width, b.height);
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
					drawSectorNumbers(g, container, 400);
				} // not accumulating
			}
		};

		getContainer().setAfterDraw(afterDraw);

	}

	// get the attributes to pass to the super constructor
	private static Object[] getAttributes(String title) {

		Properties props = new Properties();
		props.put(PropertySupport.TITLE, title);

		props.put(PropertySupport.PROPNAME, "DCXY");

		// set to a fraction of screen
		Dimension d = GraphicsUtilities.screenFraction(0.65);

		props.put(PropertySupport.WORLDSYSTEM, _defaultWorld);
		props.put(PropertySupport.WIDTH, (int) (0.866 * d.height));
		props.put(PropertySupport.HEIGHT, d.height);

		props.put(PropertySupport.TOOLBAR, true);
		props.put(PropertySupport.TOOLBARBITS, CedView.TOOLBARBITS);
		props.put(PropertySupport.VISIBLE, true);

		props.put(PropertySupport.BACKGROUND, Color.black);
		props.put(PropertySupport.STANDARDVIEWDECORATIONS, true);

		return PropertySupport.toObjectArray(props);
	}

	@Override
	public void getFeedbackStrings(IContainer container, Point pp, Point2D.Double wp, List<String> feedbackStrings) {

		container.worldToLocal(pp, wp);

		super.getFeedbackStrings(container, pp, wp, feedbackStrings);
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

		DCHexView view = createDCHexView();
		view.setBounds(vr);
		return view;

	}

}
