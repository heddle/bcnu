package cnuphys.bCNU.simanneal.example.layout;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.geom.Point2D;

public class Connection {
	
	//layout bounds
	protected static Rectangle _bounds = LayoutSimulation.bounds;

	/** one connected pr */
	public PositionedRectangle pr1;
	
	/** other connected pr */
	public PositionedRectangle pr2;
	
	public Connection(PositionedRectangle pr1, PositionedRectangle pr2) {
		this.pr1 = pr1;
		this.pr2 = pr2;
	
	}
	
	public void draw(Graphics g) {
		

		if (pr1.isSingleton() && pr2.isSingleton()) {
			g.setColor(Color.red);
			
		}
		else {
			g.setColor(Color.black);
	}
		
		
		Point2D.Double p1 = pr1.getPosition();
		Point2D.Double p2 = pr2.getPosition();
		
		int x1 = _bounds.x + (int)(p1.x);
		int y1 = _bounds.y + (int)(p1.y);
		
		int x2 = _bounds.x + (int)(p2.x);
		int y2 = _bounds.y + (int)(p2.y);
		
		g.drawLine(x1, y1, x2, y2);
	}
	


}
