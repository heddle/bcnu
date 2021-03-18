package cnuphys.bCNU.simanneal.example.layout;

import java.awt.Color;
import java.awt.Graphics;

/**
 * A single icon not in a box
 * @author heddle
 *
 */
public class Singleton extends PositionedRectangle {
	public Singleton() {
		width  = _size;
		height = _size;
	}
	
	/**
	 * copy constructor
	 * @param srcSing the source
	 */
	private Singleton(Singleton srcSing) {
		x = srcSing.x;
		y = srcSing.y;
		width = srcSing.width;
		height = srcSing.height;
		id = srcSing.id;
	}
	
	@Override
	public PositionedRectangle copy() {
		return new Singleton(this);
	}


	/**
	 * Draw the singleton
	 * @param g the graphics context
	 */
	public void draw(Graphics g) {
		g.setColor(Color.cyan);
		g.fillOval(_bounds.x + x, _bounds.y + y, width, height);
		g.setColor(Color.blue);
		g.drawOval(_bounds.x + x, _bounds.y + y, width, height);
		
		g.setColor(Color.black);
		g.drawString("" + id, (int)getCenterX(), (int)getCenterY());

	}
	

}
