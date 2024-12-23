package cnuphys.bCNU.wordle;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Insets;

import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class LetterGrid extends JPanel {

	protected static int _dh = 2;
	protected static int _dv = 1;
	protected int _rectSize = 60;
	private Dimension _size;

	private Word _word[] = new Word[6];

	private static volatile LetterGrid _instance;

	private LetterGrid() {
		setLayout(new GridLayout(6, 1, 0, 2));

		int w = 5 *_rectSize + 10*_dh;
		int h = 6 * (_rectSize + _dv);

		_size = new Dimension(w, h);

		for (int i = 0; i < 6; i++) {
			_word[i] = new Word(i, w, _rectSize-2);
		}

		for (int i = 5; i >= 0; i--) {
			add(_word[i], SwingConstants.CENTER);
		}

		setBackground(Color.white);
	}

	/**
	 * Reset to start of game conditions
	 */
	public void reset() {
		for (int i = 0; i < 6; i++) {
			_word[i].reset();
		}
	}

	/**
	 * Process a letter
	 *
	 * @param index the index of the word
	 * @param c     the letter to process
	 */
	public void processLetter(int index, char c) {
        _word[index].insetChar(c);
    }


	/**
	 * Refresh the drawing
	 */
	public void refresh() {
		for (int i = 0; i < 6; i++) {
			_word[i].repaint();
		}
	}

	/**
	 * Get the singleton instance
	 *
	 * @return the singleton instance
	 */
	public static LetterGrid getInstance() {
		if (_instance == null) {
			_instance = new LetterGrid();
		}
		return _instance;
	}

	/**
	 * Set the completed flag for word at the index
	 *
	 * @param completed the completed flag
	 */
	public void setCompleted(int index, boolean completed) {
		_word[index].setCompleted(completed);
	}

	/**
	 * Get the current char array for word at the index
	 * @param index the word index
	 * @return the current char array for
	 */
	public char[] getWord(int index) {
		return _word[index].getWord();
	}

	/**
	 * Insert a char into the first null space of the word
	 * at the index. Do nothing if no null space
	 * @param index the word index
	 * @param c the char to insert
	 */
	public void insetChar(int index, char c) {
		_word[index].insetChar(c);
	}

	/**
	 * There was a bad guess for the word at the index
	 */
	public void badGuess(int index) {
		//TODO bad guess effect
	}

	/**
	 * Delete the last non-null char of the word at the index
	 * @param index the word index
	 */
	public void delete(int index) {
		_word[index].delete();
	}


	@Override
	public Insets getInsets() {
		Insets def = super.getInsets();
		return new Insets(def.top + 2, def.left + 2, def.bottom + 2, def.right + 2);
	}


	@Override
	public Dimension getPreferredSize() {
		return _size;
	}

}
