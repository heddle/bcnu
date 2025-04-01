package cnuphys.ced.alldata.datacontainer.dc;

public class HBTrkgAISegmentData extends ATrkgSegmentData {

	// singleton
	private static volatile HBTrkgAISegmentData _instance;

	/**
	 * Public access to the singleton
	 *
	 * @return the singleton
	 */
	public static HBTrkgAISegmentData getInstance() {
		if (_instance == null) {
			synchronized (HBTrkgAISegmentData.class) {
				if (_instance == null) {
					_instance = new HBTrkgAISegmentData();
				}
			}
		}
		return _instance;
	}


	@Override
	public String bankName() {
		return "HitBasedTrkg::AISegments";
	}

	@Override
	public String feedbackName() {
		return "HBAISegment";
	}

}
