package cnuphys.ced.alldata.datacontainer.dc;

public class HBTrkgAICrossData extends ATrkgCrossData {

	// singleton
	private static volatile HBTrkgAICrossData _instance;

	/**
	 * Public access to the singleton
	 *
	 * @return the singleton
	 */
	public static HBTrkgAICrossData getInstance() {
		if (_instance == null) {
			synchronized (HBTrkgAICrossData.class) {
				if (_instance == null) {
					_instance = new HBTrkgAICrossData();
				}
			}
		}
		return _instance;
	}

	@Override
	public String bankName() {
		return "HitBasedTrkg::AICrosses";
	}

	@Override
	public String feedbackName() {
		return "HBAICross";
	}
}
