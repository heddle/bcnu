package cnuphys.ced.ced3d;

import java.awt.Color;

import com.jogamp.opengl.GLAutoDrawable;

import bCNU3D.Support3D;
import cnuphys.ced.alldata.datacontainer.bst.BSTADCData;
import cnuphys.ced.event.data.BSTCrosses;
import cnuphys.ced.event.data.Cosmic;
import cnuphys.ced.event.data.Cosmics;
import cnuphys.ced.event.data.Cross2;
import cnuphys.ced.event.data.lists.CosmicList;
import cnuphys.ced.event.data.lists.CrossList2;
import cnuphys.ced.geometry.BSTGeometry;
import cnuphys.lund.X11Colors;

public class BSTPanel3D extends DetectorItem3D {

	protected static final Color outlineHitColor = new Color(0, 255, 64, 24);

	protected static final float CROSS_LEN = 3f; // in cm
	protected static final Color crossColor = X11Colors.getX11Color("dark green");
	
	//bst adc data
	private BSTADCData _bstADCData = BSTADCData.getInstance();


	// the 1-based sect
	private int _sector;

	// the 1-based "biglayer" [1..8] used by the data
	private int _layer;

	public BSTPanel3D(CedPanel3D panel3D, int sector, int layer) {
		super(panel3D);
		_sector = sector;
		_layer = layer;
	}

	@Override
	public void drawShape(GLAutoDrawable drawable) {
		float coords[] = new float[36];

		BSTGeometry.getLayerQuads(_sector-1, _layer-1, coords);

		Color color = ((_layer % 2) == 0) ? X11Colors.getX11Color("coral", getVolumeAlpha())
				: X11Colors.getX11Color("Powder Blue", getVolumeAlpha());
		Support3D.drawQuads(drawable, coords, color, 1f, true);
	}

	@Override
	public void drawData(GLAutoDrawable drawable) {

		float coords6[] = new float[6];
		float coords36[] = new float[36];
		boolean drawOutline = false;
		
		for(int i = 0; i < _bstADCData.count(); i++ ) {
			if (_bstADCData.sector[i] == _sector && _bstADCData.layer[i] == _layer) {
				drawOutline = true;
				int strip = _bstADCData.component[i];
				BSTGeometry.getStripCM(_sector-1, _layer-1, strip-1, coords6);
				Support3D.drawLine(drawable, coords6, _bstADCData.adc[i] > 0 ? Color.red : Color.blue, STRIPLINEWIDTH);
			}
		}

		if (drawOutline) { // if any hits, draw it once
			BSTGeometry.getLayerQuads(_sector-1, _layer-1, coords36);
			Support3D.drawQuads(drawable, coords36, outlineHitColor, 1f, true);
		}

		// reconstructed crosses?
		if (_cedPanel3D.showReconCrosses()) {
			// BST
			CrossList2 crosses = BSTCrosses.getInstance().getCrosses();
			int len = (crosses == null) ? 0 : crosses.size();
			for (int i = 0; i < len; i++) {
				Cross2 cross = crosses.elementAt(i);
				if (cross != null) {
					// should now be in cm after v 1.0
					float x1 = cross.x;
					float y1 = cross.y;
					float z1 = cross.z;

					Support3D.drawLine(drawable, x1, y1, z1, cross.ux, cross.uy, cross.uz, CROSS_LEN, crossColor, 3f);
					Support3D.drawLine(drawable, x1, y1, z1, cross.ux, cross.uy, cross.uz, (float) (1.1 * CROSS_LEN),
							Color.black, 1f);

					drawCrossPoint(drawable, x1, y1, z1, crossColor);
				}
			} // bst

		}

		// cosmics?
		if (_cedPanel3D.showCosmics()) {
			CosmicList cosmics;
			cosmics = Cosmics.getInstance().getCosmics();

			if ((cosmics != null) && !cosmics.isEmpty()) {
				for (Cosmic cosmic : cosmics) {
					float y1 = 1000;
					float y2 = -1000;
					float x1 = cosmic.trkline_yx_slope * y1 + cosmic.trkline_yx_interc;
					float x2 = cosmic.trkline_yx_slope * y2 + cosmic.trkline_yx_interc;
					float z1 = cosmic.trkline_yz_slope * y1 + cosmic.trkline_yz_interc;
					float z2 = cosmic.trkline_yz_slope * y2 + cosmic.trkline_yz_interc;

					// no longer have to convert to cm?

//					x1 /= 10;
//					x2 /= 10;
//					y1 /= 10;
//					y2 /= 10;
//					z1 /= 10;
//					z2 /= 10;

					Support3D.drawLine(drawable, x1, y1, z1, x2, y2, z2, Color.red, 1f);

				}
			}

		}

		// cosmics

	}

	// show BST?
	@Override
	protected boolean show() {
		switch (_layer) {
		case 1:
			return _cedPanel3D.showBSTLayer1();
		case 2:
			return _cedPanel3D.showBSTLayer2();
		case 3:
			return _cedPanel3D.showBSTLayer3();
		case 4:
			return _cedPanel3D.showBSTLayer4();
		case 5:
			return _cedPanel3D.showBSTLayer5();
		case 6:
			return _cedPanel3D.showBSTLayer6();
		}
		return false;
	}

	// show strip hits?
	protected boolean showHits() {
		return show() && _cedPanel3D.showBSTHits();
	}

}
