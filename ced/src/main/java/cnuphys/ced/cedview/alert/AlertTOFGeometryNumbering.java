package cnuphys.ced.cedview.alert;

import cnuphys.ced.geometry.alert.TOFLayer;

public class AlertTOFGeometryNumbering {

	public int sector;  //0-based
	public int superlayer; //0-based
	public int layer; //0-based
	public int component; //0-based (wire)


	/**
	 * Create an ALert TOF geometry numbering
	 */
	public AlertTOFGeometryNumbering() {		// TODO Auto-generated constructor stub
	}

	/**
	 * Set the geometry numbering from a data numbering
	 * @param sect the 1-based sector
	 * @param compLayer the 1-based layer
	 * @param comp the 1-based component (paddle)
	 * @param order
	 */
	public void fromDataNumbering(int sect, int compLayer, int comp, int order) {

		sector = sect - 1;

		//edge case, 30 maps to sl1 = 2, lay1 = 10

		int sl1 = (compLayer-1) / 10;  // will be 1 or 2
		int lay1 = compLayer % 10;

		//edge case of 30
		if (lay1 == 0) {
			lay1 = 10;
		}

		superlayer = sl1 - 1;
		layer = lay1 - 1;
		component = comp - 1;
	}

	/**
     * Match a DC layer
     * @param tofl the DC layer
     * @return <code>true</code> if the DC layer matches
     */
	public boolean match(TOFLayer tofl) {
		return (tofl.sector == sector) && (tofl.superlayer == superlayer) && (tofl.layer == layer);
	}

}
