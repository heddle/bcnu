package cnuphys.eventManager.graphics;

import java.awt.Dimension;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

import cnuphys.bCNU.graphics.component.CommonBorder;
import cnuphys.eventManager.namespace.NameSpaceManager;

public class FullColumnNameList extends JList<String> {

	
	private static Dimension _size = new Dimension(220, 250);

	// the scroll pane
	private JScrollPane _scrollPane;

	public FullColumnNameList() {
		super(new DefaultListModel());
		setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		_scrollPane = new JScrollPane(this);
		_scrollPane.setPreferredSize(_size);
		_scrollPane.setBorder(new CommonBorder("Bank Name"));
	}
	
	/**
	 * Clear the list
	 */
	public void clear() {
		DefaultListModel listModel = (DefaultListModel) getModel();
		listModel.removeAllElements();
	}
	
	/**
	 * Add a full column name to the list
	 * @param fullColumnName
	 */
	public void addFullColumn(String fullColumnName) {
		DefaultListModel listModel = (DefaultListModel) getModel();
		if (fullColumnName == null || listModel.contains(fullColumnName)) {
			return;
		}
		
		
	}
	
	public void removeSelectedColumns() {
		
	}


	/**
	 * Get the scroll pane
	 *
	 * @return the scroll pane
	 */
	public JScrollPane getScrollPane() {
		return _scrollPane;
	}

}
