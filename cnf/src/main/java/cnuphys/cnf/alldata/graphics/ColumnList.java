package cnuphys.cnf.alldata.graphics;

import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Arrays;

import javax.swing.DefaultListModel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;



import cnuphys.bCNU.graphics.component.CommonBorder;
import cnuphys.cnf.event.dictionary.Dictionary;

public class ColumnList extends DragDropList implements KeyListener {

	private static Dimension _size = new Dimension(220, 250);

	// the scroll pane
	private JScrollPane _scrollPane;

	public ColumnList() {
		this(ListSelectionModel.SINGLE_SELECTION);
	}
	
	public ColumnList(int selectionMode) {
		super(new DefaultListModel());
		setSelectionMode(selectionMode);
		_scrollPane = new JScrollPane(this);
		_scrollPane.setPreferredSize(_size);
		_scrollPane.setBorder(new CommonBorder("Column Name"));
		addKeyListener(this);
	}


	private void clear() {
		DefaultListModel listModel = (DefaultListModel) getModel();
		listModel.removeAllElements();
	}

	/**
	 * Set the list to the columns of the given bank
	 * 
	 * @param bankName the name of the bank
	 */
	public void setList(String bankName) {
		if (bankName != null) {
			String columns[] = Dictionary.getInstance().getColumnNames(bankName);
			if (columns != null) {
				Arrays.sort(columns);
				
				DefaultListModel model = (DefaultListModel)(this.getModel());
				model.clear();
				for (String cn : columns) {
					model.addElement(cn);
				}
			} else {
				clear();
			}
		} else {
			clear();
		}
	}

	/**
	 * Get the scroll pane
	 * 
	 * @return the scroll pane
	 */
	public JScrollPane getScrollPane() {
		return _scrollPane;
	}

	@Override
	public void keyTyped(KeyEvent e) {
//		System.out.println("key typed");
	}

	@Override
	public void keyPressed(KeyEvent e) {
		int selected[] = getSelectedIndices();
		
		if ((selected == null) || (selected.length != 1)) {
			return;
		}
		
		int index = selected[0];
		int dropIndex = -1;
		
		System.out.println("key pressed");
		int keyCode = e.getKeyCode();
		if (keyCode == KeyEvent.VK_DOWN) {
			dropIndex = index+1;
			System.out.println(" down arrow on index: " + index);
		}
		if (keyCode == KeyEvent.VK_UP) {
			dropIndex = index-1;
			System.out.println(" up arrow on index: " + index);
		}
		
		swap(index, dropIndex);
	}

	@Override
	public void keyReleased(KeyEvent e) {
	}

}
