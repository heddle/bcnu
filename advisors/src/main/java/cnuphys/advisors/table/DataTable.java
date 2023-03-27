package cnuphys.advisors.table;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;

import cnuphys.advisors.io.DataModel;

public class DataTable extends JTable {
	
	// a scroll pane for this table
	private JScrollPane _scrollPane;
	
	private DataModel _model;
	
	public DataTable(DataModel model) {
		super(model);
		_model = model;
		
		setFont(new Font("SansSerif", Font.PLAIN, 10));
		HeaderRenderer hrender = new HeaderRenderer();
		SimpleRenderer renderer = new SimpleRenderer();

		// set preferred widths
		TableColumn column = null;
		for (int i = 0; i < getColumnCount(); i++) {
			column = getColumnModel().getColumn(i);
			column.setCellRenderer(renderer);
			column.setHeaderRenderer(hrender);			
			column.setPreferredWidth(_model.columnWidths[i]);
		}

		setGridColor(Color.lightGray);
		showVerticalLines = true;
		setRowSelectionAllowed(true);
		setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		getSelectionModel().addListSelectionListener(model);
		
		JTableHeader header = getTableHeader();
		header.addMouseListener(model);

	}

	/**
	 * Get the data model.
	 * 
	 * @return the data model.
	 */
	public DataModel getDataModel() {
		return (DataModel) getModel();
	}
	
	/**
	 * Clear all the data from the table
	 */
	public void clear() {
//		getDataModel().clear();
	}

	public JScrollPane getScrollPane() {
		if (_scrollPane == null) {
			_scrollPane = new JScrollPane(this);
		}
		return _scrollPane;
	}


}
