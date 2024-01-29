package cnuphys.ced.cedview.sectorview;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.util.List;

import cnuphys.bCNU.graphics.container.IContainer;
import cnuphys.bCNU.item.PolygonItem;
import cnuphys.bCNU.layer.LogicalLayer;
import cnuphys.bCNU.util.X11Colors;
import cnuphys.ced.alldata.DataWarehouse;
import cnuphys.ced.alldata.datacontainer.cc.LTCCADCData;
import cnuphys.ced.clasio.ClasIoEventManager;
import cnuphys.ced.common.SuperLayerDrawing;
import cnuphys.ced.event.AccumulationManager;
import cnuphys.ced.event.data.DataDrawSupport;
import cnuphys.ced.event.data.DataSupport;
import cnuphys.ced.event.data.arrays.adc.CC_ADCArrays;
import cnuphys.ced.event.data.arrays.tdc.TDCArrays;
import cnuphys.ced.geometry.GeometryManager;
import cnuphys.ced.geometry.LTCCGeometry;

public class SectorLTCCItem extends PolygonItem {

	private static Color _fillColors[] = { X11Colors.getX11Color("mint cream"), X11Colors.getX11Color("alice blue") };

	// convenient access to the event manager
	private ClasIoEventManager _eventManager = ClasIoEventManager.getInstance();
	
	//the data warehouse
	private DataWarehouse _dataWarehouse = DataWarehouse.getInstance();


	// sector 1-based 1..6
	private byte _sector;

	// 1-based half, 1..2 (hipo: layer)
	private byte _half;

	// 1-based ring 1..18 (hipo: component)
	private short _ring;

	// cache the outline
	private Point2D.Double[] _cachedWorldPolygon = GeometryManager.allocate(4);

	// the view this item lives on.
	private SectorView _view;

	/**
	 * Create a LTCC item for the sector view. Note, no points are added in
	 * the constructor. The points will always be supplied by the setPoints method,
	 * which will send projected wire positions (with a border of guard wires)
	 *
	 * @param logLayer   the Layer this item is on.
	 * @param view       the view this item lives on.
	 * @param sector     the 1-based sector [1..6]
	 * @param half       the 1-based layer [1..2]
	 * @param ring       the 1-based component [1..18]
	 */
	public SectorLTCCItem(LogicalLayer logLayer, SectorView view, byte sector, byte half, short ring) {
		super(logLayer);
		_view = view;
		_sector = sector;
		_ring = ring;
		_half = half;
		setPath(getWorldPolygon());
	}

	/**
	 * Custom drawer for the item.
	 *
	 * @param g         the graphics context.
	 * @param container the graphical container being rendered.
	 */
	@Override
	public void drawItem(Graphics g, IContainer container) {

		if (_eventManager.isAccumulating()) {
			return;
		}

		setPath(getWorldPolygon());

		getStyle().setFillColor(_fillColors[_ring % 2]);
		super.drawItem(g, container); // draws shell

		// hits
		drawHits(g, container);
	}

	// draw any hits
	private void drawHits(Graphics g, IContainer container) {

		if (_view.isSingleEventMode()) {
			drawSingleEventHits(g, container);
		} else {
			drawAccumulatedHits(g, container);
		}
	}

	// single event drawer
	private void drawSingleEventHits(Graphics g, IContainer container) {

		//use the adc arrays
		LTCCADCData adcData = LTCCADCData.getInstance();
		for (int i = 0; i < adcData.count(); i++) {
			if ((adcData.sector[i] == _sector) && (adcData.layer[i] == _half) && (adcData.component[i] == _ring)) {
				g.setColor(adcData.getADCColor(i));
				g.fillPolygon(_lastDrawnPolygon);
				g.setColor(Color.black);
				g.drawPolygon(_lastDrawnPolygon);
			}
		} // end has data
		
		
				
		//the LTCC.rec data straight from the source
		if (_view.showReconHits()) {
			float x[] = _dataWarehouse.getFloat("LTCC::rec", "x");
			if (x != null) {
				Point.Double wp = new Point.Double();
				Point pp = new Point();

				float y[] = _dataWarehouse.getFloat("LTCC::rec", "y");
				float z[] = _dataWarehouse.getFloat("LTCC::rec", "z");
				
				for (int i = 0; i < x.length; i++) {
					int sect = GeometryManager.getSector(x[i], y[i]);
					if (sect == _sector) {
						_view.projectClasToWorld(x[i], y[i], z[i], _view.getProjectionPlane(), wp);
						container.worldToLocal(pp, wp);

						DataDrawSupport.drawReconHit(g, pp);

					}
				}
				
			}
		}
	}

	// accumulated drawer
	private void drawAccumulatedHits(Graphics g, IContainer container) {
		int maxHit = AccumulationManager.getInstance().getMaxLTCCCount();

		int hits[][][] = AccumulationManager.getInstance().getAccumulatedLTCCData();

		int hitCount = hits[_sector - 1][_half - 1][_ring - 1];

		double fract = (maxHit == 0) ? 0 : (((double) hitCount) / maxHit);
		Color color = AccumulationManager.getInstance().getColor(_view.getColorScaleModel(), fract);

		g.setColor(color);
		g.fillPolygon(_lastDrawnPolygon);
		g.setColor(Color.black);
		g.drawPolygon(_lastDrawnPolygon);
	}

	/**
	 * Add any appropriate feedback strings panel.
	 *
	 * @param container       the Base container.
	 * @param screenPoint     the mouse location.
	 * @param worldPoint      the corresponding world point.
	 * @param feedbackStrings the List of feedback strings to add to.
	 */
	@Override
	public void getFeedbackStrings(IContainer container, Point screenPoint, Point2D.Double worldPoint,
			List<String> feedbackStrings) {
		
		if (contains(container, screenPoint)) {
			
			feedbackStrings.add(DataSupport.prelimColor + "LTCC sect " + _sector + " ring " + _ring + " half " + _half);

			
			//adc feedback
			CC_ADCArrays adcArrays = CC_ADCArrays.getArrays("LTCC::adc");
			if (adcArrays.hasData()) {
				adcArrays.addFeedback(_sector, _half, _ring, feedbackStrings);
			} // end has data
			
			//tdc feedback
			TDCArrays tdcArrays = TDCArrays.getArrays("LTCC::tdc");
			if (tdcArrays.hasData()) {
				tdcArrays.addFeedback(_sector, _half, _ring, feedbackStrings);
			} // end has data
		}

		// hit feedback
		// the LTCC.rec data straight from the source
		if (_view.showReconHits()) {
			float x[] = _dataWarehouse.getFloat("LTCC::rec", "x");
			if (x != null) {
				Point.Double wp = new Point.Double();
				Point pp = new Point();
				Rectangle r = new Rectangle();

				float y[] = _dataWarehouse.getFloat("LTCC::rec", "y");
				float z[] = _dataWarehouse.getFloat("LTCC::rec", "z");

				for (int i = 0; i < x.length; i++) {
					int sect = GeometryManager.getSector(x[i], y[i]);
					if (sect == _sector) {
						_view.projectClasToWorld(x[i], y[i], z[i], _view.getProjectionPlane(), wp);
						container.worldToLocal(pp, wp);
						r.setBounds(pp.x - 4, pp.y - 4, 8, 8);

						if (r.contains(screenPoint)) {
							String s = String.format("$Orange Red$LTCC hit loc: (%6.3f, %6.3f, %6.3f) cm", x[i], y[i],
									z[i]);
							if (!feedbackStrings.contains(s)) {
							feedbackStrings.add(s);
							}
							break;
						}

					}

				}
			} // end has data

		} // end show recon hits		
	}

	// get the world polygon corresponding to the boundary of the superlayer
	private Point2D.Double[] getWorldPolygon() {

		if (_dirty) {
			LTCCGeometry.getSimpleWorldPoly(_ring, _half, _view.getSliderPhi(), _cachedWorldPolygon);
			if (isLowerSector()) {
				SuperLayerDrawing.flipPolyToLowerSector(_cachedWorldPolygon);
			}
		} // end dirty
		return _cachedWorldPolygon;
	}

	/**
	 * Test whether this is a lower sector
	 *
	 * @return <code>true</code> if this is a lower sector
	 */
	public boolean isLowerSector() {
		return (_sector > 3);
	}

	/**
	 * Get the 1-based sector
	 *
	 * @return the 1-based sector
	 */
	public int sector() {
		return _sector;
	}

	/**
	 * Get the 1-based ring
	 *
	 * @return the 1-based ring
	 */
	public int ring() {
		return _ring;
	}

	/**
	 * Get the 1-based half
	 *
	 * @return the 1-based half
	 */
	public int half() {
		return _half;
	}

}