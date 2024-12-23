package cnuphys.kinetic.box3D;

/**
 * A 3D box
 * This will be the 1x1x1 gas container for the kinetic theory simulation
 */

import java.awt.Color;
import java.awt.Font;

import bCNU3D.Panel3D;
import cnuphys.kinetic.molecule.Molecules;
import item3D.Axes3D;
import item3D.Cube;

public class Box3D extends Panel3D {

	//make the axes a little bigger than the box
	static final float xymax = 1.1f;
	static final float zmax = 1.1f;
	static final float zmin = -1.1f;
	static final float xdist = -.183f;
	static final float ydist = 0f;
	static final float zdist = -3f;

	static final float thetax = 45f;
	static final float thetay = 45f;
	static final float thetaz = 45f;

	private Molecules molecules = new Molecules(this);

	public Box3D() {
		super(thetax, thetay, thetaz, xdist, ydist, zdist);

		Axes3D axes = new Axes3D(this, -xymax, xymax, -xymax, xymax, zmin, zmax, null, Color.darkGray, 1f, 7, 7,
				8, Color.black, Color.blue, new Font("SansSerif", Font.PLAIN, 11), 1);
		addItem(axes);

		addItem(new Cube(this, 0f, 0f, 0f, 1, new Color(128, 128, 128, 32), true));

		addItem(molecules);

	}


}
