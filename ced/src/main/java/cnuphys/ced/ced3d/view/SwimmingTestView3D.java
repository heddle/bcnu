package cnuphys.ced.ced3d.view;

import java.awt.Dimension;

import cnuphys.bCNU.graphics.GraphicsUtilities;
import cnuphys.ced.ced3d.PlainPanel3D;
import cnuphys.ced.ced3d.SwimmerPanel3D;

public class SwimmingTestView3D extends PlainView3D {


	public static final float xdist = -200f;
	public static final float ydist = 0f;
	public static final float zdist = -1600f;

	private static final float thetax = -90f;
	private static final float thetay = 0f;
	private static final float thetaz = -90f;
	
	public SwimmingTestView3D() {
		super("Swimming Testing 3D View", thetax, thetay, thetaz, xdist, ydist, zdist);
		Dimension d = GraphicsUtilities.getDisplaySize();

		setSize((int)(0.8*d.width), (int)(0.8*d.height));
	}

	@Override
	protected PlainPanel3D make3DPanel(float angleX, float angleY, float angleZ, float xDist, float yDist, float zDist) {
		return new SwimmerPanel3D(this, angleX, angleY, angleZ, xDist, yDist, zDist);
	}

}
