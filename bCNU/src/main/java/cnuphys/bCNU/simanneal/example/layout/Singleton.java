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
	
	public void draw(Graphics g) {
		g.setColor(Color.cyan);
		g.fillOval(_bounds.x + x, _bounds.y + y, width, height);
		g.setColor(Color.blue);
		g.drawOval(_bounds.x + x, _bounds.y + y, width, height);

	}
	

}
