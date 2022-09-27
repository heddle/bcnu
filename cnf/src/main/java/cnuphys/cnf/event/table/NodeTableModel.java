package cnuphys.cnf.event.table;

import java.util.ArrayList;
import java.util.Collections;

import javax.swing.table.DefaultTableModel;

import org.jlab.io.base.DataEvent;

import cnuphys.cnf.event.namespace.ColumnInfo;
import cnuphys.cnf.event.namespace.DataUtils;
import cnuphys.cnf.event.namespace.NameSpaceManager;

public class NodeTableModel extends DefaultTableModel {

	// indices
	private static final int NAME_INDEX = 0;
	private static final int TYPE_INDEX = 1;
	private static final int COUNT_INDEX = 2;

	// the names of the columns
	protected static final String colNames[] = { "Name", "Type", "Count" };

	// the column widths
	protected static final int columnWidths[] = { 270, // name
			110, // type
			110, // length
	};

	// the model data
	private ArrayList<ColumnInfo> _data = new ArrayList<>();

	// the current event
	private DataEvent _event;

	/**
	 * Constructor
	 */
	public NodeTableModel() {
		super(colNames, 2);
	}

	/**
	 * Find the row by the value in the name column
	 *
	 * @param name the full column name to search for
	 * @return the row, or -1
	 */
	public int findRowByName(String name) {

		if ((name != null) && (_data != null) && !_data.isEmpty()) {

			int index = 0;
			for (ColumnInfo cd : _data) {
				if (name.equals(cd.getFullName())) {
					return index;
				}
				index++;
			}
		}

		return -1;
	}

	/**
	 * Get the event being displayed
	 *
	 * @return the event being displayed
	 */
	public DataEvent getCurrentEvent() {
		return _event;
	}

	/**
	 * Get the number of columns
	 *
	 * @return the number of columns
	 */
	@Override
	public int getColumnCount() {
		return colNames.length;
	}

	/**
	 * Get the number of rows, which is the total
	 * number of columns for all columns with data
	 * @return the number of rows
	 */
	@Override
	public int getRowCount() {
		return (_data == null) ? 0 : _data.size();
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
		
		
		if (row < getRowCount()) {
			ColumnInfo cd = _data.get(row);

			if (cd != null) {
				switch (col) {
				case NAME_INDEX:
					return cd.getFullName();

				case TYPE_INDEX:
					return cd.getTypeName();

				case COUNT_INDEX:
					String rs = "" + DataUtils.bankLength(_event, cd.getBankInfo().getName());
					return rs;

				default:
					return "?";
				}
			}
		}

		return "";
	}

	/**
	 * Clear all the data
	 */
	public void clear() {
		_data = new ArrayList<>();
	}

	/**
	 * @param data the data to set
	 */
	public void setData(DataEvent event) {
		clear();
		_event = event;

		_data = DataUtils.columnsWithData(event);
		fireTableDataChanged();
	}

	/**
	 * Get the column name of the bank column at the given row
	 *
	 * @param row the row in question
	 * @return the corresponding data bank column name, or <code>null</code>
	 */
	public ColumnInfo getColumnData(int row) {
		if (row < 0) {
			return null;
		}

		ColumnInfo cd = (_data == null) ? null : _data.get(row);
		return cd;
	}

	/**
	 * Forces the cell to not be editable.
	 */
	@Override
	public boolean isCellEditable(int row, int column) {
		return false;
	}

}
