package cnuphys.ced.alldata.datacontainer.dc;

public class TBTrkgHitData extends ATrkgHitData {

	// singleton
	private static volatile TBTrkgHitData _instance;


	/**
	 * Public access to the singleton
	 *
	 * @return the singleton
	 */
	public static TBTrkgHitData getInstance() {
		if (_instance == null) {
			synchronized (TBTrkgHitData.class) {
				if (_instance == null) {
					_instance = new TBTrkgHitData();
				}
			}
		}
		return _instance;
	}

	@Override
	public String bankName() {
		return "TimeBasedTrkg::TBHits";
	}

	@Override
	public String feedbackName() {
        return "TBHit";
	}

}
