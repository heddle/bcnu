package cnuphys.cnf.event.datatable;

import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.table.DefaultTableModel;

import org.jlab.io.base.DataEvent;

import cnuphys.bCNU.format.DoubleFormat;
import cnuphys.cnf.event.dictionary.Column;
import cnuphys.cnf.event.dictionary.Dictionary;

public class BankTableModel extends DefaultTableModel {

	private String _bankName;
	private String _columnNames[];

	// parent table
	private BankDataTable _table;

	// the data
	private DataEvent _event;

	public BankTableModel(String bankName) {
		super(getColumnNames(bankName), 2);
		_bankName = bankName;
		_columnNames = getColumnNames(bankName);
		Arrays.sort(_columnNames);
		System.out.print("");
	}

	/**
	 * Provide a link to the owner table
	 *
	 * @param table the owner table
	 */
	public void setTable(BankDataTable table) {
		_table = table;
	}

	/**
	 * Get the number of columns
	 *
	 * @return the number of columns
	 */
	@Override
	public int getColumnCount() {
		return _columnNames.length;
	}

	public void setData(DataEvent event) {
		_event = event;
		fireTableDataChanged();
	}

	/**
	 * Get the number of rows
	 *
	 * @return the number of rows
	 */
	@Override
	public int getRowCount() {
		if (_event == null) {
			return 0;
		}

		ArrayList<Column> cds = Dictionary.getInstance().columnsWithData(_event, _bankName);
		int rowCount = 0;

		for (Column cd : cds) {

			if (cd != null) {
				rowCount = Math.max(rowCount, cd.length(_event));
			}
		}

		return rowCount;
	}

	/**
	 * Get the value at a given row and column
	 *
	 * @param row the 0-based row
	 * @param col the 0-based column
	 * @return the value at a given row and column
	 */
	@Override
	public Object getValueAt(int row, int col) {
		if ((row < 0) || (col < 0)) {
			return null;
		}

		if (col == 0) {
			return " " + (row + 1);
		}

		Column column = Dictionary.getInstance().getColumn(_bankName, _columnNames[col]);
		if (column == null) {
			return "???";
		}
		int len = column.length(_event);
		if ((len == 0) || (row >= len)) {
			return "";
		}

		String fullName = column.getFullName();

		switch (column.getType()) {
		case Dictionary.INT8:
			return "" + Dictionary.getInstance().getByteArray(_event, fullName)[row];
		case Dictionary.INT16:
			return "" + Dictionary.getInstance().getShortArray(_event, fullName)[row];
		case Dictionary.INT32:
			return "" + Dictionary.getInstance().getIntArray(_event, fullName)[row];
		case Dictionary.INT64:
			return "" + Dictionary.getInstance().getLongArray(_event, fullName)[row];
		case Dictionary.FLOAT32:
			float f = Dictionary.getInstance().getFloatArray(_event, fullName)[row];
			return DoubleFormat.doubleFormat(f, 5, 4);
		case Dictionary.FLOAT64:
			double d = Dictionary.getInstance().getDoubleArray(_event, fullName)[row];
			return DoubleFormat.doubleFormat(d, 5, 4);
		default:
			return "???";
		}
	}

	@Override
	public boolean isCellEditable(int row, int column) {
		return false;
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
	 * Get the table column names, which are the names of all the known banks for
	 * this bank
	 *
	 * @return the tames of all known data columns for this bank
	 */
	public String[] columnNames() {
		return _columnNames;
	}

	// add an extra column name for index
	private static String[] getColumnNames(String bankName) {
		String cnames[] = Dictionary.getInstance().getColumnNames(bankName);

		Arrays.sort(cnames);
		String expNames[] = new String[cnames.length + 1];
		expNames[0] = "";
		for (int i = 0; i < cnames.length; i++) {
			expNames[i + 1] = cnames[i];
		}

		return expNames;
	}
}
