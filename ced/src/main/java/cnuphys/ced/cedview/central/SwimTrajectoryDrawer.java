package cnuphys.ced.cedview.central;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.Point2D;

import cnuphys.bCNU.graphics.container.IContainer;
import cnuphys.bCNU.magneticfield.swim.ASwimTrajectoryDrawer;
import cnuphys.ced.clasio.ClasIoEventManager;
import cnuphys.swim.SwimTrajectory;
import cnuphys.swim.SwimTrajectory2D;

public class SwimTrajectoryDrawer extends ASwimTrajectoryDrawer {

	private CentralXYView _view;

	public SwimTrajectoryDrawer(CentralXYView view) {
		_view = view;
	}

	/**
	 * Actual drawing method
	 *
	 * @param g         the graphics context
	 * @param container the base container
	 */
	@Override
	public void draw(Graphics g, IContainer container) {

		if (!ClasIoEventManager.getInstance().isAccumulating()) {

			Rectangle sr = container.getInsetRectangle();
			Graphics2D g2 = (Graphics2D) g;

			Shape oldClip = g2.getClip();

			g2.clipRect(sr.x, sr.y, sr.width, sr.height);
			super.draw(g, container);
			g2.setClip(oldClip);
		}
	}

	/**
	 * Here we have a chance to veto a trajectory. For example, we may decide that
	 * the trajectory won't appear on this view (assuming a view owns this drawer)
	 * and so don't bother to compute it. The default implementation vetoes nothing.
	 *
	 * @param trajectory the trajectory to test.
	 * @return <code>true</code> if this trajectory is vetoed.
	 */
	@Override
	protected boolean veto(SwimTrajectory trajectory) {
//		double theta = trajectory.getOriginalTheta();
//		return theta < 30 || theta > 150;
		return false;
	}

	/**
	 * From detector xyz get the projected world point.
	 *
	 * @param v3d the 3D vector (meters)
	 * @param wp  the projected world point.
	 */
	@Override
	public void project(double[] v3d, Point2D.Double wp) {

		// the path in the 3D traj is in cm. We want mm.
		_view.labToWorld(10 * v3d[0], 10 * v3d[1], 10 * v3d[2], wp);

	}

	@Override
	public void prepareForRemoval() {
	}

	@Override
	public void setDirty(boolean dirty) {
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isVisible() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setVisible(boolean visible) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean acceptSimpleTrack(SwimTrajectory2D trajectory) {

		String source = trajectory.getSource().toLowerCase();

		if (source.contains("hbtracks")) {
			return _view.showHB();
		} else if (source.contains("tbtracks")) {
			return _view.showTB();
		} else if (source.contains("cvtrec")) {
			return _view.showCVTRecTracks();
		} else if (source.contains("cvt")) {
			return _view.showRecKFTraj();
		} else if (source.contains("cvt")) {
			return _view.showCVTP1Tracks();
		}

		return true;
	}

}
