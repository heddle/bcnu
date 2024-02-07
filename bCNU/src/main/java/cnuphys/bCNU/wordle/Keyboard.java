package cnuphys.bCNU.wordle;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Insets;

import javax.swing.JPanel;

public class Keyboard extends JPanel {
	
	public static final char BACKSPACE = '\u232B';
	
	//enter
	public static final char ENTER = '\u21B5';


	private int keyWidth;
	
	private ButtonRow rows[] = new ButtonRow[3];
	
	/**
	 * Create a keyboard
	 * 
	 * @param keyWidth the width of the keys
	 */
	
	public Keyboard(int keyWidth) {
		this.keyWidth = keyWidth;
		setLayout(new GridLayout(3, 1, 4, 6));
		createRows(keyWidth);
	}
	
	public Dimension getPreferredSize() {
		return new Dimension(11*(keyWidth + 4), 3*(keyWidth + 6));
	}
	
	@Override
	public Insets getInsets() {
		Insets def = super.getInsets();
		return new Insets(def.top + 6, def.left + 2, def.bottom + 6, def.right + 2);
	}

	//create the three rows of keys
	private void createRows(int keyWidth) {
		rows[0] = new ButtonRow(keyWidth, 'Q', 'W', 'E', 'R', 'T', 'Y', 'U', 'I', 'O', 'P');
		rows[1] = new ButtonRow(keyWidth, 'A', 'S', 'D', 'F', 'G', 'H', 'J', 'K', 'L');
		rows[2] = new ButtonRow(keyWidth, ENTER, 'Z', 'X', 'C', 'V', 'B', 'N', 'M', BACKSPACE);
		
		for (ButtonRow row : rows) {
			add(row);
		}
	}

}
