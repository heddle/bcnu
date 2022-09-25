package cnuphys.cnf.event.dictionary;

import org.jlab.io.base.DataBank;
import org.jlab.io.base.DataEvent;


public class Column implements Comparable<Column> {

	//the name of the column
	private String _name;

	//the data type
	private int _type;

	//the owner of the column
	private Bank _bank;

	//the bank name
	private String _bankName;

	//for table backgrounds
	public int bankIndex;

	/**
	 * Create a Column
	 * @param bank the parent bank
	 * @param name the column name
	 * @param type the data type
	 */
	public Column(Bank bank, String name, int type) {
		_bank = bank;
		_name = name;
		_type = type;
		_bankName = bank.getName();
	}

	/**
	 * Get the column name
	 * @return the column name
	 */
	public String getName() {
		return _name;
	}

	/**
	 * Set the column name
	 * @param name the column name
	 */
	protected void setName(String name) {
		_name = name;
	}

	/**
	 * Get the owner (parent) bank
	 * @return the parent bank
	 */
	public Bank getBank() {
		return _bank;
	}

	/**
	 * Get the int representing the data type
	 * @return the int representing the data type
	 */
	public int getType() {
		return _type;
	}

	/**
	 * Get the name of the data type
	 *
	 * @return the name of the data type
	 */
	public String getTypeName() {
		if ((_type < 0) || (_type >= Dictionary.typeNames.length)) {
			return "???";
		} else {
			return Dictionary.typeNames[_type];
		}
	}

	/**
	 * get the full name, i.e. A::B.C
	 * @return the full name, including the bank
	 */
	public String getFullName() {
		return _bankName + "." + _name;
	}


	/**
	 * Get a byte array for the bank and column names in the given event
	 * @param event the given event
	 * @return a byte array
	 */
	public byte[] getByteArray(DataEvent event) {
		DataBank bank = event.getBank(_bankName);
		byte[] array = bank.getByte(_name);
		return array;
	}

	/**
	 * Get a short array for the bank and column names in the given event
	 * @param event the given event
	 * @return a short array
	 */
	public short[] getShortArray(DataEvent event) {
		DataBank bank = event.getBank(_bankName);
		short[] array = bank.getShort(_name);
		return array;
	}

	/**
	 * Get an int array for the bank and column names in the given event
	 * @param event the given event
	 * @return an int array
	 */
	public int[] getIntArray(DataEvent event) {
		DataBank bank = event.getBank(_bankName);
		int[] array = bank.getInt(_name);
		return array;
	}

	/**
	 * Get a long array for the bank and column names in the given event
	 * @param event the given event
	 * @return a long array
	 */
	public long[] getLongArray(DataEvent event) {
		DataBank bank = event.getBank(_bankName);
		long[] array = bank.getLong(_name);
		return array;
	}
	/**
	 * Get a float array for the bank and column names in the given event
	 * @param event the given event
	 * @return a float array
	 */
	public float[] getFloatArray(DataEvent event) {
		DataBank bank = event.getBank(_bankName);
		float[] array = bank.getFloat(_name);
		return array;
	}

	/**
	 * Get a double array for the bank and column names in the given event
	 * @param event the given event
	 * @return a double array
	 */
	public double[] getDoubleArray(DataEvent event) {
		DataBank bank = event.getBank(_bankName);
		double[] array = bank.getDouble(_name);
		return array;
	}

	/**
	 * Get the array with double values regardless of type
	 *
	 * @return the data as a double array
	 */
	public double[] getAsDoubleArray(DataEvent event) {
		double da[] = null;

		if (event != null) {
			switch (_type) {
			case Dictionary.INT8:
				byte b[] = getByteArray(event);
				int len = (b == null) ? 0 : b.length;
				if (len > 0) {
					da = new double[len];
					for (int j = 0; j < len; j++) {
						da[j] = b[j];
					}
				}
				break;

			case Dictionary.INT16:
				short s[] = getShortArray(event);
				len = (s == null) ? 0 : s.length;
				if (len > 0) {
					da = new double[len];
					for (int j = 0; j < len; j++) {
						da[j] = s[j];
					}
				}
				break;

			case Dictionary.INT32:
				int i[] = getIntArray(event);
				len = (i == null) ? 0 : i.length;
				if (len > 0) {
					da = new double[len];
					for (int j = 0; j < len; j++) {
						da[j] = i[j];
					}
				}
				break;

			case Dictionary.INT64:
				long l[] = getLongArray(event);
				len = (l == null) ? 0 : l.length;
				if (len > 0) {
					da = new double[len];
					for (int j = 0; j < len; j++) {
						da[j] = l[j];
					}
				}
				break;


			case Dictionary.FLOAT32:
				float f[] = getFloatArray(event);
				len = (f == null) ? 0 : f.length;
				if (len > 0) {
					da = new double[len];
					for (int j = 0; j < len; j++) {
						da[j] = f[j];
					}
				}
				break;

			case Dictionary.FLOAT64:
				da = getDoubleArray(event);
				break;
			}
		}

		return da;
	}

	/**
	 * Get the length of the underlying data array
	 *
	 * @return the length of the underlying data array
	 */
	public int length(DataEvent event) {

		int len = 0;

			switch (_type) {
			case Dictionary.INT8:
				byte ba[] = getByteArray(event);
				len = (ba != null) ? ba.length : 0;
				break;

			case Dictionary.INT16:
				short sa[] = getShortArray(event);
				len = (sa != null) ? sa.length : 0;
				break;

			case Dictionary.INT32:
				int ia[] = getIntArray(event);
				len = (ia != null) ? ia.length : 0;
				break;

			case Dictionary.INT64:
				long la[] = getLongArray(event);
				len = (la != null) ? la.length : 0;
				break;

			case Dictionary.FLOAT32:
				float fa[] = getFloatArray(event);
				len = (fa != null) ? fa.length : 0;
				break;

			case Dictionary.FLOAT64:
				double da[] = getDoubleArray(event);
				len = (da != null) ? da.length : 0;
				break;
			}

		return len;
	}

	/**
	 * Get a descriptor of the bank
	 * @return a descriptor of the bank
	 */
	public String getDescriptor() {
		return String.format("[%s] [%s]", getFullName(), getTypeName());
	}

	@Override
	public int compareTo(Column o) {
		//sort with ignore case
		String s1 = getFullName().toLowerCase();
		String s2 = o.getFullName().toLowerCase();

		return s1.compareTo(s2);
	}


}
