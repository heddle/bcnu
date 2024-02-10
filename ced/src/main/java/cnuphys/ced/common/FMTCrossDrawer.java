package cnuphys.ced.common;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
import java.util.List;

import cnuphys.bCNU.graphics.container.IContainer;
import cnuphys.bCNU.graphics.world.WorldGraphicsUtilities;
import cnuphys.ced.alldata.datacontainer.fmt.FMTRecCrossData;
import cnuphys.ced.cedview.CedView;
import cnuphys.ced.cedview.dcxy.DCXYView;
import cnuphys.ced.clasio.ClasIoEventManager;
import cnuphys.ced.event.data.DataDrawSupport;
import cnuphys.ced.geometry.GeometryManager;

public class FMTCrossDrawer extends CedViewDrawer {

	private static final int ARROWLEN = 30; // pixels
	private static final Stroke THICKLINE = new BasicStroke(1.5f);

	public FMTCrossDrawer(CedView view) {
		super(view);
	}
	
	// data container
	private FMTRecCrossData _fmtRecCrossData = FMTRecCrossData.getInstance();

	@Override
	public void draw(Graphics g, IContainer container) {

		if (!_view.showFMTCrosses() || ClasIoEventManager.getInstance().isAccumulating() || !_view.isSingleEventMode()) {
			return;
		}

		Graphics2D g2 = (Graphics2D) g;
		Shape oldClip = g2.getClip();
		// clip the active area
		Rectangle sr = container.getInsetRectangle();
		g2.clipRect(sr.x, sr.y, sr.width, sr.height);

		Stroke oldStroke = g2.getStroke();
		g2.setStroke(THICKLINE);

		drawFMTCrosses(g, container);

		g2.setStroke(oldStroke);
		g2.setClip(oldClip);
	}

	/**
	 * Draw FMT crosses
	 *
	 * @param g         the graphics context
	 * @param container the drawing container
	 */
	public void drawFMTCrosses(Graphics g, IContainer container) {

		// treat DCXY view separately
		if (_view instanceof DCXYView) {
			drawFMTCrossesXY(g, container);
			return;
		}
		
		int count = _fmtRecCrossData.count();
		if (count > 0) {
			Point2D.Double wp = new Point2D.Double();
			Point pp = new Point();
			Point2D.Double wp2 = new Point2D.Double();
			Point pp2 = new Point();
			double result[] = new double[3];

			for (int i = 0; i < _fmtRecCrossData.count(); i++) {

				result[0] = _fmtRecCrossData.x[i];
				result[1] = _fmtRecCrossData.y[i];
				result[2] = _fmtRecCrossData.z[i];

				int crossSector = GeometryManager.labXYZToSectorNumber(result);
				_view.projectClasToWorld(result[0], result[1], result[2], _view.getProjectionPlane(), wp);
				int mySector = _view.getSector(container, null, wp);
				if (mySector == crossSector) {
					container.worldToLocal(pp, wp);
					_fmtRecCrossData.setLocation(i, pp);

					// arrows

					int pixlen = ARROWLEN;
					double r = pixlen / WorldGraphicsUtilities.getMeanPixelDensity(container);

					// lab coordinates of end of arrow
					result[0] = _fmtRecCrossData.x[i] + r * _fmtRecCrossData.ux[i];
					result[1] = _fmtRecCrossData.y[i] + r * _fmtRecCrossData.uy[i];
					result[2] = _fmtRecCrossData.z[i] + r * _fmtRecCrossData.uz[i];
					_view.projectClasToWorld(result[0], result[1], result[2], _view.getProjectionPlane(), wp2);
					container.worldToLocal(pp2, wp2);

					g.setColor(Color.orange);
					g.drawLine(pp.x + 1, pp.y, pp2.x + 1, pp2.y);
					g.drawLine(pp.x, pp.y + 1, pp2.x, pp2.y + 1);
					g.setColor(Color.darkGray);
					g.drawLine(pp.x, pp.y, pp2.x, pp2.y);

					// the circles and crosses
					DataDrawSupport.drawCross(g, pp.x, pp.y, DataDrawSupport.FMT_CROSS);
				}
			}
		}
	}

	/**
	 * Draw FMT crosses
	 *
	 * @param g         the graphics context
	 * @param container the drawing container
	 */
	public void drawFMTCrossesXY(Graphics g, IContainer container) {

		int count = _fmtRecCrossData.count();
		if (count > 0) {
			Point pp = new Point();
			Point2D.Double wp2 = new Point2D.Double();
			Point pp2 = new Point();

			for (int i = 0; i < _fmtRecCrossData.count(); i++) {
				container.worldToLocal(pp, _fmtRecCrossData.x[i], _fmtRecCrossData.y[i]);
				_fmtRecCrossData.setLocation(i, pp);

				// arrows

				int pixlen = ARROWLEN;
				double r = pixlen / WorldGraphicsUtilities.getMeanPixelDensity(container);

				// lab coordinates of end of arrow
				wp2.setLocation(_fmtRecCrossData.x[i] + r * _fmtRecCrossData.ux[i],
						_fmtRecCrossData.y[i] + r * _fmtRecCrossData.uy[i]);
				container.worldToLocal(pp2, wp2);

				g.setColor(Color.orange);
				g.drawLine(pp.x + 1, pp.y, pp2.x + 1, pp2.y);
				g.drawLine(pp.x, pp.y + 1, pp2.x, pp2.y + 1);
				g.setColor(Color.darkGray);
				g.drawLine(pp.x, pp.y, pp2.x, pp2.y);

				// the circles and crosses
				DataDrawSupport.drawCross(g, pp.x, pp.y, DataDrawSupport.FMT_CROSS);
			}
		}
	}

	/**
	 * Use what was drawn to generate feedback strings
	 *
	 * @param container       the drawing container
	 * @param screenPoint     the mouse location
	 * @param worldPoint      the corresponding world location
	 * @param feedbackStrings add strings to this collection
	 */
	private void feedback(IContainer container, Point screenPoint, Point2D.Double worldPoint,
			List<String> feedbackStrings) {

		// fmt crosses?
		
		int count = _fmtRecCrossData.count();
		if (count > 0) {
			for (int i = 0; i < count; i++) {
				if (_fmtRecCrossData.contains(i, screenPoint)) {
					_fmtRecCrossData.feedback("FMTRec", i, feedbackStrings);
					break;
				}
			}
		}

	}

	@Override
	public void vdrawFeedback(IContainer container, Point screenPoint, Double worldPoint, List<String> feedbackStrings,
			int option) {
		feedback(container, screenPoint, worldPoint, feedbackStrings);

	}

}