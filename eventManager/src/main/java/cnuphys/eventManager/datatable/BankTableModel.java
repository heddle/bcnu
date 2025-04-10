package cnuphys.eventManager.datatable;

import java.util.Arrays;

import javax.swing.table.DefaultTableModel;

import org.jlab.io.base.DataEvent;

import cnuphys.bCNU.format.DoubleFormat;
import cnuphys.eventManager.namespace.DataUtils;
import cnuphys.eventManager.namespace.NameSpaceManager;

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
		return DataUtils.bankLength(_event, _bankName);
	}

	/**
	 * Get the value at a given row and column
	 *
	 * @param row the 0-based row
	 * @param col the 0-based column (column 0 is for index)
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

		int len = getRowCount();

		if ((len == 0) || (row >= len)) {
			return "";
		}

		String columnName = _columnNames[col];
		int type = DataUtils.getDataType(_bankName, columnName);

		switch (type) {
		case NameSpaceManager.INT8:
			return "" + DataUtils.getByteArray(_event, _bankName, columnName)[row];
		case NameSpaceManager.INT16:
			return "" + DataUtils.getShortArray(_event, _bankName, columnName)[row];
		case NameSpaceManager.INT32:
			return "" + DataUtils.getIntArray(_event, _bankName, columnName)[row];
		case NameSpaceManager.INT64:
			return "" + DataUtils.getLongArray(_event, _bankName, columnName)[row];
		case NameSpaceManager.FLOAT32:
			float f = DataUtils.getFloatArray(_event, _bankName, columnName)[row];
			return DoubleFormat.doubleFormat(f, 5, 4);
		case NameSpaceManager.FLOAT64:
			double d = DataUtils.getDoubleArray(_event, _bankName, columnName)[row];
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
		String cnames[] = NameSpaceManager.getInstance().getColumnNames(bankName);

		//add an extra for index in column 0
		String expNames[] = new String[cnames.length + 1];
		expNames[0] = "";
		for (int i = 0; i < cnames.length; i++) {
			expNames[i + 1] = cnames[i];
		}

		return expNames;
	}
}
