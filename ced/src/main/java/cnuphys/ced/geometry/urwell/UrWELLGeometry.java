package cnuphys.ced.geometry.urwell;

import org.jlab.detector.calib.utils.DatabaseConstantProvider;
import org.jlab.detector.geant4.v2.URWELL.URWellConstants;
import org.jlab.detector.geant4.v2.URWELL.URWellStripFactory;
import org.jlab.geom.prim.Line3D;

import cnuphys.ced.frame.Ced;

/**
 * Geometric data for the uRwell detectors
 * @author heddle
 *
 */
public class UrWELLGeometry {

	//the urwell geometry factory
	public static URWellStripFactory factory = new URWellStripFactory();

	//chamber data
	private static ChamberData[][][] _chamberData;

	//the strips
	private static Line3D _strips[][][];

	//number of strips by chamber [0..2]
	public static int numStripsByChamber[] = new int[3];

	/**
	 * Init the uRwell geometry
	 */
	public static void initialize() {
		System.out.println("\n=======================================");
		System.out.println("===  uRWell Geometry Initialization ===");
		System.out.println("=======================================");


        String variationName = Ced.getGeometryVariation();
        DatabaseConstantProvider cp = new DatabaseConstantProvider(11, variationName);
        factory.init(cp);

        getChamberData();
	}

	//load and cache some useful chamber data
	private static void getChamberData() {

		for (int chamber = 0; chamber < URWellConstants.NCHAMBERS; chamber++) {
			numStripsByChamber[chamber] = factory.getNStripChamber(chamber);
			System.err.println("uRwell numb strips for chamber " + chamber + ": " + numStripsByChamber[chamber]);
		}


		_chamberData = new ChamberData[6][URWellConstants.NCHAMBERS][URWellConstants.NLAYERS];
		for (int sector = 0; sector < 6; sector++) {
			for (int chamber = 0; chamber < URWellConstants.NCHAMBERS; chamber++) {
				for (int layer = 0; layer < URWellConstants.NLAYERS; layer++) {
					//note only chamber passed as a zero based
					_chamberData[sector][chamber][layer] = new ChamberData(sector+1, chamber, layer+1);
				}
			}
		}
	}

	/**
	 * Convert a chamber and chamber strip to a "global" strip.
	 * @param chamber [0..2]
	 * @param chamberStrip [1.. num strips in chamber]
	 * @return global strip 1..1884
	 */
	public static int stripNumber(int chamber, int chamberStrip) {
		if ((chamber < 0) || (chamber >= URWellConstants.NCHAMBERS)) {
			System.err.println("bad chamber in UrWELL.stripNumber: " + chamber);
			return -1;
		}

		if ((chamberStrip < 1) || (chamber > numStripsByChamber[chamber])) {
			System.err.println("bad (chamber, chamberStrip) in UrWELL.stripNumber: (" + chamber + ", " + chamberStrip + ")");
			return -1;
		}

		if (chamber == 0) {
			return chamberStrip;
		}
		else if (chamber == 1) {
			return numStripsByChamber[0] + chamberStrip;
		}

		else {
			return numStripsByChamber[0] + numStripsByChamber[1] + chamberStrip;
		}
	}



	//test the chamber to global numbering
	private static void stripNumberTest() {

		int strip = 1;
		for (int chamber = 0; chamber < URWellConstants.NCHAMBERS; chamber++) {
			for (int chamberStrip = 1; chamberStrip <= numStripsByChamber[chamber]; chamberStrip++) {
				int testStrip = stripNumber(chamber, chamberStrip);
				if (testStrip != strip) {
					System.err.println("FAILED stripNumberTest");
					System.err.println(String.format("chamber: %d   chamberStrip: %d  strip: %d   testStrip: %d", chamber, chamberStrip, strip, testStrip));
					System.exit(0);
				}
				strip++;
			}
		}
		System.err.println("PASSED stripNumberTest.");
	}


	//main program for testing
	public static void main(String[] arg) {
		initialize();
		System.err.println("uRwell num layers: " + URWellConstants.NLAYERS);
		System.err.println("uRwell num sectors: " + URWellConstants.NSECTORS);
		System.err.println("uRwell num chambers: " + URWellConstants.NCHAMBERS);



		//tests
		stripNumberTest();


		//strips are 3D lines
		Line3D line3D;


		double minLen = Double.POSITIVE_INFINITY;
		double maxLen = Double.NEGATIVE_INFINITY;

		for (int sector = 1; sector <= 6; sector++) {
			for (int layer = 1; layer <= 2; layer++) {
				for (int strip = 1; strip <= 1884; strip++) {
					line3D = factory.getStrip(sector, layer, strip);


					if (line3D == null) {
						System.err.println(String.format("null strip for [sector, layer, strip] = [%d, %d, %d]", sector,
								layer, strip));
						System.exit(1);
					}

					double len = line3D.length();
					minLen = Math.min(minLen, len);
					maxLen = Math.max(maxLen, len);

				}
			}
		}

		System.err.println("Min strip length = " + minLen);
		System.err.println("Max strip length = " + maxLen);

		System.err.println("Done");

	}

}
