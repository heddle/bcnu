package cnuphys.ced.alldata.datacontainer.dc;

public class TBTrkgAISegmentData extends ATrkgSegmentData {

	// singleton
	private static volatile TBTrkgAISegmentData _instance;

	/**
	 * Public access to the singleton
	 *
	 * @return the singleton
	 */
	public static TBTrkgAISegmentData getInstance() {
		if (_instance == null) {
			synchronized (TBTrkgAISegmentData.class) {
				if (_instance == null) {
					_instance = new TBTrkgAISegmentData();
				}
			}
		}
		return _instance;
	}

	@Override
	public String bankName() {
		return "TimeBasedTrkg::AISegments";
	}

	@Override
	public String feedbackName() {
		return "TBAISegment";
	}

}
