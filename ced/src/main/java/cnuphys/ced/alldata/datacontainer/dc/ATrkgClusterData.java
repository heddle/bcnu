package cnuphys.ced.alldata.datacontainer.dc;

import java.awt.Point;
import java.util.List;

import org.jlab.io.base.DataBank;
import org.jlab.io.base.DataEvent;

import cnuphys.ced.alldata.DataDrawSupport;
import cnuphys.ced.alldata.DataWarehouse;
import cnuphys.ced.alldata.datacontainer.IDataContainer;

public abstract class ATrkgClusterData implements IDataContainer {

	// the data warehouse
	protected static DataWarehouse _dataWarehouse = DataWarehouse.getInstance();

	/** 1-based sectors */
	public byte sector[];
	
	/** 1-based superlayers */
	public byte superlayer[];
	
	/** 1-based id */
	public short id[];
	
	/** cluster size */
	public byte size[];
	
	/** cluster status */
	public short status[];
	
	/** average wire */
	public float avgWire[];
	
	/** fit chisq probability */
	public float fitChisqProb[];
	
	/** fit intercept */
	public float fitInterc[];
	
	/** fit intercept error */
	public float fitIntercErr[];
	
	/** fit slope */
	public float fitSlope[];
	
	/** fit slope error */
	public float fitSlopeErr[];
	
	/** cached x coordinate of drawing locations */
	public int ppx[];

	/** cached y coordinate of drawing locations */
	public int ppy[];
	
	private short Hit1_ID[];
	private short Hit2_ID[];
	private short Hit3_ID[];
	private short Hit4_ID[];
	private short Hit5_ID[];
	private short Hit6_ID[];
	private short Hit7_ID[];
	private short Hit8_ID[];
	private short Hit9_ID[];
	private short Hit10_ID[];
	private short Hit11_ID[];
	private short Hit12_ID[];
	
	/**
	 * Create a data container and notify the data warehouse that it wants to be
	 * notified of data events.
     */
	public ATrkgClusterData() {
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
		id = bank.getShort("id");
		size = bank.getByte("size");
		status = bank.getShort("status");
		avgWire = bank.getFloat("avgWire");
		fitChisqProb = bank.getFloat("fitChisqProb");
		fitInterc = bank.getFloat("fitInterc");
		fitIntercErr = bank.getFloat("fitIntercErr");
		fitSlope = bank.getFloat("fitSlope");
		fitSlopeErr = bank.getFloat("fitSlopeErr");
		
		Hit1_ID = bank.getShort("Hit1_ID");
		Hit2_ID = bank.getShort("Hit2_ID");
		Hit3_ID = bank.getShort("Hit3_ID");
		Hit4_ID = bank.getShort("Hit4_ID");
		Hit5_ID = bank.getShort("Hit5_ID");
		Hit6_ID = bank.getShort("Hit6_ID");
		Hit7_ID = bank.getShort("Hit7_ID");
		Hit8_ID = bank.getShort("Hit8_ID");
		Hit9_ID = bank.getShort("Hit9_ID");
		Hit10_ID = bank.getShort("Hit10_ID");
		Hit11_ID = bank.getShort("Hit11_ID");
		Hit12_ID = bank.getShort("Hit12_ID");
		
		int n = (sector != null) ? sector.length : 0;
		if (n > 0) {
			ppx = new int[n];
			ppy = new int[n];
		}
 	}
	
	public short[] getHitIds(int index) {
		short[] hitIds = new short[12];
		hitIds[0] = (Hit1_ID != null) ? Hit1_ID[index] : -1;
		hitIds[1] = (Hit2_ID != null) ? Hit2_ID[index] : -1;
		hitIds[2] = (Hit3_ID != null) ? Hit3_ID[index] : -1;
		hitIds[3] = (Hit4_ID != null) ? Hit4_ID[index] : -1;
		hitIds[4] = (Hit5_ID != null) ? Hit5_ID[index] : -1;
		hitIds[5] = (Hit6_ID != null) ? Hit6_ID[index] : -1;
		hitIds[6] = (Hit7_ID != null) ? Hit7_ID[index] : -1;
		hitIds[7] = (Hit8_ID != null) ? Hit8_ID[index] : -1;
		hitIds[8] = (Hit9_ID != null) ? Hit9_ID[index] : -1;
		hitIds[9] = (Hit10_ID != null) ? Hit10_ID[index] : -1;
		hitIds[10] = (Hit11_ID != null) ? Hit11_ID[index] : -1;
		hitIds[11] = (Hit12_ID != null) ? Hit12_ID[index] : -1;
		return hitIds;
	}
	
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

		Hit1_ID = null;
		Hit2_ID = null;
		Hit3_ID = null;
		Hit4_ID = null;
		Hit5_ID = null;
		Hit6_ID = null;
		Hit7_ID = null;
		Hit8_ID = null;
		Hit9_ID = null;
		Hit10_ID = null;
		Hit11_ID = null;
		Hit12_ID = null;
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
