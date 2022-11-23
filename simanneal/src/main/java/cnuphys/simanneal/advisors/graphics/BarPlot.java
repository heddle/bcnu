package cnuphys.simanneal.advisors.graphics;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Rectangle;

import javax.swing.JComponent;

import cnuphys.bCNU.util.Fonts;
import cnuphys.bCNU.util.X11Colors;

public class BarPlot extends JComponent {
	
	private static final Color plotBG = new Color(240, 240, 240);
	private static final Color barFill = X11Colors.getX11Color("light blue");
	
	/** width of the plot area */
	public static final int width = 700;

	/** height of the plot area */
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
	
	//the value rects
	private ValueRect[] _valueRects;
	
	/**
	 * A simple bar plot
	 * @param title the title of the plot
	 * @param categories the categories (labels)
	 * @param values the values
	 */
	public BarPlot(String title, String[] categories, int[] values) {
		
		_title = title;
		_categories = categories;
		_values = values;

		//get the max value;
		for (int val : values) {
			_maxVal = Math.max(_maxVal,  val);
		}
		
	   _offsets = new int[_categories.length];
	   _valueRects = new ValueRect[_categories.length];
	   for (int i = 0; i < _valueRects.length; i++) {
		   _valueRects[i] = new ValueRect(values[i], categories[i]);
	   }

	   setPreferredSize(new Dimension(width, height));
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Rectangle bounds = new Rectangle(getBounds());
		bounds.grow(0, -20);
		
		
		System.out.println("BOUNDS: " + bounds);
		Rectangle plotBox = new Rectangle(bounds);
		plotBox.grow(-10, -60);
		
		fillAndFrame(g, bounds, plotBG, Color.black);
		fillAndFrame(g, plotBox, Color.white, Color.black);
		
		int del = plotBox.width/(_offsets.length + 1);
		int vrw = del - 4;
		
		int pbbottom = plotBox.y + plotBox.height; 

		//get the x offsets
		for (int i = 0; i < _offsets.length; i++) {
			_offsets[i] = plotBox.x + (i+1)*del;
			
			int vrh = valRectHeight(_values[i], plotBox.height);
			
			_valueRects[i].setBounds(_offsets[i] - vrw/2, pbbottom - vrh,
					vrw, vrh);
		}
		
		
		drawTitle(g, bounds);
		drawXTicks(g, pbbottom);
		drawCategories(g, pbbottom);
		drawValueRects(g);
		
	}
	
	//draw the vale rect
	private void drawValueRects(Graphics g) {
		int count = _valueRects.length;
		Font font;
		
		if (count < 20) {
			font = Fonts.largeFont;
		}
		else if (count < 30) {
			font = Fonts.mediumFont;
		}
		else {
			font = Fonts.smallFont;
		}
		
		
		g.setFont(font);
		FontMetrics fm = g.getFontMetrics();

		for (ValueRect r : _valueRects) {
			fillAndFrame(g, r, barFill, Color.black);
			
			g.setColor(Color.black);
			String s = "" + r.value;
			int sw = fm.stringWidth(s);
			int x = r.x + (r.width - sw)/2;
			int y = r.y - 4;
			g.drawString(s, x, y);
		}
	}
	
	private int valRectHeight(double val, double h) {
		h = h - 40;
		return (int)((val/_maxVal)*h);
	}
	
	//draw the title
	private void drawTitle(Graphics g, Rectangle r) {
		g.setFont(Fonts.hugeFont);
		g.setColor(Color.black);
		FontMetrics fm = g.getFontMetrics();
		int sw = fm.stringWidth(_title);
		int x = r.x + (r.width - sw)/2;
		int y = r.y + fm.getHeight() + 4;
		g.drawString(_title, x, y);
	}
	
	//draw the x tick marks
	private void drawXTicks(Graphics g, int y) {
		g.setColor(Color.black);
		int i = 0;
		for (int x : _offsets) {
			g.drawLine(x, y, x, y + ((i % 2 == 0) ? 4: 20));
			i++;
		}
	}
	
	//draw the categories
	private void drawCategories(Graphics g, int pbbottom) {
		
		int count = _categories.length;
		Font font;
		
		if (count < 20) {
			font = Fonts.mediumFont;
		}
		else {
			font = Fonts.tinyFont;
		}

		
		
		g.setFont(font);
		g.setColor(Color.black);
		FontMetrics fm = g.getFontMetrics();
		
		g.setColor(Color.black);
		
		for (int i = 0; i < _categories.length; i++) {
			String s = _categories[i];
			int sw = fm.stringWidth(s);
			int x = _offsets[i] - sw/2;
			int y = pbbottom + ((i % 2 == 0) ? 6: 22) + fm.getHeight();
			g.drawString(s, x, y);
		}
		
	}

	
	private void fillAndFrame(Graphics g, Rectangle r, Color fill, Color frame) {
		g.setColor(fill);
		g.fillRect(r.x, r.y, r.width, r.height);
		g.setColor(frame);
		g.drawRect(r.x, r.y, r.width, r.height);
		
	}

}
