package cnuphys.ced.alldata;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.event.EventListenerList;

import org.jlab.io.base.DataBank;
import org.jlab.io.base.DataEvent;
import org.jlab.jnp.hipo4.data.Schema;
import org.jlab.jnp.hipo4.data.SchemaFactory;

import cnuphys.ced.alldata.datacontainer.IDataContainer;
import cnuphys.ced.clasio.ClasIoEventManager;
import cnuphys.ced.clasio.ClasIoEventManager.EventSourceType;
import cnuphys.ced.clasio.IClasIoEventListener;

public class DataWarehouse implements IClasIoEventListener {

	//the singleton
	private static volatile DataWarehouse _instance;

	// all the known banks in the current evenr
	private ArrayList<String> _knownBanks = new ArrayList<>();

	//the current schema factory (dictionary)
	private SchemaFactory _schemaFactory;

	// private constructor for singleton
	private DataWarehouse() {
		ClasIoEventManager.getInstance().addClasIoEventListener(this, 0);
	}

	// list of data container listeners
	private static EventListenerList _listeners;

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
	public static String[] typeNames = { "Unknown", "byte", "short", "int", "float", "double", "string", "group", "long", "vector3f", "composite", "table", "branch"};

	/**
	 * Public access to the singleton
	 *
	 * @return the singleton
	 */
	public static DataWarehouse getInstance() {
		if (_instance == null) {
			synchronized (DataWarehouse.class) {
                if (_instance == null) {
                    _instance = new DataWarehouse();
                }
            }
        }
		return _instance;
	}

	/**
	 * Get the banks in the current event
	 *
	 * @return the known banks in a String array
	 */
	public String[] getKnownBanks() {
		String[] kbArray = new String[this._knownBanks.size()];
		_knownBanks.toArray(kbArray);
		return kbArray;
	}
	
	/**
	 * Does the current event have a bank with the given name?
	 * 
	 * @param bankName the bank name
	 * @return <code>true</code> if the current event has the bank
	 */
	public boolean hasBank(String bankName) {
		DataEvent event = getCurrentEvent();
		
		return (event != null) ? event.hasBank(bankName) : false;
	}

	/**
	 * Update the schema factory and the columa dataobjects
	 * @param schemaFactory
	 */
	public void updateSchema(SchemaFactory schemaFactory) {

		_schemaFactory = schemaFactory;
		_knownBanks.clear();

		//schemas are banks
		List<Schema> schemas = schemaFactory.getSchemaList();

		// a schema is a bank
		if (schemas == null || schemas.isEmpty()) {
			return;
		}

		for (Schema schema : schemas) {
			_knownBanks.add(schema.getName());
		}

        // sort the banks
		_knownBanks.sort(null);
	}


	/**
	 * Get the type of a column
	 * @param bankName the bank name
	 * @param columnName the column name
	 * @return the data type of the column, or UNKNOWN if not found
	 */
	public int getType(String bankName, String columnName) {
		Schema schema = _schemaFactory.getSchema(bankName);
		return (schema == null) ? UNKNOWN : schema.getType(columnName);
	}

	/**
	 * Get the data type name of a column
	 * @param bankName the bank name
	 * @param columnName the column name
	 * @return the data type name of the column, or "Unknown" if not found
	 */
	public String getTypeName(String bankName, String columnName) {
		int type = getType(bankName, columnName);
		return typeNames[type];
	}

	/**
	 * Get the sorted list of column names for a bank name
	 *
	 * @param bankName the bank name
	 * @return the list of column names
	 */
	public String[] getColumnNames(String bankName) {
		if (_knownBanks.contains(bankName)) {
			List<String> list = _schemaFactory.getSchema(bankName).getEntryList();
			String[] array = new String[list.size()];
			list.toArray(array);
			Arrays.sort(array);
			return array;
		}
		return null;
	}

	//get the current event from the IO manager
	public DataEvent getCurrentEvent() {
		return ClasIoEventManager.getInstance().getCurrentEvent();
	}

	/**
	 * Get a list of the banks in the current event
	 * @return banks in current event
	 */
	public String[] banks() {
		DataEvent event = getCurrentEvent();
		if (event != null) {
			String[] banks = event.getBankList();

			if (banks != null) {
				Arrays.sort(banks);
			}
			return banks;
		}
        return null;
    }

	/**
	 * Get a byte array for the bank and column names in the current event
	 *
	 * @param bankName   the bank name
	 * @param columnName the column name
	 * @return a byte array or null.
	 */
	public byte[] getByte(String bankName, String columnName) {
		DataEvent event = getCurrentEvent();
		if (event != null) {
			DataBank bank = event.getBank(bankName);
			if (bank != null) {
				return bank.getByte(columnName);
			}
		}
		return null;
	}
	/**
	 * Get a float array for the bank and column names in the current event
	 *
	 * @param bankName   the bank name
	 * @param columnName the column name
	 * @return a float array or null.
	 */
	public float[] getFloat(String bankName, String columnName) {
		DataEvent event = getCurrentEvent();
		if (event != null) {
			DataBank bank = event.getBank(bankName);
			if (bank != null) {
				return bank.getFloat(columnName);
			}
		}
		return null;
	}

	/**
	 * Get a short array for the bank and column names in the current event
	 * @param bankName the bank name
	 * @param columnName the column name
	 * @return a short array or null.
	 */
	public short[] getShort(String bankName, String columnName) {
		DataEvent event = getCurrentEvent();
        if (event != null) {
			DataBank bank = event.getBank(bankName);
			if (bank != null) {
				return bank.getShort(columnName);
			}
		}
		return null;
	}

	/**
	 * Get an int array for the bank and column names in the current event
	 * @param bankName the bank name
	 * @param columnName the column name
	 * @return a short array or null.
	 */
	public int[] getInt(String bankName, String columnName) {
		DataEvent event = getCurrentEvent();
        if (event != null) {
			DataBank bank = event.getBank(bankName);
			if (bank != null) {
				return bank.getInt(columnName);
			}
		}
		return null;
	}

	/**
	 * Get a long array for the bank and column names in the current event
	 *
	 * @param bankName   the bank name
	 * @param columnName the column name
	 * @return a short array or null.
	 */
	public long[] getLong(String bankName, String columnName) {
		DataEvent event = getCurrentEvent();
		if (event != null) {
			DataBank bank = event.getBank(bankName);
			if (bank != null) {
				return bank.getLong(columnName);
			}
		}
		return null;
   }

	/**
	 * Get a double array for the bank and column names in the current event
	 *
	 * @param bankName   the bank name
	 * @param columnName the column name
	 * @return a short array or null.
	 */
	public double[] getDouble(String bankName, String columnName) {
		DataEvent event = getCurrentEvent();
		if (event != null) {
			DataBank bank = event.getBank(bankName);
			if (bank != null) {
				return bank.getDouble(columnName);
			}
		}
		return null;
	}

	/**
	 * Notify the data containers of a new event
	 *
	 * @param event the new event they should use to update themselves.
	 *
	 */
	public void notifyListeners(DataEvent event) {

		if (_listeners != null) {

			// Guaranteed to return a non-null array
			Object[] listeners = _listeners.getListenerList();

			// This weird loop is the bullet proof way of notifying all
			// listeners.
			for (int i = listeners.length - 2; i >= 0; i -= 2) {
				if (listeners[i] == IDataContainer.class) {
					((IDataContainer) listeners[i + 1]).update(event);
				}
			}
		}
	}

	/**
	 * Notify the data containers to clear their data
	 *
	 */
	public void notifyListeners() {

		if (_listeners != null) {

			// Guaranteed to return a non-null array
			Object[] listeners = _listeners.getListenerList();

			// This weird loop is the bullet proof way of notifying all
			// listeners.
			for (int i = listeners.length - 2; i >= 0; i -= 2) {
				if (listeners[i] == IDataContainer.class) {
					((IDataContainer) listeners[i + 1]).clear();
				}
			}
		}
	}


	/**
	 * Get the number of rows in a bank for the current event
	 *
	 * @param bankName the bank name
	 * @return the number of rows in the bank, or 0 if the bank is not found.
	 */
	public int rows(String bankName) {
		DataEvent event = getCurrentEvent();
		if (event != null) {
			DataBank bank = event.getBank(bankName);
			if (bank != null) {
				return bank.rows();
			}
		}
		return 0;
	}
	

	/**
	 * Add an data container listener.
	 *
	 * @param listener the data container listener to add.
	 */
	public void addDataContainerListener(IDataContainer listener) {

		if (listener == null) {
			return;
		}

		if (_listeners == null) {
			_listeners = new EventListenerList();
		}

		_listeners.add(IDataContainer.class, listener);
	}


	@Override
	public void newClasIoEvent(DataEvent event) {
		notifyListeners(); //clear previous data
		notifyListeners(event);
	}

	@Override
	public void openedNewEventFile(String path) {
		notifyListeners();
	}

	@Override
	public void changedEventSource(EventSourceType source) {
		notifyListeners();
	}

}
