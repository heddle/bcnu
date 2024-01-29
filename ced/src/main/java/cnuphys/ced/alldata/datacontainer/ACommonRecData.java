package cnuphys.ced.alldata.datacontainer;

import java.awt.Point;

import org.jlab.io.base.DataEvent;

import cnuphys.ced.alldata.DataWarehouse;
import cnuphys.ced.clasio.ClasIoEventManager;
import cnuphys.ced.event.data.DataDrawSupport;

public abstract class ACommonRecData implements IDataContainer {

	
	// the data warehouse
	protected static DataWarehouse _dataWarehouse = DataWarehouse.getInstance();

	// event manager
	protected static ClasIoEventManager _eventManager = ClasIoEventManager.getInstance();

	/** 1-based id */
	public short id[];
		
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
	public ACommonRecData() {
		_dataWarehouse.addDataContainerListener(this);
	}
	
	@Override
	public void clear() {
		id = null;
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
	 * @param pp rge screen point
	 * @return true if the screen point is in the cluster
	 */
	public boolean contains(int index, Point pp) {
		return ((Math.abs(ppx[index] - pp.x) <= DataDrawSupport.HITHALF)
				&& (Math.abs(ppy[index] - pp.y) <= DataDrawSupport.HITHALF));
	}


}
