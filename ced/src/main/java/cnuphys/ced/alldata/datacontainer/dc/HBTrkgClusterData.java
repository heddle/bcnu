package cnuphys.ced.alldata.datacontainer.dc;

public class HBTrkgClusterData extends ATrkgClusterData {

	// singleton
	private static volatile HBTrkgClusterData _instance;

	
	/**
	 * Public access to the singleton
	 *
	 * @return the singleton
	 */
	public static HBTrkgClusterData getInstance() {
		if (_instance == null) {
			synchronized (HBTrkgClusterData.class) {
				if (_instance == null) {
					_instance = new HBTrkgClusterData();
				}
			}
		}
		return _instance;
	}

	@Override
	public String bankName() {
		return "HitBasedTrkg::HBClusters";
	}

	@Override
	public String feedbackName() {
		return "HBCluster";
	}

}
