package cnuphys.bCNU.wordle;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JComponent;
import javax.swing.Timer;

import cnuphys.bCNU.util.Fonts;
import cnuphys.splot.plot.GraphicsUtilities;

public class Key extends JComponent {

	private static final Color _unusedColor = new Color(210, 210, 210);
	
	//the label on the key
	private char _char;
	
	//the pixel width of the key
	public int width;
	
	//for a press effect
	boolean isPressed = false;
	
	public Key(int width, char label) {
		this._char = label;
		this.width = width;

		MouseAdapter ml = new MouseAdapter() {
			public void mousePressed(MouseEvent me) {

				isPressed = true;
				repaint(); // Request a repaint to update the visual appearance

				// Timer to simulate the button being pressed for a short duration
				new Timer(250, ae -> { // 250ms delay
					isPressed = false;
					repaint(); // After delay, repaint to "out" state
				}).start();
				
				// Notify the Brain
				Brain brain = Brain.getInstance();
				brain.processCharacterEntry(label);
			}

		};

		addMouseListener(ml);
	}
	
	@Override
	public Dimension getPreferredSize() {
		Dimension size = new Dimension(width, width);
		return size;
	}
	
	@Override
	public void paintComponent(Graphics g) {

		int w = getWidth();
		int h = getHeight();

		//has this letter been tried?
		int val = Brain.getInstance().used(_char);

		g.setColor((val < 0) ? _unusedColor : Colors.colors[val]);
		g.fillRect(0, 0, w-2, h-2);
		
		g.setColor(Color.darkGray);
		GraphicsUtilities.drawSimple3DRect(g, 0, 0, w-3, h-3, !isPressed);

		FontMetrics fm = g.getFontMetrics();
		int cw = fm.charWidth(_char);
		
		g.setFont(Fonts.defaultLargeFont);
		g.setColor((val < 0) ? Color.black : Color.white);
		g.drawString("" + _char, (w-cw)/2 - 2, h / 2 + 5);
	}
	
	/**
	 * Reset to start of game conditions
	 */
	public void reset() {
	}
}
