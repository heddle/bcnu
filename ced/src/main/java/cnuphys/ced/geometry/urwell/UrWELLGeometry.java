package cnuphys.ced.geometry.urwell;

import org.jlab.detector.calib.utils.DatabaseConstantProvider;
import org.jlab.detector.geant4.v2.URWELL.URWellConstants;
import org.jlab.detector.geant4.v2.URWELL.URWellStripFactory;
import org.jlab.geom.prim.Line3D;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

import cnuphys.bCNU.util.UnicodeSupport;
import cnuphys.ced.frame.Ced;
import cnuphys.ced.geometry.cache.ACachedGeometry;

/**
 * Geometric data for the uRwell detector
 * 
 * @author heddle
 *
 */
public class UrWELLGeometry extends ACachedGeometry {

	// the name of the detector
	public static String NAME = UnicodeSupport.SMALL_MU + "Rwell";

	// chamber data indices are sector [0..5] chamber [0..2] layer [0..1]
	private static ChamberData[][][] _chamberData;

	// number of strips by chamber [0..2]
	public static int numStripsByChamber[] = new int[3];

	// the maximun global strip ID (num strips per sector and layer)
	public static int maxStrip;

	// indices to story chamber and chamber strip
	private static int[][] _inidices = new int[1884][2];

	// used to make outline polygons (taken from sector 1 and rotated as needed)
	// the index corresponds to chamber
	public static double minX[] = { Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY };
	public static double maxX[] = { Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY };
	public static double minY[] = { Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY };
	public static double maxY[] = { Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY };

	public UrWELLGeometry() {
		super(NAME);
	}

	/**
	 * Init the uRwell geometry
	 */
	@Override
	public void initializeUsingCCDB() {
		System.out.println("\n=======================================");
		System.out.println("===  " + NAME + " Geometry Initialization ===");
		System.out.println("=======================================");

		String variationName = Ced.getGeometryVariation();
		DatabaseConstantProvider cp = new DatabaseConstantProvider(11, variationName);
		URWellStripFactory factory = new URWellStripFactory();
		factory.init(cp);

		getChamberData(factory);
		getIndices(factory);
	}

	// load and cache some useful chamber data
	private static void getChamberData(URWellStripFactory factory) {

		maxStrip = 0;
		for (int chamber = 0; chamber < URWellConstants.NCHAMBERS; chamber++) {
			numStripsByChamber[chamber] = factory.getNStripChamber(chamber);
			maxStrip += numStripsByChamber[chamber];
		}

		_chamberData = new ChamberData[6][URWellConstants.NCHAMBERS][URWellConstants.NLAYERS];
		for (int sector = 0; sector < 6; sector++) {
			for (int chamber = 0; chamber < URWellConstants.NCHAMBERS; chamber++) {
				for (int layer = 0; layer < URWellConstants.NLAYERS; layer++) {
					// passed as 1-based
					_chamberData[sector][chamber][layer] = new ChamberData(factory, sector + 1, chamber + 1, layer + 1);
				}
			}
		}

		// get some limits to make outlines

		double maxTheta = Double.NEGATIVE_INFINITY;
		double theta;

		int sector = 1;
		for (int chamber = 1; chamber <= URWellConstants.NCHAMBERS; chamber++) {
			int cm1 = chamber - 1;
			for (int layer = 1; layer <= URWellConstants.NLAYERS; layer++) {

				for (int chamberStrip = 1; chamberStrip <= numStripsByChamber[chamber - 1]; chamberStrip++) {

					Line3D line3D = getStrip(sector, chamber, layer, chamberStrip);

					minX[cm1] = Math.min(minX[cm1], line3D.origin().x());
					minX[cm1] = Math.min(minX[cm1], line3D.end().x());
					maxX[cm1] = Math.max(maxX[cm1], line3D.origin().x());
					maxX[cm1] = Math.max(maxX[cm1], line3D.end().x());

					theta = Math.atan2(line3D.origin().y(), line3D.origin().x());
					maxTheta = Math.abs(Math.max(theta, maxTheta));
					theta = Math.atan2(line3D.end().y(), line3D.end().x());
					maxTheta = Math.abs(Math.max(theta, maxTheta));

					maxY[cm1] = Math.max(maxY[cm1], Math.abs(line3D.origin().y()));
					maxY[cm1] = Math.max(maxY[cm1], Math.abs(line3D.end().y()));
				}

			}

			minY[cm1] = Math.abs(minX[cm1] * Math.tan(maxTheta));

		} // end chamber loop
	}

	private static void getIndices(URWellStripFactory factory) {

		_inidices = new int[1884][2];
		for (int strip = 1; strip <= 1884; strip++) {
			// data[0] is the 1-based chamber, data[1] is the 1-based chamber strip.
			int data[] = _inidices[strip - 1];
			data[0] = factory.getChamberIndex(strip) + 1;

			if (data[0] == 1) {
				data[1] = strip;
			} else if (data[0] == 2) {
				data[1] = strip - numStripsByChamber[0];
			}

			else {
				data[1] = strip - numStripsByChamber[0] - numStripsByChamber[1];
			}
		}
	}

	/**
	 * Convert global strip to chamber and chamber strip
	 * 
	 * @param strip global strip 1..1884
	 * @param data  on return data[0] is the 1-based chamber, data[1] is the 1-based
	 *              chamber strip.
	 */
	public static int[] chamberStrip(int strip) {
		return _inidices[strip];
	}

	/**
	 * Convert a chamber and chamber strip to a "global" strip.
	 * 
	 * @param chamber      [1..3]
	 * @param chamberStrip [1.. num strips in chamber]
	 * @return global strip 1..1884
	 */
	public static int stripNumber(int chamber, int chamberStrip) {
		if ((chamber < 1) || (chamber > URWellConstants.NCHAMBERS)) {
			System.err.println("bad chamber in UrWELL.stripNumber: " + chamber);
			return -1;
		}

		if ((chamberStrip < 1) || (chamber > numStripsByChamber[chamber - 1])) {
			System.err.println(
					"bad (chamber, chamberStrip) in UrWELL.stripNumber: (" + chamber + ", " + chamberStrip + ")");
			return -1;
		}

		if (chamber == 1) {
			return chamberStrip;
		} else if (chamber == 2) {
			return numStripsByChamber[0] + chamberStrip;
		}

		else {
			return numStripsByChamber[0] + numStripsByChamber[1] + chamberStrip;
		}
	}

	/**
	 * Get a strip
	 * 
	 * @param sector       the 1-based sector [1..6]
	 * @param chamber      the 1-based chamber [1..3]
	 * @param layer        the 1-based layer [1..2]
	 * @param chamberStrip the 1-based strip
	 * @return the strip
	 */
	public static Line3D getStrip(int sector, int chamber, int layer, int chamberStrip) {
		return _chamberData[sector - 1][chamber - 1][layer - 1].strips[chamberStrip - 1];
	}

	@Override
	public boolean readGeometry(Kryo kryo, Input input) {
		try {
			// Read _chamberData (3D array)
			int outer = input.readInt();
			if (outer == 0) {
				_chamberData = null;
			} else {
				_chamberData = new ChamberData[outer][][];
				for (int i = 0; i < outer; i++) {
					int inner = input.readInt();
					if (inner == 0) {
						_chamberData[i] = null;
					} else {
						_chamberData[i] = new ChamberData[inner][];
						for (int j = 0; j < inner; j++) {
							int third = input.readInt();
							if (third == 0) {
								_chamberData[i][j] = null;
							} else {
								_chamberData[i][j] = new ChamberData[third];
								for (int k = 0; k < third; k++) {
									_chamberData[i][j][k] = kryo.readObjectOrNull(input, ChamberData.class);
								}
							}
						}
					}
				}
			}

			// Read numStripsByChamber (int[])
			int lenNum = input.readInt();
			if (lenNum == 0) {
				numStripsByChamber = null;
			} else {
				numStripsByChamber = new int[lenNum];
				for (int i = 0; i < lenNum; i++) {
					numStripsByChamber[i] = input.readInt();
				}
			}

			// Read _inidices (int[][])
			int outer2 = input.readInt();
			if (outer2 == 0) {
				_inidices = null;
			} else {
				_inidices = new int[outer2][];
				for (int i = 0; i < outer2; i++) {
					int inner2 = input.readInt();
					if (inner2 == 0) {
						_inidices[i] = null;
					} else {
						_inidices[i] = new int[inner2];
						for (int j = 0; j < inner2; j++) {
							_inidices[i][j] = input.readInt();
						}
					}
				}
			}

			// Read maxStrip (int)
			maxStrip = input.readInt();

			// Read maxX (double[])
			int lenMaxX = input.readInt();
			if (lenMaxX == 0) {
				maxX = null;
			} else {
				maxX = new double[lenMaxX];
				for (int i = 0; i < lenMaxX; i++) {
					maxX[i] = input.readDouble();
				}
			}

			// Read minX (double[])
			int lenMinX = input.readInt();
			if (lenMinX == 0) {
				minX = null;
			} else {
				minX = new double[lenMinX];
				for (int i = 0; i < lenMinX; i++) {
					minX[i] = input.readDouble();
				}
			}

			// Read maxY (double[])
			int lenMaxY = input.readInt();
			if (lenMaxY == 0) {
				maxY = null;
			} else {
				maxY = new double[lenMaxY];
				for (int i = 0; i < lenMaxY; i++) {
					maxY[i] = input.readDouble();
				}
			}

			// Read minY (double[])
			int lenMinY = input.readInt();
			if (lenMinY == 0) {
				minY = null;
			} else {
				minY = new double[lenMinY];
				for (int i = 0; i < lenMinY; i++) {
					minY[i] = input.readDouble();
				}
			}

			return true;
		} catch (Exception e) {
			return false;
		}
	}

	@Override
	public boolean writeGeometry(Kryo kryo, Output output) {
		try {
			// Write _chamberData (3D array:
			// [6][URWellConstants.NCHAMBERS][URWellConstants.NLAYERS])
			if (_chamberData == null) {
				output.writeInt(0);
			} else {
				output.writeInt(_chamberData.length); // expected 6 sectors
				for (int i = 0; i < _chamberData.length; i++) {
					if (_chamberData[i] == null) {
						output.writeInt(0);
					} else {
						output.writeInt(_chamberData[i].length); // number of chambers
						for (int j = 0; j < _chamberData[i].length; j++) {
							if (_chamberData[i][j] == null) {
								output.writeInt(0);
							} else {
								output.writeInt(_chamberData[i][j].length); // number of layers
								for (int k = 0; k < _chamberData[i][j].length; k++) {
									kryo.writeObjectOrNull(output, _chamberData[i][j][k], ChamberData.class);
								}
							}
						}
					}
				}
			}

			// Write numStripsByChamber (int[])
			if (numStripsByChamber == null) {
				output.writeInt(0);
			} else {
				output.writeInt(numStripsByChamber.length);
				for (int i = 0; i < numStripsByChamber.length; i++) {
					output.writeInt(numStripsByChamber[i]);
				}
			}

			// Write _inidices (int[][])
			if (_inidices == null) {
				output.writeInt(0);
			} else {
				output.writeInt(_inidices.length);
				for (int i = 0; i < _inidices.length; i++) {
					if (_inidices[i] == null) {
						output.writeInt(0);
					} else {
						output.writeInt(_inidices[i].length); // should be 2
						for (int j = 0; j < _inidices[i].length; j++) {
							output.writeInt(_inidices[i][j]);
						}
					}
				}
			}

			// Write maxStrip (int)
			output.writeInt(maxStrip);

			// Write maxX (double[])
			if (maxX == null) {
				output.writeInt(0);
			} else {
				output.writeInt(maxX.length);
				for (int i = 0; i < maxX.length; i++) {
					output.writeDouble(maxX[i]);
				}
			}

			// Write minX (double[])
			if (minX == null) {
				output.writeInt(0);
			} else {
				output.writeInt(minX.length);
				for (int i = 0; i < minX.length; i++) {
					output.writeDouble(minX[i]);
				}
			}

			// Write maxY (double[])
			if (maxY == null) {
				output.writeInt(0);
			} else {
				output.writeInt(maxY.length);
				for (int i = 0; i < maxY.length; i++) {
					output.writeDouble(maxY[i]);
				}
			}

			// Write minY (double[])
			if (minY == null) {
				output.writeInt(0);
			} else {
				output.writeInt(minY.length);
				for (int i = 0; i < minY.length; i++) {
					output.writeDouble(minY[i]);
				}
			}

			return true;
		} catch (Exception e) {
			return false;
		}
	}

}
