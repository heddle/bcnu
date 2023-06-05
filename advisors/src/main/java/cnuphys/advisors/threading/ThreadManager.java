package cnuphys.advisors.threading;


public class ThreadManager  {
	
	//singleton
	private static ThreadManager _instance;
	
	//the blocking (with wait/notify) queue
	private BlockingFIFO<ILaunchable> _fifo = new BlockingFIFO<>();
	
	private LaunchReader _reader = new LaunchReader(_fifo);
	
	//the dequing thread
	
	
	//singleton constructor
	private ThreadManager() {
	}
	
	/**
	 * Accessor for the singleton
	 * @return the ThreadManager
	 */
	public static ThreadManager getInstance() {
		if (_instance == null) {
			_instance = new ThreadManager();
		}
		return _instance;
		
	}
	
	/**
	 * Put a launchable object
	 * @param launchable the object to qqueue
	 */
	public void queue(ILaunchable launchable) {
		_fifo.queue(launchable);
	}
	
	public void done() {
		_reader.stopReader();
	}

}
