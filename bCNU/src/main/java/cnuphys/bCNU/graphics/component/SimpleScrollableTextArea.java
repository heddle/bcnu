package cnuphys.bCNU.graphics.component;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.TextArea;

import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class SimpleScrollableTextArea extends JTextArea {

	
	// the scroll pane
	private JScrollPane _scrollPane;
	
	public SimpleScrollableTextArea(int numRows, int numColumns) {
		super(numRows, numColumns);
		_scrollPane = new JScrollPane(this);
	}
	
	/**
	 * Get the scroll pane.
	 * 
	 * @return the scroll pane.
	 */
	public JScrollPane getScrollPane() {
		return _scrollPane;
	}


}
