package cnuphys.simanneal.advisors.io;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

import cnuphys.simanneal.advisors.model.DataAttribute;
import cnuphys.simanneal.advisors.table.DataTable;
import cnuphys.simanneal.advisors.table.InputOutput;

/**
 * This holds the data for a table
 * @author heddle
 *
 */

public abstract class DataModel extends DefaultTableModel implements ListSelectionListener, MouseListener {
	
	//one synonym per table column
	protected DataAttribute[] _columnAttributes;
	
	//the column names for the display table
	public String[] columnNames;
	
	//the column widths for the display table
	public int[] columnWidths;

	//the header of the csv file discarded and NOT used in table
	protected transient String[] _header;
	
	//the rest of the csv file discarded and NOT used in table
	protected transient ArrayList<String[]> _data;

	//the base name. The folder is the application dataDir
	protected String _baseName;
	
	//the table data 
	protected ArrayList<ITabled> _tableData = new ArrayList<>();
	
	//the table
	protected DataTable _dataTable;

	/**
	 * A CSV Data object
	 * @param baseName
	 */
	public DataModel(String baseName, DataAttribute[] atts) {
		super(colNamesFromAttributes(atts), 2);
		_baseName = baseName;
		_columnAttributes = atts;
		setColumnParameters();
				
		File file = InputOutput.openDataFile(baseName);
		
		InputOutput.debugPrint(baseName);
		
		try {
			_data = new ArrayList<>();
			new CSVReader(file, this);
			checkColumnCount();
			processData();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
	
	/**
	 * get the collection of objects used as data
	 * @return the collection of objects used as data
	 */
	public ArrayList<ITabled> getData() {
		return _tableData;
	}
	
	//used to send the column headers
	private static String[] colNamesFromAttributes(DataAttribute[] atts) {
		int len = atts.length;
		String[] names = new String[len];
		for (int i = 0; i < len; i++) {
			names[i] = atts[i].name;
		}
		
		return names;
	}
	
	/**
	 * Get the scroll pane containing the data table
	 * @return the scroll pane
	 */
	public JScrollPane getScrollPane() {
		if (_dataTable == null) {
			_dataTable = new DataTable(this);	
			
			Dimension d = _dataTable.getScrollPane().getPreferredSize();
			d.width = getPreferredWidth();
			_dataTable.getScrollPane().setPreferredSize(d);
		}
		return _dataTable.getScrollPane();
	}
	
	/**
	 * Assign the column names and widths for the display table
	 */
	private String[] setColumnParameters() {
		int len = _columnAttributes.length;
		
		columnNames = new String[len];
		columnWidths = new int[len];
		
		for (int i = 0; i < len; i++) {
			columnNames[i] = _columnAttributes[i].name;
			columnWidths[i] = _columnAttributes[i].width;
		}
		
		return columnNames;
	}
	
	//check the columns count of the csv file
	private void checkColumnCount() {
		int colCount = _header.length;
		
		for (String[] row : _data) {
			if (row.length != colCount) {
				System.err.println("Mismatch column count in " + _baseName);
				System.err.println("Header: [" + _header + "  count: " + _header.length);
				System.err.println("Bad row:");
				printRow(System.err, row);
			}
		}
	}
	
	//print a row
	protected void printRow(PrintStream ps, String[] row) {
		if (row != null) {
			int len = row.length;
			if (len > 0) {
				for (int i = 0; i < (len-1); i++) {
					ps.println(row[i] + ",");
				}
				ps.println(row[len-1]);
			}
		}
	}
	
	/**
	 * Add a row of data
	 * @param row the row to add
	 */
	public void addRow(String row[]) {
		_data.add(row);
	}
	
	//process the raw csv data
	protected abstract void processData();
	
	/**
	 * Get the data count
	 * @return the data count
	 */
	public int count() {
		return (_data == null) ? 0 : _data.size();
	}
	
	//try to get a column index
	protected int getColumnIndex(DataAttribute dataAtt) {
		int index = DataAttribute.getColumnIndex(_header, dataAtt);
		if (index < 0) {
			System.err.println(String.format("Unrecoverable error could not find column index for %s", dataAtt.name));
			System.exit(1);
		}
		return index;
	}
	
	//after processing, raw csv data is probably not needed
	protected void deleteRawData() {
		_header = null;
		_data = null;
	}
	
	/**
	 * Get the number of columns
	 * 
	 * @return the number of columns
	 */
	@Override
	public int getColumnCount() {
		return columnWidths.length;
	}
	
	
	/**
	 * Get the preferred width of the display table
	 * @return the preferred width of the display table
	 */
	public int getPreferredWidth() {
		int w = 20;

		for (int cw : columnWidths) {
			w += cw;
		}
		return w;
	}
	
	/**
	 * Get the number of rows
	 * 
	 * @return the number of rows
	 */
	@Override
	public int getRowCount() {
		return (_tableData == null) ? 0 : _tableData.size();
	}
	
	/**
	 * Get the value at a given row and column
	 * 
	 * @return the value at a given row and column
	 */
	@Override
	public Object getValueAt(int row, int col) {
		ITabled rowObject = _tableData.get(row);
		return rowObject.getValueAt(col);
	}
	
	/**
	 * Forces the cell to not be editable.
	 */
	@Override
	public boolean isCellEditable(int row, int column) {
		return false;
	}


	/**
	 * Convenience method to get the selectedRows from the table
	 * @return the selectedRows from the table
	 */
	protected int[] getSelectedRows() {
		return _dataTable.getSelectedRows();
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		if (e.getClickCount() == 1) {
			Point point = e.getPoint();
			int index = _dataTable.columnAtPoint(point);
			if (index < 0) {
				return;
			}
			String name =  _dataTable.getColumnName(index);
			sort(index, name);
		}
	}


	@Override
	public void mousePressed(MouseEvent e) {
	}


	@Override
	public void mouseReleased(MouseEvent e) {
	}


	@Override
	public void mouseEntered(MouseEvent e) {
	}
	
	@Override
	public void mouseExited(MouseEvent e) {
	}
	
	/**
	 * Sort the data based on the colum that was clicked
	 * @param column the column index
	 * @param name the name of the column
	 */
	protected void sort(final int column, String name) {
		InputOutput.debugPrintln("Clicked on header: [" + name + "] at index = " + column);
		
		Comparator<ITabled> comp = new Comparator<>() {

			@Override
			public int compare(ITabled o1, ITabled o2) {
				String s1 = o1.getValueAt(column);
				String s2 = o2.getValueAt(column);
				return s1.compareTo(s2);
			}
			
		};
		
		Collections.sort(_tableData, comp);
		fireTableDataChanged();

	}
	

}
