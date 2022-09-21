package cnuphys.cnf.event.dictionary;

import java.util.Enumeration;
import java.util.Hashtable;

public class Bank extends Hashtable<String, Column> implements Comparable<Bank> {
	
	//the name of the bank
	private String _name;
	

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
	
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer(20*this.size());
		
		
		for (Column column : this.values()) {
			sb.append(column.getDescriptor()+ "\n");
		}
		return sb.toString();
	}
}
