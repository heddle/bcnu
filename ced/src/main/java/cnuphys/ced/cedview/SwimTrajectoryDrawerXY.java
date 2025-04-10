package cnuphys.ced.cedview;


import java.awt.Graphics;
import java.awt.geom.Point2D;

import cnuphys.bCNU.graphics.container.IContainer;
import cnuphys.bCNU.magneticfield.swim.ASwimTrajectoryDrawer;
import cnuphys.ced.clasio.ClasIoEventManager;
import cnuphys.swim.SwimTrajectory;
import cnuphys.swim.SwimTrajectory2D;

public class SwimTrajectoryDrawerXY extends ASwimTrajectoryDrawer {

	private CedView _view;

	public SwimTrajectoryDrawerXY(CedView view) {
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
			if (!ClasIoEventManager.getInstance().isAccumulating() && _view.isSingleEventMode()) {
			super.draw(g, container);
		}
	}

	@Override
	public void drawTrajectories(Graphics g, IContainer container) {

		for (SwimTrajectory2D trajectory2D : _trajectories2D) {

			boolean show = true;

			String source = trajectory2D.getSource();

			if (source != null) {
				if (source.contains("HitBasedTrkg::HBTracks")) {
					show = _view.showHB();
				} else if (source.contains("TimeBasedTrkg::TBTracks")) {
					show = _view.showTB();
				} else if (source.contains("HitBasedTrkg::AITracks")) {
					show = _view.showAIHB();
				} else if (source.contains("TimeBasedTrkg::AITracks")) {
					show = _view.showAITB();
				} else if (source.contains("REC::Particle")) {
					show = _view.showRecPart();
				}
			}

			if (!show) {
				continue;
			}

			drawSwimTrajectory(g, container, trajectory2D);
			if (_view.showSectorChange()) {
				markSectorChanges(g, container, trajectory2D);
			}
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
		return false;
	}

	/**
	 * Just us the xy coordinates directly. Ignore z.
	 *
	 * @param v3d the 3D vector (meters)
	 * @param wp  the projected world point.
	 */
	@Override
	public void project(double[] v3d, Point2D.Double wp) {
		// convert to cm
		wp.setLocation(v3d[0], v3d[1]);
	}

	@Override
	public void prepareForRemoval() {
	}

	@Override
	public void setDirty(boolean dirty) {
	}

	@Override
	public String getName() {
		return null;
	}

	@Override
	public boolean isVisible() {
		return false;
	}

	@Override
	public void setVisible(boolean visible) {
	}

	@Override
	public boolean acceptSimpleTrack(SwimTrajectory2D trajectory) {


		boolean show = true;
		String source = trajectory.getSource();

	//	System.err.println("AST SOURCE: [" +  source + "]");

		if (source != null) {
			if (source.contains("HitBasedTrkg::HBTracks")) {
				show = _view.showHB();
			} else if (source.contains("TimeBasedTrkg::TBTracks")) {
				show = _view.showTB();
			} else if (source.contains("HitBasedTrkg::AITracks")) {
				show = _view.showAIHB();
			} else if (source.contains("TimeBasedTrkg::AITracks")) {
				show = _view.showAITB();
			} else if (source.contains("CVTRec::Tracks")) {
				return _view.showCVTRecTracks();
			} else if (source.contains("CVT::Tracks")) {
				return _view.showCVTP1Tracks();
			}
		}


		return show;
	}

}