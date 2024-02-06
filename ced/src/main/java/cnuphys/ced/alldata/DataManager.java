package cnuphys.ced.alldata;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Hashtable;
import java.util.List;

import org.jlab.clas.detector.DetectorResponse;
import org.jlab.detector.base.DetectorType;
import org.jlab.io.base.DataEvent;
import org.jlab.jnp.hipo4.data.Schema;
import org.jlab.jnp.hipo4.data.SchemaFactory;

public class DataManager {

	// the data dictionary
	//private DataDictionary _dictionary;

	// all the known banks
	private String[] _knownBanks;

	// the full set of column data. ALL columns for a full bank name key
	// key is something like DET::NAME.COLUMN
	// this maps bank name to a ColumnData object
	private Hashtable<String, ColumnData> _columnData;

	//this maps a bank name to a list of columns
	private Hashtable<String, String[]> _banks;

	// singleton
	private static volatile DataManager _instance;

	/**
	 * Create a DataManager
	 *
	 * @param dictionary
	 */
	private DataManager() {
		_columnData = new Hashtable<>();
		_banks = new Hashtable<>();
	}

	/**
	 * Update the schema factory and the columa dataobjects
	 * @param schemaFactory
	 */
	public void updateSchema(SchemaFactory schemaFactory) {

		_columnData.clear();
		_banks.clear();
		_knownBanks = null;


		List<Schema> schemas = schemaFactory.getSchemaList();

		if (schemas == null || schemas.isEmpty()) {
			return;
		}

		int size = schemas.size();
		_knownBanks = new String[size];
		for (int i = 0; i < size; i++) {
			Schema schema = schemas.get(i);
			_knownBanks[i] = schema.getName();
		}
		Arrays.sort(_knownBanks);


		// a schema is a bank
		for (Schema schema : schemas) {
			String bankName = schema.getName();
			List<String> columns = schema.getEntryList();

			if (columns == null || columns.isEmpty()) {
				continue;
			}

			size = columns.size();
			String[] colArray = new String[size];
			for (int i = 0; i < size; i++) {
				colArray[i] = columns.get(i);
			}

			//don't sort col array any more
		//	Arrays.sort(colArray);
			_banks.put(bankName, colArray);

			for (String columnName : columns) {
				int type = schema.getType(columnName);
				ColumnData cd = new ColumnData(bankName, columnName, type);
				_columnData.put(cd.getFullName(), cd);

			}
		}

	}
	/**
	 * Get the collection of recognized columns
	 *
	 * @return the collection of recognized columns
	 */
	public ArrayList<ColumnData> getColumnData() {
		if ((_columnData == null) || (_columnData.size() < 1)) {
			return null;
		}
		ArrayList<ColumnData> columns = new ArrayList<>();
		for (ColumnData cd : _columnData.values()) {
			columns.add(cd);
		}
		Collections.sort(columns);
		return columns;
	}

	/**
	 * public access to singleton
	 *
	 * @return data manager singleton
	 */
	public static DataManager getInstance() {
		if (_instance == null) {
            synchronized (DataManager.class) {
                if (_instance == null) {
                    _instance = new DataManager();
                }
            }
		}
		return _instance;
	}

	/**
	 * Get a list of all column data objects that have data in the given event for a
	 * specific bank
	 *
	 * @param event    the event in question
	 * @param bankName the bank
	 * @return a list of all columns in the given bank with data
	 */

	public ArrayList<ColumnData> hasData(DataEvent event, String bankName) {
		ArrayList<ColumnData> list = new ArrayList<>();

		String columns[] = event.getColumnList(bankName);
		if (columns != null) {
			for (String columnName : columns) {
				list.add(getColumnData(bankName, columnName));
			}
		}

		return list;

	}

	/**
	 * Get a list of all column data objects that have data in the given event
	 *
	 * @param event the event in question
	 * @return a list of all columns in all banks with data
	 */
	public ArrayList<ColumnData> hasData(DataEvent event) {
		ArrayList<ColumnData> list = new ArrayList<>();

		String banks[] = event.getBankList();
		if (banks != null) {
			for (String bankName : banks) {
				String columns[] = event.getColumnList(bankName);
				if (columns != null) {
					for (String columnName : columns) {
						ColumnData cd = getColumnData(bankName, columnName);
						if (cd == null) {
							System.err.println("Dictionary does not seem to know about bank named [" + bankName
									+ "." + columnName + "] May be a disconnect with json files");
						} else {
							list.add(cd);
						}
					}
				}
			}
		}

		Collections.sort(list);

		String bankName = "";
		int bankIndex = 1;
		for (ColumnData cd : list) {
			if (!cd.getBankName().equals(bankName)) {
				bankIndex++;
				bankName = cd.getBankName();
			}
			cd.bankIndex = bankIndex;
		}
		return list;
	}

	/**
	 * Get the number of rows (length) of a given bank
	 * @param event the event
	 * @param bankName the bank name
	 * @return the number of rows
	 */
	public int getRowCount(DataEvent event, String bankName) {
		if (bankName == null) {
			return 0;
		}
		String colNames[] = getColumnNames(bankName);

		if ((colNames == null) || (colNames.length < 1)) {
			return 0;
		}

		//uses the 1st column, assumes all columns have the same length
		ColumnData cd = getColumnData(bankName, colNames[0]);

		if (cd == null) {
			return 0;
		}

		return cd.getLength(event);
	}

	/**
	 * Get the list of column names for a bank name
	 *
	 * @param bankName the bank name
	 * @return the list of column names
	 */
	public String[] getColumnNames(String bankName) {
		return _banks.get(bankName);
	}


	/**
	 * Get the known banks
	 *
	 * @return the (sorted) known bank names
	 */
	public String[] getKnownBanks() {
		return _knownBanks;
	}

	/**
	 * Get a ColumnData
	 *
	 * @param bankName   the bank name
	 * @param columnName the column data
	 * @return the ColumnData
	 */
	public ColumnData getColumnData(String bankName, String columnName) {
		return _columnData.get(bankName + "." + columnName);
	}

	/**
	 * Get a ColumnData
	 *
	 * @param fullName the full name
	 * @return the ColumnData
	 */
	public ColumnData getColumnData(String fullName) {
		return _columnData.get(fullName);
	}

	/**
	 * Obtain an byte array from the given event for the given full name
	 *
	 * @param event    the given event
	 * @param fullName the full name
	 * @return the array, or <code>null</code>
	 */
	public byte[] getByteArray(DataEvent event, String fullName) {
		ColumnData cd = getColumnData(fullName);
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
		ColumnData cd = getColumnData(fullName);
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
		ColumnData cd = getColumnData(fullName);
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
		ColumnData cd = getColumnData(fullName);
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
		ColumnData cd = getColumnData(fullName);
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
		ColumnData cd = getColumnData(fullName);
		return (cd == null) ? null : cd.getDoubleArray(event);
	}


	/**
	 * Get a list of detector responses
	 *
	 * @param event    the event
	 * @param bankName the bank name
	 * @param type     the detector type
	 * @return a list of detector responses
	 */
	public List<DetectorResponse> getDetectorResponse(DataEvent event, String bankName, DetectorType type) {

		if (event == null) {
			return null;
		}

		List<DetectorResponse> responses = null;
		if (event.hasBank(bankName)) {
			System.err.println("Event is null: " + (event == null));
			System.err.println("bankName: [" + bankName + "]");
			System.err.println("event has bank: " + event.hasBank(bankName));
			System.err.println("detector type: [" + type + "]");
			responses = DetectorResponse.readHipoEvent(event, bankName, type);
		}
		return responses;
	}
}
