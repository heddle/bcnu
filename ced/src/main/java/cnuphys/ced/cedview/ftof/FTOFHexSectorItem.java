package cnuphys.ced.cedview.ftof;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.util.List;

import cnuphys.bCNU.graphics.container.IContainer;
import cnuphys.bCNU.item.ItemList;
import cnuphys.ced.clasio.ClasIoEventManager;
import cnuphys.ced.item.HexSectorItem;

public class FTOFHexSectorItem extends HexSectorItem {

	private FTOFView _view;

	/**
	 * Get a hex sector item
	 *
	 * @param itemList  the item list
	 * @param sector the 1-based sector
	 */
	public FTOFHexSectorItem(ItemList itemList, FTOFView view, int sector) {
		super(itemList, view, sector);
		_view = view;
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


		for (int stripType = 0; stripType < 3; stripType++) {
		}

		drawOutlines(g, container, Color.lightGray);

		drawFTOFHits(g, container);


	}

	// draw strip outlines
	private void drawOutlines(Graphics g, IContainer container, Color color) {
	}

	// draw the hits
	private void drawFTOFHits(Graphics g, IContainer container) {
		if (_view.isSingleEventMode()) {
			drawSingleEvent(g, container);
		} else {
			drawAccumulatedHits(g, container);
		}
	}

	// draw single event hit
	private void drawSingleEvent(Graphics g, IContainer container) {
	}

	// draw accumulated hits
	private void drawAccumulatedHits(Graphics g, IContainer container) {
	}

	@Override
	public void getFeedbackStrings(IContainer container, Point pp, Point2D.Double wp, List<String> feedbackStrings) {

		if (contains(container, pp)) {

		}
	}


}
