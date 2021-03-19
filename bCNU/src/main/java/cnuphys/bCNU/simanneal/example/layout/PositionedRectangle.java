package cnuphys.bCNU.simanneal.example.layout;

import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.util.Random;


public abstract class PositionedRectangle extends Rectangle {
	
	public static int _nextId = 0;
	
	//shared random number generator
	protected static Random _rand = LayoutSimulation.random;

	//layout bounds
	protected static Rectangle _bounds = LayoutSimulation.bounds;
		
	// icon size
	protected static int _size = LayoutSimulation.size;
	
	// a pixel gap 
	protected static int _gap = LayoutSimulation.gap;
	
	//unique id
	public int id = (++_nextId);
	
	//for computing energy
	public int mass = 1;
	
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
	public Point2D.Double getPosition() {
		return new Point2D.Double(getCenterX(), getCenterY());
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
	 * Get the distance to another positioned rectangle
	 * @param opr the other rectangle
	 * @return the distance
	 */
	public double distance(PositionedRectangle opr) {
		
		double dx = opr.getCenterX() - getCenterX();
		double dy = opr.getCenterY() - getCenterY();
		return Math.sqrt(dx*dx + dy*dy);
	}
		
	/**
	 * Swap the ppositions of two rectangles
	 * @param p one pr
	 * @param q the other pr
	 */
	public static void swapPosition(PositionedRectangle p, PositionedRectangle q) {
		
		int tx = q.x;
		int ty = q.y;
		int tw = q.width;
		int th = q.height;
		
		q.x = p.x;
		q.y = p.y;
		q.width = p.width;
		q.height = p.height;

		p.x = tx;
		p.y = ty;
		p.width = tw;
		p.height = th;

	}

}
