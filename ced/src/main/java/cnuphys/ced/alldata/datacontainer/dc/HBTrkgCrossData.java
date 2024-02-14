package cnuphys.ced.alldata.datacontainer.dc;

public class HBTrkgCrossData extends ATrkgCrossData {
	
	// singleton
	private static volatile HBTrkgCrossData _instance;

	
	/**
	 * Public access to the singleton
	 *
	 * @return the singleton
	 */
	public static HBTrkgCrossData getInstance() {
		if (_instance == null) {
			synchronized (HBTrkgCrossData.class) {
				if (_instance == null) {
					_instance = new HBTrkgCrossData();
				}
			}
		}
		return _instance;
	}
	
	@Override
	public String bankName() {
		return "HitBasedTrkg::HBCrosses";
	}


	@Override
	public String feedbackName() {
		return "HBCross";
	}
}
