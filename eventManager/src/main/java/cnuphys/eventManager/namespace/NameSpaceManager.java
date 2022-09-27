package cnuphys.eventManager.namespace;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.event.EventListenerList;

import org.jlab.io.hipo.HipoDataSource;
import org.jlab.jnp.hipo4.data.Schema;

/**
 * Manages the name space of banks and columns forthe current data source
 *
 * @author heddle
 *
 */
public class NameSpaceManager extends ArrayList<BankInfo> {

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
	 * Get the names of banks present in the current datasource
	 * @return the names of known banks
	 */
	public String[] getKnownBanks() {
		return _knownBanks;
	}

	/**
	 * Get the type name for a given int type
	 * @param type the int type
	 * @return the type name
	 */
	public static String getTypeName(int type) {
		return ((type < 0) || (type >= typeNames.length) ? "???" : typeNames[type]);
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
	public BankInfo getBankInfo(String bankName) {
		_workBankInfo.setName(bankName);
		int index = Collections.binarySearch(this, _workBankInfo);
		if (index >= 0) {
			return get(index);
		}
		else {
			return null;
		}
	}

	/**
	 * Get an array of column names for the given bank name
	 * @param bankName the bank name
	 * @return the column names, or null on failure
	 */
	public String[] getColumnNames(String bankName) {
		BankInfo bankInfo = getBankInfo(bankName);

		if (bankInfo != null) {
			return bankInfo.getColumnNames();
		}

		return null;
	}

	/**
	 * Get the ColumnInfo object
	 * @param bankName the bank name
	 * @param columnName the column name
	 * @return the ColumnInfo, or null
	 */
	public ColumnInfo getColumnInfo(String bankName, String columnName) {
		if (bankName == null || columnName == null) {
			return null;
		}

		BankInfo bank = this.getBankInfo(bankName);
		return (bank == null) ? null : bank.getColumnInfo(columnName);
	}

	/**
	 * Get the ColumnInfo object
	 * @param fullColumnName of the form a::b.c
	 * @return the ColumnInfo, or null
	 */
	public ColumnInfo getColumnInfo(String fullColumnName) {
		if (fullColumnName == null) {
			return null;
		}

		int index = fullColumnName.indexOf(".");
		if (index < 2) {
			return null;
		}

		String bankName = fullColumnName.substring(0, index);
		String columnName = fullColumnName.substring(index+1);

		return getColumnInfo(bankName, columnName);
	}


	/**
	 * Get the data type for the given bank and column names
	 * @param bankName the bank name
	 * @param columnName the column name
	 * @return the data type, or -1 on failure
	 */
	public int getDataType(String bankName, String columnName) {
		BankInfo bankInfo = getBankInfo(bankName);
		if (bankInfo != null) {
			ColumnInfo columnInfo = bankInfo.getColumnInfo(columnName);
			return columnInfo.getType();
		}
		return -1;
	}

	/**
	 * Check to see if string appears to have the correct format
	 * for a full column name i.e. a::b.c
	 * @param columnName a full column name to check
	 * @return true if the full name passes this basic test
	 */
	public static boolean validColumnName(String columnName) {
		return (columnName != null && columnName.length() > 4 && columnName.contains("::") && columnName.contains("."));
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


}
