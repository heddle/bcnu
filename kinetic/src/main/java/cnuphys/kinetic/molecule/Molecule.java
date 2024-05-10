package cnuphys.kinetic.molecule;

/**
 * A molecule in a 3D box
 */

import bCNU3D.Vector3f;
import cnuphys.kinetic.frame.Kinetic;

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
	
	public Molecule(double T) {
		set(T);
    }
	
	public void set(double T) {
		float v = (float) MaxwellSpeedDistribution.generateSpeed(T);
		
		double phi = Kinetic.random.nextFloat() * 2 * Math.PI;
		double theta =Kinetic.random.nextFloat() * Math.PI;
		
		float sinTheta = (float) Math.sin(theta);
		double cosTheta = Math.cos(theta);
		
		this.vx = (float) (v * sinTheta * Math.cos(phi));
		this.vy = (float) (v * sinTheta * Math.sin(phi));
		this.vz = (float) (v * cosTheta);
		
		
		this.x = Kinetic.random.nextFloat();
		this.y = Kinetic.random.nextFloat();
		this.z = Kinetic.random.nextFloat();
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
