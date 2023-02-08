package cnuphys.ced.clasio.datatable;

import javax.swing.event.EventListenerList;

/**
 * Deals with the feature that you can selected data from a BankDataTable and have it highlighted
 * @author heddle
 *
 */
public class SelectedDataManager {


	// list of accumulation listeners
	private static EventListenerList _listeners;


	/**
	 * Notify listeners of an selected data event
	 *
	 * @param bankName the name of the bank
	 * @param index the 1-based index into the bank. Index of 0 indicates no row currently selected
	 *
	 */
	public static void notifyListeners(String bankName, int index) {
		
		System.err.println(String.format("NOTIFY BANK: [%s]   index: %d", bankName, index));

		if (_listeners != null) {

			// Guaranteed to return a non-null array
			Object[] listeners = _listeners.getListenerList();

			// This weird loop is the bullet proof way of notifying all
			// listeners.
			for (int i = listeners.length - 2; i >= 0; i -= 2) {
				if (listeners[i] == IDataSelectedListener.class) {
					((IDataSelectedListener) listeners[i + 1]).dataSelected(bankName, index);
				}
			}
		}
	}
	


	/**
	 * Remove a data selected listener.
	 *
	 * @param listener the data selected listener to remove.
	 */
	public static void removeDataSelectedListener(IDataSelectedListener listener) {

		if (listener == null) {
			return;
		}

		if (_listeners != null) {
			_listeners.remove(IDataSelectedListener.class, listener);
		}
	}

	/**
	 * Add a data selected listener.
	 *
	 * @param listener the data selected listener to add.
	 */
	public static void addDataSelectedListener(IDataSelectedListener listener) {

		if (listener == null) {
			return;
		}

		if (_listeners == null) {
			_listeners = new EventListenerList();
		}

		_listeners.add(IDataSelectedListener.class, listener);
	}



}
