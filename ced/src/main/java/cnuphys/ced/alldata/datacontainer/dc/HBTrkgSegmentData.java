package cnuphys.ced.alldata.datacontainer.dc;

public class HBTrkgSegmentData extends ATrkgSegmentData {
	
	// singleton
	private static volatile HBTrkgSegmentData _instance;
	
	/**
	 * Public access to the singleton
	 *
	 * @return the singleton
	 */
	public static HBTrkgSegmentData getInstance() {
		if (_instance == null) {
			synchronized (HBTrkgSegmentData.class) {
				if (_instance == null) {
					_instance = new HBTrkgSegmentData();
				}
			}
		}
		return _instance;
	}

	@Override
	public String bankName() {
		return "HitBasedTrkg::HBSegments";
	}

	@Override
	public String feedbackName() {
		return "HBSegment";
	}

}
