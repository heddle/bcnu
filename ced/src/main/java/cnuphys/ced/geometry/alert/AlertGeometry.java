package cnuphys.ced.geometry.alert;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.geom.Point2D;
import java.util.Collection;
import java.util.Hashtable;
import java.util.List;

import org.jlab.detector.calib.utils.DatabaseConstantProvider;
import org.jlab.geom.component.ScintillatorPaddle;
import org.jlab.geom.detector.alert.AHDC.AlertDCDetector;
import org.jlab.geom.detector.alert.AHDC.AlertDCFactory;
import org.jlab.geom.detector.alert.ATOF.AlertTOFDetector;
import org.jlab.geom.detector.alert.ATOF.AlertTOFFactory;
import org.jlab.geom.detector.alert.ATOF.AlertTOFLayer;
import org.jlab.geom.prim.Point3D;
import org.jlab.logging.DefaultLogger;

import cnuphys.bCNU.graphics.GraphicsUtilities;
import cnuphys.bCNU.graphics.container.IContainer;
import cnuphys.bCNU.util.Fonts;
import cnuphys.ced.frame.Ced;

public class AlertGeometry {
	
	//for debugging
	private static boolean _debug = false;

	// the name of the detector
	public static String NAME = "ALERT";

	//the layer objects used for DC drawing
	private static Hashtable<String, DCLayer> _dcLayers = new Hashtable<>();

	//the layer objects used for TOF drawing
	private static Hashtable<String, TOFLayer> _tofLayers = new Hashtable<>();

	//sector boundaries for XY view
	//there are 1 sectors
	public static Point2D.Double tofSectorXY[][] = new Point2D.Double[15][16];
	
	//the dc factory
	private static AlertDCFactory dcFactory = new AlertDCFactory();
	
	private static DatabaseConstantProvider constantProvider;

	/**
	 * Init the Alert geometry
	 */
	public static void initialize() {
		System.out.println("\n=======================================");
		System.out.println("===  " + NAME + " Geometry Initialization ===");
		System.out.println("=======================================");

		String variationName = Ced.getGeometryVariation();
		constantProvider = new DatabaseConstantProvider(11, variationName);

		initializeDC(constantProvider);
		initializeTOF(constantProvider);

	}
	
	private static void debugPrint(String s, int option) {
		if (_debug) {
			if (option == 0) {
				System.out.println("ALERT_DC  " + s);
			} else if (option == 1){
				System.out.println("ALERT_TOF " + s);
			}
			else {
				System.out.println(s);
			}
		}
	}

	// init the drift chambers
	private static void initializeDC(DatabaseConstantProvider cp) {

		AlertDCDetector dcCLASDetector = dcFactory.createDetectorCLAS(cp);

		int numsect = dcCLASDetector.getNumSectors();
		
		debugPrint(String.format("numsect: %d", numsect), 0);
		
		for (int sect = 0; sect < numsect; sect++) {
			debugPrint("", 2);
			debugPrint(String.format("  for sect: %d", sect), 0);
			
			int numsupl = dcFactory.createSector(cp, sect).getNumSuperlayers();
			
			debugPrint(String.format("  numsuperlayer: %d", numsupl), 0);
			for (int superlayer = 0; superlayer < numsupl; superlayer++) {
				debugPrint(String.format("    for superlayer: %d", superlayer), 0);
				int numlay = dcFactory.createSuperlayer(cp, sect, superlayer).getNumLayers();
				debugPrint(String.format("    numlayer: %d", numlay), 0);

				for (int layer = 0; layer < numlay; layer++) {
					DCLayer dcLayer = new DCLayer(dcFactory.createLayer(cp, sect, superlayer, layer));
					
					debugPrint(String.format("      for layer: %d  numwires: %d", layer, dcLayer.numWires), 0);
					_dcLayers.put(hash(sect, superlayer, layer), dcLayer);
				}
			}
		}
		debugPrint("", 2);

	}
	
	public static int getDCNumLayers(int sector, int superlayer) {
		int numlay = dcFactory.createSuperlayer(constantProvider, sector, superlayer).getNumLayers();
		return numlay;
	}
	
	public static int getDCNumWires(int sector, int superlayer, int layer) {
		DCLayer dcLayer = _dcLayers.get(hash(sector, superlayer, layer));
		if (dcLayer != null) {
			return dcLayer.numWires;
		}
		return 0;
	}

	// init the time of flight
	private static void initializeTOF(DatabaseConstantProvider cp) {

		AlertTOFFactory tofFactory = new AlertTOFFactory();
		AlertTOFDetector tofCLASDetector = tofFactory.createDetectorCLAS(cp);

		int numsect = tofCLASDetector.getNumSectors();
		
		debugPrint(String.format("numsect: %d", numsect), 1);
		
		for (int sect = 0; sect < numsect; sect++) {
			debugPrint("", 2);
			debugPrint(String.format("  for sect: %d", sect), 1);
			int numsupl = tofFactory.createSector(cp, sect).getNumSuperlayers();
			debugPrint(String.format("  numsuperlayer: %d", numsupl), 1);

			for (int superlayer = 0; superlayer < numsupl; superlayer++) {
				debugPrint(String.format("    for superlayer: %d", superlayer), 1);

				int numlay = tofFactory.createSuperlayer(cp, sect, superlayer).getNumLayers();
				debugPrint(String.format("    numlayer: %d", numlay), 1);

				for (int layer = 0; layer < numlay; layer++) {
					debugPrint(String.format("      for layer: %d", layer), 1);
					
					AlertTOFLayer alertTOFLayer = tofFactory.createLayer(cp, sect, superlayer, layer);
					TOFLayer tofLayer = new TOFLayer(tofFactory.createLayer(cp, sect, superlayer, layer));
					
					int numpaddle = alertTOFLayer.getNumComponents();
					debugPrint(String.format("      numpaddle: %d", numpaddle), 1);
					
					if (_debug) {
					   List<ScintillatorPaddle> paddles = alertTOFLayer.getAllComponents();
					   System.out.print("      numpaddle: " + numpaddle + " with ids: ");
						for (int i = 0; i < numpaddle; i++) {
							System.out.print(paddles.get(i).getComponentId() + " ");
						}
						System.out.println();
					}


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
	 * @param container the container
	 */
	public static void drawAlertTOFSectorOutlines(Graphics g, IContainer container) {

		
		Point[] anchorPP = new Point[15];
		
		for (int sect = 0; sect < 15; sect++) {
			Point2D.Double anchor = tofSectorXY[sect][11];
			anchorPP[sect] = new Point();		
			container.worldToLocal(anchorPP[sect], anchor);
		}
		
		//draw the sector numbers
		g.setColor(Color.red);
		for (int sect = 0; sect < 15; sect++) {
			int oppSect = (sect + 7) % 15;
			Point pp0 = anchorPP[sect];
			Point pp1 = anchorPP[oppSect];
			GraphicsUtilities.drawNumberAtEnd(g, (sect+1), pp1, pp0, 16, Fonts.hugeFont, Color.black);
		}

	}
	
	
    /**
     * Get the corner of a paddle
     * @param paddle the paddle
     * @param corner the corner 0..3
     * @return the corner
     */
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

	
	/**
	 * Used by the 3D drawing
	 * @param sector   the 0-based sector 0..14
	 * @param superlayer  the 0-based layer 0..1
	 * @param layer    the 0-based layer 0, 0..9
	 * @param paddleId the 0-based paddle 0..3
	 * @param coords   holds 8*3 = 24 values [x1, y1, z1, ..., x8, y8, z8]
	 */
	public static void paddleVertices(int sector, int superlayer, int layer, int paddleId, float[] coords) {

		Point3D v[] = new Point3D[8];

		ScintillatorPaddle paddle = getPaddle(sector, superlayer, layer, paddleId);
		for (int i = 0; i < 8; i++) {
			v[i] = new Point3D(paddle.getVolumePoint(i));
		}

		for (int i = 0; i < 8; i++) {
			int j = 3 * i;
			coords[j] = (float) v[i].x();
			coords[j + 1] = (float) v[i].y();
			coords[j + 2] = (float) v[i].z();
		}
	}	
	
	//all 0 based
	private static String hash(int sector, int superlayer, int layer) {
		return String.format("%d|%d|%d", sector, superlayer, layer);
	}
	
	/**
     * Get the DC layer
     * @param sector 0 based
     * @param superlayer 0 based
     * @param layer 0 based
     * @return the DC layer
     */
	public static DCLayer getDCLayer(int sector, int superlayer, int layer) {
		return _dcLayers.get(hash(sector, superlayer, layer));
	}
	
	/**
     * Get the TOF layer
     * @param sector 0 based
     * @param superlayer 0 based
     * @param layer 0 based
     * @return the DC layer
     */
	public static TOFLayer getTOFLayer(int sector, int superlayer, int layer) {
		return _tofLayers.get(hash(sector, superlayer, layer));
	}


	public static void main(String[] arg) {
		_debug = true;
		// this is supposed to create less pounding of ccdb
		DefaultLogger.initialize();

		initialize();

		System.out.println("done");

	}

}
