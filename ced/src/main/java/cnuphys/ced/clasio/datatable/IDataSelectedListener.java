package cnuphys.ced.clasio.datatable;

import java.util.EventListener;

public interface IDataSelectedListener extends EventListener {

	/**
	 * In the BankDataTable a row was selected. Notify listeners who may want to highlight
	 * @param bankName the name of the bank
	 * @param index the 1-based index into the bank
	 */
	public void dataSelected(String bankName, int index);
	
	/**
	 * Notifies that highlight mode is now off
	 */
	public void highlightModeOff();

}
