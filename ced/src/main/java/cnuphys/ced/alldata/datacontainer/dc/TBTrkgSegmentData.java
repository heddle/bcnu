package cnuphys.ced.alldata.datacontainer.dc;

public class TBTrkgSegmentData extends ATrkgSegmentData  {

	// singleton
	private static volatile TBTrkgSegmentData _instance;

	/**
	 * Public access to the singleton
	 *
	 * @return the singleton
	 */
	public static TBTrkgSegmentData getInstance() {
		if (_instance == null) {
			synchronized (TBTrkgSegmentData.class) {
				if (_instance == null) {
					_instance = new TBTrkgSegmentData();
				}
			}
		}
		return _instance;
	}

	@Override
	public String bankName() {
		return "TimeBasedTrkg::TBSegments";
	}

	@Override
	public String feedbackName() {
		return "TBSegment";
	}

}
