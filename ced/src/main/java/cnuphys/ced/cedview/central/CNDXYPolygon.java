package cnuphys.ced.cedview.central;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.awt.geom.Point2D;
import java.util.List;

import org.jlab.geom.component.ScintillatorPaddle;

import cnuphys.bCNU.graphics.container.IContainer;
import cnuphys.bCNU.graphics.world.WorldGraphicsUtilities;
import cnuphys.bCNU.util.Fonts;
import cnuphys.bCNU.util.X11Colors;
import cnuphys.ced.alldata.DataWarehouse;
import cnuphys.ced.cedview.CedView;
import cnuphys.ced.cedview.CedXYView;
import cnuphys.ced.event.AccumulationManager;
import cnuphys.ced.geometry.CNDGeometry;

@SuppressWarnings("serial")
public class CNDXYPolygon extends Polygon {

	//work points
	private Point2D.Double wp[] = new Point2D.Double[4];
	private Point pp = new Point();


	//the data warehouse
	private DataWarehouse _dataWarehouse = DataWarehouse.getInstance();

	/**
	 * The layer, 1..3
	 */
	public int layer;

	/**
	 * The paddleId 1..48
	 */
	public int paddleId;

	private static final Color _navy = X11Colors.getX11Color("navy");
	private static final Color _powder = X11Colors.getX11Color("powder blue");

	private static Font _font = Fonts.hugeFont;

	// "REAL" numbering
	int sector; // 1..24
	int _leftRight; // 1..2

	private ScintillatorPaddle paddle;

	/**
	 * Create a XY Polygon for the CND
	 *
	 * @param layer    the layer 1..3
	 * @param paddleId the paddle ID 1..48
	 */
	public CNDXYPolygon(int layer, int paddleId) {
		this.layer = layer;
		this.paddleId = paddleId;
		paddle = CNDGeometry.getPaddle(layer, paddleId);

		int real[] = new int[3];
		int geo[] = { 1, layer, paddleId };
		CNDGeometry.geoTripletToRealTriplet(geo, real);

		sector = real[0];
		_leftRight = real[2];
	}

	/**
	 * Draw the polygon
	 *
	 * @param g         the graphics object
	 * @param container the drawing container
	 */
	public void draw(Graphics g, IContainer container) {
		draw(g, container, CedXYView.LIGHT, Color.black);
	}

	/**
	 * Draw the polygon
	 *
	 * @param g         the graphics object
	 * @param container the drawing container
	 */
	public void draw(Graphics g, IContainer container, Color fillColor, Color lineColor) {
		Graphics2D g2 = (Graphics2D)g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		reset();

		for (int i = 0; i < 4; i++) {
			// convert cm to mm
			wp[i] = new Point2D.Double(10 * paddle.getVolumePoint(i).x(), 10 * paddle.getVolumePoint(i).y());
			container.worldToLocal(pp, wp[i]);

			addPoint(pp.x, pp.y);
		}

		if (fillColor != null) {
			g.setColor(fillColor);
			g.fillPolygon(this);
		}
		g.setColor(lineColor);
		g.drawPolygon(this);

		if ((_leftRight == 1) && (layer == 2)) {
			Point2D.Double centroid = WorldGraphicsUtilities.getCentroid(wp);
			container.worldToLocal(pp, centroid);
			g.setColor(_powder);
			g.setFont(_font);
			g.drawString("" + sector, pp.x - 6, pp.y + 6);
			g.setColor(_navy);
			g.drawString("" + sector, pp.x - 5, pp.y + 7);

		}

	}

	/**
	 * Get the feedback strings
	 *
	 * @param container       the cdrawing container
	 * @param screenPoint     the mouse location
	 * @param worldPoint      the corresponding world point
	 * @param feedbackStrings where to add the strings
	 * @return true
	 */
	public boolean getFeedbackStrings(IContainer container, Point screenPoint, Point2D.Double worldPoint,
			List<String> feedbackStrings) {

		if (!contains(screenPoint)) {
			return false;
		}

		fbString("cyan", "cnd sect " + sector + " layer " + layer + (_leftRight == 1 ? " [left]" : " [right]"),
				feedbackStrings);

		CedView view = (CedView) (container.getView());

		if (view.isSingleEventMode()) {

			int adcCount = _dataWarehouse.rows("CND::adc");
			int tdcCount = _dataWarehouse.rows("CND::tdc");

			// adc?
			if (adcCount > 0) {

				byte sect[] = _dataWarehouse.getByte("CND::adc", "sector");
				byte lay[] = _dataWarehouse.getByte("CND::adc", "layer");
				byte order[] = _dataWarehouse.getByte("CND::adc", "order");
				short ped[] = _dataWarehouse.getShort("CND::adc", "ped");
				int adc[] = _dataWarehouse.getInt("CND::adc", "ADC");
				float time[] = _dataWarehouse.getFloat("CND::adc", "time");

				for (int i = 0; i < adcCount; i++) {
					int hsect = sect[i];
					int hlayer = lay[i];
					int hleftright = 1 + (order[i] % 2);

					if ((sector == hsect) && (layer == hlayer) && (_leftRight == hleftright)) {
						fbString("cyan", "cnd adc " + adc[i], feedbackStrings);
						fbString("cyan", "cnd ped " + ped[i], feedbackStrings);

						String timeStr = String.format("cnd time %-6.1f", time[i]);
						fbString("cyan", timeStr, feedbackStrings);
					}
				}
			}

			// tdc?
			if (tdcCount > 0) {

				byte sect[] = _dataWarehouse.getByte("CND::tdc", "sector");
				byte lay[] = _dataWarehouse.getByte("CND::tdc", "layer");
				byte order[] = _dataWarehouse.getByte("CND::tdc", "order");
                int tdc[] = _dataWarehouse.getInt("CND::tdc", "TDC");

				for (int i = 0; i < tdcCount; i++) {
					int hsect = sect[i];
					int hlayer = lay[i];
					int hleftright = 1 + (order[i] % 2);

					if ((sector == hsect) && (layer == hlayer) && (_leftRight == hleftright)) {
						fbString("cyan", "cnd tdc " + tdc[i], feedbackStrings);
					}
				}
			}
		} else { // accumulated

			int[][][] cndAccumData = AccumulationManager.getInstance().getAccumulatedCNDData();
			int count = cndAccumData[sector - 1][layer - 1][_leftRight - 1];
			fbString("cyan", "accumulated count " + count, feedbackStrings); // TODO FINISH
		}

		return true;
	}

	// convenience method to create a feedback string
	private void fbString(String color, String str, List<String> fbstrs) {
		fbstrs.add("$" + color + "$" + str);
	}

}
