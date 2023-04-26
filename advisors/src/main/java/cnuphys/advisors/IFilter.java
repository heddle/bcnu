package cnuphys.advisors;

import cnuphys.advisors.io.ITabled;

public interface IFilter {

	public boolean pass(ITabled itabled);
}
