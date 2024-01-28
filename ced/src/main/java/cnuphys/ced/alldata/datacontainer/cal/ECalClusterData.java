package cnuphys.ced.alldata.datacontainer.cal;

import java.util.ArrayList;

import org.jlab.io.base.DataBank;
import org.jlab.io.base.DataEvent;

public class ECalClusterData extends ACalClusterData {

	
	// singleton
	private static volatile  ECalClusterData _instance;
	

	/** 0-based planes (0, 1) for (inner, outer) */
	public ArrayList<Byte> plane = new ArrayList<>();
	
	/**
	 * Public access to the singleton
	 *
	 * @return the singleton
	 */
	public static ECalClusterData getInstance() {
		if (_instance == null) {
			synchronized (ECalClusterData.class) {
				if (_instance == null) {
					_instance = new ECalClusterData();
				}
			}
		}
		return _instance;
	}

	@Override
	public void clear() {
		super.clear();
		plane.clear();
	}

	@Override
	public void update(DataEvent event) {
		
		//don't need cluster data if accumulating
		if (_eventManager.isAccumulating()) {
			return;
		}
		
		DataBank bank = event.getBank("ECAL::clusters");
		if (bank == null) {
			return;
		}


		byte[] sectorArray = bank.getByte("sector");
		if (sectorArray != null) {
			// layers are 1..3 for PCAL and 4..9 for EC
			byte layerArray[] = bank.getByte("layer");
			float timeArray[] = bank.getFloat("time");
			float energyArray[] = bank.getFloat("energy");
			float xArray[] = bank.getFloat("x");
			float yArray[] = bank.getFloat("y");
			float zArray[] = bank.getFloat("z");

			for (int i = 0; i < sectorArray.length; i++) {

				if (layerArray[i] > 3) { // means it is ECAL, not PCAL
					byte layer = (byte) (layerArray[i] - 4); // 0..5
					byte plane0 = (byte) (layer / 3); // 0, 1, for inner, outer
					byte view0 = (byte) (layer % 3); // 0, 1, 2 for U, V, W

					sector.add(sectorArray[i]);
					plane.add(plane0);
					view.add(view0);
					
					time.add(timeArray[i]);
					energy.add(energyArray[i]);
					x.add(xArray[i]);
					y.add(yArray[i]);
					z.add(zArray[i]);
				}
			}
			ppx = new int[sectorArray.length];
			ppy = new int[sectorArray.length];
		} // end sectorArray not null	}
		
	} // end update
}
