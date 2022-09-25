package cnuphys.cnf.event.namespace;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.event.EventListenerList;

import org.jlab.io.base.DataEvent;
import org.jlab.io.hipo.HipoDataSource;
import org.jlab.jnp.hipo4.data.Schema;

import cnuphys.cnf.event.EventManager;
import cnuphys.cnf.event.IEventListener;

/**
 * Manages the name space of banks and columns forthe current data source
 * 
 * @author heddle
 *
 */
public class NameSpaceManager extends ArrayList<BankInfo> implements IEventListener {

	/** type is unknown */
	public static final int UNKNOWN = 0;

	/** type is a byte */
	public static final int INT8 = 1;

	/** type is a short */
	public static final int INT16 = 2;

	/** type is an int */
	public static final int INT32 = 3;

	/** type is a float */
	public static final int FLOAT32 = 4;

	/** type is a double */
	public static final int FLOAT64 = 5;

	/** type is a string */
	public static final int STRING = 6;

	/** type is a group */
	public static final int GROUP = 7;

	/** type is a long int */
	public static final int INT64 = 8;

	/** type is a vector3f */
	public static final int VECTOR3F = 9;

	/** type is a composite */
	public static final int COMPOSITE = 10;

	/** type is a table */
	public static final int TABLE = 11;

	/** type is a branch */
	public static final int BRANCH = 12;

	/** type names */
	public static final String[] typeNames = { "Unknown", "byte", "short", "int", "float", "double", "string", "group",
			"long", "vector3f", "composite", "table", "branch" };

	// list of namespace listeners
	private EventListenerList _listenerList;
	
	//singleton
	private static NameSpaceManager _instance;
	
	//for convenience
	private String[] _knownBanks;
	
	//for binary search
	private BankInfo _workBankInfo = new BankInfo(null);
	
	//private constructor for singleton
	private NameSpaceManager() {
		EventManager.getInstance().addEventListener(this, 1);
	}
	
	/**
	 * Access to the singleton
	 * @return the singleton name space manager
	 */
	public static NameSpaceManager getInstance() {
		if (_instance == null) {
			_instance = new NameSpaceManager();
		}

		return _instance;
	}
	
	/**
	 * Update the namespace, probably because a new file was opened.
	 * @param dataSource the data source, which is usually tied to a file
	 */
	public void updateNameSpace(HipoDataSource dataSource) {
		
		clear();
		_knownBanks = null;
	
		List<Schema> schemas = dataSource.getReader().getSchemaFactory().getSchemaList();
		

		for (Schema schema : schemas) {
	        add(new BankInfo(schema));
		}

		Collections.sort(this);
		
		//for convenience
		if (size() > 0) {
			_knownBanks = new String[size()];
			for (int i = 0; i < size(); i++) {
				_knownBanks[i] = get(i).getName();
			}
		}
		
		System.out.println(this);

		//tell whoever is interested
		notifyListeners();
	}

	
	/**
	 * Notify all listeners that a change has occurred in the namespace
	 */
	protected void notifyListeners() {

		if (_listenerList == null) {
			return;
		}

		// Guaranteed to return a non-null array
		Object[] listeners = _listenerList.getListenerList();

		// This weird loop is the bullet proof way of notifying all listeners.
		for (int i = 0; i < listeners.length; i += 2) {
			if (listeners[i] == INameSpaceListener.class) {
				INameSpaceListener listener = (INameSpaceListener) listeners[i + 1];
				listener.nameSpaceChanged();
			}

		}
	}

	/**
	 * Add a name space change listener
	 *
	 * @param listener the listener to add
	 */
	public void addNameSpaceListener(INameSpaceListener listener) {

		if (_listenerList == null) {
			_listenerList = new EventListenerList();
		}

		// avoid adding duplicates
		_listenerList.remove(INameSpaceListener.class, listener);
		_listenerList.add(INameSpaceListener.class, listener);

	}

	/**
	 * Remove a name space listener.
	 *
	 * @param listener the listener to remove.
	 */

	public void removeNameSpaceListener(INameSpaceListener listener) {

		if ((listener == null) || (_listenerList == null)) {
			return;
		}

		_listenerList.remove(INameSpaceListener.class, listener);
	}
	
	/**
	 * Get the bank using a binary search
	 * @param bankName the name of the bank
	 * @return  the bank, or null if not found
	 */
	public BankInfo getBank(String bankName) {
		_workBankInfo.setName(bankName);
		int index = Collections.binarySearch(this, _workBankInfo);
		if (index >= 0) {
			return get(index);
		}
		else {
			return null;
		}
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer(2048);
		
		sb.append("Number of banks: " + size() + "\n");
		
		for (BankInfo bankInfo : this) {
			sb.append(bankInfo + "\n");
		}
		return sb.toString();
	}

	@Override
	public void newEvent(DataEvent event, boolean isStreaming) {
		if (isStreaming) {
			
		}
		
		ArrayList<ColumnInfo> dataColumns = DataUtils.columnsWithData(event);
		for (ColumnInfo s : dataColumns) {
			System.out.println("[" + s.getFullName() +"] " + s.colorIndex);
		}
	}

	@Override
	public void openedNewEventFile(File file) {
	}

	@Override
	public void rewoundFile(File file) {
		
	}

	@Override
	public void streamingStarted(File file, int numToStream) {
	}

	@Override
	public void streamingEnded(File file, int reason) {
	}

}
