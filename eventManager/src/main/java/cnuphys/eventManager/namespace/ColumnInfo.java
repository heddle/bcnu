package cnuphys.eventManager.namespace;

public class ColumnInfo implements Comparable<ColumnInfo>{

	//info for the parent bank
	private BankInfo _bankInfo;

	//base column name
	private String _name;

	//full column name
	private String _fullName;

	//the data type
	private int _type;

	//used for making table look nice
	public int colorIndex;
	/**
	 * Create a column info object
	 * @param bankInfo the parent bank info object
	 * @param name the column name
	 * @param type the data type
	 */
	public ColumnInfo(BankInfo bankInfo, String name, int type) {

		if (bankInfo == null) {
			name = "???";
			return;
		}

		_bankInfo = bankInfo;
		_name = name;
		_fullName = _bankInfo.getName() + "." + _name;
		_type = type;
	}

	/**
	 * Get the info object for the parent bank
	 * @return the parent BankInfo object
	 */
	public BankInfo getBankInfo() {
		return _bankInfo;
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
	 * Get the data type
	 * @return the data type
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
		return NameSpaceManager.getTypeName(_type);
	}

	/**
	 * Get the full column name
	 * @return the full column name
	 */
	public String getFullName() {
		return _fullName;
	}

	/**
	 * Get a descriptor of the column
	 * @return a descriptor of the column
	 */
	public String getDescriptor() {
		return String.format("[%s] [%s]", getFullName(), getTypeName());
	}


	@Override
	public int compareTo(ColumnInfo o) {
		return _name.compareTo(o._name);
	}

	@Override
	public String toString() {
		return getFullName() + " [" + getTypeName() + "]";
	}

}
