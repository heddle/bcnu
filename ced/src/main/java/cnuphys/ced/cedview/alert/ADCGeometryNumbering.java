package cnuphys.ced.cedview.alert;

import cnuphys.ced.geometry.alert.DCLayer;

public class ADCGeometryNumbering  {
	
	public int sector;  //0-based
	public int superlayer; //0-based
	public int layer; //0-based
	public int component; //0-based (wire)


	/**
	 * Create a geometry numbering
	 */
	public ADCGeometryNumbering() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * Set the geometry numbering from a data numbering
	 * @param sect the 1-based sector
	 * @param compLayer the 1-based layer
	 * @param comp the 1-based component (wire)
	 */
	public void fromDataNumbering(int sect, int compLayer, int comp) {

		sector = sect-1;

		int sl1 = compLayer / 10;
		int lay1 =compLayer % 10;

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
