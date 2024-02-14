package cnuphys.ced.alldata.datacontainer.dc;

public class TBTrkgAIClusterData extends ATrkgClusterData {

	// singleton
	private static volatile TBTrkgAIClusterData _instance;

	/**
	 * Public access to the singleton
	 *
	 * @return the singleton
	 */
	public static TBTrkgAIClusterData getInstance() {
		if (_instance == null) {
			synchronized (TBTrkgAIClusterData.class) {
				if (_instance == null) {
					_instance = new TBTrkgAIClusterData();
				}
			}
		}
		return _instance;
	}

	@Override
	public String bankName() {
		return "TimeBasedTrkg::AIClusters";
	}

	@Override
	public String feedbackName() {
		return "TBAICluster";
	}
}
