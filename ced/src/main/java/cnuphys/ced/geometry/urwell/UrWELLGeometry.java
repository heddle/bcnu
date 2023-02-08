package cnuphys.ced.geometry.urwell;

import org.jlab.detector.calib.utils.DatabaseConstantProvider;
import org.jlab.detector.geant4.v2.URWELL.URWellConstants;
import org.jlab.detector.geant4.v2.URWELL.URWellStripFactory;
import org.jlab.geom.prim.Line3D;

import cnuphys.bCNU.util.UnicodeSupport;
import cnuphys.ced.frame.Ced;

/**
 * Geometric data for the uRwell detector
 * @author heddle
 *
 */
public class UrWELLGeometry {
	
	//the name of the detector
	public static String NAME = UnicodeSupport.SMALL_MU + "Rwell";

	//the urwell geometry factory
	public static URWellStripFactory factory = new URWellStripFactory();

	//chamber data indices are sector [0..5] chamber [0..2] layer [0..1]
	private static ChamberData[][][] _chamberData;

	//number of strips by chamber [0..2]
	public static int numStripsByChamber[] = new int[3];
	
	//the maximun global strip ID (num strips per sector and layer)
	public static int MAXSTRIP;
	
	//used to make outline polygons (taken from sector 1 and rotated as needed)
	//the index corresponds to chamber
	public static double minX[] = {Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY};
	public static double maxX[] = {Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY};
	public static double minY[] = {Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY};
	public static double maxY[] = {Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY};

	/**
	 * Init the uRwell geometry
	 */
	public static void initialize() {
		System.out.println("\n=======================================");
		System.out.println("===  " + NAME + " Geometry Initialization ===");
		System.out.println("=======================================");


        String variationName = Ced.getGeometryVariation();
        DatabaseConstantProvider cp = new DatabaseConstantProvider(11, variationName);
        factory.init(cp);

        getChamberData();
	}

	//load and cache some useful chamber data
	private static void getChamberData() {

		MAXSTRIP = 0;
		for (int chamber = 0; chamber < URWellConstants.NCHAMBERS; chamber++) {
			numStripsByChamber[chamber] = factory.getNStripChamber(chamber);
			MAXSTRIP += numStripsByChamber[chamber];
		}

		_chamberData = new ChamberData[6][URWellConstants.NCHAMBERS][URWellConstants.NLAYERS];
		for (int sector = 0; sector < 6; sector++) {
			for (int chamber = 0; chamber < URWellConstants.NCHAMBERS; chamber++) {
				for (int layer = 0; layer < URWellConstants.NLAYERS; layer++) {
					//passed as 1-based
					_chamberData[sector][chamber][layer] = new ChamberData(sector+1, chamber+1, layer+1);
				}
			}
		}
		
		//get some limits to make outlines

		double maxTheta = Double.NEGATIVE_INFINITY;
		double theta;

		
		int sector = 1;
		for (int chamber = 1; chamber <= URWellConstants.NCHAMBERS; chamber++) {
			int cm1 = chamber-1;
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



//					minY[cm1] = Math.min(minY[cm1], Math.abs(line3D.origin().y()));
//					minY[cm1] = Math.min(minY[cm1], Math.abs(line3D.end().y()));
					maxY[cm1] = Math.max(maxY[cm1], Math.abs(line3D.origin().y()));
					maxY[cm1] = Math.max(maxY[cm1], Math.abs(line3D.end().y()));
				}

			}
			
			minY[cm1] = Math.abs(minX[cm1]*Math.tan(maxTheta));

			
		} //end chamber loop
	}
	
	/**
	 * Convert global strip to chamber and chamber strip
	 * @param strip global strip 1..1884
	 * @param data on return data[0] is the 1-based chamber, data[1] is the 1-based chamber strip.
	 */
	public static void chamberStrip(int strip, int data[]) {
		data[0] = factory.getChamberIndex(strip) + 1;
		
		if (data[0] == 1) {
			data[1] = strip;
		}
		else if (data[0] == 2) {
			data[1] = strip  - numStripsByChamber[0];
		}

		else {
			data[1] = strip - numStripsByChamber[0] - numStripsByChamber[1];
		}
	}
	
	

	/**
	 * Convert a chamber and chamber strip to a "global" strip.
	 * @param chamber [1..3]
	 * @param chamberStrip [1.. num strips in chamber]
	 * @return global strip 1..1884
	 */
	public static int stripNumber(int chamber, int chamberStrip) {
		if ((chamber < 1) || (chamber > URWellConstants.NCHAMBERS)) {
			System.err.println("bad chamber in UrWELL.stripNumber: " + chamber);
			return -1;
		}

		if ((chamberStrip < 1) || (chamber > numStripsByChamber[chamber-1])) {
			System.err.println("bad (chamber, chamberStrip) in UrWELL.stripNumber: (" + chamber + ", " + chamberStrip + ")");
			return -1;
		}

		if (chamber == 1) {
			return chamberStrip;
		}
		else if (chamber == 2) {
			return numStripsByChamber[0] + chamberStrip;
		}

		else {
			return numStripsByChamber[0] + numStripsByChamber[1] + chamberStrip;
		}
	}
	
	
	/**
	 * Get a strip
	 * @param sector the 1-based sector [1..6]
	 * @param chamber the 1-based chamber [1..3]
	 * @param layer the 1-based layer [1..2]
	 * @param chamberStrip the 1-based strip
	 * @return the strip
	 */
	public static Line3D getStrip(int sector, int chamber, int layer, int chamberStrip) {
		return _chamberData[sector-1][chamber-1][layer-1].strips[chamberStrip-1];
	}



	//test the chamber to global numbering
	private static void stripNumberTest() {
		
		int data[] = new int[2];

		int strip = 1;
		for (int chamber = 1; chamber < URWellConstants.NCHAMBERS; chamber++) {
			for (int chamberStrip = 1; chamberStrip <= numStripsByChamber[chamber-1]; chamberStrip++) {
				int testStrip = stripNumber(chamber, chamberStrip);
				if (testStrip != strip) {
					System.err.println("FAILED stripNumberTest (A)");
					System.err.println(String.format("chamber: %d   chamberStrip: %d  strip: %d   testStrip: %d", chamber, chamberStrip, strip, testStrip));
					System.exit(0);
				}
				
				chamberStrip(strip, data);
				if (data[0] != chamber) {
					System.err.println("FAILED stripNumberTest (B)");
					System.err.println(String.format("inverse chamber doesn't match chamber: %d  data[0]:  %d ", chamber, data[0]));
					System.exit(0);
				}
				if (data[1] != chamberStrip) {
					System.err.println("FAILED stripNumberTest (C)");
					System.err.println(String.format("inverse chamber strip doesn't match chamberStrip: %d  data[1]:  %d ", chamberStrip, data[1]));
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
		
		double minX = Double.POSITIVE_INFINITY;
		double maxX = Double.NEGATIVE_INFINITY;
		
		double minY = Double.POSITIVE_INFINITY;
		double maxY = Double.NEGATIVE_INFINITY;



		for (int sector = 1; sector <= 1; sector++) {
			for (int layer = 1; layer <= 2; layer++) {
				//global strip id that crosses chamber boundaries
				for (int strip = 1; strip <= MAXSTRIP; strip++) {
					line3D = factory.getStrip(sector, layer, strip);


					if (line3D == null) {
						System.err.println(String.format("null strip for [sector, layer, strip] = [%d, %d, %d]", sector,
								layer, strip));
						System.exit(1);
					}
					
					minX = Math.min(minX, line3D.origin().x());
					minX = Math.min(minX, line3D.end().x());
					maxX = Math.max(maxX, line3D.origin().x());
					maxX = Math.max(maxX, line3D.end().x());
					
					minY = Math.min(minY, line3D.origin().y());
					minY = Math.min(minY, line3D.end().y());
					maxY = Math.max(maxY, line3D.origin().y());
					maxY = Math.max(maxY, line3D.end().y());



					double len = line3D.length();
					minLen = Math.min(minLen, len);
					maxLen = Math.max(maxLen, len);

				}
			}
		}
		
		System.err.println("Min X = " + minX);
		System.err.println("Max X = " + maxX);

		System.err.println("Min Y = " + minY);
		System.err.println("Max Y = " + maxY);

		System.err.println("Min strip length = " + minLen);
		System.err.println("Max strip length = " + maxLen);

		System.err.println("Done");
		
		//get total strip count per sector:
		int layer1Count = _chamberData[0][0][0].count + _chamberData[0][1][0].count + _chamberData[0][2][0].count;
		int layer2Count = _chamberData[0][0][1].count + _chamberData[0][1][1].count + _chamberData[0][2][1].count;
		
		System.err.println("Number of strips each sector layer 1: " + layer1Count);
		System.err.println("Number of strips each sector layer 2: " + layer2Count);
		System.err.println("Total number of strips all sectors all layers: " + 6*(layer1Count+layer2Count));

		

	}

}
