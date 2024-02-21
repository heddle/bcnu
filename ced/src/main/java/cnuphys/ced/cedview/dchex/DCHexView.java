package cnuphys.ced.cedview.dchex;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.List;
import java.util.Properties;

import javax.swing.JLabel;
import javax.swing.JTabbedPane;

import cnuphys.bCNU.component.IRollOverListener;
import cnuphys.bCNU.component.RollOverPanel;
import cnuphys.bCNU.drawable.DrawableAdapter;
import cnuphys.bCNU.drawable.IDrawable;
import cnuphys.bCNU.graphics.GraphicsUtilities;
import cnuphys.bCNU.graphics.container.IContainer;
import cnuphys.bCNU.item.ItemList;
import cnuphys.bCNU.util.Fonts;
import cnuphys.bCNU.util.PropertySupport;
import cnuphys.bCNU.util.X11Colors;
import cnuphys.bCNU.view.BaseView;
import cnuphys.ced.alldata.datacontainer.dc.DCTDCandDOCAData;
import cnuphys.ced.cedview.CedView;
import cnuphys.ced.cedview.HexView;
import cnuphys.ced.component.ControlPanel;
import cnuphys.ced.component.DisplayBits;
import cnuphys.ced.geometry.DCGeometry;

public class DCHexView extends HexView implements IRollOverListener {
	
	//rollover colors
	private static final Color inactiveFG = Color.cyan;
	private static final Color inactiveBG = Color.black;
	private static final Color activeFG = Color.yellow;
	private static final Color activeBG = Color.darkGray;

	//roll over labels
	private static final String HB_ROLLOVER = "Reg Hit Based DC Clusters";
	private static final String TB_ROLLOVER = "Reg Time Based DC Clusters";
	private static final String AIHB_ROLLOVER = "AI Hit Based DC Clusters";
	private static final String AITB_ROLLOVER = "AI Time Based DC Clusters";

	//rollover labels
	private static String roLabels[] = {HB_ROLLOVER,
			TB_ROLLOVER,
			AIHB_ROLLOVER,
			AITB_ROLLOVER};

	//rollover boolean flags
	private boolean _roShowHBDCClusters;
	private boolean _roShowTBDCClusters;
	private boolean _roShowAIHBDCClusters;
	private boolean _roShowAITBDCClusters;


	// for naming clones
	private static int CLONE_COUNT = 0;
	
	private static final Color _fillColor = X11Colors.getX11Color("steel blue");

	// base title
	private static final String _baseTitle = "DC Hex";
	
	// sector items
	private DCHexSectorItem _hexItems[];

	//bank matches
	private static String _defMatches[] = {"DC:", "HitBased", "TimeBased"};

	// data containers
	private static DCTDCandDOCAData _dcData = DCTDCandDOCAData.getInstance();
	
	//superlayer items
	private DCHexSuperLayer[][] _superLayerItems;

	//rollover panel for drawing clusters
	private RollOverPanel _rollOverPanel;

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
				ControlPanel.DISPLAYARRAY
				+ ControlPanel.FEEDBACK + ControlPanel.ACCUMULATIONLEGEND + ControlPanel.MATCHINGBANKSPANEL
				+ ControlPanel.ALLDCDISPLAYPANEL,
				DisplayBits.ACCUMULATION, 3, 5);

		add(_controlPanel, BorderLayout.EAST);
		
		customize(this);


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
			_hexItems[sector].getStyle().setFillColor(_fillColor);
			_hexItems[sector].getStyle().setLineColor(null);
		}
		
		//superlayer items
		_superLayerItems = new DCHexSuperLayer[6][6];
		for (int sector = 1; sector <= 6; sector++) {
			for (int superlayer = 1; superlayer <= 6; superlayer++) {
				_superLayerItems[sector - 1][superlayer - 1] = new DCHexSuperLayer(detectorLayer, this, sector,
						superlayer);
			}
		}

	}

	//add the rollover panel
	private void customize(CedView view) {
		JTabbedPane tabbedPane =  _controlPanel.getTabbedPane();
		_rollOverPanel = new RollOverPanel("DC Clusters", 1, Fonts.mediumFont, inactiveFG, inactiveBG,
				roLabels);

		_rollOverPanel.addRollOverListener(this);
		tabbedPane.add(_rollOverPanel, "DC Clusters");

//		view._clusterDrawer = new ClusterDrawer(view);
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
				g.setColor(_fillColor);
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

					drawSectorNumbers(g, container, Color.cyan, 85);
					
					Point p0 = new Point();
					Point p1 = new Point();
					
					for (int sect = 0; sect < 3; sect++) {
						Point2D.Double poly0[] = _superLayerItems[sect][0].getPolygon();
						Point2D.Double poly3[] = _superLayerItems[sect+3][0].getPolygon();
						
						container.worldToLocal(p0, poly0[0]);
						container.worldToLocal(p1, poly3[0]);
						g.setColor(Color.cyan);
						g.drawLine(p0.x, p0.y, p1.x, p1.y);
					}
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
		Dimension d = GraphicsUtilities.screenFraction(0.75);

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

		// get the sector
		int sector = 0;
		int superlayer = 0;
		for (int i = 0; i < 6; i++) {
			if (_hexItems[i].contains(container, pp)) {
				sector = i + 1;
				break;
			}
		}
		if (sector > 0) {
			for (int j = 0; j < 6; j++) {
				if (_superLayerItems[sector - 1][j].contains(container, pp)) {
					superlayer = j + 1;
					break;
				}
			}
		}	
		
		if (superlayer > 0) {
			feedbackStrings.add("$aqua$sector " + sector + " superlayer " + superlayer);
			int layer = _superLayerItems[sector - 1][superlayer - 1].whichLayer(container, pp);
			if (layer > 0) {
				int wire = _superLayerItems[sector - 1][superlayer - 1].whichWire(container, layer, pp);
				feedbackStrings.add("$aqua$layer " + layer + " wire " + wire);
			}
			
			
			double totalOcc = 100. * _dcData.totalOccupancy();
			double sectorOcc = 100. * _dcData.totalSectorOccupancy(sector);
			double superlayerOcc = 100. * _dcData.totalSuperlayerOccupancy(sector, superlayer);
			
			String occStr = String.format("occ  total %-6.2f%%  sect %-6.2f%%  suplay %-6.2f%%", totalOcc,
					sectorOcc, superlayerOcc);
			feedbackStrings.add("$aqua$" + occStr);
			
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

		DCHexView view = createDCHexView();
		view.setBounds(vr);
		return view;
	}
	

	/**
	 * Display raw DC hits?
	 *
	 * @return <code> if we should display raw hits
	 */
	public boolean showRawHits() {
		return _controlPanel.getAllDCDisplayPanel().showRawHits();
	}

	/**
	 * Display hit based hits?
	 *
	 * @return <code> if we should display hit based hits
	 */
	public boolean showHBHits() {
		return _controlPanel.getAllDCDisplayPanel().showHBHits();
	}

	/**
	 * Display time based hits?
	 *
	 * @return <code> if we should display time based hits
	 */
	public boolean showTBHits() {
		return _controlPanel.getAllDCDisplayPanel().showTBHits();
	}

	/**
	 * Display AI hit based hits?
	 *
	 * @return <code> if we should display AI hit based hits
	 */
	public boolean showAIHBHits() {
		return _controlPanel.getAllDCDisplayPanel().showAIHBHits();
	}

	/**
	 * Display AI time based hits?
	 *
	 * @return <code> if we should display AI time based hits
	 */
	public boolean showAITBHits() {
		return _controlPanel.getAllDCDisplayPanel().showAITBHits();
	}



	@Override
	public void RollOverMouseEnter(JLabel label, MouseEvent e) {

		String text = label.getText();
		if (text.contains(HB_ROLLOVER)) {
			_roShowHBDCClusters = true;
		}
		else if (text.contains(TB_ROLLOVER)) {
			_roShowTBDCClusters = true;
		}
		else if (text.contains(AIHB_ROLLOVER)) {
			_roShowAIHBDCClusters = true;
		}
		else if (text.contains(AITB_ROLLOVER)) {
			_roShowAITBDCClusters = true;
		}

		label.setForeground(activeFG);
		label.setBackground(activeBG);

		refresh();
	}

	@Override
	public void RollOverMouseExit(JLabel label, MouseEvent e) {

		if (e.isAltDown() || e.isControlDown() || e.isMetaDown()) {
			return;
		}

		String text = label.getText();
		if (text.contains(HB_ROLLOVER)) {
			_roShowHBDCClusters = false;
		}
		else if (text.contains(TB_ROLLOVER)) {
			_roShowTBDCClusters = false;
		}
		else if (text.contains(AIHB_ROLLOVER)) {
			_roShowAIHBDCClusters = false;
		}
		else if (text.contains(AITB_ROLLOVER)) {
			_roShowAITBDCClusters = false;
		}

		label.setForeground(inactiveFG);
		label.setBackground(inactiveBG);

		refresh();
	}

}
