package cnuphys.bCNU.wordle;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Insets;

import javax.swing.JPanel;

public class Keyboard extends JPanel {
	
	public static final char BACKSPACE = '\u232B';
	
	//enter
	public static final char ENTER = '\u21B5';


	//pixel width of a key
	private int keyWidth;
	
	//the three rows of keys
	private KeyboardRow rows[] = new KeyboardRow[3];
	
	//the enter key
	public Key enterKey;
	
	//the backspace key
	public Key deleteKey;
	
	private static volatile Keyboard _instance;
	
	/**
	 * Create a keyboard
	 * 
	 * @param keyWidth the width of the keys
	 */
	
	private Keyboard(int keyWidth) {
		this.keyWidth = keyWidth;
		setLayout(new GridLayout(3, 1, 3, 4));
		createRows(keyWidth);
	}
	
	/**
	 * Get the singleton instance
	 * 
	 * @return the singleton instance
	 */
	public static Keyboard getInstance() {
		if (_instance == null) {
			_instance = new Keyboard(28);
		}
		return _instance;
	}
	
	/**
	 * Reset to start of game conditions
	 */
	public void reset() {
		for (KeyboardRow row : rows) {
			row.reset();
		}
	}
	
	/**
	 * Refresh the drawing
	 */
	public void refresh() {
		for (KeyboardRow row : rows) {
			row.refresh();
		}
	}
	
	public Dimension getPreferredSize() {
		return new Dimension(11*(keyWidth + 3), 3*(keyWidth + 4));
	}
	
	@Override
	public Insets getInsets() {
		Insets def = super.getInsets();
		return new Insets(def.top + 6, def.left + 2, def.bottom + 6, def.right + 2);
	}

	//create the three rows of keys
	private void createRows(int keyWidth) {
		rows[0] = new KeyboardRow(keyWidth, 'Q', 'W', 'E', 'R', 'T', 'Y', 'U', 'I', 'O', 'P');
		rows[1] = new KeyboardRow(keyWidth, 'A', 'S', 'D', 'F', 'G', 'H', 'J', 'K', 'L');
		rows[2] = new KeyboardRow(keyWidth, ENTER, 'Z', 'X', 'C', 'V', 'B', 'N', 'M', BACKSPACE);
		
		enterKey = rows[2].getKey(0);
		deleteKey = rows[2].getKey(8);
		
		for (KeyboardRow row : rows) {
			add(row);
		}
	}

}
