package cnuphys.bCNU.simanneal.example.layout;

/**
 * A class for holding "deltas"
 * @author heddle
 *
 */
public 	class Del {
	/** the x component of the delta */
	public double dx;
	
	/** the y component of the delta */
	public double dy;
	
	
	double lengthSq;
	private double length;
	private double ux;
	private double uy;
	
	public void set(double dx, double dy) {
		this.dx = dx;
		this.dy = dy;
		lengthSq = dx*dx + dy*dy;
		length = Math.sqrt(lengthSq);
		ux = (length > 1.0e-20) ? dx/length : 0;
		uy = (length > 1.0e-20) ? dy/length : 0;
	}
	
	
	/**
	 * 
	 * @return
	 */
	public double getLength() {
		return length;
	}
	
	/**
	 * 
	 * @return
	 */
	public double getLengthSq() {
		return lengthSq;
	}
	
	/**
	 * Get the x component of the unit vector
	 * @return the x component of the unit vector
	 */
	public double getUx() {
		return ux;
	}
	
	/**
	 * Get the y component of the unit vector
	 * @return the y component of the unit vector
	 */
	public double getUy() {
		return uy;
	}
	
}