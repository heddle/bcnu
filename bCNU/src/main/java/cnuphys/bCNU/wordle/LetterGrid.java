package cnuphys.bCNU.wordle;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Insets;

import javax.swing.JPanel;

public class LetterGrid extends JPanel {
	
	protected static int _dh = 2;
	protected static int _dv = 1;
	protected int _rectSize = 50;
	private Dimension _size;
	
	private Word _word[] = new Word[6];
	
	public LetterGrid() {
		setLayout(new GridLayout(6, 1, 0, 2));
		
		int w = 5 * (_rectSize + 2*_dh);
		int h = 6 * (_rectSize + _dv);
	
		_size = new Dimension(w, h);
		
		for (int i = 0; i < 6; i++) {
			_word[i] = new Word(w, _rectSize-2);
			add(_word[i]);
		}
		
		setBackground(Color.white);
		
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
