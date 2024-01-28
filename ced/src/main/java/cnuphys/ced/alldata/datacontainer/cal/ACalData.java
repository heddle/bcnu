package cnuphys.ced.alldata.datacontainer.cal;

import java.util.ArrayList;


import cnuphys.ced.alldata.DataWarehouse;
import cnuphys.ced.alldata.datacontainer.IDataContainer;
import cnuphys.ced.clasio.ClasIoEventManager;

public abstract class ACalData implements IDataContainer {
	
	
	// the data warehouse
	protected static DataWarehouse _dataWarehouse = DataWarehouse.getInstance();

	// event manager
	protected static ClasIoEventManager _eventManager = ClasIoEventManager.getInstance();

	public static final int NOPID = -999999;
	
	/** 1-based sectors */
	public ArrayList<Byte> sector = new ArrayList<>();

	/** 0-based views (0, 1, 2) for (u, v, w) */
	public ArrayList<Byte> view = new ArrayList<>();
	
	/**
	 * Create a data container and notify the data warehouse that it wants to be
	 * notified of data events.
     */
	public ACalData() {
		_dataWarehouse.addDataContainerListener(this);
	}


	@Override
	public void clear() {
		sector.clear();
		view.clear();
	}
	
	@Override
	public int count() {
		return sector.size();
	}
	


}
