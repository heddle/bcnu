package cnuphys.advisors.graphics;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Rectangle;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JPanel;

import cnuphys.bCNU.graphics.ImageManager;

public class AdvisorDisplay extends JPanel {

	//content component
	private JComponent _content;

	// the image for tiling
	private ImageIcon _icon;

	// The size of a tile.
	private Dimension _tileSize;

	//tiled background image
	private String _bgImageFile = "images/cnu.png";

	//singleton
	private static AdvisorDisplay _instance;

	private AdvisorDisplay() {
		setLayout(new BorderLayout());
		add(new AdvisorButtonPanel(), BorderLayout.SOUTH);
		add(AdvisorInfoLabel.getInstance(), BorderLayout.NORTH);
		_icon = ImageManager.getInstance().loadImageIcon(_bgImageFile);
		_tileSize = new Dimension(_icon.getIconWidth(), _icon.getIconHeight());
	}

	/**
	 * Access to the AdvisorDisplay singleton
	 * @return the AdvisorDisplay singleton
	 */
	public static AdvisorDisplay getInstance() {
		if (_instance == null) {
			_instance =new AdvisorDisplay();
		}
		return _instance;
	}

	/**
	 * Add a component that represents the content
	 *
	 * @param content the content component
	 */
	public void setContent(JComponent content) {

		if (_content != null) {
			this.remove(_content);
		}
		_content = content;
	 	add(_content, BorderLayout.WEST);

	 	Dimension cd = _content.getPreferredSize();
	 	Dimension size = getPreferredSize();
	 	size.width = cd.width+40;
	 	setMinimumSize(size);
	 	setMaximumSize(size);
	 	validate();
		repaint();
	}

	/**
	 * Tile the background.
	 *
	 * @param g the graphics context
	 */
	private void tile(Graphics g) {

		Rectangle bounds = getBounds();
		int ncol = bounds.width / _tileSize.width + 1;
		int nrow = bounds.height / _tileSize.height + 1;

		for (int i = 0; i < ncol; i++) {
			int x = i * _tileSize.width;
			for (int j = 0; j < nrow; j++) {
				int y = j * _tileSize.height;
				g.drawImage(_icon.getImage(), x, y, this);
			}
		}

	}



	@Override
	public void paintComponent(Graphics g) {
		tile(g);
	}


	@Override
	public Insets getInsets() {
		Insets def = super.getInsets();
		return new Insets(def.top + 2, def.left + 2, def.bottom + 2, def.right + 2);
	}


}
