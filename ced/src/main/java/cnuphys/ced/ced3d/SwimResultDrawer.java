package cnuphys.ced.ced3d;

import java.awt.Color;
import java.util.ArrayList;
import com.jogamp.opengl.GLAutoDrawable;

import bCNU3D.Support3D;
import cnuphys.adaptiveSwim.AdaptiveSwimResult;
import cnuphys.lund.LundId;
import cnuphys.lund.LundStyle;
import cnuphys.swim.SwimTrajectory;
import item3D.Item3D;

public class SwimResultDrawer extends Item3D {
	
	private static final Color failColor = Color.black;
	private static final Color posColor = Color.red;
	private static final Color negColor = Color.blue;
	
	public SwimResultDrawer(PlainPanel3D panel3D) {
		super(panel3D);
	}

	@Override
	public void draw(GLAutoDrawable drawable) {
		
		ArrayList<AdaptiveSwimResult> results = SwimmerControlPanel.getSwimResults();
		
		for (AdaptiveSwimResult result : results) {
			
			if (!SwimmerControlPanel.showTrajectory(result)) {
				continue;
			}
			
			SwimTrajectory traj = result.getTrajectory();
			
			if (traj != null) {
				Color color = failColor;
				if (result.getStatus() == 0) { //success
					color = (result.getInitialValues().charge < 0) ?  negColor : posColor;
				}
				drawSwimTrajectory(drawable, traj, color);
			}
		}

	}

	// draw a trajectory in 3D
	private void drawSwimTrajectory(GLAutoDrawable drawable, SwimTrajectory traj, Color color) {
		int size = traj.size();
		if (size < 2) {
			return;
		}

		float coords[] = new float[3 * size];

		if (color == null) {
			LundId lid = traj.getLundId();
			LundStyle style = LundStyle.getStyle(lid);

			color = Color.black;

			if (style != null) {
				color = style.getFillColor();
			}
		}

		for (int i = 0; i < size; i++) {
			double v[] = traj.get(i);
			int j = i * 3;			// convert to cm
			coords[j] = 100 * (float) v[0];
			coords[j + 1] = 100 * (float) v[1];
			coords[j + 2] = 100 * (float) v[2];
		}

		Support3D.drawPolyLine(drawable, coords, color, 2f);
	}
}

