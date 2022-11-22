package cnuphys.simanneal.advisors.menu;

import java.awt.Color;
import java.awt.Toolkit;
import java.awt.event.ActionListener;
import java.awt.event.ItemListener;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

import cnuphys.simanneal.advisors.AdvisorAssign;

public class MenuManager {

	//the menu bar
	private JMenuBar _menuBar;

	//singleton
	private static MenuManager _instance;

	//private constructor for singleton
	private MenuManager() {
	}

	public void init() {
		_menuBar = new JMenuBar();
		AdvisorAssign.getFrame().setJMenuBar(_menuBar);
		new FileMenu();
		new PlotMenu();

	}

	/**
	 * Access to the MenuManager
	 * @return the MenuManager
	 */
	public static MenuManager getInstance() {
		if (_instance == null) {
			_instance = new MenuManager();
		}
		return _instance;
	}

	/**
	 * Add a menu to the main menu bar.
	 *
	 * @param menu the menu to add.
	 */
	public void addMenu(JMenu menu) {
		_menuBar.add(menu);
		menu.setForeground(Color.black);
	}

	/**
	 * Convenience routine for adding a checkbox menu item.
	 *
	 * @param label the menu label.
	 * @param menu  optional menu to add the item to.
	 * @param ilist optional ItemListener.
	 */

	public static JCheckBoxMenuItem addCheckboxMenuItem(String label, JMenu menu, ItemListener ilist) {
		return addCheckboxMenuItem(label, menu, ilist, false);
	}

	/**
	 * Convenience routine for adding a checkbox menu item.
	 *
	 * @param label the menu label.
	 * @param menu  optional menu to add the item to.
	 * @param ilist optional ItemListener.
	 * @param state initial state.
	 */

	public static JCheckBoxMenuItem addCheckboxMenuItem(String label, JMenu menu, ItemListener ilist, boolean state) {

		JCheckBoxMenuItem mitem = null;

		mitem = new JCheckBoxMenuItem((label != null) ? label : "???", state);

		if (menu != null) {
			menu.add(mitem);
		}

		if (ilist != null) {
			mitem.addItemListener(ilist);
		}

		return mitem;
	}

	/**
	 * Convenience routine for adding a menu item.
	 *
	 * @param label    the menu label.
	 * @param vk       the KeyEvent virtual key constant for short cut.
	 * @param menu     the menu to add the item to.
	 * @param alist    optional action listener.
	 */

	public static JMenuItem addMenuItem(String label, int vk, JMenu menu, ActionListener alist) {
		JMenuItem mitem = new JMenuItem(label);
		menu.add(mitem);
		if (vk > 0) {
			mitem.setAccelerator(
					KeyStroke.getKeyStroke(vk, Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()));

		}

		if (alist != null) {
			mitem.addActionListener(alist);
		}

		return mitem;
	}



	/**
	 * Convenience routine for adding a menu item.
	 *
	 * @param label the menu label;
	 * @param menu  the menu to add the item to.
	 * @param alist optional action listener.
	 */

	public static JMenuItem addMenuItem(String label, JMenu menu, ActionListener alist) {

		return addMenuItem(label, -1, menu, alist);
	}


}
