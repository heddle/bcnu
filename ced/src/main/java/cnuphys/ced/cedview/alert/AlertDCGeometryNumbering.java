package cnuphys.ced.cedview.alert;

import cnuphys.ced.geometry.alert.DCLayer;

public class AlertDCGeometryNumbering  {

	public int sector;  //0-based
	public int superlayer; //0-based
	public int layer; //0-based
	public int component; //0-based (wire)


	/**
	 * Create an Alert DC geometry numbering
	 */
	public AlertDCGeometryNumbering() {
	}

	/**
	 * Set the geometry numbering from a data numbering
	 * @param sect the 1-based sector in the ALERT hipo data
	 * @param compLayer the 1-based layer in the ALERT hipo data
	 * @param comp the 1-based component (wire or paddle) in the ALERT hipo data
	 * @param order in the ALERT hipo data
	 */
	public void fromDataNumbering(int sect, int compLayer, int comp, int order) {

		//convert to 0-based sector, superlayer, layer component for the
		// geometry database numbering
		sector = sect-1;

		int sl1 = compLayer / 10;
		int lay1 = compLayer % 10;

		superlayer = sl1 - 1;
		layer = lay1 - 1;
		component = comp - 1;
	}

	/**
     * Match a DC layer
     * @param dcl the DC layer
     * @return <code>true</code> if the DC layer matches
     */
	public boolean match(DCLayer dcl) {
		return (dcl.sector == sector) && (dcl.superlayer == superlayer) && (dcl.layer == layer);
	}

}
