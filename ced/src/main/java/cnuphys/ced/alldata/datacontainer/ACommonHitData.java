package cnuphys.ced.alldata.datacontainer;

import java.awt.Point;
import java.util.List;

import cnuphys.ced.alldata.DataWarehouse;
import cnuphys.ced.clasio.ClasIoEventManager;
import cnuphys.ced.event.data.DataDrawSupport;

public abstract class ACommonHitData implements IDataContainer {


	// the data warehouse
	protected static DataWarehouse _dataWarehouse = DataWarehouse.getInstance();

	// event manager
	protected static ClasIoEventManager _eventManager = ClasIoEventManager.getInstance();

	/** 1-based sectors */
	public byte[] sector;

	/** 1-based layer*/
	public byte layer[];

	/** 1-based component*/
	public short component[];

	/** 1-based id */
	public short id[];

	/** the hit x */
	public float x[];

	/** the hit y */
	public float y[];

	/** the hit z */
	public float z[];

	/** the hit energy */
	public float energy[];

	/** the hit time */
	public float time[];

	/** cached x coordinate of drawing locations */
	public int ppx[];

	/** cached y coordinate of drawing locations */
	public int ppy[];

	/**
	 * Create a data container and notify the data warehouse that it wants to be
	 * notified of data events.
     */
	public ACommonHitData() {
		_dataWarehouse.addDataContainerListener(this);
	}

	@Override
	public void clear() {
		sector = null;
		layer = null;
	    component = null;
		id = null;
		x = null;
		y = null;
		z = null;
		ppx = null;
		ppy = null;
		energy = null;
		time = null;
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
	 * Common feedback format for hits
	 * @param detectorName the name of the detector
	 * @param index the index of the data
	 * @param feedbackStrings the list of feedback strings
	 */
	public void hitFeedback(String detectorName, int index, List<String> feedbackStrings) {

		int idv = (id == null) ? 0 : id[index];
		String s = String.format("$wheat$%s id %d hit loc (%5.2f, %5.2f, %5.2f) cm",
				detectorName, idv, x[index], y[index], z[index]);

		if (!feedbackStrings.contains(s)) {
			feedbackStrings.add(s);
		}
	}



}