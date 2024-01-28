package cnuphys.ced.alldata.datacontainer.cal;

import java.util.ArrayList;

import org.jlab.io.base.DataBank;
import org.jlab.io.base.DataEvent;

import cnuphys.lund.LundId;
import cnuphys.lund.LundSupport;

public abstract class ACalReconData extends ACalData {

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

	/** the pindex into the REC:Particle bank */
	public ArrayList<Short> pIndex = new ArrayList<>();
	
	/** Lund particle ids */
	public int pid[];
	
	@Override
	public void clear() {
		super.clear();
		time.clear();
		energy.clear();
		x.clear();
		y.clear();
		z.clear();
		pIndex.clear();
		pid = null;
	}
	
	/**
	 * Get the cluster drawing radius from the energy
	 * @param energy the energy in GeV
	 * @return the radius in cm
	 */
	public float getRadius(double energy) {
		if (energy < 0.05) {
			return 0;
		}

		float radius = (float) (Math.log((energy + 1.0e-8) / 1.0e-8));
		radius = Math.max(1, Math.min(40f, radius));
		return radius;
	}



	//get the pids from the REC::Particle bank
	//the pindex array points to rows in this bank
	protected void getPIDArray(DataEvent event) {

		pid = null;

		if (count() > 0) {
			DataBank particleBank = event.getBank("REC::Particle");
			if (particleBank != null) {
				pid = particleBank.getInt("pid");
			}
		}

	}
	
	/**
	 * Get the feedback string for the PID
	 * @param index the row
	 * @return the pid string
	 */
	public String getPIDStr(int index) {
		int pidval = getPID(index);

		if (pidval == NOPID) {
			return "REC PID not available";
		} else {
			LundId lundId = getLundId(index);

			if (lundId == null) {
				return "REC PID " + pidval;
			} else {
				return "REC PID " + lundId.getName();
			}
		}
	}

	
	/**
	 * Try to get a pid associated with this index
	 * @param index the index of the row in the REC::Calorimeter table
	 * @return the pid from REC::Particle, or NOPID if fails
	 */
	public int getPID(int index) {
		if ((pid == null) || (index < 0) || (index >= count())) {
			return NOPID;
		}
		
		int pidx = pIndex.get(index);
		return pid[pidx];
	}

	
	/**
	 * Get the LundId object
	 * @param index the index (row)
	 * @return the LindId if available, or <code>null</code>
	 */
	public LundId getLundId(int index) {
		int pid = getPID(index);
		if (pid == NOPID) {
			return null;
		}
		return LundSupport.getInstance().get(pid);
	}
}
