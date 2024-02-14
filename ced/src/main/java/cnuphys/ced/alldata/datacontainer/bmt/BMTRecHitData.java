package cnuphys.ced.alldata.datacontainer.bmt;

import java.util.List;

import org.jlab.io.base.DataBank;
import org.jlab.io.base.DataEvent;

import cnuphys.ced.alldata.datacontainer.ACommonStripHitData;

public class BMTRecHitData extends ACommonStripHitData {
	
	// singleton
	private static volatile BMTRecHitData _instance;
	
	/** the hit  ID */
	public short ID[];
	
	/** the hit  status */
	public byte status[];
	
	/** the hit fit residual */
	public float fitResidual[];
	
	/** the hit cluster */
	public short clusterID[];
	
	/** the hit track ID */
	public short trkID[];

	
	/**
	 * Public access to the singleton
	 *
	 * @return the singleton
	 */
	public static BMTRecHitData getInstance() {
		if (_instance == null) {
			synchronized (BMTRecHitData.class) {
				if (_instance == null) {
					_instance = new BMTRecHitData();
				}
			}
		}
		return _instance;
	}

	@Override
	public void clear() {
		super.clear();
		ID = null;
		status = null;
		fitResidual = null;
		clusterID = null;
		trkID = null;
	}

	@Override
	public void update(DataEvent event) {
		DataBank bank = event.getBank("BMTRec::Hits");

		if (bank == null) {
			return;
		}
		
		sector = bank.getByte("sector");
		layer = bank.getByte("layer");
		strip = bank.getShort("strip");
		energy = bank.getFloat("energy");
		time = bank.getFloat("time");
		ID = bank.getShort("ID");
		status = bank.getByte("status");
		fitResidual = bank.getFloat("fitResidual");
		clusterID = bank.getShort("clusterID");
		trkID = bank.getShort("trkID");
		
		int n = (sector != null) ? sector.length : 0;
		if (n > 0) {
			ppx = new int[n];
			ppy = new int[n];
		}
		
	}
	
	/**
	 * Common feedback format for hits
	 * @param name the name of the detector
	 * @param index the index of the data
	 * @param feedbackStrings the list of feedback strings
	 */
	public void hitFeedback(int index, List<String> feedbackStrings) {
		super.hitFeedback("BMTRecHit", index, feedbackStrings);
        feedbackStrings.add("$wheat$BMTRecHit ID: " + ID[index]);
        feedbackStrings.add("$wheat$BMTRecHit status: " + status[index]);
        feedbackStrings.add("$wheat$BMTRecHit fit residual: " + fitResidual[index]);
        feedbackStrings.add("$wheat$BMTRecHit cluster ID: " + clusterID[index]);
        feedbackStrings.add("$wheat$BMTRecHit track ID: " + trkID[index]);
	}

}
