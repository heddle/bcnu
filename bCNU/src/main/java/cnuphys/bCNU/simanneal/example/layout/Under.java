package cnuphys.bCNU.simanneal.example.layout;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.geom.Point2D;

/**
 * Wire goes under a rectangle
 * @author heddle
 *
 */
public class Under {
	
	//layout bounds
	protected static Rectangle _bounds = LayoutSimulation.bounds;

	//segment endpoint
	public PositionedRectangle p1;
	
	//other segment endpoint
	public PositionedRectangle p2;
	
	//the "covering" rect
	public PositionedRectangle cover; 
	
	/** intersection point */
	public double x1;
	public double y1;
	public double x2;
	public double y2;


	/**
	 * Create an object indicates a "wires" crossed
	 * @param p1 endpoint of first segment
	 * @param p2 endpoint of first segment
	 * @param cover pr that is covering
	 * @param u  the intersection point
	 */
	public Under(PositionedRectangle p1, PositionedRectangle p2, 
			PositionedRectangle cover, double x1, double y1, double x2, double y2) {
		super();
		this.p1 = p1;
		this.p2 = p2;
		this.cover = cover;
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;

	}
	
	
	public void draw(Graphics g) {
		g.setColor(Color.magenta);
		g.fillOval((int)(_bounds.x + x1 - 4), (int)(_bounds.y + y1-4), 8, 8);
		g.fillOval((int)(_bounds.x + x2 - 4), (int)(_bounds.y + y2-4), 8, 8);

	}

}
