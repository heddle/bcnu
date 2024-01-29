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
import cnuphys.ced.cedview.CedView;
import cnuphys.ced.cedview.CedXYView;
import cnuphys.ced.event.AccumulationManager;
import cnuphys.ced.event.data.arrays.adc.LR_ADCArrays;
import cnuphys.ced.event.data.arrays.tdc.TDCArrays;
import cnuphys.ced.geometry.CNDGeometry;

@SuppressWarnings("serial")
public class CNDXYPolygon extends Polygon {

	//work points
	private Point2D.Double wp[] = new Point2D.Double[4];
	private Point pp = new Point();

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

		byte order;

		if (view.isSingleEventMode()) {
			
			

			LR_ADCArrays adcArrays = LR_ADCArrays.getArrays("CND::adc");
			if (adcArrays.hasData()) {
				order = (byte) (_leftRight-1); //0 or 1
				adcArrays.addFeedback((byte) sector, (byte) layer, (short)1, order, feedbackStrings);
			}

			TDCArrays tdcArrays = TDCArrays.getArrays("CND::tdc");
			if (tdcArrays.hasData()) {
				order = (byte) (_leftRight+1); //2 or 3
				tdcArrays.addFeedback((byte) sector, (byte) layer, (short)1, order, feedbackStrings);
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
