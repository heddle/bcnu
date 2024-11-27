package chimera.grid.mapping;

import java.awt.Graphics;
import java.awt.geom.Point2D;

import cnuphys.bCNU.graphics.container.IContainer;

public interface IMapProjection {
	
	/**
	 * Convert from lat/lon to x/y
	 * 
	 * @param latLon the lat/lon point
	 * @param xy     the x/y point
	 */
	public void latLonToXY(Point2D.Double latLon, Point2D.Double xy);
	
	/**
	 * Convert from x/y to lat/lon
	 * 
	 * @param latLon the lat/lon point
	 * @param xy     the x/y point
	 */
	public void latLonFromXY(Point2D.Double latLon, Point2D.Double xy);
	
	/**
	 * Draw the map outline
	 * 
	 * @param g         the graphics context
	 * @param container the {@link cnuphys.bCNU.graphics.container.IContainer
	 *                  IContainer}
	 */
	public void drawMapOutline(Graphics g, IContainer container);
	
	
	/**
	 * Check if a given xy point is on the map
	 * @param xy the point to check
	 * @return <code>true</code> if the point is on the map
	 */
	public boolean isPointOnMap(Point2D.Double xy);

}
