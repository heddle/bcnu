package cnuphys.ced.alldata.datacontainer.dc;

public class HBTrkgAIHitData extends ATrkgHitData {

	// singleton
	private static volatile HBTrkgAIHitData _instance;


	/**
	 * Public access to the singleton
	 *
	 * @return the singleton
	 */
	public static HBTrkgAIHitData getInstance() {
		if (_instance == null) {
			synchronized (HBTrkgAIHitData.class) {
				if (_instance == null) {
					_instance = new HBTrkgAIHitData();
				}
			}
		}
		return _instance;
	}

	@Override
	public String bankName() {
		return "HitBasedTrkg::AIHits";
	}

	@Override
	public String feedbackName() {
        return "HBAIHit";
    }

}
