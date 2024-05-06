package cnuphys.kinetic.box3D;

import java.awt.Color;
import java.awt.Font;

import bCNU3D.GrowablePointSet;
import bCNU3D.Panel3D;
import item3D.Axes3D;
import item3D.Cube;
import item3D.GrowablePointSets3D;

public class Box3D extends Panel3D {
	
	static final float xymax = 1.1f;
	static final float zmax = 1.1f;
	static final float zmin = -1.1f;
	static final float xdist = -.183f;
	static final float ydist = 0f;
	static final float zdist = -3f;

	static final float thetax = 45f;
	static final float thetay = 45f;
	static final float thetaz = 45f;	
	
	private GrowablePointSets3D molecules = new GrowablePointSets3D(this);

	public Box3D() {
		super(thetax, thetay, thetaz, xdist, ydist, zdist);
		
		Axes3D axes = new Axes3D(this, -xymax, xymax, -xymax, xymax, zmin, zmax, null, Color.darkGray, 1f, 7, 7,
				8, Color.black, Color.blue, new Font("SansSerif", Font.PLAIN, 11), 1);
		addItem(axes);
		
		addItem(new Cube(this, 0f, 0f, 0f, 1, new Color(0, 0, 255, 32), true));

		addItem(molecules);

	}


}
