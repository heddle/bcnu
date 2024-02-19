package cnuphys.bCNU.item;

import java.awt.Graphics;
import java.awt.geom.Point2D;

import cnuphys.bCNU.graphics.container.IContainer;
import cnuphys.bCNU.graphics.world.WorldGraphicsUtilities;

public class PolylineItem extends PathBasedItem {

	/**
	 * Create a world polyline item
	 *
	 * @param itemList  the list this item is on.
	 * @param points the points of the polygon
	 */
	public PolylineItem(ItemList itemList, Point2D.Double points[]) {
		super(itemList);

		// get the path
		_path = WorldGraphicsUtilities.worldPolygonToPath(points);
		_focus = WorldGraphicsUtilities.getCentroid(_path);

		_style.setFillColor(null);
	}

	/**
	 * Custom drawer for the item.
	 *
	 * @param g         the graphics context.
	 * @param container the graphical container being rendered.
	 */
	@Override
	public void drawItem(Graphics g, IContainer container) {
		_lastDrawnPolygon = WorldGraphicsUtilities.drawPath2D(g, container, _path, _style, false);

	}

	/**
	 * Reshape the polygon based on the modification. Not much we can do to a
	 * polygon except move the selected point. Keep in mind that if control or shift
	 * was pressed, the polygon will scale rather than coming here.
	 */
	@Override
	protected void reshape() {
		int index = _modification.getSelectIndex();
		Point2D.Double[] wpoly = WorldGraphicsUtilities.pathToWorldPolygon(_path);
		Point2D.Double wp = _modification.getCurrentWorldPoint();
		wpoly[index] = wp;
		_path = WorldGraphicsUtilities.worldPolygonToPath(wpoly);
		updateFocus();
	}

}
