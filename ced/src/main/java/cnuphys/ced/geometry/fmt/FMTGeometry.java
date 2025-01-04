package cnuphys.ced.geometry.fmt;

import java.util.Collection;
import java.util.Hashtable;

import org.jlab.detector.base.GeometryFactory;
import org.jlab.geom.base.ConstantProvider;
import org.jlab.geom.component.TrackerStrip;
import org.jlab.geom.detector.fmt.FMTDetector;
import org.jlab.geom.detector.fmt.FMTFactory;
import org.jlab.geom.detector.fmt.FMTLayer;
import org.jlab.geom.detector.fmt.FMTSector;
import org.jlab.geom.detector.fmt.FMTSuperlayer;
import org.jlab.geom.prim.Line3D;
import org.jlab.geom.prim.Point3D;
import org.jlab.geom.prim.Vector3D;
import org.jlab.logging.DefaultLogger;

import cnuphys.ced.frame.Ced;

public class FMTGeometry {
	//for debugging
	private static boolean _debug = false;

	// the name of the detector
	public static String NAME = "FMT";

	//the layer objects used for FMT geometry and drawing
	private static Hashtable<String, FMTLayer> _fmtLayers = new Hashtable<>();

	//the dc factory
	private static FMTFactory fmtFactory = new FMTFactory();

	/**
	 * Init the Alert geometry
	 */
	public static void initialize() {
		System.out.println("\n=======================================");
		System.out.println("===  " + NAME + " Geometry Initialization ===");
		System.out.println("=======================================");

		String variation = Ced.getGeometryVariation();
		ConstantProvider constantProvider = GeometryFactory.getConstants(org.jlab.detector.base.DetectorType.FMT, 11, variation);


		initialize(constantProvider);
	}

	// init the time of flight
	private static void initialize(ConstantProvider cp) {


		FMTDetector fmtDetector = fmtFactory.createDetectorCLAS(cp);

		int numsect = fmtDetector.getNumSectors();

		debugPrint(String.format("numsect: %d", numsect), 0);

		for (int sect = 0; sect < numsect; sect++) {
			debugPrint("", 2);
			debugPrint(String.format("  for sect: %d", sect), 1);

			FMTSector fmtSector = fmtFactory.createSector(cp, sect);
			int numsupl = fmtSector.getNumSuperlayers();
			debugPrint(String.format("  numsuperlayer: %d", numsupl), 1);

			for (int superlayer = 0; superlayer < numsupl; superlayer++) {
				debugPrint(String.format("    for superlayer: %d", superlayer), 1);


				FMTSuperlayer fmtSuperlayer = fmtFactory.createSuperlayer(cp, sect, superlayer);
				int numlay = fmtSuperlayer.getNumLayers();
				debugPrint(String.format("    numlayer: %d", numlay), 1);

				for (int layer = 0; layer < numlay; layer++) {
					debugPrint(String.format("      for layer: %d", layer), 1);
                    FMTLayer fmtLayer = fmtFactory.createLayer(cp, sect, superlayer, layer);

					debugPrint(String.format("      for layer: %d  num components: %d", layer, fmtLayer.getNumComponents()), 0);
					_fmtLayers.put(hash(sect, superlayer, layer), fmtLayer);

 				}
			}
		}

	}

	public static TrackerStrip getStrip(int sector, int superlayer, int layer, int strip) {
		FMTLayer fmtLayer = _fmtLayers.get(hash(sector, superlayer, layer));
		if (fmtLayer != null) {
			return fmtLayer.getComponent(strip);
		}
		return null;
	}


	//all 0 based
	private static String hash(int sector, int superlayer, int layer) {
		return String.format("%d|%d|%d", sector, superlayer, layer);
	}



	// print a debug message
	private static void debugPrint(String s, int option) {
		if (_debug) {
			System.out.println("FMT  " + s);
		}
	}

	/**
	 * Get all the fmt layers
	 * @return the collection of FMT layers
	 */
	public static Collection<FMTLayer> getAllFMTLayers() {
		return _fmtLayers.values();
	}


	/**
	 * Used by the 3D drawing
	 * @param sector   the 0-based sector 0..0
	 * @param superlayer  the 0-based layer 0..0
	 * @param layer    the 0-based layer 0..5
	 * @param stripId the 0-based paddle 0..1023
	 * @param coords   holds 8*3 = 24 values [x1, y1, z1, ..., x8, y8, z8]
	 */
	public static void stripVertices(int sector, int superlayer, int layer, int stripId, float[] coords) {

		Point3D v[] = new Point3D[8];

		TrackerStrip strip = getStrip(sector, superlayer, layer, stripId);
		for (int i = 0; i < 8; i++) {
			v[i] = new Point3D(strip.getVolumePoint(i));
		}

		for (int i = 0; i < 8; i++) {
			int j = 3 * i;
			coords[j] = (float) v[i].x();
			coords[j + 1] = (float) v[i].y();
			coords[j + 2] = (float) v[i].z();
		}
	}


	public static void main(String[] arg) {
		_debug = true;
		// this is supposed to create less pounding of ccdb
		DefaultLogger.initialize();

		initialize();

		double xmin = Double.MAX_VALUE;
		double xmax = -Double.MAX_VALUE;
		double ymin = Double.MAX_VALUE;
		double ymax = -Double.MAX_VALUE;
		double zmin = Double.MAX_VALUE;
		double zmax = -Double.MAX_VALUE;

		// TrackerStrip strip = getStrip(0, 0, 0, 200);

		for (int layer = 0; layer < 6; layer++) {
			for (int stripId = 0; stripId < 1024; stripId++) {
				TrackerStrip strip = getStrip(0, 0, layer, stripId);
				for (int i = 0; i < 8; i++) {
					Point3D p = strip.getVolumePoint(i);
					xmin = Math.min(xmin, p.x());
					xmax = Math.max(xmax, p.x());
					ymin = Math.min(ymin, p.y());
					ymax = Math.max(ymax, p.y());
					zmin = Math.min(zmin, p.z());
					zmax = Math.max(zmax, p.z());
				}
			}
		}

		System.out.println("xmin: " + xmin);
		System.out.println("xmax: " + xmax);
		System.out.println("ymin: " + ymin);

		System.out.println("ymax: " + ymax);
		System.out.println("zmin: " + zmin);

		System.out.println("zmax: " + zmax);

		for (int layer = 0; layer < 6; layer++) {
			for (int stripId = 0; stripId < 1024; stripId++) {
				if ((stripId == 1023) || (stripId % 10) == 0) {
				//	if ((stripId % 100) == 0) {
					TrackerStrip strip = getStrip(0, 0, layer, stripId);
					Line3D line = strip.getLine();
					Vector3D v = line.toVector();
					Point3D p = strip.getMidpoint();
					double x = p.x();
					double y = p.y();
					double z = p.z();
					double r = Math.sqrt(x * x + y * y + z * z);
					double rho = Math.sqrt(x * x + y * y);
					double theta = Math.toDegrees(Math.acos(z / r));
					double phi = Math.toDegrees(Math.atan2(y, x));
					System.out.println(String.format("layer: %d    strip: %d    theta: %-7.3f    phi: %-7.3f    rho: %-7.3f    z: %-7.3f",
							layer + 1, stripId + 1, theta, phi, rho, z));
				}
			}
		}

		System.out.println("done");

	}

}
