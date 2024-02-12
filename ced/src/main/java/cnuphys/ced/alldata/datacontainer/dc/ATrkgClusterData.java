package cnuphys.ced.alldata.datacontainer.dc;

import java.awt.Point;
import java.util.List;

import cnuphys.ced.alldata.DataWarehouse;
import cnuphys.ced.alldata.datacontainer.IDataContainer;
import cnuphys.ced.clasio.ClasIoEventManager;
import cnuphys.ced.event.data.DataDrawSupport;

public abstract class ATrkgClusterData implements IDataContainer {
	

	// the data warehouse
	protected static DataWarehouse _dataWarehouse = DataWarehouse.getInstance();

	// event manager
	protected static ClasIoEventManager _eventManager = ClasIoEventManager.getInstance();

	/** 1-based sectors */
	byte sector[];
	
	/** 1-based superlayers */
	byte superlayer[];
	
	/** 1-based id */
	short id[];
	
	/** cluster size */
	byte size[];
	
	/** cluster status */
	short status[];
	
	/** average wire */
	float avgWire[];
	
	/** fit chisq probability */
	float fitChisqProb[];
	
	/** fit intercept */
	float fitInterc[];
	
	/** fit intercept error */
	float fitIntercErr[];
	
	/** fit slope */
	float fitSlope[];
	
	/** fit slope error */
	float fitSlopeErr[];
	
	/** cached x coordinate of drawing locations */
	public int ppx[];

	/** cached y coordinate of drawing locations */
	public int ppy[];
	

	
	@Override
	public void clear() {
		sector = null;
		superlayer = null;
		id = null;
		size = null;
		status = null;
		avgWire = null;
		fitChisqProb = null;
		fitInterc = null;
		fitIntercErr = null;
		fitSlope = null;
		fitSlopeErr = null;

		ppx = null;
		ppy = null;
	}


	@Override
	public int count() {
		return (sector == null) ? 0 : sector.length;
	}
	
	/**
	 * Provide feedback for a cross
	 * 
	 * @param index           the index of the cluster
	 * @param feedbackStrings add strings to this collection
	 */
	public void feedback(int index, List<String> feedbackStrings) {
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
	 * Get the name of the trkg cross bank
	 * @return the name of the bank
	 */
	public abstract String bankName();

	/**
	 * Get the name of the trkg fb name
	 * @return the name of the feedback
	 */
	public abstract String feedbackName();


}
