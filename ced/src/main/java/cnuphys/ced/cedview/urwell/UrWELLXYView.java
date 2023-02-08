package cnuphys.ced.cedview.urwell;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Stroke;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.List;
import java.util.Properties;

import org.jlab.geom.prim.Line3D;
import org.jlab.io.base.DataEvent;

import cnuphys.bCNU.drawable.DrawableAdapter;
import cnuphys.bCNU.drawable.IDrawable;
import cnuphys.bCNU.graphics.GraphicsUtilities;
import cnuphys.bCNU.graphics.container.IContainer;
import cnuphys.bCNU.graphics.style.LineStyle;
import cnuphys.bCNU.layer.LogicalLayer;
import cnuphys.bCNU.log.Log;
import cnuphys.bCNU.util.Fonts;
import cnuphys.bCNU.util.PropertySupport;
import cnuphys.bCNU.util.X11Colors;
import cnuphys.bCNU.view.BaseView;
import cnuphys.ced.alldata.ColumnData;
import cnuphys.ced.cedview.CedView;
import cnuphys.ced.cedview.HexView;
import cnuphys.ced.clasio.ClasIoEventManager;
import cnuphys.ced.clasio.datatable.SelectedDataManager;
import cnuphys.ced.component.ControlPanel;
import cnuphys.ced.component.DisplayBits;
import cnuphys.ced.geometry.GeometryManager;
import cnuphys.ced.geometry.urwell.UrWELLGeometry;
import cnuphys.ced.item.HexSectorItem;


public class UrWELLXYView extends HexView {

	// for naming clones
	private static int CLONE_COUNT = 0;
	
	//layer colors
	private static Color _layerColors[] = {Color.red, Color.green};

	// sector items
	private UrWELLHexSectorItem _hexItems[];
	
	// chamber outline items
	private UrWELLChamberItem _chamberItems[][];
	
	private SwimTrajectoryDrawer _swimTrajectoryDrawer;

	private static Stroke stroke = GraphicsUtilities.getStroke(0.5f, LineStyle.SOLID);

	protected static Rectangle2D.Double _defaultWorld;

	// the z location of the projection plane
	private double _zplane = 10;
	
	//work soace
	private Point _pp1 = new Point();
	private Point _pp2 = new Point();
	private Point2D.Double _wp1 = new Point2D.Double();
	private Point2D.Double _wp2 = new Point2D.Double();


	static {
		double _xsize = 160;
		double _ysize = 160 * 1.154734;

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


	}

	// add the control panel
	@Override
	protected void addControls() {

		_controlPanel = new ControlPanel(this,
				ControlPanel.DISPLAYARRAY + ControlPanel.FEEDBACK + ControlPanel.ACCUMULATIONLEGEND
						+ ControlPanel.DRAWLEGEND,
				DisplayBits.ACCUMULATION + DisplayBits.CROSSES + DisplayBits.FMTCROSSES + DisplayBits.RECPART
						+ DisplayBits.GLOBAL_HB + DisplayBits.GLOBAL_TB + DisplayBits.GLOBAL_AIHB
						+ DisplayBits.GLOBAL_AITB + DisplayBits.CVTRECTRACKS + DisplayBits.CVTP1TRACKS
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
		LogicalLayer detectorLayer = getContainer().getLogicalLayer(_detectorLayerName);
		
		

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
	 * Draw the strips
	 * @param g the graphics context
	 * @param container the container
	 * @param sector sector [1..6]
	 * @param chamber chamber [1..3]
	 * @param layer [1..2]
	 * @param color strip color
	 */
	public void drawStrips(Graphics g, IContainer container, int sector, int chamber, int layer, Color color) {
		Point pp1 = new Point();
		Point pp2 = new Point();
		Point2D.Double wp1 = new Point2D.Double();
		Point2D.Double wp2 = new Point2D.Double();

		g.setColor(color);

		for (int chamberStrip = 1; chamberStrip < UrWELLGeometry.numStripsByChamber[chamber-1]; chamberStrip++) {
			projectStrip(g, container, sector, chamber, layer, chamberStrip, wp1, wp2, pp1, pp2);
			g.drawLine(pp1.x, pp1.y, pp2.x, pp2.y);
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
					
					//data selected highlight?
					if (SelectedDataManager.isHighlightOn()) {
						drawDataSelectedHighlight(g, container);
					}



					drawCoordinateSystem(g, container);
					drawSectorNumbers(g, container, 145);
				} // not acumulating
			}

		};
		

		getContainer().setAfterDraw(afterDraw);
	}
	
	//draw data selected hightlight data
	private void drawDataSelectedHighlight(Graphics g, IContainer container) {
		System.err.println("draw highlighted data");
	}



	//draw the hits
	private void drawHits(Graphics g, IContainer container) {

		if (isSingleEventMode()) {
			DataEvent event = ClasIoEventManager.getInstance().getCurrentEvent();
			if (event == null) {
				return;
			}
			
			byte sector[] = ColumnData.getByteArray("URWELL::hits.sector");
			
			int count = (sector == null) ? 0 : sector.length;
			if (count == 0) {
				return;
			}
			
			byte layer[] = ColumnData.getByteArray("URWELL::hits.layer");
			short strip[] = ColumnData.getShortArray("URWELL::hits.strip");
			
			int data[] = new int[2];
			
			for (int i = 0; i < count; i++) {
				g.setColor(_layerColors[layer[i]-1]);
				UrWELLGeometry.chamberStrip(strip[i], data);
				projectStrip(g, container, sector[i], data[0], layer[i], data[1], _wp1, _wp2, _pp1, _pp2);
				g.drawLine(_pp1.x, _pp1.y, _pp2.x, _pp2.y);
			}
	

		} else {
			drawAccumulatedHits(g, container);
		}
	}

	/**
	 * 
	 * @param g the graphics context
	 * @param container the container
	 * @param sector 1-based sector [1..6]
	 * @param chamber 1-based sector [1..3]
	 * @param layer 1-based layer [1..2]
	 * @param chamberStrip 1-based strip
	 * @param wp1
	 * @param wp2
	 * @param p1
	 * @param p2
	 */
	private void projectStrip(Graphics g, IContainer container, int sector, int chamber, int layer, int chamberStrip, 
			Point2D.Double wp1, Point2D.Double wp2, Point p1, Point p2) {

		
		Line3D line = UrWELLGeometry.getStrip(sector, chamber, layer, chamberStrip);
		projectClasToWorld(line.origin(), projectionPlane, wp1);
		projectClasToWorld(line.end(), projectionPlane, wp2);

		container.worldToLocal(p1, wp1);
		container.worldToLocal(p2, wp2);

	}

	//draw accumulated hits
	private void drawAccumulatedHits(Graphics g, IContainer container) {
	}

	// get the attributes to pass to the super constructor
	private static Object[] getAttributes(String title) {

		Properties props = new Properties();
		props.put(PropertySupport.TITLE, title);

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
			Log.getInstance().warning("Bad sector in DCXYView getHexSectorItem, sector = " + sector);
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
	 * @param index the 1-based index into the bank
	 */
	@Override
	public void dataSelected(String bankName, int index) {
		
		index--;  //convert to 0 based
		if (index < 0) {
			return;
		}
		
		if ("URWELL::hits".equals(bankName)) {
			int data[] = new int[2];

			byte sectors[] = ColumnData.getByteArray("URWELL::hits.sector");
			
			int count = (sectors == null) ? 0 : sectors.length;
			if ((count == 0) || (index >= count)) {
				return;
			}
			
			byte layers[] = ColumnData.getByteArray("URWELL::hits.layer");
			short strips[] = ColumnData.getShortArray("URWELL::hits.strip");
			
	
			int sector = sectors[index];
			int layer = layers[index];
			int strip = strips[index];
			UrWELLGeometry.chamberStrip(strip, data);

			int chamber = data[0];
			int chamberStrip = data[1];
			
			System.err.println(String.format("sect: %d   layer: %d   strip: %d   chamber: %d   chamberStrip: %d", sector, layer, strip, chamber, chamberStrip));
			
			refresh();
		}
	}
	
	/**
	 * Highlight mode is now off
	 */
	@Override
	public void highlightModeOff() {
		System.err.println("Highlight Mode Off");
	}


}
