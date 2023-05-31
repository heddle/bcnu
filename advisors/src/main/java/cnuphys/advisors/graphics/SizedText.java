package cnuphys.advisors.graphics;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JComponent;

import cnuphys.bCNU.util.FileUtilities;

public class SizedText extends JComponent {
	
	private static Color bg = new Color(248, 255, 248);

	private Dimension size;

	private static int margin = 6;

	private Font _font;
	
	private int _width;

	private ArrayList<String> lines = new ArrayList<>();

	/**
	 * Create a sort of label that will size to the text
	 * @param text
	 * @param font
	 * @param width
	 */
	public SizedText(String text, Font font, int width) {
		_font = font;
		_width = width;
		sizeText(text, font, width);
		
		setBorder(BorderFactory.createLoweredBevelBorder());
	}


	private void sizeText(String text, Font font, int width) {
		String tokens[] = FileUtilities.tokens(text);
		FontMetrics fm = this.getFontMetrics(font);
		lines.clear();

		String s = "";
		for (String tok : tokens) {

			int slen = fm.stringWidth(s + tok);
			if (slen < width) {
				s += (tok + " ");
			}
			else {
				lines.add(s);
				s = tok + " ";
			}
		}
		if (s.length() > 0) {
			lines.add(s);
		}

		size = new Dimension(width + 2*margin, 12 + lines.size()*(fm.getHeight()+2));
	}

	@Override
	public void paintComponent(Graphics g) {
		
		Rectangle b = getBounds();
		g.setColor(bg);
		g.fillRect(0, 0, b.width, b.height);
		
		g.setFont(_font);
		FontMetrics fm = this.getFontMetrics(_font);
		g.setColor(Color.black);
		int dh = fm.getHeight() + 2;

		int y = 2;
		for (String line : lines) {
			y += dh;
			g.drawString(line, margin, y);
		}
	}

	@Override
	public Dimension getPreferredSize() {
		return size;
	}
	
	public void setText(String text) {
		sizeText(text, _font, _width);
		repaint();
	}

}
