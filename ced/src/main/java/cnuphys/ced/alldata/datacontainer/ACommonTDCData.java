package cnuphys.ced.alldata.datacontainer;

import cnuphys.ced.alldata.DataWarehouse;
import cnuphys.ced.clasio.ClasIoEventManager;

public abstract class ACommonTDCData implements IDataContainer {
	
	
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

	/** Used for subdivisions like left/right */
	public byte order[];
	
	/** tdc value */
	public int tdc[];

	/**
	 * Create a data container and notify the data warehouse that it wants to be
	 * notified of data events.
     */
	public ACommonTDCData() {
		_dataWarehouse.addDataContainerListener(this);
	}


	@Override
	public void clear() {
		sector = null;
		layer = null;
		component = null;
		order = null;
		tdc = null;
	}

	@Override
	public int count() {
		return (sector == null) ? 0 : sector.length;
	}

}
