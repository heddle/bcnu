package cnuphys.ced.cedview.sectorview;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.util.List;

import cnuphys.bCNU.graphics.container.IContainer;
import cnuphys.bCNU.item.ItemList;
import cnuphys.bCNU.item.PolygonItem;
import cnuphys.bCNU.util.X11Colors;
import cnuphys.ced.alldata.DataDrawSupport;
import cnuphys.ced.alldata.datacontainer.cc.LTCCADCData;
import cnuphys.ced.alldata.datacontainer.cc.LTCCRecData;
import cnuphys.ced.alldata.datacontainer.cc.LTCCTDCData;
import cnuphys.ced.clasio.ClasIoEventManager;
import cnuphys.ced.common.SuperLayerDrawing;
import cnuphys.ced.event.AccumulationManager;
import cnuphys.ced.geometry.GeometryManager;
import cnuphys.ced.geometry.LTCCGeometry;

public class SectorLTCCItem extends PolygonItem {

	private static Color _fillColors[] = { X11Colors.getX11Color("mint cream"), X11Colors.getX11Color("alice blue") };

	// convenient access to the event manager
	private ClasIoEventManager _eventManager = ClasIoEventManager.getInstance();

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
	 * @param itemList   the list this item is on.
	 * @param view       the view this item lives on.
	 * @param sector     the 1-based sector [1..6]
	 * @param half       the 1-based layer [1..2]
	 * @param ring       the 1-based component [1..18]
	 */
	public SectorLTCCItem(ItemList itemList, SectorView view, byte sector, byte half, short ring) {
		super(itemList);
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
				g.setColor(adcData.getADCColor(adcData.adc[i]));
				g.fillPolygon(_lastDrawnPolygon);
				g.setColor(Color.black);
				g.drawPolygon(_lastDrawnPolygon);
			}
		} // end has data



		//the LTCC.rec data
		if (_view.showReconHits()) {

			Point.Double wp = new Point.Double();
			Point pp = new Point();

			LTCCRecData recData = LTCCRecData.getInstance();
			for (int i = 0; i < recData.count(); i++) {
				float x = recData.x[i];
				float y = recData.y[i];
				float z = recData.z[i];

				int sect = GeometryManager.getSector(x, y);
				if (sect == _sector) {
					_view.projectClasToWorld(x, y, z, _view.getProjectionPlane(), wp);
					container.worldToLocal(pp, wp);

					DataDrawSupport.drawReconHit(g, pp);
					recData.setLocation(i, pp);

				}

			} // end loop over rec data
		} // end show recon hits
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

			feedbackStrings.add(DataDrawSupport.prelimColor + "LTCC sect " + _sector + " ring " + _ring + " half " + _half);

			LTCCADCData adcData = LTCCADCData.getInstance();
			LTCCTDCData tdcData = LTCCTDCData.getInstance();

			for (int i = 0; i < adcData.count(); i++) {
				if ((adcData.sector[i] == _sector) && (adcData.layer[i] == _half) && (adcData.component[i] == _ring)) {
					String s = String.format("LTCC adc: %d time: %8.3f", adcData.adc[i], adcData.time[i]);
					feedbackStrings.add(s);
					break;
				}
			} // end has data

			for (int i = 0; i < tdcData.count(); i++) {
				if ((tdcData.sector[i] == _sector) && (tdcData.layer[i] == _half) && (tdcData.component[i] == _ring)) {
					String s = String.format("LTCC tdc: %d", tdcData.tdc[i]);
					feedbackStrings.add(s);
					break;
				}
			} // end has data
		}

		// hit feedback
		if (_view.showReconHits()) {

			LTCCRecData recData = LTCCRecData.getInstance();
			for (int i = 0; i < recData.count(); i++) {

				float x = recData.x[i];
				float y = recData.y[i];

				int sect = GeometryManager.getSector(x, y);
				if (sect == _sector) {

					if (recData.contains(i, screenPoint)) {
						recData.recFeedback("LTTC", i, feedbackStrings);
						break;
					}
				}

			} // end loop over rec data
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