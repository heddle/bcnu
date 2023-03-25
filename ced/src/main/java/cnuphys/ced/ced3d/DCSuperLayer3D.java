package cnuphys.ced.ced3d;

import java.awt.Color;

import com.jogamp.opengl.GLAutoDrawable;

import bCNU3D.Support3D;
import cnuphys.ced.event.data.DC;
import cnuphys.ced.event.data.DCTdcHit;
import cnuphys.ced.event.data.lists.DCTdcHitList;
import cnuphys.ced.geometry.DCGeometry;
import cnuphys.lund.X11Colors;

public class DCSuperLayer3D extends DetectorItem3D {

	protected static final Color docaColor = new Color(255, 0, 0, 64);

	private static final boolean frame = true;

	// one based sector [1..6]
	private final int _sector;

	// one based superlayer [1..6]
	private final int _superLayer;

	// the vertices
	private float[] coords = new float[18];

	/**
	 * The owner panel
	 *
	 * @param panel3d
	 * @param sector     one based sector [1..6]
	 * @param superLayer one based superlayer [1..6]
	 */
	public DCSuperLayer3D(PlainPanel3D panel3D, int sector, int superLayer) {
		super(panel3D);
		_sector = sector;
		_superLayer = superLayer;
		DCGeometry.superLayerVertices(_sector, _superLayer, coords);
	}

	@Override
	public void drawShape(GLAutoDrawable drawable) {

		Color outlineColor = X11Colors.getX11Color("wheat", getVolumeAlpha());

		Support3D.drawTriangle(drawable, coords, 0, 1, 2, outlineColor, 1f, frame);
		Support3D.drawQuad(drawable, coords, 1, 4, 3, 0, outlineColor, 1f, frame);
		Support3D.drawQuad(drawable, coords, 0, 3, 5, 2, outlineColor, 1f, frame);
		Support3D.drawQuad(drawable, coords, 1, 4, 5, 2, outlineColor, 1f, frame);
		Support3D.drawTriangle(drawable, coords, 3, 4, 5, outlineColor, 1f, frame);

	}

	@Override
	public void drawData(GLAutoDrawable drawable) {

		float coords[] = new float[6];

		DCTdcHitList hits = DC.getInstance().getTDCHits();
		if ((hits != null) && !hits.isEmpty()) {
			for (DCTdcHit hit : hits) {
				if ((hit.sector == _sector) && (hit.superlayer == _superLayer)) {
					getWire(hit.layer6, hit.wire, coords);
					Support3D.drawLine(drawable, coords, hitColor, WIRELINEWIDTH);
				}
			}
		}


		drawTBData(drawable);
	}

	private void drawTBData(GLAutoDrawable drawable) {

	}

	/**
	 * Get the 1-based sector [1..6]
	 *
	 * @return the 1-based sector [1..6]
	 */
	public int getSector() {
		return _sector;
	}

	/**
	 * Get the 1-based super layer [1..6]
	 *
	 * @return the 1-based super layer [1..6]
	 */
	public int getSuperLayer() {
		return _superLayer;
	}

	private void getWire(int layer, int wire, float coords[]) {
		org.jlab.geom.prim.Line3D dcwire = DCGeometry.getWire(_sector, _superLayer, layer, wire);
		org.jlab.geom.prim.Point3D p0 = dcwire.origin();
		org.jlab.geom.prim.Point3D p1 = dcwire.end();
		coords[0] = (float) p0.x();
		coords[1] = (float) p0.y();
		coords[2] = (float) p0.z();
		coords[3] = (float) p1.x();
		coords[4] = (float) p1.y();
		coords[5] = (float) p1.z();
	}

	// show DCs?
	@Override
	protected boolean show() {
		boolean showdc = _cedPanel3D.showDC();
		return showdc && _cedPanel3D.showSector(_sector);
	}

}
