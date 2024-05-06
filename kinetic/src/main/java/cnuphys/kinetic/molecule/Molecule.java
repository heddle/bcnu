package cnuphys.kinetic.molecule;

import bCNU3D.Vector3f;

public class Molecule extends Vector3f {
	
	//the x velocity
	public float vx;
	
	//the y velocity
	public float vy;
	
	//the z velocity
	public float vz;
	
	/**
	 * Create a molecule
	 * 
	 * @param x  the x coordinate
	 * @param y  the y coordinate
	 * @param z  the z coordinate
	 * @param vx the x velocity
	 * @param vy the y velocity
	 * @param vz the z velocity
	 */
	public Molecule(float x, float y, float z, float vx, float vy, float vz) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.vx = vx;
		this.vy = vy;
		this.vz = vz;
	}
	
	/**
	 * Move the molecule
	 * 
	 * @param dt the time step
	 */
	public void move(float dt) {
		x += vx * dt;
		y += vy * dt;
		z += vz * dt;
	}

}
