package cnuphys.ced.geometry.urwell;

import org.jlab.detector.geant4.v2.URWELL.URWellStripFactory;
import org.jlab.geom.prim.Line3D;

public class ChamberData {

	// sector is stored as 1-based [1..6]
	public int sector;

	// chamber stored as 1-based [1..3]
	public int chamber;

	// layer stored as [1-base] [1..2]
	public int layer;

	// the strip count
	public int count;

	// the strips
	public Line3D[] strips;

	/**
	 * Some useful chamber data
	 *
	 * @param sector  [1..6]
	 * @param chamber [1..3]
	 * @param layer   [1..2]
	 */
	public ChamberData(URWellStripFactory factory, int sector, int chamber, int layer) {

		if ((sector < 1) || (sector > 6)) {
			System.err.println("Bad sector in UrWELL ChamberData: " + sector);
			System.exit(0);
		} else if ((chamber < 1) || (chamber > 3)) {
			System.err.println("Bad chamber in UrWELL ChamberData: " + chamber);
			System.exit(0);
		} else if ((layer < 1) || (layer > 2)) {
			System.err.println("Bad layer in UrWELL ChamberData: " + layer);
			System.exit(0);
		}

		this.sector = sector;
		this.chamber = chamber;
		this.layer = layer;

		// the strip count
		count = UrWELLGeometry.numStripsByChamber[chamber - 1];

		// the strips
		strips = new Line3D[count];

		for (int chamberStrip = 1; chamberStrip <= count; chamberStrip++) {

			// global strip
			int strip = UrWELLGeometry.stripNumber(chamber, chamberStrip); // 1..MAXSTRIP

			if ((strip < 1) || (strip > UrWELLGeometry.maxStrip)) {
				System.err.println("global strip ID out of bounds in ChamberData (urWELL): " + strip);
				System.exit(1);
			}

			strips[chamberStrip - 1] = factory.getStrip(sector, layer, strip);

			if (strips[chamberStrip - 1] == null) {
				System.err.println(
						String.format("null strip in ChamberData constructor for [sector, layer, strip] = [%d, %d, %d]",
								sector, layer, strip));
				System.exit(1);
			}


		}

	}
}
