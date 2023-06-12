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
import cnuphys.ced.event.data.DataDrawSupport;

public class FTOFClusterHandler {

	private static final String fbcolor = "$salmon$";
	private static final int _fbrSize = 16;

	// work space
	private Rectangle _rect = new Rectangle();
	private Point _pp = new Point();
	private Point2D.Double _wp = new Point2D.Double();


	// the parent view
	private FTOFView _view;

	public FTOFClusterHandler(FTOFView view) {
		_view = view;
	}

	// draw the clusters
	public void draw(Graphics g, IContainer container) {
		if (_view.isSingleEventMode() && _view.showClusters()) {
			DataEvent event = ClasIoEventManager.getInstance().getCurrentEvent();
			if (event == null) {
				return;
			}

			byte sector[] = ColumnData.getByteArray("FTOF::clusters.sector");

			int count = (sector == null) ? 0 : sector.length;
			if (count == 0) {
				return;
			}

			byte layer[] = ColumnData.getByteArray("FTOF::clusters.layer");
			float x[] = ColumnData.getFloatArray("FTOF::clusters.x");
			float y[] = ColumnData.getFloatArray("FTOF::clusters.y");

			for (int i = 0; i < count; i++) {
				int panel = layer[i] - 1;
				if (panel == _view.displayPanel()) {
					_wp.setLocation(x[i], y[i]);
					container.worldToLocal(_pp, _wp);
					DataDrawSupport.drawReconCluster(g, _pp);

				}
			}


		} else {
			// drawAccumulatedHits(g, container);
		}

	}

	public void getFeedbackStrings(IContainer container, int sect, int panel, int paddleId, Point pp, Point2D.Double wp,
			List<String> feedbackStrings) {

		if (_view.isSingleEventMode() && _view.showClusters()) {

			DataEvent event = ClasIoEventManager.getInstance().getCurrentEvent();
			if (event == null) {
				return;
			}

			byte sector[] = ColumnData.getByteArray("FTOF::clusters.sector");

			int count = (sector == null) ? 0 : sector.length;
			if (count == 0) {
				return;
			}

			byte layer[] = ColumnData.getByteArray("FTOF::clusters.layer");
			float x[] = ColumnData.getFloatArray("FTOF::clusters.x");
			float y[] = ColumnData.getFloatArray("FTOF::clusters.y");
			float z[] = ColumnData.getFloatArray("FTOF::clusters.z");
			float energy[] = ColumnData.getFloatArray("FTOF::clusters.energy");
			float time[] = ColumnData.getFloatArray("FTOF::clusters.time");
			short status[] = ColumnData.getShortArray("FTOF::clusters.status");

			for (int i = 0; i < count; i++) {
				if ((sect == sector[i]) && (panel == (layer[i] - 1))) {
					// FTOFGeometry.clasToWorld(sect, panel, x[i], y[i], z[i], _wp);
					_wp.setLocation(x[i], y[i]);

					container.worldToLocal(_pp, _wp);

					_rect.setBounds(_pp.x - _fbrSize / 2, _pp.y - _fbrSize / 2, _fbrSize, _fbrSize);
					if (_rect.contains(pp)) {
						feedbackStrings
								.add(String.format("%scluster loc (%7.3f, %7.3f, %7.3f)", fbcolor, x[i], y[i], z[i]));
						feedbackStrings.add(String.format("%scluster energy %7.3f time %7.3f status %d", fbcolor, energy[i],
								time[i], status[i]));
						break;
					}
				}
			}

		} else { // accum mode
		}

	}




}
