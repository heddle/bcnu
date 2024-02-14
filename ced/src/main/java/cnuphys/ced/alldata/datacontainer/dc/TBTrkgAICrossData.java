package cnuphys.ced.alldata.datacontainer.dc;

public class TBTrkgAICrossData extends ATrkgCrossData {

	
	// singleton
	private static volatile TBTrkgAICrossData _instance;

	/**
	 * Public access to the singleton
	 *
	 * @return the singleton
	 */
	public static TBTrkgAICrossData getInstance() {
		if (_instance == null) {
			synchronized (TBTrkgCrossData.class) {
				if (_instance == null) {
					_instance = new TBTrkgAICrossData();
				}
			}
		}
		return _instance;
	}


	@Override
	public String bankName() {
		return "TimeBasedTrkg::AICrosses";
	}
	

	@Override
	public String feedbackName() {
		return "TBAICross";
	}
}
