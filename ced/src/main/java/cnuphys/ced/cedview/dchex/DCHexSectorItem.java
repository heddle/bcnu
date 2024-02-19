package cnuphys.ced.cedview.dchex;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.util.List;

import cnuphys.bCNU.graphics.container.IContainer;
import cnuphys.bCNU.item.ItemList;
import cnuphys.ced.clasio.ClasIoEventManager;
import cnuphys.ced.item.HexSectorItem;

public class DCHexSectorItem extends HexSectorItem {

	
	/**
	 * Get a hex sector item
	 *
	 * @param itemList the item list
	 * @param sector   the 1-based sector
	 */
	public DCHexSectorItem(ItemList itemList, DCHexView view, int sector) {
		super(itemList, view, sector);
	}
	
	/**
	 * Custom drawer for the item.
	 *
	 * @param g         the graphics context.
	 * @param container the graphical container being rendered.
	 */
	@Override
	public void drawItem(Graphics g, IContainer container) {
		if (ClasIoEventManager.getInstance().isAccumulating()) {
			return;
		}

		super.drawItem(g, container);
	}
	
	@Override
	public void getFeedbackStrings(IContainer container, Point pp, Point2D.Double wp, List<String> feedbackStrings) {
		super.getFeedbackStrings(container, pp, wp, feedbackStrings);
	}

}
