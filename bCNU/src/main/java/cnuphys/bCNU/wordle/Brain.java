package cnuphys.bCNU.wordle;

public class Brain {
	private static volatile Brain _instance;

	private Brain() {
		// Singleton
	}

	public static Brain getInstance() {
		if (_instance == null) {
			_instance = new Brain();
		}
		return _instance;
	}
	
	

}
