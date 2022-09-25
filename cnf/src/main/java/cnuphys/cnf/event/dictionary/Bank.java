package cnuphys.cnf.event.dictionary;

import java.util.Collection;
import java.util.Enumeration;
import java.util.Hashtable;

import org.jlab.io.base.DataEvent;

public class Bank extends Hashtable<String, Column> implements Comparable<Bank> {

	//the name of the bank
	private String _name;


	/**
	 * Create a bank with a given name
	 * @param name r=then name of the bank
	 */
	public Bank(String name) {
		super(20);
		_name = name;
	}

	@Override
	public int compareTo(Bank o) {
		//sort with ignore case
		String s1 = _name.toLowerCase();
		String s2 = o._name.toLowerCase();

		return s1.compareTo(s2);
	}

	/**
	 * Get the bank name
	 * @return the bank name
	 */
	public String getName() {
		return _name;
	}

	/**
	 * Set the bank name
	 * @param name the bank name
	 */
	protected void setName(String name) {
		_name = name;
	}


	/**
	 * Get the column from the column name
	 * @param columnName the name of the column
	 * @return  the column, or null if not found
	 */
	public Column getColumn(String columnName) {
		return get(columnName);
	}

	/**
	 * Get the column from the full name than includes the bank
	 * @param fullName the full name of the column
	 * @return  the column, or null if not found
	 */
	public Column getColumnFromFullName(String fullName) {
		String columnName = Dictionary.columnNameFromFullName(fullName);
		if (columnName == null)  {
			return null;
		}
		return getColumn(columnName);
	}

	/**
	 * An array of all the column names in the bank
	 * @return all the column names in the bank
	 */
	public String[] getColumnNames() {
		Enumeration e = keys();

		String[] cnames = new String[size()];

		int index = 0;
		while (e.hasMoreElements()) {
			String columnName = (String) e.nextElement();
			cnames[index++] = columnName;
		}

		return cnames;
	}

	/**
	 * Does the bank have any data for this evet?n
	 * @param event the event
	 * @return true if at least one column has a length > 0
	 */
	public boolean hasData(DataEvent event) {
		for (Column column : values()) {
			if (column.length(event) > 0) {
				return true;
			}
		}
		return false;
	}



	/**
	 * Get all the columns
	 * @return the collection of columns
	 */
	public Collection<Column> getColumns() {
		return values();
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer(20*this.size());


		for (Column column : values()) {
			sb.append(column.getDescriptor()+ "\n");
		}
		return sb.toString();
	}
}
