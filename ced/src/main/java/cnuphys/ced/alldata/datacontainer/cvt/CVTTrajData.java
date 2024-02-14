package cnuphys.ced.alldata.datacontainer.cvt;

import java.util.List;

import org.jlab.io.base.DataBank;
import org.jlab.io.base.DataEvent;

import cnuphys.ced.alldata.datacontainer.ACommonTrajData;

public class CVTTrajData extends ACommonTrajData {
	
	//used for DISPLAY P1 TRAJ *
	// singleton
	private static volatile CVTTrajData _instance;
	
	public byte sector[];
	public float phi[];
	public float theta[];
	public float langle[];
	public float centroid[];
	public float path[];


	/**
	 * Public access to the singleton
	 *
	 * @return the singleton
	 */
	public static CVTTrajData getInstance() {
		if (_instance == null) {
			synchronized (CVTTrajData.class) {
				if (_instance == null) {
					_instance = new CVTTrajData();
				}
			}
		}
		return _instance;
	}
	
	@Override
	public void clear() {
		super.clear();
		sector = null;
		phi = null;
		theta = null;
		langle = null;
		centroid = null;
		path = null;
	}


	@Override
	public void update(DataEvent event) {
		DataBank bank = event.getBank("CVT::Trajectory");

		if (bank == null) {
			return;
		}
		
		id = bank.getShort("id");
		detector = bank.getByte("detector");
		sector = bank.getByte("sector");
		layer = bank.getByte("layer");
		x = bank.getFloat("x");
		y = bank.getFloat("y");
		z = bank.getFloat("z");
		phi = bank.getFloat("phi");
		theta = bank.getFloat("theta");
		langle = bank.getFloat("langle");
		centroid = bank.getFloat("centroid");
		path = bank.getFloat("path");
		
		
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

		String fb1 = String.format("$yellow$%s index %d", name, i+1);
		String fb2 = String.format("$yellow$id %d  detector %d  sector %d  layer %d", id[i], detector[i], sector[i], layer[i]);
		String fb3 = String.format("$yellow$(x,y,z) (%6.3f, %6.3f, %6.3f) cm  path %6.3f", x[i], y[i], z[i], path[i]);
		String fb4 = String.format("$yellow$phi %6.3f  theta %6.3f  langle %5.2f  cent %5.2f", phi[i], theta[i], langle[i], centroid[i]);

		feedbackStrings.add(fb1);
		feedbackStrings.add(fb2);
		feedbackStrings.add(fb3);
		feedbackStrings.add(fb4);
	}



}
