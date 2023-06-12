package cnuphys.ced.cedview.ftof;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.util.List;

import org.jlab.io.base.DataEvent;

import cnuphys.bCNU.graphics.container.IContainer;
import cnuphys.ced.alldata.ColumnData;
import cnuphys.ced.clasio.ClasIoEventManager;

public abstract class FTOFGenericHitHandler {

	private String _fbcolor;
	private String _bankname;
	private String _fbprefix;
	private static final int _fbrSize = 16;

	// work space
	private Rectangle _rect = new Rectangle();
	private Point _pp = new Point();
	private Point2D.Double _wp = new Point2D.Double();

	// the parent view
	protected FTOFView _view;

	public FTOFGenericHitHandler(FTOFView view, String bankname, String fbcolor, String fbprefix) {
		_fbcolor = fbcolor;
		_bankname = bankname;
		_fbprefix = fbprefix;
		_view = view;
	}

	public abstract boolean showTheseHits();

	public abstract void drawHit(Graphics g, Point pp);

	// draw the clusters
	public void draw(Graphics g, IContainer container) {

		if (_view.isSingleEventMode() && showTheseHits()) {
			DataEvent event = ClasIoEventManager.getInstance().getCurrentEvent();
			if (event == null) {
				return;
			}

			byte sector[] = ColumnData.getByteArray(_bankname + ".sector");

			int count = (sector == null) ? 0 : sector.length;
			if (count == 0) {
				return;
			}

			byte layer[] = ColumnData.getByteArray(_bankname + ".layer");
			float x[] = ColumnData.getFloatArray(_bankname + ".x");
			float y[] = ColumnData.getFloatArray(_bankname + ".y");

			for (int i = 0; i < count; i++) {
				int panel = layer[i] - 1;
				if (panel == _view.displayPanel()) {
					_wp.setLocation(x[i], y[i]);
					container.worldToLocal(_pp, _wp);
					drawHit(g, _pp);
				}
			}

		} else {
			// drawAccumulatedHits(g, container);
		}
	}

	public void getFeedbackStrings(IContainer container, int sect, int panel, int paddleId, Point pp, Point2D.Double wp,
			List<String> feedbackStrings) {

		if (_view.isSingleEventMode() && showTheseHits()) {
			DataEvent event = ClasIoEventManager.getInstance().getCurrentEvent();
			if (event == null) {
				return;
			}

			byte sector[] = ColumnData.getByteArray(_bankname + ".sector");

			int count = (sector == null) ? 0 : sector.length;
			if (count == 0) {
				return;
			}

			byte layer[] = ColumnData.getByteArray(_bankname + ".layer");
			float x[] = ColumnData.getFloatArray(_bankname + ".x");
			float y[] = ColumnData.getFloatArray(_bankname + ".y");
			float z[] = ColumnData.getFloatArray(_bankname + ".z");
			float energy[] = ColumnData.getFloatArray(_bankname + ".energy");
			float time[] = ColumnData.getFloatArray(_bankname + ".time");
			short status[] = ColumnData.getShortArray(_bankname + ".status");

			for (int i = 0; i < count; i++) {
				if ((sect == sector[i]) && (panel == (layer[i] - 1))) {
					_wp.setLocation(x[i], y[i]);

					container.worldToLocal(_pp, _wp);

					_rect.setBounds(_pp.x - _fbrSize / 2, _pp.y - _fbrSize / 2, _fbrSize, _fbrSize);
					if (_rect.contains(pp)) {
						feedbackStrings
								.add(String.format("%s%s loc (%7.3f, %7.3f, %7.3f)", _fbcolor, _fbprefix, x[i], y[i], z[i]));
						feedbackStrings.add(String.format("%s%s energy %7.3f time %7.3f status %d", _fbcolor, _fbprefix,
								energy[i], time[i], status[i]));
						break;
					}
				}
			}

		} else { // accum mode
		}
	}

}
