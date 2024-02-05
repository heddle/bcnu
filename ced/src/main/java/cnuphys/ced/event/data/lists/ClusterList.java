package cnuphys.ced.event.data.lists;

import java.awt.Point;
import java.util.List;

import cnuphys.ced.alldata.ColumnData;
import cnuphys.ced.alldata.DataWarehouse;
import cnuphys.ced.event.data.DataDrawSupport;

@SuppressWarnings("serial")
public class ClusterList {

	//the data warehouse
	private static DataWarehouse _dataWarehouse = DataWarehouse.getInstance();

	private String _bankName;

	public int length;
	public byte sector[];
	public byte layer[];
	public float energy[];
	public float[] time;
	public float x[];
	public float y[];
	public float z[];

	//where it was last drawn
	private int _ppx[];
	private int _ppy[];

	/**
	 * Create a cluster list
	 * @param bankName, e.g. XXXXS::clusters
	 */
	public ClusterList(String bankName) {
		_bankName = bankName;
	}

	/**
	 * Update the list as a response to a new event
	 */
	public void update() {
		sector = ColumnData.getByteArray(_bankName + ".sector");

		if ((sector != null) && (sector.length > 0)) {
			length = sector.length;
			layer = ColumnData.getByteArray(_bankName + ".layer");
			energy = ColumnData.getFloatArray(_bankName + ".energy");
			time = ColumnData.getFloatArray(_bankName + ".time");
			x = ColumnData.getFloatArray(_bankName + ".x");
			y = ColumnData.getFloatArray(_bankName + ".y");
			z = ColumnData.getFloatArray(_bankName + ".z");
			_ppx = new int[length];
			_ppy = new int[length];

		}
		else {
			length = 0;
			layer = null;
			energy = null;
			time = null;
			x = null;
			y = null;
			z = null;
			_ppx = null;
			_ppy = null;
		}
	}

	/**
	 * Update the list as a response to a new event
	 * using the DataWarehouse
	 */
	public void fillList() {
		byte[] sector = _dataWarehouse.getByte(_bankName, "sector");
		if ((sector != null) && (sector.length > 0)) {
			length = sector.length;
			layer = _dataWarehouse.getByte(_bankName, "layer");
			energy = _dataWarehouse.getFloat(_bankName, "energy");
			time = _dataWarehouse.getFloat(_bankName, "time");
			x = _dataWarehouse.getFloat(_bankName, "x");
			y = _dataWarehouse.getFloat(_bankName, "y");
			z = _dataWarehouse.getFloat(_bankName, "z");
			_ppx = new int[length];
			_ppy = new int[length];

		}
		else {
			length = 0;
			layer = null;
			energy = null;
			time = null;
			x = null;
			y = null;
			z = null;
			_ppx = null;
			_ppy = null;
		}

	}

	/**
	 * For feedback
	 * @param index the cluster index
	 * @param pp the location where last drawn
	 */
	public void setLocation(int index, Point pp) {
		_ppx[index] = pp.x;
		_ppy[index] = pp.y;
	}

	/**
	 *
	 * @param index the cluster index
	 * @param pp
	 * @return
	 */
	public boolean contains(int index, Point pp) {
		return ((Math.abs(_ppx[index] - pp.x) <= DataDrawSupport.HITHALF)
				&& (Math.abs(_ppy[index] - pp.y) <= DataDrawSupport.HITHALF));
	}

	/**
	 * Get the azimuthal value of location of the cluster
	 * @param index the cluster index
	 * @return the azimuthal value (phi) of this hit in degrees
	 */
	public double phi(int index) {
		return Math.toDegrees(Math.atan2(y[index], x[index]));
	}

	/**
	 * Add to a list of feedback strings, no doubt because the mouse is ovwer this
	 * hit
	 * @param index the cluster index
	 * @param v the list to add to
	 */
	public void getFeedbackStrings(String prefix, int index, List<String> v) {
		if (v == null) {
			return;
		}

		String hitStr1 = String.format(prefix + " Cluster sect %d  layer %d ", sector[index], layer[index]);
		v.add("$red$" + hitStr1);

		String hitStr2 = String.format("x %7.3fcm  y %7.3fcm  z %7.3fcm", x[index], y[index], z[index]);
		v.add("$red$" + hitStr2);

		String hitStr3 = String.format("energy %6.3f   time %6.3f", energy[index], time[index]);
		v.add("$red$" + hitStr3);

	}


}
