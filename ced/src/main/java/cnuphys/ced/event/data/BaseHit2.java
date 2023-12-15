package cnuphys.ced.event.data;

import java.awt.Point;

public class BaseHit2 {

	// used for mouse over feedback
	private Point _screenLocation = new Point();

	/** The 1-based sector */
	public byte sector;

	/** The 1-based layer */
	public byte layer;

	/** The 1-based component */
	public int component;

	/**
	 * @param sector    the 1-based sector
	 * @param layer     the 1-based layer
	 * @param component the 1-based component
	 */
	public BaseHit2(byte sector, byte layer, int component) {
		super();
		this.sector = sector;
		this.layer = layer;
		this.component = component;
	}

	/**
	 * Get the hit location where it was last drawn
	 *
	 * @return the screen location
	 */
	public Point getLocation() {
		return _screenLocation;
	}

	/**
	 * For feedback
	 *
	 * @param pp
	 */
	public void setLocation(Point pp) {
		_screenLocation.x = pp.x;
		_screenLocation.y = pp.y;
	}

	/**
	 * Does the hit contain the mouse point?
	 * @param pp the mouse point
	 * @return true if the hit contains the mouse point
	 */
	public boolean contains(Point pp) {
		return ((Math.abs(_screenLocation.x - pp.x) <= DataDrawSupport.HITHALF)
				&& (Math.abs(_screenLocation.y - pp.y) <= DataDrawSupport.HITHALF));
	}
	
	@Override
	public String toString() {
		return String.format("Base Hit sector = %d  layer = %d  component = %d", sector, layer, component);
	}

}
