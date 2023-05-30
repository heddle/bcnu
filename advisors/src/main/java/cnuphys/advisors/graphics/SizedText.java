package cnuphys.advisors.graphics;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.util.ArrayList;

import javax.swing.JComponent;

import cnuphys.bCNU.util.FileUtilities;

public class SizedText extends JComponent {

	private Dimension size;

	private static int margin = 6;

	private Font _font;

	private ArrayList<String> lines = new ArrayList<>();

	public SizedText(String text, Font font, int width) {
		_font = font;
		sizeText(text, font, width);
	}


	private void sizeText(String text, Font font, int width) {
		String tokens[] = FileUtilities.tokens(text);
		FontMetrics fm = this.getFontMetrics(font);

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

		size = new Dimension(width + 2*margin, 16 + lines.size()*(fm.getHeight()+2));
	}

	@Override
	public void paintComponent(Graphics g) {
		g.setFont(_font);
		FontMetrics fm = this.getFontMetrics(_font);
		g.setColor(Color.black);
		int dh = fm.getHeight() + 2;

		int y = 7;
		for (String line : lines) {
			y += dh;
			g.drawString(line, margin, y);
		}
	}

	@Override
	public Dimension getPreferredSize() {
		return size;
	}

}
