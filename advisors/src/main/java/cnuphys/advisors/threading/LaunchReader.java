package cnuphys.advisors.threading;

public class LaunchReader extends Reader {

	public LaunchReader(BlockingFIFO fifo) {
		super(fifo);
		start();
	}

	@Override
	public void process(Object element) {
		ILaunchable launchable = (ILaunchable)element;
		launchable.launch();
		launchable.launchDone();
	}

}
