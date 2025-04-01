package cnuphys.ced.cedview.sectorview;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.util.List;

import cnuphys.bCNU.graphics.container.IContainer;
import cnuphys.bCNU.graphics.style.LineStyle;
import cnuphys.bCNU.graphics.world.WorldGraphicsUtilities;
import cnuphys.bCNU.item.ItemList;
import cnuphys.bCNU.item.PolygonItem;
import cnuphys.ced.alldata.datacontainer.cal.PCalADCData;
import cnuphys.ced.clasio.ClasIoEventManager;
import cnuphys.ced.event.AccumulationManager;
import cnuphys.ced.geometry.ECGeometry;
import cnuphys.ced.geometry.PCALGeometry;
import cnuphys.lund.X11Colors;

public class SectorPCALItem extends PolygonItem {

	// 1-based sector
	private int _sector;

	// the container sector view
	private SectorView _view;

	// should be PCALGeometry.PCAL_U, PCAL_V, or PCAL_W
	private int _viewType; // 0,1,2

	private static final String _pcalViews[] = { "U", "V", "W" };
	private static final Color _pcalFill = X11Colors.getX11Color("mint cream");
	private static final Color _pcalLine = new Color(140, 140, 140);

	private static int[] _stripCounts = { 68, 62, 62 }; // u,v,w

	/**
	 * Create a sector view pcal item
	 *
	 * @param itemList   the list this item is on.
	 * @param stripIndex should be PCAL_U, PCAL_V, or PCAL_W
	 * @param sector     the 1-based sector
	 */
	public SectorPCALItem(ItemList itemList, int stripIndex, int sector) {
		super(itemList, getShell((SectorView) itemList.getContainer().getView(), stripIndex, sector));

		setRightClickable(false);
		_sector = sector;
		_viewType = stripIndex;

		_name = "PCAL " + _pcalViews[stripIndex] + " sector " + _sector;

		_style.setFillColor(_pcalFill);
		_style.setLineColor(_pcalLine);
		_style.setLineWidth(0);
		_view = (SectorView) getItemList().getContainer().getView();

	}

	/**
	 * Custom drawer for the item.
	 *
	 * @param g         the graphics context.
	 * @param container the graphical container being rendered.
	 */
	@Override
	public void drawItem(Graphics g, IContainer container) {

		if (ClasIoEventManager.getInstance().isAccumulating()) {
			return;
		}

		Point2D.Double path[] = getShell(_view, _viewType, _sector);

		if (path == null) {
			return;
		}

		setPath(path);

		for (int stripIndex = 0; stripIndex < PCALGeometry.PCAL_NUMSTRIP[_viewType]; stripIndex++) {
			Point2D.Double wp[] = getStrip(stripIndex);

			if (wp != null) {

				Path2D.Double path2d = WorldGraphicsUtilities.worldPolygonToPath(wp);
				WorldGraphicsUtilities.drawPath2D(g, container, path2d, _style.getFillColor(), _style.getLineColor(), 0,
						LineStyle.SOLID, true);
			}
		}

		// draw ADC data
		drawADCData(g, container);

	}


	/**
	 * Get a strip outline
	 *
	 * @param stripIndex the 0-based index
	 * @return the strip outline
	 */
	private Point2D.Double[] getStrip(int stripId) {

		if (!PCALGeometry.doesProjectedPolyFullyIntersect(_viewType, stripId, _view.getProjectionPlane())) {
			return null;
		}

		Point2D.Double wp[] = PCALGeometry.getIntersections(_viewType, stripId, _view.getProjectionPlane(), true);

		if (wp == null) {
			return null;
		}

		// lower sectors (4, 5, 6) (need sign flip
		if (_sector > 3) {
			for (Point2D.Double twp : wp) {
				twp.y = -twp.y;
			}
		}

		return wp;
	}

	// draw any hits
	private void drawADCData(Graphics g, IContainer container) {

		if (_view.isSingleEventMode()) {
			drawSingleEventADC(g, container);
		} else {
			drawAccumulatedHits(g, container);
		}
	}

	// single event drawer
	private void drawSingleEventADC(Graphics g, IContainer container) {

		PCalADCData pcalData = PCalADCData.getInstance();
		for (int i = 0; i < pcalData.count(); i++) {
			byte sector = pcalData.sector.get(i);
			byte view = pcalData.view.get(i);

			if ((sector == _sector) &&  (view == _viewType)) {
				int strip0 = pcalData.strip.get(i) - 1;
				Point2D.Double wp[] = getStrip(strip0);

				if (wp != null) {
					Path2D.Double path = WorldGraphicsUtilities.worldPolygonToPath(wp);
					Color fc = pcalData.getADCColor(pcalData.adc.get(i));
					WorldGraphicsUtilities.drawPath2D(g, container, path, fc, fc, 0, LineStyle.SOLID, true);
				}
			}
		}

	}

	// accumulated drawer
	private void drawAccumulatedHits(Graphics g, IContainer container) {
		int maxHit = AccumulationManager.getInstance().getMaxPCALCount();

		int hits[][][] = AccumulationManager.getInstance().getAccumulatedPCALData();
		for (int strip0 = 0; strip0 < _stripCounts[_viewType]; strip0++) {
			int hitCount = hits[_sector - 1][_viewType][strip0];
			double fract = (maxHit == 0) ? 0 : (((double) hitCount) / maxHit);

			Point2D.Double wp[] = getStrip(strip0);

			if (wp != null) {
				Color color = AccumulationManager.getInstance().getColor(_view.getColorScaleModel(), fract);
				Path2D.Double path = WorldGraphicsUtilities.worldPolygonToPath(wp);
				WorldGraphicsUtilities.drawPath2D(g, container, path, color, _style.getLineColor(), 0, LineStyle.SOLID,
						true);
			}

		}
	}

	/**
	 * Get the shell of the ec.
	 *
	 * @param view      the view being rendered.
	 * @param stripType the strip index (0:U, 1:V, 2:W)
	 * @param sector    the 1-based sector 1..6
	 * @return
	 */
	private static Point2D.Double[] getShell(SectorView view, int stripType, int sector) {

		Point2D.Double wp[] = PCALGeometry.getShell(stripType, view.getProjectionPlane());

		if (wp == null) {
			System.err.println("null shell in SectorPCALItem stripType = " + stripType + "  sector = " + sector);
			return null;
		}

		// lower sectors (4, 5, 6) (need sign flip
		if (sector > 3) {
			for (Point2D.Double twp : wp) {
				twp.y = -twp.y;
			}
		}

		return wp;
	}

	/**
	 * Add any appropriate feedback strings panel. Default implementation returns
	 * the item's name.
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
			feedbackStrings.add(getName());
		}
		else {
			return;
		}

		// which strip?
		for (int strip0 = 0; strip0 < PCALGeometry.PCAL_NUMSTRIP[_viewType]; strip0++) {
			Point2D.Double wp[] = getStrip(strip0);
			if (wp != null) {
				Path2D.Double path = WorldGraphicsUtilities.worldPolygonToPath(wp);

				if (path.contains(worldPoint)) {
					feedbackStrings.add("$white$view " + _pcalViews[_viewType]
							+ " strip " + (strip0 + 1));

					PCalADCData pcalData = PCalADCData.getInstance();
					for (int i = 0; i < pcalData.count(); i++) {
						byte sector = pcalData.sector.get(i);
						byte view = pcalData.view.get(i);
						int strip = pcalData.strip.get(i);

						if ((sector == _sector) && (view == _viewType) && (strip == (strip0+1))) {
							String str = String.format("%s strip %d adc %d time %-7.3f",
									ECGeometry.VIEW_NAMES[view], strip,
									pcalData.adc.get(i), pcalData.time.get(i));

							feedbackStrings.add("$coral$" + str);

							break;
						}
					}
					return;
				}
			} // wp != null
		} // strip loop
	} // getFeedbackStrings
}