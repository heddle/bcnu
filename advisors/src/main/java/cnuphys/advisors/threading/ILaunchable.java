package cnuphys.advisors.threading;

public interface ILaunchable {

	/** launch the process */
	public void launch();
	
	/** launch is done */
	public void launchDone();
}
