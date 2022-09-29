package cnuphys.eventManager.graphics;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

import cnuphys.bCNU.graphics.component.CommonBorder;

public class FullColumnNameList extends JList<String> {
	

	//perferred size
	private static Dimension _size = new Dimension(400, 250);

	// the scroll pane
	private JScrollPane _scrollPane;

	public FullColumnNameList() {
		super(new DefaultListModel<String>());
		setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		_scrollPane = new JScrollPane(this);
		_scrollPane.setPreferredSize(_size);
		_scrollPane.setBorder(new CommonBorder("The full names of the selected columns"));
	}
	
	/**
	 * Clear the list
	 */
	public void clear() {
		DefaultListModel<String> listModel = (DefaultListModel<String>) getModel();
		listModel.removeAllElements();
	}
	
	/**
	 * Remove all selected elements
	 */
	public void removeSelected() {
		
		List<String> selected = getSelectedValuesList();
		DefaultListModel<String> listModel = (DefaultListModel<String>)getModel();
		
		for (String s : selected) {
			listModel.removeElement(s);
		}
	}
	
	/**
	 * Get the count of full names
	 * @return the count
	 */
	public int count() {
		DefaultListModel<String> listModel = (DefaultListModel<String>)getModel();
		return listModel.size();
	}
	
	
	/**
	 * Get all the full names in the list
	 * @return all the full names in the list
	 */
	public List<String> getFullNames() {
		DefaultListModel<String> listModel = (DefaultListModel<String>)getModel();
		
		if (listModel.size() < 1) {
			return null;
		}
		
		List<String> list = new ArrayList<String>();
		for (int i = 0; i < listModel.size(); i++) {
			list.add(listModel.elementAt(i));
		}
		
		return list;
	}
	
	
	/**
	 * Get the selected full names
	 * @return a list of selected full names  (or <code>null</code>
	 */
	public List<String> getSelectedFullNames() {
		List<String> slist = getSelectedValuesList();
		if ((slist == null) || slist.isEmpty()) {
			return null;
		}
		return slist;
	}
	
	/**
	 * Add a full column name to the list
	 * @param fullColumnName
	 */
	public void add(String selectedBank, List<String> selectedColumns) {
		
		if (selectedBank == null || selectedColumns == null) {
			return;
		}
		
		ArrayList<String> allNames = new ArrayList<String>();
		
		DefaultListModel<String> listModel = (DefaultListModel<String>)getModel();
		for (int i = 0; i < listModel.size(); i++) {
			String fname = listModel.elementAt(i);
			allNames.add(fname);
		}

		
		for (String cname : selectedColumns) {
			String fname = selectedBank + "." + cname;
			if (allNames.contains(fname)) {
				continue;
			}
			allNames.add(selectedBank + "." + cname);
		}
		
		Collections.sort(allNames);
		listModel.clear();
		for (String cn : allNames) {
			listModel.addElement(cn);
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
