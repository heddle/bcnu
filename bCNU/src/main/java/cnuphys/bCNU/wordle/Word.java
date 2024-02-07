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
	
	Character _letters[] = {null, null, 'J', null, 'G'};
	
	// The letter rectangles
	private Rectangle _rect[] = new Rectangle[5];

	public Word(int w, int rectSize) {
		setLayout(new GridLayout(1, 5, LetterGrid._dh, LetterGrid._dv));
		
		_size = new Dimension(w, rectSize + LetterGrid._dh);
		
		for (int i = 0; i < 5; i++) {
			_rect[i] = new Rectangle(LetterGrid._dh + i * (rectSize + _dh), 0, rectSize, rectSize);
		}
	}
	
	@Override
	public void paintComponent(java.awt.Graphics g) {
		super.paintComponent(g);
		
		g.setColor(Color.lightGray);
		for (int i = 0; i < 5; i++) {
			g.drawRect(_rect[i].x, _rect[i].y, _rect[i].width, _rect[i].height);
		}
		
		g.setFont(_font);
		g.setColor(Color.red);
		FontMetrics fm = g.getFontMetrics();
		
		for (int i = 0; i < 5; i++) {
			if (_letters[i] != null) {
				int fh = fm.getAscent();
				int cw = fm.charWidth(_letters[i]);
				g.drawString(_letters[i].toString(), _rect[i].x + 5, _rect[i].y + _rect[i].height - 5);
			}
		}
		
	}
	
	@Override
	public Dimension getPreferredSize() {
		return _size;
	}
}
