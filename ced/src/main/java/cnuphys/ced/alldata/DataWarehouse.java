package cnuphys.ced.alldata;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.jlab.io.base.DataBank;
import org.jlab.io.base.DataEvent;
import org.jlab.jnp.hipo4.data.Schema;
import org.jlab.jnp.hipo4.data.SchemaFactory;

import cnuphys.ced.clasio.ClasIoEventManager;
import cnuphys.ced.clasio.ClasIoEventManager.EventSourceType;
import cnuphys.ced.clasio.IClasIoEventListener;
import cnuphys.ced.event.data.arrays.BaseArrays;
import cnuphys.ced.event.data.lists.ClusterList;

public class DataWarehouse implements IClasIoEventListener {

	//the singleton
	private static DataWarehouse _instance;

	// all the known banks in the current evenr
	private ArrayList<String> _knownBanks = new ArrayList<>();

	//the current schema factory (dictionary)
	private SchemaFactory _schemaFactory;

    //the arrays for every event
	private HashMap<String, BaseArrays> _arrays = new HashMap<>();

	private DataWarehouse() {
		ClasIoEventManager.getInstance().addClasIoEventListener(this, 0);
	}
	
	/**
	 * Public access to the singleton
	 *
	 * @return the singleton
	 */
	public static DataWarehouse getInstance() {
		if (_instance == null) {
			_instance = new DataWarehouse();
		}
		return _instance;
	}

	/**
	 * Get the banks in the current event
	 *
	 * @return the known banks
	 */
	public ArrayList<String> getKnownBanks() {
		return _knownBanks;
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
	 * Get the list of column names for a bank name
	 *
	 * @param bankName the bank name
	 * @return the list of column names
	 */
	public List<String> getColumnNames(String bankName) {
		if (_knownBanks.contains(bankName)) {
			return _schemaFactory.getSchema(bankName).getEntryList();
		}
		return null;
	}

	//get the current event from the IO manager
	private DataEvent getCurrentEvent() {
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
	 * Get the number of rows (length) of a given bank
	 * @param bankName the bank name
	 * @return the number of rows
	 */
	public int rows(String bankName) {
		if (bankName == null) {
			return 0;
		}

		DataEvent event = getCurrentEvent();
		if (event == null) {
			return 0;
		}

		DataBank bank = event.getBank(bankName);
		if (bank == null) {
			return 0;
		}
		return bank.rows();
	}

	/**
	 * Get a cluster list for the given bank name
	 * @param bankName
	 * @return
	 */
	public ClusterList getClusters(String bankName) {

		DataEvent event = getCurrentEvent();
		if (event == null) {
			return null;
		}

		DataBank bank = event.getBank(bankName);
		if (bank == null) {
			return null;
		}

		ClusterList clusters = new ClusterList(bankName);
		clusters.fillList();
		return clusters;
	}
	
	/**
	 * Get the arrays for the given bank name from the cache
	 * 
	 * @param bankName the bank name
	 * @return the arrays
	 */
	public BaseArrays getArrays(String bankName) {
		return _arrays.get(bankName);
	}
	
	/**
	 * Put the arrays for the given bank name into the cache
	 * @param bankName
	 * @param arrays
	 */
	public void putArrays(String bankName, BaseArrays arrays) {
		_arrays.put(bankName, arrays);
	}

	@Override
	public void newClasIoEvent(DataEvent event) {
		_arrays.clear();
	}

	@Override
	public void openedNewEventFile(String path) {
		_arrays.clear();
	}

	@Override
	public void changedEventSource(EventSourceType source) {
		_arrays.clear();
	}

	@Override
	public boolean ignoreIfAccumulating() {
		return false;
	}

}
