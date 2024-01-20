package cnuphys.ced.cedview.sectorview;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.util.List;

import cnuphys.bCNU.graphics.container.IContainer;
import cnuphys.bCNU.item.PolygonItem;
import cnuphys.bCNU.layer.LogicalLayer;
import cnuphys.ced.clasio.ClasIoEventManager;
import cnuphys.ced.common.SuperLayerDrawing;
import cnuphys.ced.event.AccumulationManager;
import cnuphys.ced.event.data.AdcHit;
import cnuphys.ced.event.data.AdcList;
import cnuphys.ced.event.data.DataSupport;
import cnuphys.ced.event.data.arrays.adc.ADCArrays;
import cnuphys.ced.event.data.arrays.adc.HTCC_ADCArrays;
import cnuphys.ced.event.data.arrays.adc.LR_ADCArrays;
import cnuphys.ced.geometry.GeometryManager;
import cnuphys.ced.geometry.HTCCGeometry;

public class SectorHTCCItem extends PolygonItem {

	// convenient access to the event manager
	private ClasIoEventManager _eventManager = ClasIoEventManager.getInstance();

	// sector 1-based 1..6
	private byte _sector;

	// 1-based half, 1..2 (hipo: layer)
	private byte _half;

	// 1-based ring 1..4 (hipo: component)
	private short _ring;

	// cache the outline
	private Point2D.Double[] _cachedWorldPolygon = GeometryManager.allocate(4);

	// the view this item lives on.
	private SectorView _view;

	/**
	 * Create a super layer item for the sector view. Note, no points are added in
	 * the constructor. The points will always be supplied by the setPoints method,
	 * which will send projected wire positions (with a border of guard wires)
	 *
	 * @param logLayer   the Layer this item is on.
	 * @param view       the view this item lives on.
	 * @param sector     the 1-based sector [1..6]
	 * @param half       the 1-based layer [1..2]
	 * @param ring       the 1-based component [1..4]
	 */
	public SectorHTCCItem(LogicalLayer logLayer, SectorView view, byte sector, byte half, short ring) {
		super(logLayer);
		_view = view;
		_sector = sector;
		_half = half;
		_ring = ring;
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

		getStyle().setFillColor(Color.white);
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

	// single event drawer using adc bank
	private void drawSingleEventHits(Graphics g, IContainer container) {
		
		//use the adc arrays
		HTCC_ADCArrays arrays = HTCC_ADCArrays.getArrays("HTCC::adc");
		if (arrays.hasData()) {
			for (int i = 0; i < arrays.sector.length; i++) {
				if ((arrays.sector[i] == _sector) && (arrays.layer[i] == _half) && (arrays.component[i] == _ring)) {
					g.setColor(arrays.getColor(_sector, _half, _ring));
					g.fillPolygon(_lastDrawnPolygon);
					g.setColor(Color.black);
					g.drawPolygon(_lastDrawnPolygon);
				}
			}
		} // end has data

	}


	// accumulated drawer
	private void drawAccumulatedHits(Graphics g, IContainer container) {

		int maxHit = AccumulationManager.getInstance().getMaxHTCCCount();

		int hits[][][] = AccumulationManager.getInstance().getAccumulatedHTCCData();

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
//
//			AdcList hits = HTCC2.getInstance().getHits();
//			AdcHit hit = null;
//
//			if ((hits != null) && !hits.isEmpty()) {
//				hit = hits.get(_sector, _half, _ring);
//			}
//
//			if (hit == null) {
//				feedbackStrings.add(DataSupport.prelimColor + "HTCC sect " + _sector + " half (layer) " + _half
//						+ " ring (component) " + _ring);
//			} else {
//				hit.adcFeedback("half " + _half, "ring", feedbackStrings);
//			}
//
		}

	}

	// get the world polygon corresponding to the boundary of the superlayer
	private Point2D.Double[] getWorldPolygon() {

		if (_dirty) {
			HTCCGeometry.getSimpleWorldPoly(_ring, _half, _view.getSliderPhi(), _cachedWorldPolygon);
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
