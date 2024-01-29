package cnuphys.ced.alldata.datacontainer;

import java.awt.Point;

import cnuphys.ced.alldata.DataWarehouse;
import cnuphys.ced.clasio.ClasIoEventManager;
import cnuphys.ced.event.data.DataDrawSupport;

public abstract class ACommonClusterData implements IDataContainer {
	
	
	// the data warehouse
	protected static DataWarehouse _dataWarehouse = DataWarehouse.getInstance();

	// event manager
	protected static ClasIoEventManager _eventManager = ClasIoEventManager.getInstance();

	
	/** 1-based sectors */
	public byte[] sector;

	/** 1-based layer*/
	public byte layer[];
	
	/** 1-based id */
	public short id[];
	
	/** a status value */
	public short status[];
	
	/** the cluster energy */
	public float energy[];

	/** the cluster time */
	public float time[];
	
	/** the cluster x */
	public float x[];
	
	/** the cluster y */
	public float y[];
	
	/** the cluster z */
	public float z[];
	
	/** cached x coordinate of drawing locations */
	public int ppx[];
	
	/** cached y coordinate of drawing locations */
	public int ppy[];

	
	/**
	 * Create a data container and notify the data warehouse that it wants to be
	 * notified of data events.
     */
	public ACommonClusterData() {
		_dataWarehouse.addDataContainerListener(this);
	}


	@Override
	public void clear() {
		sector = null;
		layer = null;
		id = null;
		status = null;
		energy = null;
		time = null;
		x = null;
		y = null;
		z = null;
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
	 * @param pp rge screen point
	 * @return true if the screen point is in the cluster
	 */
	public boolean contains(int index, Point pp) {
		return ((Math.abs(ppx[index] - pp.x) <= DataDrawSupport.HITHALF)
				&& (Math.abs(ppy[index] - pp.y) <= DataDrawSupport.HITHALF));
	}

}
