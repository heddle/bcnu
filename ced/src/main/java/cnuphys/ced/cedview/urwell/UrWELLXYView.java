package cnuphys.ced.cedview.urwell;

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

import org.jlab.geom.prim.Line3D;
import org.jlab.geom.prim.Point3D;
import org.jlab.io.base.DataEvent;

import cnuphys.bCNU.drawable.DrawableAdapter;
import cnuphys.bCNU.drawable.IDrawable;
import cnuphys.bCNU.graphics.GraphicsUtilities;
import cnuphys.bCNU.graphics.container.IContainer;
import cnuphys.bCNU.item.ItemList;
import cnuphys.bCNU.util.PropertySupport;
import cnuphys.bCNU.util.X11Colors;
import cnuphys.bCNU.view.BaseView;
import cnuphys.ced.alldata.DataDrawSupport;
import cnuphys.ced.alldata.DataWarehouse;
import cnuphys.ced.cedview.CedView;
import cnuphys.ced.cedview.HexView;
import cnuphys.ced.clasio.ClasIoEventManager;
import cnuphys.ced.component.ControlPanel;
import cnuphys.ced.component.DisplayBits;
import cnuphys.ced.geometry.GeometryManager;
import cnuphys.ced.geometry.urwell.UrWELLGeometry;
import cnuphys.ced.item.HexSectorItem;


public class UrWELLXYView extends HexView {

	private static DataWarehouse _dataWarehouse = DataWarehouse.getInstance();

	private static int CLONE_COUNT = 0;

	//layer colors
	private static Color _layerColors[] = {Color.red, Color.green};


	// sector items
	private UrWELLHexSectorItem _hexItems[];

	// chamber outline items
	private UrWELLChamberItem _chamberItems[][];

	private SwimTrajectoryDrawer _swimTrajectoryDrawer;

	protected static Rectangle2D.Double _defaultWorld;

	// the z location of the projection plane
	private double _zplane = 200;

	//work space
	private Point _pp1 = new Point();
	private Point _pp2 = new Point();
	private Point2D.Double _wp1 = new Point2D.Double();
	private Point2D.Double _wp2 = new Point2D.Double();
	private Point3D _p3d1 = new Point3D();
	private Point3D _p3d2 = new Point3D();
	private Rectangle _fbRect = new Rectangle();

	//for highlighting
	private HighlightData _highlightData = new HighlightData();

	//bank matches
	private static String _defMatches[] = {"URWELL"};


	static {
		double _xsize = 160;
		double _ysize = _xsize * 1.154734;

		_defaultWorld = new Rectangle2D.Double(_xsize, -_ysize, -2 * _xsize, 2 * _ysize);

	}

	private UrWELLXYView(String title) {
		super(getAttributes(title));

		// projection plane
		projectionPlane = GeometryManager.xyPlane(_zplane);

		//draw trajectories
		_swimTrajectoryDrawer = new SwimTrajectoryDrawer(this);

		setBeforeDraw();
		setAfterDraw();
		getContainer().getComponent().setBackground(Color.gray);

		//i.e. if none were in the properties
		if (hasNoBankMatches()) {
			setBankMatches(_defMatches);
		}
		_controlPanel.getMatchedBankPanel().update();

	}

	// add the control panel
	@Override
	protected void addControls() {

		_controlPanel = new ControlPanel(this,
				ControlPanel.DISPLAYARRAY + ControlPanel.FEEDBACK + ControlPanel.ACCUMULATIONLEGEND
						+ ControlPanel.MATCHINGBANKSPANEL,
				DisplayBits.ACCUMULATION + DisplayBits.CLUSTERS +  DisplayBits.CROSSES + DisplayBits.RECPART
						+ DisplayBits.GLOBAL_HB + DisplayBits.GLOBAL_TB + DisplayBits.GLOBAL_AIHB
						+ DisplayBits.GLOBAL_AITB +
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
		ItemList detectorLayer = getContainer().getItemList(_detectorLayerName);



		_hexItems = new UrWELLHexSectorItem[6];

		for (int sector = 0; sector < 6; sector++) {
			_hexItems[sector] = new UrWELLHexSectorItem(detectorLayer, this, sector + 1);
			_hexItems[sector].getStyle().setFillColor(Color.lightGray);
		}

		//chamber outline items
		_chamberItems = new UrWELLChamberItem[6][3];
		for (int sector = 0; sector < 6; sector++) {
			for (int chamber = 0; chamber < 3; chamber++) {
				_chamberItems[sector][chamber] = UrWELLChamberItem.createUrWELLChamberItem(detectorLayer, sector+1, chamber+1);
			}
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

					// draw trajectories
					_swimTrajectoryDrawer.draw(g, container);


					//draw hits
					drawHits(g, container);

					//draw clusters
					drawClusters(g, container);

					//draw crosses
					drawCrosses(g, container);

					//data selected highlight?
					drawDataSelectedHighlight(g, container);

					drawCoordinateSystem(g, container, null);
					drawSectorNumbers(g, container, null, 145);
				} // not acumulating
			}

		};


		getContainer().setAfterDraw(afterDraw);
	}



	//draw the crosses
	private void drawCrosses(Graphics g, IContainer container) {
		if (!showCrosses()) {
			return;
		}

		if (isSingleEventMode()) {
			DataEvent event = ClasIoEventManager.getInstance().getCurrentEvent();
			if (event == null) {
				return;
			}

			byte sector[] = DataWarehouse.getInstance().getByte("URWELL::crosses", "sector");

			int count = (sector == null) ? 0 : sector.length;
			if (count == 0) {
				return;
			}

			float x[] = _dataWarehouse.getFloat("URWELL::crosses", "x");
			float y[] = _dataWarehouse.getFloat("URWELL::crosses", "y");
			float z[] = _dataWarehouse.getFloat("URWELL::crosses", "z");


			//public static void drawSphere(Graphics g, String color, int xc, int yc, int width, int height) {

			for (int i = 0; i < count; i++) {
				projectPoint(container, x[i], y[i], z[i]);
				DataDrawSupport.drawCross(g, _pp1.x, _pp1.y, 4);
			}

		}

	}


	//draw the clusters
	private void drawClusters(Graphics g, IContainer container) {
		if (!showClusters()) {
			return;
		}

		if (isSingleEventMode()) {
			DataEvent event = ClasIoEventManager.getInstance().getCurrentEvent();
			if (event == null) {
				return;
			}

			byte sector[] = _dataWarehouse.getByte("URWELL::clusters", "sector");

			int count = (sector == null) ? 0 : sector.length;
			if (count == 0) {
				return;
			}

			float xo[] = _dataWarehouse.getFloat("URWELL::clusters", "xo");
			float yo[] = _dataWarehouse.getFloat("URWELL::clusters", "yo");
			float zo[] = _dataWarehouse.getFloat("URWELL::clusters", "zo");
			float xe[] = _dataWarehouse.getFloat("URWELL::clusters", "xe");
			float ye[] = _dataWarehouse.getFloat("URWELL::clusters", "ye");
			float ze[] = _dataWarehouse.getFloat("URWELL::clusters", "ze");

			for (int i = 0; i < count; i++) {
				projectLine(container, xo[i], yo[i], zo[i], xe[i], ye[i], ze[i]);
				GraphicsUtilities.drawHighlightedLine(g, _pp1.x, _pp1.y, _pp2.x, _pp2.y, Color.black, Color.yellow);
			}

		}
	}


	//draw data selected highlighted data
	private void drawDataSelectedHighlight(Graphics g, IContainer container) {

		DataEvent dataEvent = ClasIoEventManager.getInstance().getCurrentEvent();
		if (dataEvent == null) {
			return;
		}

		//indices are zero based

		if (dataEvent.hasBank("URWELL::clusters") && (_highlightData.cluster >= 0) && showClusters()) {
			int idx = _highlightData.cluster; //0 based

			float xo = _dataWarehouse.getFloat("URWELL::clusters", "xo")[idx];
			float yo = _dataWarehouse.getFloat("URWELL::clusters", "yo")[idx];
			float zo = _dataWarehouse.getFloat("URWELL::clusters", "zo")[idx];
			float xe = _dataWarehouse.getFloat("URWELL::clusters", "xe")[idx];
			float ye = _dataWarehouse.getFloat("URWELL::clusters", "ye")[idx];
			float ze = _dataWarehouse.getFloat("URWELL::clusters", "ze")[idx];

			projectLine(container, xo, yo, zo, xe, ye, ze);
			GraphicsUtilities.drawThickHighlightedLine(g, _pp1.x, _pp1.y, _pp2.x, _pp2.y, Color.orange, Color.white);

		}

		if (dataEvent.hasBank("URWELL::crosses") && (_highlightData.cross >= 0) && showCrosses()) {
			int idx = _highlightData.cross; //0 based
			float x = _dataWarehouse.getFloat("URWELL::crosses", "x")[idx];
			float y = _dataWarehouse.getFloat("URWELL::crosses", "y")[idx];
			float z = _dataWarehouse.getFloat("URWELL::crosses", "z")[idx];
			projectPoint(container, x, y, z);
			DataDrawSupport.drawBiggerCross(g, _pp1.x, _pp1.y, 5);
		}


		if (dataEvent.hasBank("URWELL::hits") && (_highlightData.hit >= 0)) {

			int idx = _highlightData.hit; //0 based

			byte sector = _dataWarehouse.getByte("URWELL::hits", "sector")[idx];
			byte layer = _dataWarehouse.getByte("URWELL::hits", "layer")[idx];
			short strip = _dataWarehouse.getShort("URWELL::hits", "strip")[idx];

			int data[] = new int[2];

			UrWELLGeometry.chamberStrip(strip, data);
			projectStrip(container, sector, data[0], layer, data[1]);

			GraphicsUtilities.drawOval(g, _pp1.x, _pp1.y, 6, 6, Color.black, Color.cyan);
			GraphicsUtilities.drawOval(g, _pp2.x, _pp2.y, 6, 6, Color.black, Color.cyan);

		}



	}



	//draw the hits
	private void drawHits(Graphics g, IContainer container) {

		if (isSingleEventMode()) {
			DataEvent event = ClasIoEventManager.getInstance().getCurrentEvent();
			if (event == null) {
				return;
			}

			byte sector[] = _dataWarehouse.getByte("URWELL::hits", "sector");

			int count = (sector == null) ? 0 : sector.length;
			if (count == 0) {
				return;
			}

			byte layer[] = _dataWarehouse.getByte("URWELL::hits", "layer");
			short strip[] = _dataWarehouse.getShort("URWELL::hits","strip");

			int data[] = new int[2];

			for (int i = 0; i < count; i++) {
				g.setColor(_layerColors[layer[i]-1]);
				UrWELLGeometry.chamberStrip(strip[i], data);
				projectStrip(container, sector[i], data[0], layer[i], data[1]);
				g.drawLine(_pp1.x, _pp1.y, _pp2.x, _pp2.y);
			}


		} else {
			drawAccumulatedHits(g, container);
		}
	}

	/**
	 *
	 * @param container the container
	 * @param sector 1-based sector [1..6]
	 * @param chamber 1-based sector [1..3]
	 * @param layer 1-based layer [1..2]
	 * @param chamberStrip 1-based strip
	 */
	private void projectStrip(IContainer container, int sector, int chamber, int layer, int chamberStrip) {


		Line3D line = UrWELLGeometry.getStrip(sector, chamber, layer, chamberStrip);
		projectClasToWorld(line.origin(), projectionPlane, _wp1);
		projectClasToWorld(line.end(), projectionPlane, _wp2);

		container.worldToLocal(_pp1, _wp1);
		container.worldToLocal(_pp2, _wp2);

	}

	/**
	 *
	 * @param container the container
	 * @param x1
	 * @param y1
	 * @param z1
	 * @param x2
	 * @param y2
	 * @param z2
	 */
	private void projectLine(IContainer container, float x1, float y1, float z1, float x2, float y2, float z2) {

		_p3d1.set(x1, y1, z1);
		_p3d2.set(x2, y2, z2);

		projectClasToWorld(_p3d1, projectionPlane, _wp1);
		projectClasToWorld(_p3d2, projectionPlane, _wp2);

		container.worldToLocal(_pp1, _wp1);
		container.worldToLocal(_pp2, _wp2);

	}

	/**
	 *
	 * @param container the container
	 * @param x
	 * @param y
	 * @param z
	 */
	private void projectPoint(IContainer container, float x, float y, float z) {
		_p3d1.set(x, y, z);
		projectClasToWorld(_p3d1, projectionPlane, _wp1);
		container.worldToLocal(_pp1, _wp1);
	}



	//draw accumulated hits
	private void drawAccumulatedHits(Graphics g, IContainer container) {
	}

	// get the attributes to pass to the super constructor
	private static Object[] getAttributes(String title) {

		Properties props = new Properties();
		props.put(PropertySupport.TITLE, title);
		props.put(PropertySupport.PROPNAME, "URWELLXY");

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

		//crosses feedback
		crossesFeedback(container, pp, wp, feedbackStrings);
	}

	//on a cross?
	private void crossesFeedback(IContainer container, Point pp, Point2D.Double wp, List<String> feedbackStrings) {

		if (isSingleEventMode()) {
			DataEvent event = ClasIoEventManager.getInstance().getCurrentEvent();
			if (event == null) {
				return;
			}

			byte sector[] = _dataWarehouse.getByte("URWELL::crosses", "sector");

			int count = (sector == null) ? 0 : sector.length;
			if (count == 0) {
				return;
			}

			float x[] = _dataWarehouse.getFloat("URWELL::crosses", "x");
			float y[] = _dataWarehouse.getFloat("URWELL::crosses", "y");
			float z[] = _dataWarehouse.getFloat("URWELL::crosses", "z");

			for (int i = 0; i < count; i++) {
				projectPoint(container, x[i], y[i], z[i]);
				_fbRect.setBounds(_pp1.x-5, _pp1.y-5, 10, 10);

				if (_fbRect.contains(pp)) {
					short id = _dataWarehouse.getShort("URWELL::crosses", "id")[i];
					short cluster1 = _dataWarehouse.getShort("URWELL::crosses", "cluster1")[i];
					short cluster2 = _dataWarehouse.getShort("URWELL::crosses", "cluster2")[i];
					short status = _dataWarehouse.getShort("URWELL::crosses", "status")[i];

					String fbs1 = String.format("$cyan$cross: %d  status: %d", id, status);
					String fbs2 = String.format("$cyan$cross clusters: %d and %d", cluster1, cluster2);
					feedbackStrings.add(fbs1);
					feedbackStrings.add(fbs2);
					return;
				}
			}

		}

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
			System.err.println("Bad sector in DCXYView getHexSectorItem, sector = " + sector);
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


	/**
	 * In the BankDataTable a row was selected.
	 * @param bankName the name of the bank
	 * @param index the 0-based index into the bank
	 */
	@Override
	public void dataSelected(String bankName, int index) {

		if ("URWELL::hits".equals(bankName)) {
			_highlightData.hit = index;
		}
		else if ("URWELL::clusters".equals(bankName)) {
			_highlightData.cluster = index;
		}
		else if ("URWELL::crosses".equals(bankName)) {
			_highlightData.cross = index;
		}


		refresh();

	}

	/**
	 * Opened a new event file
	 *
	 * @param path the path to the new file
	 */
	@Override
	public void openedNewEventFile(final String path) {
		super.openedNewEventFile(path);
		_highlightData.reset();
	}



}
