package cnuphys.simanneal.example.layout;

import java.awt.Color;
import java.awt.Graphics;

public class Box extends PositionedRectangle  {
	

	/** number of icons contained */
	public int count;
	
	/** number of rows */
	public int numRow;
	
	/** number of columns */
	public int numCol;
			
	/**
	 * Create a box to hold a number of icons 
	 * @param count the number of icons
	 */
	public Box(int count) {
		this.count = count;
		init();
	}
	
	/**
	 * copy constructor
	 * @param srcBox the siource
	 */
	private Box(Box srcBox) {
		x = srcBox.x;
		y = srcBox.y;
		width = srcBox.width;
		height = srcBox.height;

		count = srcBox.count;
		id = srcBox.id;
		init();
	}
	
	@Override
	public PositionedRectangle copy() {
		return new Box(this);
	}
	
	//initialize the rows and columns
	private void init() {
		int rowCol[] = new int[2];
		getRowCol(count, rowCol);
		
		numRow = rowCol[0];
		numCol = rowCol[1];
		
		width = numCol * (_size + _gap);
		height = numRow * (_size + _gap);
		
		//the mass
		mass = (int) Math.max(1,  count);
	}
	
	/**
	 * Get the number of rows and columns given the number of
	 * square "things" that go into a bigger "thing". Make it 
	 * as square as possible with the width longer than the 
	 * height (if necessary).
	 * @param n the total number of "icons"
	 * @param rowCol will hold [numrow, numcol]
	 */
	private void getRowCol(int n, int[] rowCol) {
		int nr = (int)(Math.max(1, Math.round(Math.sqrt(n))));
		int nc = 1;
		while ((nr * nc) < n) {
			nc++;
		}
		
		rowCol[0] = nr;
		rowCol[1] = nc;
	}

	
	public void draw(Graphics g) {
		g.setColor(Color.lightGray);
		g.fillRect(_bounds.x + x, _bounds.y + y, width, height);
		g.setColor(Color.gray);
		g.drawRect(_bounds.x + x, _bounds.y + y, width, height);
		
		int gs = _gap + _size;

		int index = 0;
		for (int row = 0; row < numRow; row++) {
			int yy = _bounds.y + y + _gap/2 + row*gs;
			

			for (int col = 0; col < numCol; col++) {
				index++;
				
				int xx = _bounds.x + x + _gap/2 + col*gs;
				
				g.setColor(Color.yellow);
				g.fillRect(xx, yy, _size, _size);
				g.setColor(Color.red);
				g.drawRect(xx, yy, _size, _size);
				
				if (index == count) {
					break;
				}
			}
		
		}
		
		g.setColor(Color.black);
		g.drawString("" + id, (int)getCenterX(), (int)getCenterY());
	}



}
