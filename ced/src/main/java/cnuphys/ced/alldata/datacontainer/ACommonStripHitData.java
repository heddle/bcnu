package cnuphys.ced.alldata.datacontainer;

import java.awt.Point;
import java.util.List;

import cnuphys.ced.alldata.DataWarehouse;
import cnuphys.ced.clasio.ClasIoEventManager;
import cnuphys.ced.event.data.DataDrawSupport;

public abstract class ACommonStripHitData implements IDataContainer {

	// the data warehouse
	protected static DataWarehouse _dataWarehouse = DataWarehouse.getInstance();

	// event manager
	protected static ClasIoEventManager _eventManager = ClasIoEventManager.getInstance();

	/** 1-based sectors */
	public byte[] sector;

	/** 1-based layer*/
	public byte layer[];

	/** 1-based strip*/
	public short strip[];

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
	public ACommonStripHitData() {
		_dataWarehouse.addDataContainerListener(this);
	}

	@Override
	public void clear() {
		sector = null;
		layer = null;
	    strip = null;
		energy = null;
		time = null;
		ppx = null;
		ppy = null;
	}


	@Override
	public int count() {
        return (sector == null) ? 0 : sector.length;
    }

	/**
	 * Set the location where the cluster was last drawn
	 * @param index the index of the cluster
	 * @param pp the location
	 */
	public void setLocation(int index, Point pp) {

		int n = (sector == null) ? 0 : sector.length;
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
	 * @param name the name of the detector
	 * @param index the index of the data
	 * @param feedbackStrings the list of feedback strings
	 */
	public void hitFeedback(String name, int index, List<String> feedbackStrings) {

		String s1 = String.format("$wheat$%s  sector %d layer %d strip %d",
				name, sector[index], layer[index], strip[index]);
		String s2 = String.format("$wheat$energy %6.3f time %6.3f", energy[index], time[index]);

		feedbackStrings.add(s1);
		feedbackStrings.add(s2);
	}



}
