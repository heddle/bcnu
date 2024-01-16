package cnuphys.ced.cedview.central;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.geom.Point2D;

import cnuphys.bCNU.graphics.container.IContainer;
import cnuphys.ced.event.AccumulationManager;
import cnuphys.ced.event.data.AdcColorScale;
import cnuphys.ced.event.data.AdcHit;
import cnuphys.ced.event.data.AdcList;
import cnuphys.ced.event.data.BMT;
import cnuphys.ced.event.data.BST;
import cnuphys.ced.event.data.BaseHit2;
import cnuphys.ced.event.data.DataDrawSupport;
import cnuphys.ced.event.data.TdcAdcTOFHit;
import cnuphys.ced.event.data.lists.BaseHit2List;
import cnuphys.ced.event.data.lists.TdcAdcTOFHitList;
import cnuphys.ced.geometry.BMTGeometry;
import cnuphys.ced.geometry.BSTGeometry;
import cnuphys.ced.geometry.BSTxyPanel;
import cnuphys.ced.geometry.bmt.BMTSectorItem;
import cnuphys.splot.plot.X11Colors;
import eu.mihosoft.vrl.v3d.Vector3d;

public class CentralXYHitDrawer extends CentralHitDrawer {

	private static Color _baseColor = new Color(255, 0, 0, 60);
	
	//common adc color scale
	private AdcColorScale _adcColorScale = AdcColorScale.getInstance();

	// owner view
	private CentralXYView _view;

	public CentralXYHitDrawer(CentralXYView view) {
		super(view);
		_view = view;
	}

	@Override
	public String getName() {
		return "CentralXYHitDrawer";
	}

	// draw accumulated BST hits (panels)
	@Override
	protected void drawCNDAccumulatedHits(Graphics g, IContainer container) {
		int medianHit = AccumulationManager.getInstance().getMedianCNDCount();

		int cndData[][][] = AccumulationManager.getInstance().getAccumulatedCNDData();

		for (int sect0 = 0; sect0 < 24; sect0++) {
			for (int lay0 = 0; lay0 < 3; lay0++) {
				for (int order = 0; order < 2; order++) {

					int hitCount = cndData[sect0][lay0][order];

					CNDXYPolygon poly = _view.getCNDPolygon(sect0 + 1, lay0 + 1, order + 1);
					double fract = _view.getMedianSetting() * (((double) hitCount) / (1 + medianHit));
					Color color = AccumulationManager.getInstance().getColor(_view.getColorScaleModel(), fract);

					poly.draw(g, container, color, Color.black);

				}
			}
		}
	}

	// draw accumulated BST hits (panels)
	@Override
	protected void drawBSTAccumulatedHits(Graphics g, IContainer container) {
		// panels

		int medianHit = AccumulationManager.getInstance().getMedianBSTCount();

		// first index is layer 0..5, second is sector 0..23
		int bstData[][] = AccumulationManager.getInstance().getAccumulatedBSTData();

		for (int lay0 = 0; lay0 < 6; lay0++) {
			for (int sect0 = 0; sect0 < BSTGeometry.sectorsPerLayer[lay0]; sect0++) {
				BSTxyPanel panel = CentralXYView.getPanel(lay0 + 1, sect0 + 1);

				if (panel != null) {
					int hitCount = bstData[lay0][sect0];

					double fract = _view.getMedianSetting() * (((double) hitCount) / (1 + medianHit));
					Color color = AccumulationManager.getInstance().getColor(_view.getColorScaleModel(), fract);
					_view.drawBSTPanel((Graphics2D) g, container, panel, color);

				}
			}
		}
	}

	// draw CTOF accumulated hits
	@Override
	protected void drawCTOFAccumulatedHits(Graphics g, IContainer container) {
		
		//sector and layer always == 1 only worry about component
		int medianHit = AccumulationManager.getInstance().getMedianCTOFCount();

		int ctofData[] = AccumulationManager.getInstance().getAccumulatedCTOFData();

		for (int index = 0; index < 48; index++) {
			CTOFXYPolygon poly = _view.getCTOFPolygon(index + 1);
			if (poly != null) {
				int hitCount = ctofData[index];

				double fract = _view.getMedianSetting() * (((double) hitCount) / (1 + medianHit));

				Color color = AccumulationManager.getInstance().getColor(_view.getColorScaleModel(), fract);

				poly.draw(g, container, index + 1, color);
			}
		}
	}

	// only called in single event mode
	@Override
	protected void drawHitsSingleMode(Graphics g, IContainer container) {
		drawBSTHitsSingleMode(g, container);
		drawBMTHitsSingleMode(g, container);
		drawCTOFSingleHitsMode(g, container);
		drawCNDSingleHitsMode(g, container);
		drawCVTP1Traj(g, container);
		drawCVTRecTraj(g, container);
	}


	// draw CTOF hits
	@Override
	protected void drawCNDSingleHitsMode(Graphics g, IContainer container) {

		int adcCount = _dataWarehouse.rows("CND::adc");
		int tdcCount = _dataWarehouse.rows("CND::tdc");

		// tdc?
		if (tdcCount > 0) {
			
			byte sect[] = _dataWarehouse.getByte("CND::tdc", "sector");
			byte layer[] = _dataWarehouse.getByte("CND::tdc", "layer");
			byte order[] = _dataWarehouse.getByte("CND::tdc", "order");
			
			for (int i = 0; i < tdcCount; i++) {
				int hsect = sect[i];
				int hlayer = layer[i];
				int hleftright = 1 + (order[i] % 2);

				CNDXYPolygon poly = _view.getCNDPolygon(hsect, hlayer, hleftright);

				poly.draw(g, container, Color.lightGray, Color.black);
			}
		}

		// adc?
		if (adcCount > 0) {
			
			byte sect[] = _dataWarehouse.getByte("CND::adc", "sector");
			byte layer[] = _dataWarehouse.getByte("CND::adc", "layer");
			byte order[] = _dataWarehouse.getByte("CND::adc", "order");

			for (int i = 0; i < adcCount; i++) {
				byte hsect = sect[i];
				byte hlayer = layer[i];
				int hleftright = 1 + (order[i] % 2);

				CNDXYPolygon poly = _view.getCNDPolygon(hsect, hlayer, hleftright);

				Color color = _adcColorScale.getCNDADCColor(hsect, hlayer, order[i]);
				poly.draw(g, container, color, Color.black);

			}
		}

		// tdc again?
		if (tdcCount > 0) {
			
			byte sect[] = _dataWarehouse.getByte("CND::tdc", "sector");
			byte layer[] = _dataWarehouse.getByte("CND::tdc", "layer");
			byte order[] = _dataWarehouse.getByte("CND::tdc", "order");

			for (int i = 0; i < tdcCount; i++) {
				int hsect = sect[i];
				int hlayer = layer[i];
				int hleftright = 1 + (order[i] % 2);

				CNDXYPolygon poly = _view.getCNDPolygon(hsect, hlayer, hleftright);

				g.setColor(Color.black);
				g.drawLine(poly.xpoints[0], poly.ypoints[0], poly.xpoints[2], poly.ypoints[2]);
				g.drawLine(poly.xpoints[1], poly.ypoints[1], poly.xpoints[3], poly.ypoints[3]);
			}
		}

	}
	

	// draw CTOF hits
	@Override
	protected void drawCTOFSingleHitsMode(Graphics g, IContainer container) {
		
		short comp[] = _dataWarehouse.getShort("CTOF::hits", "component");

		if (comp != null) {
			for (int i = 0; i < comp.length; i++) {
				short component = comp[i];
				CTOFXYPolygon poly = _view.getCTOFPolygon(component);
				if (poly != null) {
					Color color = _adcColorScale.getCTOFADCColor(component);
					poly.draw(g, container,component, color);
				}
			}
		}

	}

	// draw BMT hits
	@Override
	protected void drawBMTHitsSingleMode(Graphics g, IContainer container) {
		drawBMTADCData(g, container);
		drawBMTReconHits(g, container);
	}

	//draw BMT adc data
	private void drawBMTADCData(Graphics g, IContainer container) {
		if (_view.showADCHits()) {

			Point pp = new Point();
			Point2D.Double wp = new Point2D.Double();

			AdcList hits = BMT.getInstance().getADCHits();
			if ((hits != null) && !hits.isEmpty()) {

				for (AdcHit hit : hits) {
					if (hit != null) {
						BMTSectorItem bmtItem = _view.getBMTSectorItem(hit.sector, hit.layer);
						if (bmtItem.getLastDrawnPolygon() != null) {
							g.setColor(X11Colors.getX11Color("tan"));
							g.fillPolygon(bmtItem.getLastDrawnPolygon());
							g.setColor(Color.red);
							g.drawPolygon(bmtItem.getLastDrawnPolygon());
						}
						Polygon poly = bmtItem.getStripPolygon(container, hit.component);
						if (poly != null) {
							g.setColor(Color.black);
							g.fillPolygon(poly);
							g.setColor(Color.yellow);
							g.drawPolygon(poly);

						}

						if (bmtItem.isZLayer()) {

							Color color = hits.adcColor(hit);

							double phi = BMTGeometry.getGeometry().CRZStrip_GetPhi(hit.sector, hit.layer, hit.component);

							double rad = bmtItem.getInnerRadius() + BMTSectorItem.FAKEWIDTH / 2.;
							wp.x = rad * Math.cos(phi);
							wp.y = rad * Math.sin(phi);

							container.worldToLocal(pp, wp);
							hit.setLocation(pp);
							DataDrawSupport.drawAdcHit(g, pp, color);
						}


					}
				}

			}
		}
	}

	// draw bmt reconstructed hits
	private void drawBMTReconHits(Graphics g, IContainer container) {

		if (_view.showReconHits()) {
			Point pp = new Point();
			Point2D.Double wp = new Point2D.Double();

	//		DataManager.getInstance().

			BaseHit2List recHits = BMT.getInstance().getRecHits();
			if (recHits != null) {

				if (recHits.count() > 0) {
					for (BaseHit2 bhit2 : recHits) {
						BMTSectorItem bmtItem = _view.getBMTSectorItem(bhit2.sector, bhit2.layer);
						if (bmtItem != null && bmtItem.isZLayer()) {

							double phi = BMTGeometry.getGeometry().CRZStrip_GetPhi(bhit2.sector, bhit2.layer,
									bhit2.component);

							double rad = bmtItem.getInnerRadius() + BMTSectorItem.FAKEWIDTH / 2.;
							wp.x = rad * Math.cos(phi);
							wp.y = rad * Math.sin(phi);
							container.worldToLocal(pp, wp);

							bhit2.setLocation(pp);
							DataDrawSupport.drawReconHit(g, pp);
						}
					}
				}
			}
		}

	}

	// draw BST hits single event mode
	@Override
	protected void drawBSTHitsSingleMode(Graphics g, IContainer container) {
		drawBSTADCData(g, container);
		drawBSTReconHits(g, container);
	}

	//draw BST adc data
	private void drawBSTADCData(Graphics g, IContainer container) {
		if (_view.showADCHits()) {
			AdcList hits = BST.getInstance().getADCHits();
			if ((hits != null) && !hits.isEmpty()) {
				Graphics2D g2 = (Graphics2D) g;

				for (AdcHit hit : hits) {
					if (hit != null) {

						BSTxyPanel panel = CentralXYView.getPanel(hit.layer, hit.sector);

						if (panel != null) {
							_view.drawBSTPanel(g2, container, panel, _baseColor);
							_view.drawBSTPanel(g2, container, panel, hits.adcColor(hit));
						}

					}
				}

			}
		}
	}


	// draw bst reconstructed hits
	private void drawBSTReconHits(Graphics g, IContainer container) {
		if (_view.showReconHits()) {
			Point pp = new Point();
			Point2D.Double wp = new Point2D.Double();

			BaseHit2List recHits = BST.getInstance().getRecHits();
			
			if (recHits != null) {

				for (BaseHit2 bhit2 : recHits) {
					if (bhit2.sector > 0 && bhit2.layer > 0 && bhit2.component > 0 && bhit2.layer < 7
							&& bhit2.component < 257 && (bhit2.sector <= BSTGeometry.sectorsPerLayer[bhit2.layer - 1])) {
						BSTGeometry.getStripMidpointXY(bhit2.sector - 1, bhit2.layer - 1, bhit2.component - 1, wp);
						container.worldToLocal(pp, wp);
						bhit2.setLocation(pp);
						DataDrawSupport.drawReconHit(g, pp);

					} else {
	//					System.err.println("bad data in CentralXYHitDrawer drawBSTReconHits   " + bhit2);
					}

				}

			}
		}
	}

}
