package cnuphys.bCNU.wordle;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GridLayout;
import java.awt.Rectangle;

import javax.swing.JComponent;

import cnuphys.bCNU.util.Fonts;

public class Word extends JComponent {
	
	private static int _dh = 5;
	
	private Font _font = Fonts.commonFont(Font.BOLD, 36);
	
	private Dimension _size;
	
	private  int _rectHeight;
	
	/** the null char */
	public static final char nullChar = '\0';
	
	//chars for each block
	private char _letters[] = {nullChar, nullChar, nullChar, nullChar, nullChar};
	
	//values for each block
	private int _values[] = {0, 0, 0, 0, 0};
	
	// The letter rectangles
	private Rectangle _rect[] = new Rectangle[5];

	public Word(int w, int rectH) {
		setLayout(new GridLayout(1, 5, LetterGrid._dh, LetterGrid._dv));
		_rectHeight = rectH;
		
		_size = new Dimension(w, rectH + LetterGrid._dh);
		
		for (int i = 0; i < 5; i++) {
			_rect[i] = new Rectangle();
		}
	}
	
	/**
	 * Get the current char array
	 * @return 
	 */
	public char[] getWord() {
		return _letters;
	}
	
	/**
	 * Insert a char into the first null space. Do nothing if no null space
	 * @param c the char to insert
	 */
	public void insetChar(char c) {
		for (int i = 0; i < 5; i++) {
			if (_letters[i] == nullChar) {
				_letters[i] = c;
				break;
			}
		}
	}
	
	/**
	 * Delete the last non-null char
	 */
	public void delete() {
		for (int i = 4; i >= 0; i--) {
			if (_letters[i] != nullChar) {
				_letters[i] = nullChar;
				break;
			}
		}
	}

	
	//reset the word as in start of a new game
	public void reset() {
		for (int i = 0; i < 5; i++) {
			_letters[i] = nullChar;
			_values[i] = 0;
		}
	}

	@Override
	public void paintComponent(java.awt.Graphics g) {
		super.paintComponent(g);
		
		String s = new String(_letters);
		String answer = Brain.getInstance().getCurrentWord();
		Scorer.scoreGuess(answer, s, _values);
		

		Rectangle r = getBounds();

		int w = (r.width - 6 * _dh) / 5;

		for (int i = 0; i < 5; i++) {
			int x = _dh + i * (w + _dh);
			_rect[i].setBounds(x, _dh/2, w, _rectHeight - _dh/2);
		}
		
		for (int i = 0; i < 5; i++) {
			g.setColor(Colors.colors[_values[i]]);
			g.fillRect(_rect[i].x, _rect[i].y, _rect[i].width, _rect[i].height);
			g.setColor(Color.lightGray);
			g.drawRect(_rect[i].x, _rect[i].y, _rect[i].width, _rect[i].height);
		}
		
		g.setFont(_font);
		g.setColor(Color.white);
		FontMetrics fm = g.getFontMetrics();
		
		for (int i = 0; i < 5; i++) {
			if (_letters[i] != nullChar) {
				int fh = fm.getAscent();
				int cw = fm.charWidth(_letters[i]);
				int x = _rect[i].x + (_rect[i].width - cw)/2;
				int y = _rect[i].y + (_rect[i].height + fh)/2 - _dh;
				g.drawString(""+_letters[i], x, y);
			}
		}
		
	}
	
	@Override
	public Dimension getPreferredSize() {
		return _size;
	}
}
