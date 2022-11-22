package cnuphys.simanneal.advisors.graphics;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Rectangle;

import javax.swing.JComponent;

import cnuphys.bCNU.util.Fonts;

public class BarPlot extends JComponent {
	
	public static final Color plotBG = new Color(240, 240, 240);
	
	/** width of the active layout bounds */
	public static final int width = 700;

	/** height of the active layout bounds */
	public static final int height = 600;

	//title of the barplot
	private String _title;

	//category lables
	private String[] _categories;

	//data values
	private int[] _values;
	
	//horizontal evenly spaces offsets
	private int[] _offsets;

	//the max value
	private int _maxVal;

	public BarPlot(String title, String[] labels, int[] values) {
		
		_title = title;
		_categories = labels;
		_values = values;

		for (int val : values) {
			_maxVal = Math.max(_maxVal,  val);
		}
		
	   _offsets = new int[_categories.length];

	   setPreferredSize(new Dimension(width, height));
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Rectangle bounds = new Rectangle(getBounds());
		bounds.grow(0, -20);
		
		
		System.out.println("BOUNDS: " + bounds);
		Rectangle plotBox = new Rectangle(bounds);
		plotBox.grow(-50, -60);
		
		fillAndFrame(g, bounds, plotBG, Color.black);
		fillAndFrame(g, plotBox, Color.white, Color.black);
		
		int del = plotBox.width/(_offsets.length + 1);
		for (int i = 0; i < _offsets.length; i++) {
			_offsets[i] = plotBox.x + (i+1)*del;
		}
		
		drawTitle(g, bounds);
		drawXTicks(g, plotBox.y + plotBox.height);
		drawCategories(g, plotBox);
		
		
	}
	
	//draw the title
	private void drawTitle(Graphics g, Rectangle r) {
		g.setFont(Fonts.hugeFont);
		g.setColor(Color.black);
		FontMetrics fm = getFontMetrics(Fonts.hugeFont);
		int sw = fm.stringWidth(_title);
		int x = r.x + (r.width - sw)/2;
		int y = r.y + fm.getHeight() + 4;
		g.drawString(_title, x, y);
	}
	
	private void drawXTicks(Graphics g, int y) {
		g.setColor(Color.black);
		int i = 0;
		for (int x : _offsets) {
			g.drawLine(x, y, x, y + ((i % 2 == 0) ? 4: 20));
			i++;
		}
	}
	
	//draw the categories
	private void drawCategories(Graphics g, Rectangle r) {
		g.setFont(Fonts.hugeFont);
		g.setColor(Color.black);
		FontMetrics fm = getFontMetrics(Fonts.hugeFont);
	}

	
	private void fillAndFrame(Graphics g, Rectangle r, Color fill, Color frame) {
		g.setColor(fill);
		g.fillRect(r.x, r.y, r.width, r.height);
		g.setColor(frame);
		g.drawRect(r.x, r.y, r.width, r.height);
		
	}

}
