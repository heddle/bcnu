package cnuphys.ced.alldata.datacontainer.cvt;

import org.jlab.io.base.DataBank;
import org.jlab.io.base.DataEvent;

import cnuphys.ced.alldata.DataWarehouse;
import cnuphys.ced.alldata.datacontainer.IDataContainer;

public class CosmicData implements IDataContainer {
	
	// the data warehouse
	protected static DataWarehouse _dataWarehouse = DataWarehouse.getInstance();

	
	// singleton
	private static volatile CosmicData _instance;
	
	/** ID */
	public short ID[];
	
	public float trkline_yx_interc[];
	public float trkline_yx_slope[];
	public float trkline_yz_interc[];
	public float trkline_yz_slope[];
	
	public float chi2[];
	public float phi[];
	public float theta[];

	/**
	 * Private constructor for singleton
	 */
	private CosmicData() {
		_dataWarehouse.addDataContainerListener(this);
	}
	
	/**
	 * Public access to the singleton
	 *
	 * @return the singleton
	 */
	public static CosmicData getInstance() {
		if (_instance == null) {
			synchronized (CosmicData.class) {
				if (_instance == null) {
					_instance = new CosmicData();
				}
			}
		}
		return _instance;
	}


	@Override
	public void clear() {
		ID = null;
		trkline_yx_interc = null;
		trkline_yx_slope = null;
		trkline_yz_interc = null;
		trkline_yz_slope = null;
		chi2 = null;
		phi = null;
		theta = null;
	}

	@Override
	public void update(DataEvent event) {
		DataBank bank = event.getBank("CVTRec::Cosmics");

		if (bank == null) {
			return;
		}
		
		ID = bank.getShort("ID");
		trkline_yx_interc = bank.getFloat("trkline_yx_interc");
		trkline_yx_slope = bank.getFloat("trkline_yx_slope");
		trkline_yz_interc = bank.getFloat("trkline_yz_interc");
		trkline_yz_slope = bank.getFloat("trkline_yz_slope");
		chi2 = bank.getFloat("chi2");
		phi = bank.getFloat("phi");
		theta = bank.getFloat("theta");
		
	}

	@Override
	public int count() {
		return (phi == null) ? 0 : phi.length;
	}

}
