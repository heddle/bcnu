package cnuphys.bCNU.application;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.InvalidPropertiesFormatException;
import java.util.Properties;

import javax.swing.ImageIcon;
import javax.swing.JDesktopPane;
import javax.swing.JFileChooser;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;

import cnuphys.bCNU.drawable.IDrawable;
import cnuphys.bCNU.graphics.ImageManager;
import cnuphys.bCNU.log.Log;
import cnuphys.bCNU.ping.IPing;
import cnuphys.bCNU.ping.Ping;
import cnuphys.bCNU.util.Environment;
import cnuphys.bCNU.util.X11Colors;
import cnuphys.bCNU.view.BaseView;

/**
 * This class is used for the desktop.
 *
 * @author heddle
 */
@SuppressWarnings("serial")
public final class Desktop extends JDesktopPane implements MouseListener, MouseMotionListener {

	/**
	 * Used to operate (e.g., refresh) all views
	 */
	public static final int ALL_VIEWS = 0;

	/**
	 * Used to operate (e.g., refresh) top view only
	 */
	public static final int TOP_VIEW = 1;

	/**
	 * View configuration properties
	 */
	private Properties _properties;

	// If <code>true</code>, tile the background with an image.
	boolean tile;

	// the image for tiling
	private ImageIcon _icon;

	// The size of a tile.
	private Dimension _tileSize;

	// the singleton
	private static Desktop instance;

	// optional after drawer
	private IDrawable _afterDraw;

	private boolean mouseReleased = false;

	//hack related to internal frame drag bug
	private static Ping _ping;
	private JInternalFrame _dragFrame;
	private long _lastDragTime = Long.MAX_VALUE;
	private int _lastX;
	private int _lastY;

	/**
	 * Create a desktop pane.
	 *
	 * @param background      optional background color.
	 * @param backgroundImage optional background image. Will be tiled. Probably
	 *                        reference into a jar file, such as
	 *                        "images/background.png".
	 */
	private Desktop(Color background, String backgroundImage) {

		setDragMode(JDesktopPane.OUTLINE_DRAG_MODE); // faster
		setDoubleBuffered(true);

		if (background != null) {
			setBackground(background);
		} else {
			setBackground(X11Colors.getX11Color("royal blue"));
		}

		// tile?
		tile = false;
		if (backgroundImage != null) {
			_icon = ImageManager.getInstance().loadImageIcon(backgroundImage);
			if (_icon != null) {
				tile = true;
				_tileSize = new Dimension(_icon.getIconWidth(), _icon.getIconHeight());
				if ((_tileSize.width < 2) || (_tileSize.height < 2)) {
					tile = false;
				}
			}
		}

		_ping = new Ping(1000);
		//use the ping to deal with hover
		IPing pingListener = new IPing() {

			@Override
			public void ping() {
				heartbeat();
			}

		};
		_ping.addPingListener(pingListener);
	}


	private void heartbeat() {
		if (_dragFrame == null) {
			return;
		}
		long del = System.currentTimeMillis() - _lastDragTime;
		if ((del > 1000) && (_dragFrame != null)) {
			System.err.println("mouse released missed!!!");

			MouseEvent mouseEvent = new MouseEvent(_dragFrame, // target component
					MouseEvent.MOUSE_RELEASED, // event type
					System.currentTimeMillis(), // current time
					0, // no modifiers
					_lastX, // x-coordinate
					_lastY, // y-coordinate
					1, // click count
					false, // not a popup trigger
					MouseEvent.BUTTON1 // left button
			);

			_dragFrame.dispatchEvent(mouseEvent);

		}
	}


	/**
	 * Create a desktop pane.
	 *
	 * @param background      optional background color.
	 * @param backgroundImage optional background image. Will be tiled. Probably
	 *                        reference into a jar file, such as
	 *                        "images/background.png".
	 */
	public static Desktop createDesktop(Color background, String backgroundImage) {
		if (instance == null) {
			instance = new Desktop(background, backgroundImage);
		}
		return instance;
	}

	/**
	 * Access to the singleton
	 *
	 * @return the singleton desktop
	 */
	public static Desktop getInstance() {
		return instance;
	}

	/**
	 * The paint method for the desktop. This is where the background image gets
	 * tiled
	 *
	 * @param g the graphics context.
	 */
	@Override
	public void paintComponent(Graphics g) {

		if (tile) {
			tile(g);
		} else {
			super.paintComponent(g);
		}

		if (_afterDraw != null) {
			_afterDraw.draw(g, null);
		}
	}

	/**
	 * Set an "after" draw
	 *
	 * @param afterDraw the drawable
	 */
	public void setAfterDraw(IDrawable afterDraw) {
		_afterDraw = afterDraw;
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
	public Component add(Component comp, int index) {

		if (comp instanceof JInternalFrame) {
			JInternalFrame frame = (JInternalFrame) comp;
			frame.setOpaque(false);

			frame.addMouseListener(this);
			frame.addMouseMotionListener(this);
		}

		return super.add(comp, index);
    }

	/**
	 * Gets the top internal frame. Surprising that we had to write this.
	 *
	 * @return the top internal frame.
	 */
	public JInternalFrame getTopFrame() {

		int minIndex = -1;
		int minZorder = 99999;

		JInternalFrame frames[] = getAllFrames();
		if (frames != null) {
			for (int index = 0; index < frames.length; index++) {
				if (getComponentZOrder(frames[index]) < minZorder) {
					minZorder = getComponentZOrder(frames[index]);
					minIndex = index;
				}
			}
		}

		if (minIndex < 0) {
			return null;
		}
		return frames[minIndex];
	}


	/**
	 * Load the configuration file that preserves the an arrangement of views.
	 */
	public void loadConfigurationFile() {

		File file = Environment.getInstance().getConfigurationFile();

		try {
			if ((file != null) && file.exists() && file.canRead()) {
				try {
					FileInputStream fis = new FileInputStream(file);
					_properties = new Properties();
					try {
						_properties.loadFromXML(fis);
					} catch (InvalidPropertiesFormatException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
					try {
						fis.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
					Log.getInstance().info("Loaded a configuration file from [" + file.getPath() + "]");
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
			} else {
				Log log = Log.getInstance();
				if (log != null) {
					Log.getInstance().info("Did not load a configuration file.");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * configure the views based on the properties (which were read-in by
	 * loadConfigurationFile)
	 */
	public void configureViews() {
		JInternalFrame[] frames = getAllFrames();
		if (frames != null) {
			for (JInternalFrame frame : frames) {
				if (frame instanceof BaseView) {
					BaseView view = (BaseView) frame;
					view.setFromProperties(_properties);
				}

			}
		}
	}

	/**
	 * Write the configuration file that preserves the current arrangement of views.
	 */
	public void writeConfigurationFile() {

		File file = Environment.getInstance().getConfigurationFile();

		if (file.exists()) {
			int answer = JOptionPane.showConfirmDialog(null,
					file.getAbsolutePath() + "  already exists.\nDo you want to overwrite it?",
					"Overwite Existing File?", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,
					ImageManager.cnuIcon);

			if (answer != JFileChooser.APPROVE_OPTION) {
				return;
			}
		} // end file exists check

		Properties properties = new Properties();
		JInternalFrame[] frames = getAllFrames();

		if (frames != null) {
			for (JInternalFrame frame : frames) {
				if (frame instanceof BaseView) {
					BaseView view = (BaseView) frame;
					Properties vprops = view.getConfigurationProperties();
					properties.putAll(vprops);
				}

			}
		}

		// write config file
		try {
			FileOutputStream fos = new FileOutputStream(file);
			try {
				properties.storeToXML(fos, null);
				fos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Delete the configuration file that preserves the current arrangement of
	 * views.
	 */
	public void deleteConfigurationFile() {

		File file = Environment.getInstance().getConfigurationFile();

		if (file.exists()) {
			int answer = JOptionPane.showConfirmDialog(null, "Confim delete operation (this can not be undone).",
					"Delete Configuration?", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE,
					ImageManager.cnuIcon);

			if (answer != JFileChooser.APPROVE_OPTION) {
				return;
			}
			file.delete();
		}
	}


	@Override
	public void mouseDragged(MouseEvent e) {

		Component component = e.getComponent();
		if (component instanceof JInternalFrame) {
			_dragFrame = (JInternalFrame) component;
			_lastDragTime = System.currentTimeMillis();
			_lastX = e.getX();
			_lastY = e.getY();
		}

	}

	@Override
	public void mouseMoved(MouseEvent e) {
	}

	@Override
	public void mouseClicked(MouseEvent e) {
	}

	@Override
	public void mousePressed(MouseEvent e) {
		mouseReleased = false;
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		mouseReleased = true;
        _dragFrame = null;
        _lastDragTime = Long.MAX_VALUE;
     }

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}


}
