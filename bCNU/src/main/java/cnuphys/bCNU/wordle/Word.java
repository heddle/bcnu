package cnuphys.bCNU.wordle;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Rectangle;

import javax.swing.JComponent;

public class Word extends JComponent {
	
	private static int _dh = 5;
	
	private Dimension _size;
	
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
	}
	
	@Override
	public Dimension getPreferredSize() {
		return _size;
	}
}
