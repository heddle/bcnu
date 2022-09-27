package cnuphys.cnf.event.namespace;

import java.util.ArrayList;
import java.util.Arrays;

import org.jlab.io.base.DataBank;
import org.jlab.io.base.DataEvent;

/**
 * A set of static convenience methods leveraging the NameSpace
 *
 * @author heddle
 *
 */
public class DataUtils {

	// the name space manager
	private static NameSpaceManager _nameSpace = NameSpaceManager.getInstance();

	/**
	 * Get the int data type for a column
	 *
	 * @param bankName   the name of the bank
	 * @param columnName the name of the column
	 * @return the integer data type or -1 on error
	 */
	public static int getDataType(String bankName, String columnName) {

		BankInfo bankInfo = _nameSpace.getBankInfo(bankName);
		if (bankInfo != null) {
			ColumnInfo columnInfo = bankInfo.getColumnInfo(columnName);
			if (columnInfo != null) {
				return columnInfo.getType();
			}
		}
		return -1;

	}

	/**
	 * Get a list of all full column info objects for any banks that have data in the given
	 * event. This is used by the Node
	 *
	 * @param event the event in question
	 * @return a list of all full column names with data in the given event
	 */

	public static ArrayList<ColumnInfo> columnsWithData(DataEvent event) {

		ArrayList<ColumnInfo> fullColumnList = new ArrayList<>();

		int colorIndex = 0;

		if (event != null) {

			String bankList[] = event.getBankList();
			if (bankList != null) {
				Arrays.sort(bankList);
				for (String bankName : bankList) {
					colorIndex++;
					BankInfo bankInfo = _nameSpace.getBankInfo(bankName);

					if (bankInfo != null) {
						for (ColumnInfo columnInfo : bankInfo) {
							columnInfo.colorIndex = colorIndex;
							fullColumnList.add(columnInfo);
						}


					}

				}
			}
		} //event not null

		return fullColumnList;
	}

	/**
	 * Get the length of the bank (the number of rows) if the
	 * bank is in the event. All columns have equal ength.
	 * @param event the data event
	 * @param bankName the bank name
	 * @return the length, or number of rows
	 */
	public static int bankLength(DataEvent event, String bankName) {
		if (event != null) {
			DataBank bank = event.getBank(bankName);
			if (bank != null) {
				return bank.rows();
			}
		}
		return 0;
	}
	

	/**
	 * Get a byte array for the bank and column names in the given event
	 * @param event the given event
	 * @param bankName the bank name
	 * @param columnName the column name
	 * @return a byte array, if it finds one
	 */
	public static byte[] getByteArray(DataEvent event, String bankName, String columnName) {
		if (event != null) {
			DataBank bank = event.getBank(bankName);
			if (bank != null) {
				byte[] array = bank.getByte(columnName);
				return array;
			}
		}
		return null;
	}

	/**
	 * Get a short array for the bank and column names in the given event
	 * @param event the given event
	 * @param bankName the bank name
	 * @param columnName the column name
	 * @return a short array, if it finds one
	 */
	public static short[] getShortArray(DataEvent event, String bankName, String columnName) {
		if (event != null) {
			DataBank bank = event.getBank(bankName);
			if (bank != null) {
				short[] array = bank.getShort(columnName);
				return array;
			}
		}
		return null;
	}

	/**
	 * Get an int array for the bank and column names in the given event
	 * @param event the given event
	 * @param bankName the bank name
	 * @param columnName the column name
	 * @return an int array, if it finds one
	 */
	public static int[] getIntArray(DataEvent event, String bankName, String columnName) {
		if (event != null) {
			DataBank bank = event.getBank(bankName);
			if (bank != null) {
				int[] array = bank.getInt(columnName);
				return array;
			}
		}
		return null;
	}

	/**
	 * Get a long array for the bank and column names in the given event
	 * @param event the given event
	 * @param bankName the bank name
	 * @param columnName the column name
	 * @return a long array, if it finds one
	 */
	public static long[] getLongArray(DataEvent event, String bankName, String columnName) {
		if (event != null) {
			DataBank bank = event.getBank(bankName);
			if (bank != null) {
				long[] array = bank.getLong(columnName);
				return array;
			}
		}
		return null;
	}

	/**
	 * Get a float array for the bank and column names in the given event
	 * @param event the given event
	 * @param bankName the bank name
	 * @param columnName the column name
	 * @return a float array, if it finds one
	 */
	public static float[] getFloatArray(DataEvent event, String bankName, String columnName) {
		if (event != null) {
			DataBank bank = event.getBank(bankName);
			if (bank != null) {
				float[] array = bank.getFloat(columnName);
				return array;
			}
		}
		return null;
	}

	/**
	 * Get a double array for the bank and column names in the given event
	 * @param event the given event
	 * @param bankName the bank name
	 * @param columnName the column name
	 * @return a double array, if it finds one
	 */
	public static double[] getDoubleArray(DataEvent event, String bankName, String columnName) {
		if (event != null) {
			DataBank bank = event.getBank(bankName);
			if (bank != null) {
				double[] array = bank.getDouble(columnName);
				return array;
			}
		}
		return null;
	}

	/**
	 * Get the array with double values regardless of type
	 * @param event the given event
	 * @param columnInfo the info object
	 * @return the data as a double array
	 */
	public static double[] getAsDoubleArray(DataEvent event, ColumnInfo columnInfo) {
		if (columnInfo == null) {
			return null;
		}
		
		return getAsDoubleArray(event, columnInfo.getBankInfo().getName(), columnInfo.getName());
	}


	/**
	 * Get the array with double values regardless of type
	 * @param event the given event
	 * @param bankName the bank name
	 * @param columnName the column name
	 * @return the data as a double array
	 */
	public static double[] getAsDoubleArray(DataEvent event, String bankName, String columnName) {
		double da[] = null;

		if (event != null) {
			int len = bankLength(event, bankName);
			if (len > 0) {
				int type = _nameSpace.getDataType(bankName, columnName);
				if (type > 0) {
					switch (type) {
					case NameSpaceManager.INT8:
						byte b[] = getByteArray(event, bankName, columnName);
						da = new double[len];
						for (int j = 0; j < len; j++) {
							da[j] = b[j];
						}
						break;

					case NameSpaceManager.INT16:
						short s[] = getShortArray(event, bankName, columnName);
						da = new double[len];
						for (int j = 0; j < len; j++) {
							da[j] = s[j];
						}
						break;

					case NameSpaceManager.INT32:
						int i[] = getIntArray(event, bankName, columnName);
						da = new double[len];
						for (int j = 0; j < len; j++) {
							da[j] = i[j];
						}
						break;

					case NameSpaceManager.INT64:
						long l[] = getLongArray(event, bankName, columnName);
						da = new double[len];
						for (int j = 0; j < len; j++) {
							da[j] = l[j];
						}
						break;


					case NameSpaceManager.FLOAT32:
						float f[] = getFloatArray(event, bankName, columnName);
						da = new double[len];
						for (int j = 0; j < len; j++) {
							da[j] = f[j];
						}
						break;

					case NameSpaceManager.FLOAT64:
						da = getDoubleArray(event, bankName, columnName);
						break;
					}
				}
			}
		}

		return da;
	}
}
