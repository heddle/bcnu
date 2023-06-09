package cnuphys.ced.cedview.central;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import cnuphys.bCNU.graphics.container.IContainer;
import cnuphys.ced.event.AccumulationManager;
import cnuphys.ced.event.data.AdcHit;
import cnuphys.ced.event.data.AdcList;
import cnuphys.ced.event.data.BST;
import cnuphys.ced.geometry.BSTGeometry;
import cnuphys.ced.geometry.BSTxyPanel;

public class CentralZHitDrawer extends CentralHitDrawer {


	// owner view
	private CentralZView _view;

	public CentralZHitDrawer(CentralZView view) {
		super(view);
		_view = view;
	}

	@Override
	public String getName() {
		return "CentralZHitDrawer";
	}

	@Override
	protected void drawBSTAccumulatedHits(Graphics g, IContainer container) {

		int medianHit = AccumulationManager.getInstance().getMedianFullBSTCount();

		// first index is layer 0..7, second is sector 0..23
		int bstFullData[][][] = AccumulationManager.getInstance().getAccumulatedBSTFullData();
		for (int lay0 = 0; lay0 < 6; lay0++) {
			for (int sect0 = 0; sect0 < BSTGeometry.sectorsPerLayer[lay0]; sect0++) {
				for (int strip0 = 0; strip0 < 256; strip0++) {
					int hitCount = bstFullData[lay0][sect0][strip0];

					if (hitCount > 1) {

						double fract = _view.getMedianSetting() * (((double) hitCount) / (1 + medianHit));

						Color color = AccumulationManager.getInstance().getColor(_view.getColorScaleModel(), fract);
						_view.drawBSTStrip((Graphics2D) g, container, color, sect0 + 1, lay0 + 1, strip0 + 1);
					}

				}
			}
		}
	}

	// draw gemc simulated hits single event mode
	@Override
	protected void drawBSTHitsSingleMode(Graphics g, IContainer container) {

		AdcList hits = BST.getInstance().getADCHits();
		if ((hits != null) && !hits.isEmpty()) {

//			Shape oldClip = g.getClip();
			Graphics2D g2 = (Graphics2D) g;

			for (AdcHit hit : hits) {
				if (hit != null) {
					BSTxyPanel panel = CentralXYView.getPanel(hit.layer, hit.sector);
					if (panel != null) {
						_view.drawBSTStrip(g2, container, Color.red, hit.sector, hit.layer, hit.component);
					} else {
						System.err.println("null BSTZ panel");
					}

				}
			}
		}

	}


}
