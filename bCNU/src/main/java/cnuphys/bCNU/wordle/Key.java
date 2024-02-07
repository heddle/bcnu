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

	private int state = Colors.NEUTRAL;
	
	public char label;
	
	public int width;
	
	boolean isPressed = false;
	
	public Key(int width, char label) {
		this.label = label;
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

		g.setColor(Colors.colors[state]);
		g.fillRect(0, 0, w-1, h-1);

		g.setColor(Color.darkGray);
		GraphicsUtilities.drawSimple3DRect(g, 0, 0, w-2, h-2, !isPressed);

		FontMetrics fm = g.getFontMetrics();
		int cw = fm.charWidth(label);
		
		g.setFont(Fonts.defaultLargeFont);
		g.setColor(Color.white);
		g.drawString("" + label, (w-cw)/2 - 2, h / 2 + 5);
	}
	
	public void setState(int state) {
		this.state = state;
	}
	
	public int getState() {
		return state;
	}
}
