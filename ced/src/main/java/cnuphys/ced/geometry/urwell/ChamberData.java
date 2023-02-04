package cnuphys.ced.geometry.urwell;

import org.jlab.geom.prim.Line3D;

public class ChamberData {
	public int sector;

	public int chamber;

	public int layer;

	public double minLen = Double.POSITIVE_INFINITY;
	public double maxLen = Double.NEGATIVE_INFINITY;

	/**
	 * Some useful chamber data
	 * @param sector [1..6]
	 * @param chamber [0..2]  //note this is zero based.
	 * @param layer [1..2]
	 */
	public ChamberData(int sector, int chamber, int layer) {

		if ((sector < 1) || (sector > 6)) {
			System.err.println("Bad sector in UrWELL ChamberData: " + sector);
			System.exit(0);
		}
		else if ((chamber < 0) || (chamber > 2)) {
			System.err.println("Bad chamber in UrWELL ChamberData: " + chamber);
			System.exit(0);
		}
		else if ((layer < 1) || (layer > 2)) {
			System.err.println("Bad layer in UrWELL ChamberData: " + layer);
			System.exit(0);
		}

		this.sector = sector;
		this.chamber = chamber;
		this.layer = layer;

		Line3D line3D;



		for (int chamberStrip = 1; chamberStrip <= UrWELLGeometry.numStripsByChamber[chamber]; chamberStrip++) {
			int strip = UrWELLGeometry.stripNumber(chamber, chamberStrip); //1..1884

			line3D = UrWELLGeometry.factory.getStrip(sector, layer, strip);


			if (line3D == null) {
				System.err.println(String.format("null strip in ChamberData constructor for [sector, layer, strip] = [%d, %d, %d]", sector,
						layer, strip));
				System.exit(1);
			}

			double len = line3D.length();


			minLen = Math.min(minLen, len);
			maxLen = Math.max(maxLen, len);

		}

		System.err.println(String.format("ChamberData for sector = %d  chamber = %d   layer = %d", sector, chamber, layer));
		System.err.println("   Min strip length = " + minLen);
		System.err.println("   Max strip length = " + maxLen);

	}
}
