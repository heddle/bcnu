package cnuphys.ced.clasio.datatable;

import javax.swing.event.EventListenerList;

/**
 * Deals with the feature that you can selected data from a BankDataTable and have it highlighted
 * @author heddle
 *
 */
public class SelectedDataManager {


	/**
	 * Should we highlight data as a result of data selection
	 * @return true if we should highlight data as a result of data selection
	 */
	public static boolean isHighlightOn() {
		return _highlite;
	}

	// list of accumulation listeners
	private static EventListenerList _listeners;

	//highlight selected data?
	private static boolean _highlite;
	private static long _stopHighliteTime;

	/**
	 * Notify listeners of an selected data event
	 *
	 * @param bankName the name of the bank
	 * @param index the 1-based index into the bank
	 *
	 */
	public static void notifyListeners(String bankName, int index) {

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

			_highlite = true;
			_stopHighliteTime = System.currentTimeMillis() + 5000;
		}
	}
	
	/**
	 * Notify listeners highlight mode if off
	 *
	 */
	public static void notifyListeners() {

		if (_listeners != null) {

			// Guaranteed to return a non-null array
			Object[] listeners = _listeners.getListenerList();

			// This weird loop is the bullet proof way of notifying all
			// listeners.
			for (int i = listeners.length - 2; i >= 0; i -= 2) {
				if (listeners[i] == IDataSelectedListener.class) {
					((IDataSelectedListener) listeners[i + 1]).highlightModeOff();
				}
			}

			_stopHighliteTime = System.currentTimeMillis() + 5000;
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


	/**
	 * The maintenance timer calls this
	 */
	public static void updateDataHighlighting() {

		if (_highlite) {
			if (System.currentTimeMillis() > _stopHighliteTime) {
				_highlite = false;
				notifyListeners();
			}
		}
	}


}
