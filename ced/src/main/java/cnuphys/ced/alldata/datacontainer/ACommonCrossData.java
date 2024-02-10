package cnuphys.ced.alldata.datacontainer;

import java.awt.Point;
import java.util.List;

import cnuphys.ced.alldata.DataWarehouse;
import cnuphys.ced.clasio.ClasIoEventManager;
import cnuphys.ced.event.data.DataDrawSupport;

public abstract class ACommonCrossData implements IDataContainer {


	// the data warehouse
	protected static DataWarehouse _dataWarehouse = DataWarehouse.getInstance();

	// event manager
	protected static ClasIoEventManager _eventManager = ClasIoEventManager.getInstance();

	/** 1-based sectors */
	public byte[] sector;

	/** 1-based region */
	public byte[] region;
	
	/** the cross x */
	public float x[];

	/** the cross y */
	public float y[];

	/** the cross z */
	public float z[];
	
	/** the cross x error */
	public float err_x[];

	/** the cross y error */
	public float err_y[];

	/** the cross z error */
	public float err_z[];
	
	/** the cross x direction */
	public float ux[];

	/** the cross y direction */
	public float uy[];

	/** the cross z direction*/
	public float uz[];
	
	/** cluster 1 index */
	public short cluster1ndex[];
	
	/** cluster 2 index */
	public short cluster2ndex[];
	
	/** cached x coordinate of drawing locations */
	public int ppx[];

	/** cached y coordinate of drawing locations */
	public int ppy[];
	
	/**
	 * Create a data container and notify the data warehouse that it wants to be
	 * notified of data events.
     */
	public ACommonCrossData() {
		_dataWarehouse.addDataContainerListener(this);
	}

	@Override
	public void clear() {
		sector = null;
		region = null;
		x = null;
		y = null;
		z = null;
		err_x = null;
		err_y = null;
		err_z = null;
		ux = null;
		uy = null;
		uz = null;
		cluster1ndex = null;
		cluster2ndex = null;

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

	/**
	 * Provide feedback for a cross
	 * 
	 * @param detectorName    the name of the detector
	 * @param index           the index of the cluster
	 * @param feedbackStrings add strings to this collection
	 */
	public void feedback(String detectorName, int index, List<String> feedbackStrings) {
		feedbackStrings.add(String.format("$Forest Green$%s cross xyz (%-6.3f, %-6.3f, %-6.3f) cm", detectorName,
				x[index], y[index], z[index]));
		feedbackStrings.add(String.format("$Forest Green$%s cross error (%-6.3f, %-6.3f, %-6.3f) cm", detectorName,
				err_x[index], err_y[index], err_z[index]));
		feedbackStrings.add(String.format("$Forest Green$%s cross direction (%-6.3f, %-6.3f, %-6.3f)", detectorName,
				ux[index], uy[index], uz[index]));
	}
}
