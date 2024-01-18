package cnuphys.ced.cedview.central;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.Point2D;
import java.util.List;
import java.util.Vector;

import cnuphys.bCNU.drawable.IDrawable;
import cnuphys.bCNU.graphics.SymbolDraw;
import cnuphys.bCNU.graphics.container.IContainer;
import cnuphys.ced.alldata.DataWarehouse;
import cnuphys.ced.cedview.CedView;
import cnuphys.ced.cedview.ILabCoordinates;
import cnuphys.ced.clasio.ClasIoEventManager;
import cnuphys.ced.event.FeedbackRect;
import cnuphys.ced.event.data.CVT;

public abstract class CentralHitDrawer implements IDrawable {

	private boolean _visible = true;

	//the DataWarehouse
	protected  DataWarehouse _dataWarehouse = DataWarehouse.getInstance();

	// the event manager
	protected final ClasIoEventManager _eventManager = ClasIoEventManager.getInstance();

	// cached rectangles for feedback
	protected Vector<FeedbackRect> _fbRects = new Vector<>();

	protected CedView _view;

	private ILabCoordinates labCoord;

	public CentralHitDrawer(CedView view) {
		_view = view;
		labCoord = (ILabCoordinates)view;
	}

	@Override
	public boolean isVisible() {
		return _visible;
	}

	@Override
	public void setVisible(boolean visible) {
		_visible = visible;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	@Override
	public void setEnabled(boolean enabled) {
	}

	@Override
	public void setDirty(boolean dirty) {
	}

	@Override
	public void prepareForRemoval() {
	}

	@Override
	public void draw(Graphics g, IContainer container) {

		if (_eventManager.isAccumulating()) {
			return;
		}

		Graphics2D g2 = (Graphics2D) g;
		Shape oldClip = g2.getClip();
		// clip the active area
		Rectangle sr = container.getInsetRectangle();
		g2.clipRect(sr.x, sr.y, sr.width, sr.height);

		_fbRects.clear();

		if (_view.isSingleEventMode()) {
			if (_view.showMcTruth()) {
				drawMCTruth(g, container);
			}

			drawHitsSingleMode(g, container);
		} else {
			drawAccumulatedHits(g, container);
		}

		g2.setClip(oldClip);

	}

	//single event mode
	protected void drawHitsSingleMode(Graphics g, IContainer container) {
		drawBSTHitsSingleMode(g, container);
		drawBMTHitsSingleMode(g, container);
		drawCTOFSingleHitsMode(g, container);
		drawCNDSingleHitsMode(g, container);
		drawCVTP1Traj(g, container);
		drawCVTRecTraj(g, container);
	}

	protected void drawBSTHitsSingleMode(Graphics g, IContainer container) {
	}

	protected void drawBMTHitsSingleMode(Graphics g, IContainer container) {
	}

	protected void drawCTOFSingleHitsMode(Graphics g, IContainer container) {
	}

	protected void drawCNDSingleHitsMode(Graphics g, IContainer container) {
	}

	private static int TRAJSIZE = 12;


	//CVT Rec
	protected void drawCVTRecTraj(Graphics g, IContainer container) {

		if (!(_view.showCVTRecTraj())) {
			return;
		}

		CVT cvt = CVT.getInstance();
		cvt.fillData();

		float[] x = cvt.rec_x;

		int count = (x == null) ? 0 : x.length;
		if (count == 0) {
			return;
		}

		Point pp = new Point();
		float[] y = cvt.rec_y;
		float[] z = cvt.rec_z;

		short[] id = cvt.rec_id;
		byte[] detector = cvt.rec_detector;
		byte[] sector = cvt.rec_sector;
		byte[] layer = cvt.rec_layer;

		float[] phi = cvt.rec_phi;
		float[] theta = cvt.rec_theta;
		float[] langle = cvt.rec_langle;
		float[] centroid = cvt.rec_centroid;
		float[] path = cvt.rec_path;

		g.setColor(Color.black);
		int s2 = TRAJSIZE/2;

		for (int i = 0; i < count; i++) {
			//cm to mm
			labCoord.labToLocal(container, 10*x[i], 10*y[i], 10*z[i], pp);


			String fb1 = String.format("CVTRec Traj index: %d", i+1);
			String fb2 = String.format("  id: %d  detector: %d  sector: %d  layer: %d", id[i], detector[i], sector[i], layer[i]);
			String fb3 = String.format("  (x,y,z): (%6.3f, %6.3f, %6.3f) cm  path: %6.3f", x[i], y[i], z[i], path[i]);
			String fb4 = String.format("  phi: %6.3f  theta: %6.3f  langle: %5.2f  cent: %5.2f", phi[i], theta[i], langle[i], centroid[i]);

			FeedbackRect fbr = new FeedbackRect(FeedbackRect.Dtype.CVTREC, pp.x-s2, pp.y-s2,
					TRAJSIZE, TRAJSIZE, i, 0, fb1, fb2, fb3, fb4);

			_fbRects.add(fbr);

			SymbolDraw.drawStar(g, pp.x, pp.y, s2, Color.black);
		}
	}


	//Pass 1 Rec
	protected void drawCVTP1Traj(Graphics g, IContainer container) {

	if (!(_view.showCVTP1Traj())) {
		return;
	}

	CVT cvt = CVT.getInstance();
	cvt.fillData();

	float[] x = cvt.p1_x;

	int count = (x == null) ? 0 : x.length;
	if (count == 0) {
		return;
	}

	Point pp = new Point();
	float[] y = cvt.p1_y;
	float[] z = cvt.p1_z;

	short[] id = cvt.p1_id;
	byte[] detector = cvt.p1_detector;
	byte[] sector = cvt.p1_sector;
	byte[] layer = cvt.p1_layer;

	float[] phi = cvt.p1_phi;
	float[] theta = cvt.p1_theta;
	float[] langle = cvt.p1_langle;
	float[] centroid = cvt.p1_centroid;
	float[] path = cvt.p1_path;

	g.setColor(Color.green);
	int s2 = TRAJSIZE/2;

	for (int i = 0; i < count; i++) {
		//cm to mm
		labCoord.labToLocal(container, 10*x[i], 10*y[i], 10*z[i], pp);


		String fb1 = String.format("CVTP1 Traj index: %d", i+1);
		String fb2 = String.format("  id: %d  detector: %d  sector: %d  layer: %d", id[i], detector[i], sector[i], layer[i]);
		String fb3 = String.format("  (x,y,z): (%6.3f, %6.3f, %6.3f) cm  path: %6.3f", x[i], y[i], z[i], path[i]);
		String fb4 = String.format("  phi: %6.3f  theta: %6.3f  langle: %5.2f  cent: %5.2f", phi[i], theta[i], langle[i], centroid[i]);

		FeedbackRect fbr = new FeedbackRect(FeedbackRect.Dtype.CVTP1, pp.x-s2, pp.y-s2,
				TRAJSIZE, TRAJSIZE, i, 0, fb1, fb2, fb3, fb4);

		_fbRects.add(fbr);

		SymbolDraw.drawStar(g, pp.x, pp.y, s2, Color.green);
	}
}

	protected void drawAccumulatedHits(Graphics g, IContainer container) {
		drawBSTAccumulatedHits(g, container);
		drawBMTAccumulateHitsHits(g, container);
		drawCTOFAccumulatedHits(g, container);
		drawCNDAccumulatedHits(g, container);
	}

	protected void drawBSTAccumulatedHits(Graphics g, IContainer container) {
	}

	protected void drawBMTAccumulateHitsHits(Graphics g, IContainer container) {
	}

	protected void drawCTOFAccumulatedHits(Graphics g, IContainer container) {
	}

	protected void drawCNDAccumulatedHits(Graphics g, IContainer container) {
	}


	protected void drawMCTruth(Graphics g, IContainer container) {
	}

	/**
	 * Mouse over feedback
	 *
	 * @param container
	 * @param screenPoint
	 * @param worldPoint
	 * @param feedbackStrings
	 */
	public void feedback(IContainer container, Point screenPoint, Point2D.Double worldPoint,
			List<String> feedbackStrings) {
		for (FeedbackRect rr : _fbRects) {
			rr.contains(screenPoint, feedbackStrings);
		}
	}


}
