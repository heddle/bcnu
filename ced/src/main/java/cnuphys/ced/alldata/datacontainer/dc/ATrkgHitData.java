package cnuphys.ced.alldata.datacontainer.dc;

import java.awt.Point;
import java.util.List;

import org.jlab.io.base.DataBank;
import org.jlab.io.base.DataEvent;

import cnuphys.ced.alldata.DataDrawSupport;
import cnuphys.ced.alldata.DataWarehouse;
import cnuphys.ced.alldata.datacontainer.IDataContainer;

public abstract class ATrkgHitData implements IDataContainer {
	
	// the data warehouse
	protected static DataWarehouse _dataWarehouse = DataWarehouse.getInstance();

	/** 1-based sectors */
	public byte sector[];
	
	/** 1-based superlayers */
	public byte superlayer[];

	/** 1-based layer [1..6] */
	public byte layer[];

	/** 1-based wire */
	public short wire[];
	
	/** hit id */
	public short id[];
	
	/** hit status */
	public short status[];
	
	/** left or right */
	public byte LR[]; 
	
	/** TDC */
	public int TDC[];
	
	/** cluster id */
	public short clusterID[];

	/** track doca */
	public float trkDoca[];
	
	/** track doca */
	public float doca[];

	/** cached x coordinate of drawing locations */
	public int ppx[];

	/** cached y coordinate of drawing locations */
	public int ppy[];
	
	public ATrkgHitData() {
		_dataWarehouse.addDataContainerListener(this);
	}

	@Override
	public void update(DataEvent event) {
		String bankName = bankName();
		DataBank bank = event.getBank(bankName);

		if (bank == null) {
			return;
		}
		
		sector = bank.getByte("sector");
		superlayer = bank.getByte("superlayer");
		layer = bank.getByte("layer");
		wire = bank.getShort("wire");
		id = bank.getShort("id");
		status = bank.getShort("status");
		LR = bank.getByte("LR");
		TDC = bank.getInt("TDC");
		clusterID = bank.getShort("clusterID");
		trkDoca = bank.getFloat("trkDoca");
		doca = bank.getFloat("docaError");
		
		int length = (sector == null) ? 0 : sector.length;
		
		
		//HB doesn't have doca column
		if ((doca == null) && (length > 0)) {
			doca = new float[length];
			for (int i = 0; i < length; i++) {
				doca[i] = -1f;
			}
		}
		
		
		ppx = new int[length];
		ppy = new int[length];
		for (int i = 0; i < length; i++) {
			ppx[i] = 0;
			ppy[i] = 0;
		}
	}
	
	@Override
	public void clear() {
		sector = null;
		superlayer = null;
		layer = null;
		wire = null;
		id = null;
		status = null;
		LR = null;
		TDC = null;
		clusterID = null;
		trkDoca = null;
		doca = null;
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
	 * Provide feedback for a cross
	 * 
	 * @param index           the index of the cluster
	 * @param feedbackStrings add strings to this collection
	 */
	public void feedback(int index, List<String> feedbackStrings) {
		String name = feedbackName();
		String color = "$red$";
		String s1 = String.format("%s%s sect %d supl %d  layer %d  wire %d", color, name, sector[index], superlayer[index],
				layer[index], wire[index]);
		
		feedbackStrings.add(s1);
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
	 * Get the index from the id. Brute force, because
	 * they are not sorted.
	 * @param id the id to match
	 * @return the hit with the matching ID, or -1.
	 */
	public int indexFromId(short id) {

		for (int i = 0; i < count(); i++) {
			if (id == this.id[i]) {
				return i;
			}
		}
		return -1;
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
