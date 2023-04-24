package cnuphys.advisors.checklist;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Rectangle;

import javax.swing.ImageIcon;
import javax.swing.JComponent;

import cnuphys.bCNU.component.led.LedState;
import cnuphys.bCNU.graphics.GraphicsUtilities;

public class Led extends JComponent {
	
	public static int LED_HEIGHT = 16;

	public static int LED_WIDTH = 16;

	private Color _bgColor = Color.darkGray;
	
	// draw the border
	private boolean _drawBorder;


	// state of the LED
	private LedState _state;
	
	/**
	 * Constructor
	 * 
	 * @param state the initial state.
	 * @param label the label. Should be no more than a few characters.
	 */
	public Led(LedState state, boolean drawBorder) {

		_state = state;
		_drawBorder = drawBorder;


		setPreferredSize(new Dimension(LED_WIDTH, LED_HEIGHT));
		setMaximumSize(new Dimension(LED_WIDTH, LED_HEIGHT));
	}
	
	/**
	 * Paint the LED
	 * 
	 * @param g the graphics context
	 */
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		// background
		Rectangle b = getBounds();

		if (_bgColor != null) {
			g.setColor(_bgColor);
			g.fillRect(0, 0, b.width, b.height);
		}

		// icon
		ImageIcon icon = getState().getIcon();
		if (icon != null) {
			g.drawImage(icon.getImage(), 2, (b.height - icon.getIconHeight()) / 2, this);
		}

		// label
		// border
		if (_drawBorder) {
			GraphicsUtilities.drawSimple3DRect(g, 0, 0, b.width - 1, b.height - 1, false);
		}

	}
	
	/**
	 * Get the state of the LED.
	 * 
	 * @return the current state of the LED.
	 */
	public LedState getState() {
		return _state;
	}

	/**
	 * Set the state of the LED.
	 * 
	 * @param state the new state of the LED.
	 */
	public void setState(LedState state) {
		_state = state;
	}




}
