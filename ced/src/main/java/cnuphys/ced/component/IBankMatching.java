package cnuphys.ced.component;

public interface IBankMatching {

	/**
	 * Get banks of interest for matching banks panel on tabbed pane on control
	 * panel. If null, all banks are of interest (not advisable); Default does
	 * nothing
	 */
	public void setBankMatches(String[] matches);

	/**
	 * Write some properties for persitance
	 */
	public void writeCommonProperties();

	/**
	 * Get banks of interest for matching banks panel on tabbed pane on control
	 * panel. If null, all banks are of interest
	 *
	 * @return banks of interest for matching banks
	 */
	public String[] getBanksMatches();
}
