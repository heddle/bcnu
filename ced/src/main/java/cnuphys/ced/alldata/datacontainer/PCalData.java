package cnuphys.ced.alldata.datacontainer;

import java.awt.Color;
import java.util.ArrayList;

import org.jlab.io.base.DataBank;
import org.jlab.io.base.DataEvent;

import cnuphys.ced.alldata.DataWarehouse;
import cnuphys.ced.clasio.ClasIoEventManager;
import cnuphys.ced.event.data.AdcColorScale;
import cnuphys.lund.LundId;
import cnuphys.lund.LundSupport;

public class PCalData implements IDataContainer {
	
	public static final int NOPID = -999999;


	// the singleton
	private static volatile PCalData _instance;

	// the data warehouse
	private static DataWarehouse _dataWarehouse = DataWarehouse.getInstance();

	// event manager
	private static ClasIoEventManager _eventManager = ClasIoEventManager.getInstance();

	// the data in arrays lists due to the way the data is stored in the banks

	/** 1-based sectors */
	public ArrayList<Byte> sector = new ArrayList<>();

	/** 0-based views (0, 1, 2) for (u, v, w) */
	public ArrayList<Byte> view = new ArrayList<>();

	/** 1-based strips */
	public ArrayList<Short> strip = new ArrayList<>();

	/** adc values */
	public ArrayList<Integer> adc = new ArrayList<>();

	/** tdc values */
	public ArrayList<Integer> tdc = new ArrayList<>();
	
	/** max adc value */
	public int maxADC;
	
	/** 1-based sectors for REC::Calorimeter */
	public ArrayList<Byte> recSector = new ArrayList<>();

	/** 0-based views (0, 1, 2) for (u, v, w) for REC::Calorimeter */
	public ArrayList<Byte> recView = new ArrayList<>();
	
	/** the time column for the REC::Calorimeter */
	public ArrayList<Float> recTime = new ArrayList<>();

	/** the energy column for the REC::Calorimeter */
	public ArrayList<Float> recEnergy = new ArrayList<>();

	/** the x column for the REC::Calorimeter */
	public ArrayList<Float> recX = new ArrayList<>();

	/** the y column for the REC::Calorimeter */
	public ArrayList<Float> recY = new ArrayList<>();
	
	/** the z column for the REC::Calorimeter */
	public ArrayList<Float> recZ = new ArrayList<>();

	/** the pindex into the REC:Particle bank */
	public ArrayList<Short> recPIndex = new ArrayList<>();
	
	/** Lund particle ids */
	public int pid[];


	//a list of all the lists
	ArrayList[] lists = {sector, view, strip, adc, tdc,
            recSector, recView, recTime, recEnergy, recX, recY, recZ, recPIndex};


	// private constructor for singleton
	private PCalData() {
		_dataWarehouse.addDataContainerListener(this);
	}

	/**
	 * Public access to the singleton
	 * 
	 * @return the singleton
	 */
	public static PCalData getInstance() {
		if (_instance == null) {
			synchronized (PCalData.class) {
				if (_instance == null) {
					_instance = new PCalData();
				}
			}
		}
		return _instance;
	}

	@Override
	public void clear() {
        for (ArrayList list : lists) {
            list.clear();
        }
	}


	@Override
	public void update(DataEvent event) {
		if (event != null) {
			updateRaw(event);
			updateRecon(event);
		}
	}

	public void updateRaw(DataEvent event) {
		if (!_eventManager.isAccumulating()) {
			byte[] sectorArray = event.getBank("ECAL::adc").getByte("sector");
			if (sectorArray != null) {
				// layers are 1..3 for PCAL and 4..9 for EC
				byte layerArray[] = event.getBank("ECAL::adc").getByte("layer");
				short componentArray[] = event.getBank("ECAL::adc").getShort("component");
				int adcArray[] = event.getBank("ECAL::adc").getInt("ADC");
				int tdcArray[] = event.getBank("ECAL::tdc").getInt("TDC");

				for (int i = 0; i < sectorArray.length; i++) {

					if (layerArray[i] < 4) { // means it is PCAL, not ECAL
						byte layer = (byte) (layerArray[i] - 1); // 0..2
						byte view0 = (byte) (layer % 3); // 0, 1, 2 for U, V, W

						sector.add(sectorArray[i]);
						view.add(view0);
						strip.add(componentArray[i]);
						adc.add(adcArray[i]);
						tdc.add(tdcArray[i]);

					}

				} // end loop over sector array
			} // end sectorArray not null

			// get the max adc
			int n = adc.size();
			maxADC = 0;

			if (n > 0) {
				for (int i = 0; i < n; i++) {
					int a = adc.get(i);
					if (a > maxADC) {
						maxADC = a;
					}
				}
			}

		} // end not accumulating
	}
	
	// update the reconstructed data
	private void updateRecon(DataEvent event) {
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

				if (layerArray[i] < 4) { // means it is PCAL, not ECAL
					byte layer = (byte) (layerArray[i] - 1); // 0..2
					byte view0 = (byte) (layer % 3); // 0, 1, 2 for U, V, W

					recSector.add(sectorArray[i]);
					recView.add(view0);
					
					recTime.add(timeArray[i]);
					recEnergy.add(energyArray[i]);
					recX.add(xArray[i]);
					recY.add(yArray[i]);
					recZ.add(zArray[i]);
					
					recPIndex.add(pindexArray[i]);
						
				}
			}
			
			//get the pids from the REC::Particle bank
			getPIDArray(event);
		} // end sectorArray not null
	}


	@Override
	public int rawCount() {
		return sector.size();
	}

	@Override
	public Color getADCColor(int adc) {
		if (adc > 0) {
			double fract = ((double) adc) / maxADC;
			fract = Math.max(0, Math.min(1.0, fract));
			int alpha = 128 + (int) (127 * fract);
			alpha = Math.min(255, alpha);

			return AdcColorScale.getInstance().getAlphaColor(fract, alpha);
		}
		return ADCZERO;
	}

	
	@Override
	public int clusterCount() {
		return 0;
	}

	@Override
	public int recCount() {
		return recSector.size();
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
	private void getPIDArray(DataEvent event) {

		pid = null;

		if (recCount() > 0) {
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
		if ((pid == null) || (index < 0) || (index >= recCount())) {
			return NOPID;
		}
		
		int pidx = recPIndex.get(index);
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
