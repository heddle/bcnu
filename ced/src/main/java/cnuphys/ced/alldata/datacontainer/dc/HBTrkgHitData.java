package cnuphys.ced.alldata.datacontainer.dc;

public class HBTrkgHitData extends ATrkgHitData {
	
	// singleton
	private static volatile HBTrkgHitData _instance;

	
	/**
	 * Public access to the singleton
	 *
	 * @return the singleton
	 */
	public static HBTrkgHitData getInstance() {
		if (_instance == null) {
			synchronized (HBTrkgHitData.class) {
				if (_instance == null) {
					_instance = new HBTrkgHitData();
				}
			}
		}
		return _instance;
	}
	
	@Override
	public String bankName() {
		return "HitBasedTrkg::HBHits";
	}


	@Override
	public String feedbackName() {
		return "HBHit";
	}

}
