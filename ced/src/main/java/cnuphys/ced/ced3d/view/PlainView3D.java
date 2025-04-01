package cnuphys.ced.ced3d.view;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;

import javax.swing.Box;
import javax.swing.JMenuBar;

import cnuphys.bCNU.util.PropertySupport;
import cnuphys.bCNU.view.BaseView;
import cnuphys.ced.ced3d.PlainPanel3D;

public abstract class PlainView3D extends BaseView implements ActionListener {

	// the menu bar
	private final JMenuBar _menuBar;


	// the 3D panel
	protected final PlainPanel3D _panel3D;

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
	}


	@Override
	public void actionPerformed(ActionEvent e) {
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
