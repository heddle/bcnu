package cnuphys.ced.alldata.datacontainer.cvt;

import java.util.List;

import org.jlab.io.base.DataBank;
import org.jlab.io.base.DataEvent;

import cnuphys.ced.alldata.datacontainer.ACommonTrajData;

public class CVTRecKFTrajData extends ACommonTrajData {

	// singleton
	private static volatile CVTRecKFTrajData _instance;

	public byte[] index;

	/**
	 * Public access to the singleton
	 *
	 * @return the singleton
	 */
	public static CVTRecKFTrajData getInstance() {
		if (_instance == null) {
			synchronized (CVTRecKFTrajData.class) {
				if (_instance == null) {
					_instance = new CVTRecKFTrajData();
				}
			}
		}
		return _instance;
	}

	@Override
	public void clear() {
		super.clear();
		index = null;
	}

	@Override
	public void update(DataEvent event) {
		DataBank bank = event.getBank("CVTRec::KFTrajectory");

		if (bank == null) {
			return;
		}

		id = bank.getShort("id");
		detector = bank.getByte("detector");
		layer = bank.getByte("layer");
		x = bank.getFloat("x");
		y = bank.getFloat("y");
		z = bank.getFloat("z");
		index = bank.getByte("index");


		int n = (x != null) ? x.length : 0;
		if (n > 0) {
			ppx = new int[n];
			ppy = new int[n];
		}

	}

	/**
	 * Common feedback format for Rec hits
	 * @param name the name
	 * @param i the index of the data
	 * @param feedbackStrings the list of feedback strings
	 */
	@Override
	public void recTrajFeedback(String name, int i, List<String> feedbackStrings) {

		String fb1 = String.format("$white$%s index %d", name, i+1);
		String fb2 = String.format("$white$id %d  detector %d  layer %d", id[i], detector[i], layer[i]);
		String fb3 = String.format("$white$(x,y,z) (%6.3f, %6.3f, %6.3f) cm  index %d", x[i], y[i], z[i], index[i]);

		feedbackStrings.add(fb1);
		feedbackStrings.add(fb2);
		feedbackStrings.add(fb3);
	}


}