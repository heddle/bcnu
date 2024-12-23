package cnuphys.ced.alldata.datacontainer;

import java.awt.Point;
import java.util.List;

import cnuphys.ced.alldata.DataDrawSupport;
import cnuphys.ced.alldata.DataWarehouse;
import cnuphys.ced.clasio.ClasIoEventManager;

public abstract class ACommonTrajData implements IDataContainer {


	// the data warehouse
	protected static DataWarehouse _dataWarehouse = DataWarehouse.getInstance();

	// event manager
	protected static ClasIoEventManager _eventManager = ClasIoEventManager.getInstance();


	public short id[];
	public byte detector[];
	public byte layer[];
	public float x[]; //cm
	public float y[];
	public float z[];


	/** cached x coordinate of drawing locations */
	public int ppx[];

	/** cached y coordinate of drawing locations */
	public int ppy[];


	public ACommonTrajData() {
		_dataWarehouse.addDataContainerListener(this);
	}

	@Override
	public void clear() {
		id = null;
		detector = null;
		layer = null;
		x = null;
		y = null;
		z = null;
		ppx = null;
		ppy = null;
	}

	@Override
	public int count() {
		return (x == null) ? 0 : x.length;
	}

	/**
	 * Set the location where the cluster was last drawn
	 * @param index the index of the cluster
	 * @param pp the location
	 */
	public void setLocation(int index, Point pp) {

		int n = (x == null) ? 0 : x.length;
		if (n == 0) {
			return;
		}

		if ((ppx == null) || (ppy == null)) {
			ppx = new int[n];
			ppy = new int[n];
		}
		ppx[index] = pp.x;
		ppy[index] = pp.y;
	}

	/**
	 * Used for hit detection
	 * @param index the cluster index
	 * @param pp the screen point
	 * @return true if the screen point is in the cluster
	 */
	public boolean contains(int index, Point pp) {
		return ((Math.abs(ppx[index] - pp.x) <= DataDrawSupport.HITHALF)
				&& (Math.abs(ppy[index] - pp.y) <= DataDrawSupport.HITHALF));
	}

	/**
	 * Common feedback format for Rec hits
	 * @param name the name
	 * @param i the index of the data
	 * @param feedbackStrings the list of feedback strings
	 */
	public void recTrajFeedback(String name, int i, List<String> feedbackStrings) {

		String fb1 = String.format("$yellow$%s index %d", name, i+1);

		feedbackStrings.add(fb1);
	}




}
