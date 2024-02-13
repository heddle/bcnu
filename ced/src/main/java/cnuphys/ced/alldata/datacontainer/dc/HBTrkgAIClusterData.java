package cnuphys.ced.alldata.datacontainer.dc;

public class HBTrkgAIClusterData extends ATrkgClusterData {

	// singleton
	private static volatile HBTrkgAIClusterData _instance;

	
	/**
	 * Public access to the singleton
	 *
	 * @return the singleton
	 */
	public static HBTrkgAIClusterData getInstance() {
		if (_instance == null) {
			synchronized (HBTrkgAIClusterData.class) {
				if (_instance == null) {
					_instance = new HBTrkgAIClusterData();
				}
			}
		}
		return _instance;
	}

	@Override
	public String bankName() {
		return "HitBasedTrkg::AIClusters";
	}

	@Override
	public String feedbackName() {
		return "HBAICluster";
	}

}
