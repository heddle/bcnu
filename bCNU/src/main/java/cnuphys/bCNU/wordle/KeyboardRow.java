package cnuphys.bCNU.wordle;

import java.awt.FlowLayout;

import javax.swing.JPanel;

public class KeyboardRow extends JPanel {

	private Key keys[];

	public KeyboardRow(int width, char...labels) {
		setLayout(new FlowLayout(FlowLayout.CENTER, 4, 0));

		keys = new Key[labels.length];
		for (int i = 0; i < labels.length; i++) {
			keys[i] = new Key(width, labels[i]);
			add(keys[i]);
		}
	}

	/**
	 * Reset to start of game conditions
	 */
	public void reset() {
		for (Key key : keys) {
			key.reset();
		}
	}

	/**
	 * Refresh the drawing
	 */
	public void refresh() {
		for (Key key : keys) {
			key.repaint();
		}
	}

	/**
	 * Get the key at the given index
	 *
	 * @param index the index
	 * @return the key at the given index
	 */
	public Key getKey(int index) {
        if ((index < 0) || (index >= keys.length)) {
            return null;
        }
        return keys[index];
    }
}
