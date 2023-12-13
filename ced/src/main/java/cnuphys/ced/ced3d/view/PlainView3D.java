package cnuphys.ced.ced3d.view;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;

import javax.swing.Box;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import cnuphys.bCNU.graphics.GraphicsUtilities;

import cnuphys.bCNU.util.PropertySupport;
import cnuphys.bCNU.view.BaseView;
import cnuphys.ced.ced3d.PlainPanel3D;

public abstract class PlainView3D extends BaseView implements ActionListener {

	// the menu bar
	private final JMenuBar _menuBar;


	// the 3D panel
	protected final PlainPanel3D _panel3D;

	// menu
	private JMenuItem _printMenuItem;
	private JMenuItem _pngMenuItem;
	private JMenuItem _refreshItem;

	/**
	 * Create a 3D view
	 *
	 * @param title
	 * @param angleX
	 * @param angleY
	 * @param angleZ
	 * @param xDist
	 * @param yDist
	 * @param zDist
	 */
	public PlainView3D(String title, float angleX, float angleY, float angleZ, float xDist, float yDist, float zDist) {
		super(PropertySupport.TITLE, title, PropertySupport.ICONIFIABLE, true, PropertySupport.MAXIMIZABLE, true,
				PropertySupport.CLOSABLE, true, PropertySupport.RESIZABLE, true, PropertySupport.VISIBLE, true);

		_menuBar = new JMenuBar();
		setJMenuBar(_menuBar);
		addMenus();

		setLayout(new BorderLayout(1, 1));
		_panel3D = make3DPanel(angleX, angleY, angleZ, xDist, yDist, zDist);

		add(_panel3D, BorderLayout.CENTER);
		add(Box.createHorizontalStrut(1), BorderLayout.WEST);
		pack();

	}

	// make the 3d panel
	protected abstract PlainPanel3D make3DPanel(float angleX, float angleY, float angleZ, float xDist, float yDist,
			float zDist);


	// add the menus
	protected void addMenus() {
		JMenu actionMenu = new JMenu("ced3D");
		_printMenuItem = new JMenuItem("Print...");
		_printMenuItem.addActionListener(this);
		actionMenu.add(_printMenuItem);

		_pngMenuItem = new JMenuItem("Save as PNG...");
		_pngMenuItem.addActionListener(this);
		actionMenu.add(_pngMenuItem);

		_refreshItem = new JMenuItem("Refresh");
		_refreshItem.addActionListener(this);
		actionMenu.add(_refreshItem);

		_menuBar.add(actionMenu);
	}


	@Override
	public void actionPerformed(ActionEvent e) {

		Object source = e.getSource();
        if (source == _pngMenuItem) {
			GraphicsUtilities.saveAsPng(_panel3D);
		} else if (source == _refreshItem) {
			_panel3D.refresh();
		}
	}

	@Override
	public void focusGained(FocusEvent e) {
		if (_panel3D != null) {
			_panel3D.requestFocus();
		}
	}

	@Override
	public void refresh() {
		if (_panel3D != null) {
			_panel3D.refresh();
		}
	}

}
