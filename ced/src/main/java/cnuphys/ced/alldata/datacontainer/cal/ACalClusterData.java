package cnuphys.ced.alldata.datacontainer.cal;

import java.awt.Point;
import java.util.ArrayList;

import cnuphys.ced.event.data.DataDrawSupport;

public abstract class ACalClusterData extends ACalData {

	/** the time column for the REC::Calorimeter */
	public ArrayList<Float> time = new ArrayList<>();

	/** the energy column for the REC::Calorimeter */
	public ArrayList<Float> energy = new ArrayList<>();

	/** the x column for the REC::Calorimeter */
	public ArrayList<Float> x = new ArrayList<>();

	/** the y column for the REC::Calorimeter */
	public ArrayList<Float> y = new ArrayList<>();
	
	/** the z column for the REC::Calorimeter */
	public ArrayList<Float> z = new ArrayList<>();
	
	/** cached x coordinate of drawing locations */
	public int ppx[];
	
	/** cached y coordinate of drawing locations */
	public int ppy[];

	@Override
	public void clear() {
		super.clear();
		time.clear();
		energy.clear();
		x.clear();
		y.clear();
		z.clear();
		ppx = null;
		ppy = null;
	}
	
	/**
	 * Set the location where the cluster was last drawn
	 * @param index the index of the cluster
	 * @param pp the location
	 */
	public void setLocation(int index, Point pp) {
		if ((ppx == null) || (ppy == null)) {
			ppx = new int[sector.size()];
			ppy = new int[sector.size()];
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
