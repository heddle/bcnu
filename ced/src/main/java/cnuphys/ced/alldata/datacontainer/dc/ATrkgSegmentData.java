package cnuphys.ced.alldata.datacontainer.dc;

import java.awt.Point;
import java.util.List;

import org.jlab.io.base.DataBank;
import org.jlab.io.base.DataEvent;

import cnuphys.ced.alldata.DataDrawSupport;
import cnuphys.ced.alldata.DataWarehouse;
import cnuphys.ced.alldata.datacontainer.IDataContainer;
import cnuphys.ced.clasio.ClasIoEventManager;

public abstract class ATrkgSegmentData implements IDataContainer {
	
	//for feedback
	private static final String color = "powder blue";
	
	// the data warehouse
	protected static DataWarehouse _dataWarehouse = DataWarehouse.getInstance();

	// event manager
	protected static ClasIoEventManager _eventManager = ClasIoEventManager.getInstance();

	/** 1-based sectors */
	public byte[] sector;

    /** 1-based superlayers */
	public byte[] superlayer;
	
	/** x coordinate of one endpoint */
	public float x1[];
	
    /** z coordinate of one endpoint */
    public float z1[];
    
    /** x coordinate of the other endpoint */
    public float x2[];
    
    /** z coordinate of the other endpoint */
    public float z2[];
    
	/** cached x coordinate of drawing locations */
	public int ppx1[];

	/** cached y coordinate of drawing locations */
	public int ppy1[];
	
	/** cached x coordinate of drawing locations */
	public int ppx2[];
	
	/** cached y coordinate of drawing locations */
	public int ppy2[];
	
	/**
	 * Create a data container and notify the data warehouse that it wants to be
	 * notified of data events.
     */
	public ATrkgSegmentData() {
		_dataWarehouse.addDataContainerListener(this);
	}

	@Override
	public void clear() {
		sector = null;
		superlayer = null;
		x1 = null;
		z1 = null;
		x2 = null;
		z2 = null;

		ppx1 = null;
		ppy1 = null;
		ppx2 = null;
		ppy2 = null;
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
		x1 = bank.getFloat("SegEndPoint1X");
		z1 = bank.getFloat("SegEndPoint1Z");
		x2 = bank.getFloat("SegEndPoint2X");
		z2 = bank.getFloat("SegEndPoint2Z");
		
		
		int n = (sector == null) ? 0 : sector.length;
		if (n > 0) {
			ppx1 = new int[n];
			ppy1 = new int[n];
			ppx2 = new int[n];
			ppy2 = new int[n];
		}
	}
	
	/**
	 * Set the location where the cross was last drawn
	 * @param index the index of the cross
	 * @param pp1 the location of one endpoint
	 * @param pp2 the location of the other endpoint
	 */
	public void setLocation(int index, Point pp1, Point  pp2) {

		int n = (sector == null) ? 0 : sector.length;
		if (n == 0) {
			return;
		}

		if (ppx1 == null) {
			ppx1 = new int[n];
			ppy1 = new int[n];
			ppx2 = new int[n];
			ppy2 = new int[n];
		}
		ppx1[index] = pp1.x;
		ppy1[index] = pp1.y;
		ppx2[index] = pp2.x;
		ppy2[index] = pp2.y;
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
		String name = feedbackName();
		String s1 = String.format("%s%s sector %d, superlayer: %d", color, name, sector[index], superlayer[index]);
		feedbackStrings.add(s1);
	}

	/**
	 * Used for hit detection
	 * 
	 * @param index the cluster index
	 * @param pp    the screen point
	 * @return true if the screen point is in the cluster
	 */
	public boolean contains(int index, Point pp) {
		return (subcontains(index, pp, ppx1[index], ppy1[index]) || subcontains(index, pp, ppx2[index], ppy2[index]));
	}

	private boolean subcontains(int index, Point sp, int x, int y) {
		return ((Math.abs(sp.x - x) <= DataDrawSupport.HITHALF)
				&& (Math.abs(sp.y - y) <= DataDrawSupport.HITHALF));

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
