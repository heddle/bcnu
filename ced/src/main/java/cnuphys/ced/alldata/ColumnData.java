package cnuphys.ced.alldata;

/**
 * This class represents a column of data in a bank.
 * Its is used for the node panel
 *
 */
public class ColumnData {


	/** the bank name */
	public final String bankName;

	/** the column name */
	public final String columnName;

	/** the data type of the column */
	public final int type;

	/** the full name of the column */
	public final String fullName;

	/** the type name */
	public final String typeName;

	/** used for table rendering */
	public int bankIndex;


	public ColumnData(String bankName, String columnName, int type, int bankIndex) {
		this.bankName = bankName;
		this.columnName = columnName;
		this.type = type;
		this.bankIndex = bankIndex ;

		fullName = bankName + "." + columnName;
		typeName = DataWarehouse.getInstance().getTypeName(bankName, columnName);
	}

	/**
	 * Get the length of the backing data array
	 * @return
	 */
	public int getCount() {
		return DataWarehouse.getInstance().rows(bankName);
	}

}
