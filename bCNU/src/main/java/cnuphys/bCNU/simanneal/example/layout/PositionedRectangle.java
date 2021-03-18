package cnuphys.bCNU.simanneal.example.layout;

import java.awt.Rectangle;
import java.util.Random;


public abstract class PositionedRectangle extends Rectangle {
	
	//shared random number generator
	protected static Random _rand = LayoutSimulation.random;

	//layout bounds
	protected static Rectangle _bounds = LayoutSimulation.bounds;
	
	// used for center location
	protected Position _position = new Position();
	
	// icon size
	protected static int _size = LayoutSimulation.size;
	
	// a pixel gap 
	protected static int _gap = LayoutSimulation.gap;
	
	/**
	 * Create a positioned rectangle at a random location
	 */
	public PositionedRectangle() {
		//default to a random location
		x = _bounds.x + _rand.nextInt(_bounds.width/2);
		y = _bounds.y + _rand.nextInt(_bounds.height/2);
		width =1;
		height = 1;
	}
	
	/**
	 * Get the position (the center)
	 * @return the position
	 */
	public Position getPosition() {
		_position.x = getCenterX();
		_position.y = getCenterY();
		
		return _position;
	}
	
	/**
	 * Offset the pr
	 * @param dx the horizontal offset
	 * @param dy the vertical offset
	 */
	public void offset(double dx, double dy) {
		x += (int)dx;
		y += (int)dy;
	}
	

	/**
	 * Set the position (center) of the pr
	 * @param x the horizontal location
	 * @param y the vertical location
	 */
	public void setPosition(double x, double y) {
		double w2 = width/2.0;
		double h2 = height/2.0;
		setFrame(x-w2, y-h2, width, height);
	}
	
	/**
	 * Is this a box (as opposed to a singleton)
	 * @return <code>true if this is a box
	 */
	public boolean isBox() {
		return this instanceof Box;
	}
	
	/**
	 * Is this a singleton (as opposed to a box)
	 * @return <code>true if this is a singleton
	 */
	public boolean isSingleton() {
		return this instanceof Singleton;
	}
	
	/**
	 * Does this pr overlap another?
	 * @param opr the other pr
	 * @return <code>true</code> if they overlap
	 */
	public boolean overlaps(PositionedRectangle opr) {
		return intersects(opr);
	}
	
	/**
	 * Is this pr out of the boundary?
	 * @return <code>true</code> if the pr is out of the boundary
	 */
	public boolean outOfBounds() {
		boolean inside =  _bounds.contains(this);
		return !inside;
	}
		
	/**
	 * Make a deep copy (a clone)
	 * @return a copy
	 */
	public abstract PositionedRectangle copy();
	
	/**
	 * Get the gap distance which is the "air" between the boxes
	 * @param opr the other pr
	 * @return the gap distance (reasonably accurately)
	 */
	public double gapDistance(PositionedRectangle opr, double[] p0, double[] p1) {
		if (overlaps(opr)) {
			return 0;
		}
		
		final double x0 = getCenterX();
		final double y0 = getCenterY();
		final double x1 = opr.getCenterX();
		final double y1 = opr.getCenterY();
		
		double dx = x1 - x0;
		double dy = y1 - y0;
		
		double dt = 0.05;
		double t0 = dt;
		
		while (t0 < 0.999) {
			double x = x0 + dx*t0;
			double y = y0 + dy*t0;
			
			if (!contains(x, y)) {
				break;
			}
			t0 += dt;
		}
		
		double t1 = 1-dt;
		
		double tmax = t0 + dt;
		while (t1 > tmax) {
			double x = x0 + dx*t1;
			double y = y0 + dy*t1;
			
			if (!opr.contains(x, y)) {
				break;
			}
			t1 -= dt;
		}
		
		
		p0[0] = x0 + dx*t0;
		p0[1] = y0 + dy*t0;
		p1[0] = x0 + dx*t1;
		p1[1] = y0 + dy*t1;


		dx = p1[0] - p0[0];
		dy = p1[1] - p0[1];
		
		double d = Math.sqrt(dx*dx + dy*dy);
		
		if (d < 1) {
			System.out.println("");
		}
		
		return d;
	}
	
	public void getDel(PositionedRectangle opr, Del del) {			
		double dx = opr.getCenterX() - getCenterX();
		double dy = opr.getCenterY() - getCenterY();
		del.set(dx, dy);
	}
	
	public double distance(PositionedRectangle opr) {
		
		double dx = opr.getCenterX() - getCenterX();
		double dy = opr.getCenterY() - getCenterY();
		return Math.sqrt(dx*dx + dy*dy);
	}
	
	public void toCenter(Del del) {
		double dx = _bounds.getCenterX() - getCenterX();
		double dy = _bounds.getCenterY() - getCenterY();

		del.set(dx, dy);
	}
	

}
