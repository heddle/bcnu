package cnuphys.ced.alldata.datacontainer.cal;

import java.util.ArrayList;

import org.jlab.io.base.DataEvent;

public class ECalReconData extends ACalReconDataContainer {

	// singleton
	private static volatile  ECalReconData _instance;
	

	/** 0-based planes (0, 1) for (inner, outer) */
	public ArrayList<Byte> plane = new ArrayList<>();
	
	/**
	 * Public access to the singleton
	 *
	 * @return the singleton
	 */
	public static ECalReconData getInstance() {
		if (_instance == null) {
			synchronized (PCalReconData.class) {
				if (_instance == null) {
					_instance = new ECalReconData();
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

	// update the reconstructed data
	@Override
	public void update(DataEvent event) {
		
		//don't need recon data if accumulating
		if (_eventManager.isAccumulating()) {
			return;
		}

		byte[] sectorArray = event.getBank("REC::Calorimeter").getByte("sector");
		if (sectorArray != null) {
			// layers are 1..3 for PCAL and 4..9 for EC
			byte layerArray[] = event.getBank("REC::Calorimeter").getByte("layer");
			float timeArray[] = event.getBank("REC::Calorimeter").getFloat("time");
			float energyArray[] = event.getBank("REC::Calorimeter").getFloat("energy");
			float xArray[] = event.getBank("REC::Calorimeter").getFloat("x");
			float yArray[] = event.getBank("REC::Calorimeter").getFloat("y");
			float zArray[] = event.getBank("REC::Calorimeter").getFloat("z");
			short pindexArray[] = event.getBank("REC::Calorimeter").getShort("pindex");

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
					
					pIndex.add(pindexArray[i]);
						
				}
			}
			
			//get the pids from the REC::Particle bank
			getPIDArray(event);
		} // end sectorArray not null
	} //

}
