package cnuphys.ced.alldata;

import org.jlab.io.base.DataBank;
import org.jlab.io.base.DataEvent;

public class ColumnData implements Comparable<ColumnData> {

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


	// the simple bank name
	private String _bankName;

	// the simple column name
	private String _columnName;

	// the data type
	private int _type;

	// the full a::b.c name
	private String _fullName;

	// used for table rendering
	public int bankIndex;

	/**
	 * Holds the data for one column, one event
	 *
	 * @param bankName   the bank name
	 * @param columnName the column name
	 * @param type       the data type (one of the class constants)
	 */
	public ColumnData(String bankName, String columnName, int type) {
		_bankName = bankName;
		_columnName = columnName;
		_fullName = bankName + "." + columnName;

		if ((type < 1) || (type > 6) || (type == 24)) {
			type = 0;
		}
		_type = type;
	}

	@Override
	public String toString() {
		return "bank name: [" + _bankName + "] column name: [" + _columnName + "] full name: [" + _fullName
				+ "] data type: " + typeNames[_type];
	}

	/**
	 * Get the name of the data type
	 *
	 * @return the name of the data type
	 */
	public String getTypeName() {
		if ((_type < 0) || (_type >= typeNames.length)) {
			return "???";
		} else {
			return typeNames[_type];
		}
	}



	/**
	 * Get a byte array for the bank and column names in the given event
	 * @param event the given event
	 * @return a byte array
	 */
	public byte[] getByteArray(DataEvent event) {
		DataBank bank = event.getBank(_bankName);
		byte[] array = bank.getByte(_columnName);
		return array;
	}

	/**
	 * Get a short array for the bank and column names in the given event
	 * @param event the given event
	 * @return a shortarray
	 */
	public short[] getShortArray(DataEvent event) {
		DataBank bank = event.getBank(_bankName);
		short[] array = bank.getShort(_columnName);
		return array;
	}

	/**
	 * Get an int array for the bank and column names in the given event
	 * @param event the given event
	 * @return an int array
	 */
	public int[] getIntArray(DataEvent event) {
		DataBank bank = event.getBank(_bankName);
		int[] array = bank.getInt(_columnName);
		return array;
	}

	/**
	 * Get a long array for the bank and column names in the given event
	 * @param event the given event
	 * @return a long array
	 */
	public long[] getLongArray(DataEvent event) {
		DataBank bank = event.getBank(_bankName);
		long[] array = bank.getLong(_columnName);
		return array;
	}
	/**
	 * Get a float array for the bank and column names in the given event
	 * @param event the given event
	 * @return a float array
	 */
	public float[] getFloatArray(DataEvent event) {
		DataBank bank = event.getBank(_bankName);
		float[] array = bank.getFloat(_columnName);
		return array;
	}

	/**
	 * Get a double array for the bank and column names in the given event
	 * @param event the given event
	 * @return a double array
	 */
	public double[] getDoubleArray(DataEvent event) {
		DataBank bank = event.getBank(_bankName);
		double[] array = bank.getDouble(_columnName);
		return array;
	}



	/**
	 * Get the length of the underlying data array
	 *
	 * @return the length of the underlying data array
	 */
	public int length() {

		return DataWarehouse.getInstance().rows(_bankName);

	}

	/**
	 * Get the bank name
	 *
	 * @return the bank name
	 */
	public String getBankName() {
		return _bankName;
	}

	/**
	 * Get the column name
	 *
	 * @return column name
	 */
	public String getColumnName() {
		return _columnName;
	}

	/**
	 * get the full name
	 *
	 * @return the full name
	 */
	public String getFullName() {
		return _fullName;
	}

	/**
	 * Get the data type
	 *
	 * @return the data type [0..6] (0 is error)
	 */
	public int getType() {
		return _type;
	}


	/**
	 * Obtain a byte array from the current event for the given full name
	 *
	 * @param fullName the full name
	 * @return the array, or <code>null</code>
	 */
	public byte[] getByteArray(String fullName) {
		return DataWarehouse.getInstance().getByte(_bankName, _columnName);
	}

	/**
	 * Obtain a short array from the current event for the given full name
	 *
	 * @param fullName the full name
	 * @return the array, or <code>null</code>
	 */
	public short[] getShortArray() {
		return DataWarehouse.getInstance().getShort(_bankName, _columnName);
	}

	/**
	 * Obtain an int array from the current event for the given full name
	 *
	 * @param fullName the full name
	 * @return the array, or <code>null</code>
	 */
	public int[] getIntArray() {
		return DataWarehouse.getInstance().getInt(_bankName, _columnName);
	}

	/**
	 * Obtain a long array from the current event for the given full name
	 *
	 * @return the array, or <code>null</code>
	 */
	public long[] getLongArray() {
		return DataWarehouse.getInstance().getLong(_bankName, _columnName);
	}

	/**
	 * Obtain a float array from the current event for the given full name
	 *
	 * @return the array, or <code>null</code>
	 */
	public float[] getFloatArray() {
		return DataWarehouse.getInstance().getFloat(_bankName, _columnName);
	}

	/**
	 * Obtain double array from the current event for the given full name
	 *
	 * @param fullName the full name
	 * @return the array, or <code>null</code>
	 */
	public double[] getDoubleArray(String fullName) {
		return DataWarehouse.getInstance().getDouble(_bankName, _columnName);
	}

	@Override
	public int compareTo(ColumnData o) {
		return _fullName.compareTo(o._fullName);
	}

}
