package cnuphys.ced.geometry.alert;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.geom.Point2D;
import java.util.Collection;
import java.util.Hashtable;

import org.jlab.detector.calib.utils.DatabaseConstantProvider;
import org.jlab.geom.component.ScintillatorPaddle;
import org.jlab.geom.detector.alert.AHDC.AlertDCDetector;
import org.jlab.geom.detector.alert.AHDC.AlertDCFactory;
import org.jlab.geom.detector.alert.ATOF.AlertTOFDetector;
import org.jlab.geom.detector.alert.ATOF.AlertTOFFactory;
import org.jlab.geom.prim.Point3D;
import org.jlab.logging.DefaultLogger;

import cnuphys.bCNU.graphics.container.IContainer;
import cnuphys.bCNU.util.Fonts;
import cnuphys.ced.cedview.CedView;
import cnuphys.ced.frame.Ced;

public class AlertGeometry {

	// the name of the detector
	public static String NAME = "ALERT";

	//the layer objects used for DC drawing
	private static Hashtable<String, DCLayer> _dcLayers = new Hashtable<>();

	//the layer objects used for TOF drawing
	private static Hashtable<String, TOFLayer> _tofLayers = new Hashtable<>();

	//sector boundaries for XY view
	//there are 1 sectors
	public static Point2D.Double tofSectorXY[][] = new Point2D.Double[15][16];

	private static Color[] sectFill = {new Color(255, 0, 0, 10), new Color(0, 255, 0, 10), new Color(0, 0, 255, 10)};


	/**
	 * Init the uRwell geometry
	 */
	public static void initialize() {
		System.out.println("\n=======================================");
		System.out.println("===  " + NAME + " Geometry Initialization ===");
		System.out.println("=======================================");

		String variationName = Ced.getGeometryVariation();
		DatabaseConstantProvider cp = new DatabaseConstantProvider(11, variationName);

		initializeDC(cp);
		initializeTOF(cp);

	}

	// init the drift chambers
	private static void initializeDC(DatabaseConstantProvider cp) {

		AlertDCFactory dcFactory = new AlertDCFactory();
		AlertDCDetector dcCLASDetector = dcFactory.createDetectorCLAS(cp);

		int numsect = dcCLASDetector.getNumSectors();
		for (int sect = 0; sect < numsect; sect++) {
			int numsupl = dcFactory.createSector(cp, sect).getNumSuperlayers();
			for (int superlayer = 0; superlayer < numsupl; superlayer++) {
				int numlay = dcFactory.createSuperlayer(cp, sect, superlayer).getNumLayers();
				for (int layer = 0; layer < numlay; layer++) {
					DCLayer dcLayer = new DCLayer(dcFactory.createLayer(cp, sect, superlayer, layer));
					_dcLayers.put(hash(sect, superlayer, layer), dcLayer);
				}
			}
		}


	}

	// init the time of flight
	private static void initializeTOF(DatabaseConstantProvider cp) {

		AlertTOFFactory tofFactory = new AlertTOFFactory();
		AlertTOFDetector tofCLASDetector = tofFactory.createDetectorCLAS(cp);

		int numsect = tofCLASDetector.getNumSectors();
		for (int sect = 0; sect < numsect; sect++) {
			int numsupl = tofFactory.createSector(cp, sect).getNumSuperlayers();
			for (int superlayer = 0; superlayer < numsupl; superlayer++) {
				int numlay = tofFactory.createSuperlayer(cp, sect, superlayer).getNumLayers();



				for (int layer = 0; layer < numlay; layer++) {
					TOFLayer tofLayer = new TOFLayer(tofFactory.createLayer(cp, sect, superlayer, layer));
					_tofLayers.put(hash(sect, superlayer, layer), tofLayer);
				}
			}
		}

		//get the sector boundries
		// and tofSectorLabelPoint

		for (int sect = 0; sect < 15; sect++) {
			ScintillatorPaddle p0  = getPaddle(sect, 0, 0, 0);
			ScintillatorPaddle p1  = getPaddle(sect, 0, 0, 1);
			ScintillatorPaddle p2  = getPaddle(sect, 0, 0, 2);
			ScintillatorPaddle p3  = getPaddle(sect, 0, 0, 3);
			ScintillatorPaddle p4  = getPaddle(sect, 1, 0, 3);
			ScintillatorPaddle p5  = getPaddle(sect, 1, 0, 2);
			ScintillatorPaddle p6  = getPaddle(sect, 1, 0, 1);
			ScintillatorPaddle p7  = getPaddle(sect, 1, 0, 0);

			tofSectorXY[sect][0] = getCorner(p0, 0);
			tofSectorXY[sect][1] = getCorner(p0, 3);
			tofSectorXY[sect][2] = getCorner(p1, 0);
			tofSectorXY[sect][3] = getCorner(p1, 3);
			tofSectorXY[sect][4] = getCorner(p2, 0);
			tofSectorXY[sect][5] = getCorner(p2, 3);
			tofSectorXY[sect][6] = getCorner(p3, 0);
			tofSectorXY[sect][7] = getCorner(p3, 3);
			tofSectorXY[sect][8] = getCorner(p4, 2);
			tofSectorXY[sect][9] = getCorner(p4, 1);
			tofSectorXY[sect][10] = getCorner(p5, 2);
			tofSectorXY[sect][11] = getCorner(p5, 1);
			tofSectorXY[sect][12] = getCorner(p6, 2);
			tofSectorXY[sect][13] = getCorner(p6, 1);
			tofSectorXY[sect][14] = getCorner(p7, 2);
			tofSectorXY[sect][15] = getCorner(p7, 1);
		}

	}

	/**
	 * Draw the TOF sector outlines
	 * @param g the graphics context
	 * @param container the conyainer
	 */
	public static void drawTOFSectorOutlines(Graphics g, IContainer container) {

		Point pp =new Point();
		Polygon poly = new Polygon();
		g.setFont(Fonts.hugeFont);
		for (int sect = 0; sect < 15; sect++) {
			poly.reset();
			for (int index = 0; index < 16; index++) {
				container.worldToLocal(pp, tofSectorXY[sect][index]);
				poly.addPoint(pp.x, pp.y);

			}

			g.setColor(sectFill[sect%3]);
			g.fillPolygon(poly);
			g.setColor(Color.black);
			g.drawPolygon(poly);

			Point2D.Double anchor = tofSectorXY[sect][11];
			//drawTextAtLineEnd(Graphics g, IContainer container, String s, Font font, Color color, Point2D.Double end,  boolean rotateText)
			CedView.drawTextAtLineEnd(g, container, "" + (sect+1), Fonts.hugeFont, anchor);
		}

	}

	public static Point2D.Double getCorner(ScintillatorPaddle paddle, int corner) {
		Point3D p3d = paddle.getVolumePoint(corner);
		return new Point2D.Double(p3d.x(), p3d.y());
	}


	/**
	 * Get all the DC layers
	 * @return the collection of DC layers
	 */
	public static Collection<DCLayer> getAllDCLayers() {
		return _dcLayers.values();
	}

	/**
	 * Get all the TOF layers
	 * @return the collection of TOF layers
	 */
	public static Collection<TOFLayer> getAllTOFLayers() {
		return _tofLayers.values();
	}

	/**
	 * Get the scintillator paddle
	 * @param sector 0 based
	 * @param superlayer 0 based
	 * @param layer 0 based
	 * @param paddle 0 based
	 * @return the scintillator paddle
	 */
	public static ScintillatorPaddle getPaddle(int sector, int superlayer, int layer, int paddle) {
		TOFLayer tof = _tofLayers.get(hash(sector, superlayer, layer));

		if (tof == null) {
			return null;
		}

		return tof.getPaddle(paddle);
	}

	private static String hash(int sector, int superlayer, int layer) {
		return String.format("%d|%d|%d", sector, superlayer, layer);
	}

	public static void main(String[] arg) {
		// this is supposed to create less pounding of ccdb
		DefaultLogger.initialize();

		initialize();

		System.out.println("done");

	}

}
