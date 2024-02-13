package cnuphys.ced.alldata.datacontainer.dc;

public class TBTrkgAIHitData extends ATrkgHitData {

	// singleton
	private static volatile TBTrkgAIHitData _instance;

	/**
	 * Public access to the singleton
	 *
	 * @return the singleton
	 */
	public static TBTrkgAIHitData getInstance() {
		if (_instance == null) {
			synchronized (TBTrkgAIHitData.class) {
				if (_instance == null) {
					_instance = new TBTrkgAIHitData();
				}
			}
		}
		return _instance;
	}

	@Override
	public String bankName() {
		return "TimeBasedTrkg::AIHits";
	}

	@Override
	public String feedbackName() {
		return "TBAIHit";
	}

}
