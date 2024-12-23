package cnuphys.simanneal.example.layout;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.geom.Point2D;

/**
 * Two connections cross
 * @author heddle
 *
 */
public class Crossing {

	//layout bounds
	protected static Rectangle _bounds = LayoutSimulation.bounds;

	/** one endpoint of first segment */
	public PositionedRectangle p1;

	/** other endpoint of first segment */
	public PositionedRectangle p2;

	/** one endpoint of second segment */
	public PositionedRectangle q1;

	/** other endpoint of second segment */
	public PositionedRectangle q2;

	/** intersection point */
	public double x;
	public double y;

	/**
	 * Create an object indicates a "wires" crossed
	 * @param p1 endpoint of first segment
	 * @param p2 endpoint of first segment
	 * @param q1 endpoint of second segment
	 * @param q2 endpoint of second segment
	 * @param u  the intersection point
	 */
	public Crossing(PositionedRectangle p1, PositionedRectangle p2,
			PositionedRectangle q1, PositionedRectangle q2, double x, double y) {
		super();
		this.p1 = p1;
		this.p2 = p2;
		this.q1 = q1;
		this.q2 = q2;
		this.x = x;
		this.y = y;
	}

	@Override
	public String toString() {

		Point2D.Double dp1 = p1.getPosition();
		Point2D.Double dp2 = p2.getPosition();
		Point2D.Double dq1 = q1.getPosition();
		Point2D.Double dq2 = q2.getPosition();

		String s = String.format("Seg1: [<%d> (%d, %d) to <%d> (%d, %d)] Seg 2 [<%d> (%d, %d), <%d> (%d, %d)] isect: (%d, %d)",
				p1.id, (int) dp1.x, (int) dp1.y,
				p2.id, (int) dp2.x, (int) dp2.y,
				q1.id, (int) dq1.x, (int) dq1.y,
				q2.id, (int) dq2.x, (int) dq2.y,
				(int) x, (int) y);

		return s;
	}

	/**
	 * Returns an endpoint from the first segment that is a singleton.
	 * @return an endpoint that is a singleton.
	 */
	public Singleton getSingleton1() {
		if (p1.isSingleton()) {
			return (Singleton) p1;
		}
		else if (p2.isSingleton()) {
			return (Singleton) p2;
		}
		return null;

	}
	/**
	 * Returns an endpoint from the second segment that is a singleton.
	 * @return an endpoint that is a singleton.
	 */
	public Singleton getSingleton2() {
		if (q1.isSingleton()) {
			return (Singleton) q1;
		}
		else if (q2.isSingleton()) {
			return (Singleton) q2;
		}
		return null;

	}

	public void draw(Graphics g) {
		g.setColor(Color.green);
		g.fillOval((int)(_bounds.x + x - 4), (int)(_bounds.y + y-4), 8, 8);

	}

}
