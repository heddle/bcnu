package cnuphys.ced.alldata.datacontainer.dc;

public class TBTrkgCrossData extends ATrkgCrossData {
	
	// singleton
	private static volatile TBTrkgCrossData _instance;
	
	/**
	 * Public access to the singleton
	 *
	 * @return the singleton
	 */
	public static TBTrkgCrossData getInstance() {
		if (_instance == null) {
			synchronized (TBTrkgCrossData.class) {
				if (_instance == null) {
					_instance = new TBTrkgCrossData();
				}
			}
		}
		return _instance;
	}


	@Override
	public String bankName() {
		return "TimeBasedTrkg::TBCrosses";
	}
	

	@Override
	public String feedbackName() {
		return "TBCross";
	}

}
