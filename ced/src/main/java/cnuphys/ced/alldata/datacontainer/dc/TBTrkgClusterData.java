package cnuphys.ced.alldata.datacontainer.dc;

public class TBTrkgClusterData extends ATrkgClusterData {

	// singleton
	private static volatile TBTrkgClusterData _instance;

	/**
	 * Public access to the singleton
	 *
	 * @return the singleton
	 */
	public static TBTrkgClusterData getInstance() {
		if (_instance == null) {
			synchronized (TBTrkgClusterData.class) {
				if (_instance == null) {
					_instance = new TBTrkgClusterData();
				}
			}
		}
		return _instance;
	}

	@Override
	public String bankName() {
		return "TimeBasedTrkg::TBClusters";
	}

	@Override
	public String feedbackName() {
		return "TBCluster";
	}
}
