package cnuphys.cnf.event.dictionary;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.List;

import javax.swing.event.EventListenerList;

import org.jlab.io.base.DataEvent;
import org.jlab.io.hipo.HipoDataSource;
import org.jlab.jnp.hipo4.data.Schema;


public class Dictionary extends ArrayList<Bank> {
	
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
	public static final String[] typeNames = { "Unknown", "byte", "short", "int", "float", "double", "string", "group", "long", "vector3f", "composite", "table", "branch"};

	//private work bank used for binary search
	private Bank _workBank = new Bank("");
	
	// Bank exclusion list
	private String _exclusions[] = null;


	//list of dictionary listeners
	private EventListenerList _listenerList;

	//the singleton
	private static Dictionary _instance;
	
	//convenient list of known banks
	private String[] _knownBanks;
	
	//convenience to cache all columns
	private Hashtable<String, Column> _allColumns = new Hashtable<>();

	//private constructor for singleton
	private Dictionary() {
	}
	
	/**
	 * Access to the singleton
	 * @return the singleton dictionary
	 */
	public static Dictionary getInstance() {
		if (_instance == null) {
			_instance = new Dictionary();
		}
		
		return _instance;
	}
	
	/**
	 * Update the dictionary, probably because a new file was opened.
	 * @param source the data source, which is tied to a file
	 */
	public void updateDictionary(HipoDataSource source) {
		clear();
		_allColumns.clear();
		
		List<String> schemaNames = source.getReader().getSchemaFactory().getSchemaKeys();

		for (String bankName : schemaNames) {
			
			if (exclude(bankName)) {
				continue;
			}
			
			Bank bank = new Bank(bankName);
			add(bank);
			
			Schema schema = source.getReader().getSchemaFactory().getSchema(bankName);

			//get the columns
			List<String> columns = schema.getEntryList();

			int bankIndex = 0;
			for (String columnName : columns) {
				int type = schema.getType(columnName);
				Column column = new Column(bank, columnName, type);
				column.bankIndex = bankIndex++;  //used for table coloring
				_allColumns.put(column.getFullName(), column);
				bank.put(columnName, column);
			}
			
			
		}
		
		//sort
		Collections.sort(this);
		
		//for convenience
		_knownBanks = new String[size()];
		int index = 0;
		for (Bank bank : this) {
			_knownBanks[index++] = bank.getName();
		}
		
		System.out.println(this);
		
		//tell whoever is interested
		notifyListeners();
	}
	
	// check exclusions
	private boolean exclude(String bankName) {
		if ((_exclusions != null) && (_exclusions.length > 0)) {
			for (String es : _exclusions) {
				if (bankName.contains(es)) {
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * Get the collection of recognized columns
	 * 
	 * @return the collection of recognized columns
	 */
	public ArrayList<Column> getKnownColumns() {

		if (_allColumns.isEmpty()) {
			return null;
		}

		ArrayList<Column> columns = new ArrayList<Column>();
		for (Column cd : _allColumns.values()) {
			columns.add(cd);
		}
		Collections.sort(columns);
		return columns;
	}
	
	/**
	 * Get the column names for a bank
	 * @param bankName the name of the bank
	 * @return a list of the columns, or null if bank not found.
	 */
	public String[] getColumnNames(String bankName) {
		Bank bank = getBank(bankName);
		
		if (bank == null) {
			return null;
		}
		return bank.getColumnNames();
	}
	
	/**
	 * Get a list of all column  objects that have data in the given event for a
	 * specific bank
	 * 
	 * @param event    the event in question
	 * @param bankName the bank
	 * @return a list of all columns in the given bank with data
	 */

	public ArrayList<Column> hasData(DataEvent event, String bankName) {

		ArrayList<Column> list = new ArrayList<Column>();

		String columns[] = event.getColumnList(bankName);
		if (columns != null) {
			for (String columnName : columns) {
				list.add(getColumn(bankName, columnName));
			}
		}

		return list;

	}


	/**
	 * Get a list of all column objects that have data in the given event
	 * 
	 * @param event the event in question
	 * @return a list of all columns in all banks with data
	 */
	public ArrayList<Column> hasData(DataEvent event) {
		ArrayList<Column> list = new ArrayList<Column>();

		String banks[] = event.getBankList();
		if (banks != null) {
			for (String bankName : banks) {
				String columns[] = event.getColumnList(bankName);
				if (columns != null) {
					for (String columnName : columns) {
						Column cd = getColumn(bankName, columnName);

						if (cd != null) {
							list.add(cd);
						}
					}
				}
			}
		}
		return list;
	}
	/**
	 * Get all the banks known to be in the current file
	 * @return all the banks known to be in the current file
	 */
	public String[] getKnownBanks() {
		return _knownBanks;
	}
	
	/**
	 * Notify all listeners that a change has occurred in the magnetic fields
	 */
	protected void notifyListeners() {

		if (_listenerList == null) {
			return;
		}

		// Guaranteed to return a non-null array
		Object[] listeners = _listenerList.getListenerList();

		// This weird loop is the bullet proof way of notifying all listeners.
		for (int i = 0; i < listeners.length; i += 2) {
			if (listeners[i] == IDictionaryListener.class) {
				IDictionaryListener listener = (IDictionaryListener) listeners[i + 1];
				listener.dictionaryChanged();
			}

		}
	}
	
	/**
	 * Add a dictionary change listener
	 *
	 * @param listener the listener to add
	 */
	public void addDictionaryListener(IDictionaryListener listener) {

		if (_listenerList == null) {
			_listenerList = new EventListenerList();
		}

		// avoid adding duplicates
		_listenerList.remove(IDictionaryListener.class, listener);
		_listenerList.add(IDictionaryListener.class, listener);

	}

	/**
	 * Remove a dictionary listener.
	 *
	 * @param listener the listener to remove.
	 */

	public void removeDictionaryListener(IDictionaryListener listener) {

		if ((listener == null) || (_listenerList == null)) {
			return;
		}

		_listenerList.remove(IDictionaryListener.class, listener);
	}
	
	/**
	 * Get the bank using a binary search
	 * @param bankName the name of the bank
	 * @return  the bank, or null if not found
	 */
	public Bank getBank(String bankName) {
		_workBank.setName(bankName);
		int index = Collections.binarySearch(this, _workBank);
		if (index >= 0) {
			return get(index);
		}
		else {
			return null;
		}
	}
	
	/**
	 * Get the Column object from the bank and column names
	 * @param bankName the bank name
	 * @param columName the column name
	 * @return the Column, or null if not found
	 */
	public Column getColumn(String bankName, String columName) {
		Bank bank = getBank(bankName);
		if (bank == null) {
			return null;
		}
		
		return bank.getColumn(columName);
	}
	
	/**
	 * Get a column from the full name
	 * @param columnName the full name of the column
	 * @return the column object, or null
	 */
	public Column getColumnFromFullName(String columnName) {
		return _allColumns.get(columnName);
	}
	
	
	/**
	 * Obtain an byte array from the given event for the given full name
	 * 
	 * @param event    the given event
	 * @param fullName the full name
	 * @return the array, or <code>null</code>
	 */
	public byte[] getByteArray(DataEvent event, String fullName) {
		Column cd = getColumnFromFullName(fullName);
		return (cd == null) ? null : cd.getByteArray(event);
	}

	/**
	 * Obtain a short array from the given event for the given full name
	 * 
	 * @param event    the given event
	 * @param fullName the full name
	 * @return the array, or <code>null</code>
	 */
	public short[] getShortArray(DataEvent event, String fullName) {
		Column cd = getColumnFromFullName(fullName);
		return (cd == null) ? null : cd.getShortArray(event);
	}

	/**
	 * Obtain an int array from the current event for the given full name
	 * 
	 * @param event    the given event
	 * @param fullName the full name
	 * @return the array, or <code>null</code>
	 */
	public int[] getIntArray(DataEvent event, String fullName) {
		Column cd = getColumnFromFullName(fullName);
		return (cd == null) ? null : cd.getIntArray(event);
	}

	/**
	 * Obtain a long array from the current event for the given full name
	 * 
	 * @param event    the given event
	 * @param fullName the full name
	 * @return the array, or <code>null</code>
	 */
	public long[] getLongArray(DataEvent event, String fullName) {
		Column cd = getColumnFromFullName(fullName);
		return (cd == null) ? null : cd.getLongArray(event);
	}

	/**
	 * Obtain a float array from the current event for the given full name
	 * 
	 * @param event    the given event
	 * @param fullName the full name
	 * @return the array, or <code>null</code>
	 */
	public float[] getFloatArray(DataEvent event, String fullName) {
		Column cd = getColumnFromFullName(fullName);
		return (cd == null) ? null : cd.getFloatArray(event);
	}

	/**
	 * Obtain a double array from the current event for the given full name
	 * 
	 * @param event    the given event
	 * @param fullName the full name
	 * @return the array, or <code>null</code>
	 */
	public double[] getDoubleArray(DataEvent event, String fullName) {
		Column cd = getColumnFromFullName(fullName);
		return (cd == null) ? null : cd.getDoubleArray(event);
	}

	/**
	 * (Approximate) test whether this is a valid column (full) name. Doesn't test whether
	 * the column exists.
	 * 
	 * @param name the name to test
	 * @return <code>true</code> if name is structured as a valid column name.
	 */
	public boolean validColumnName(String name) {
		return ((name != null) && (name.length() > 4) && name.contains(":") && name.contains("."));
	}

	/**
	 * Get the column name from the full name, i.e. A::B.columnName
	 * @param fullName the full name with the bank
	 * @return the column name from the full name
	 */
	public static String columnNameFromFullName(String fullName) {
		int index = fullName.indexOf(".");
		if (index < 0) {
			return null;
		}
		return fullName.substring(index+1);
	}
	
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer(2048);
		
		for (Bank bank : this) {
			sb.append(bank);
		}
	
		return sb.toString();
	}

}
