package cnuphys.advisors.menu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

public class FileMenu extends JMenu implements ActionListener {
	
	//the menu items
	private JMenuItem _quitItem;
	
	public FileMenu() {
		super("File");
		MenuManager.getInstance().addMenu(this);
		_quitItem = MenuManager.addMenuItem("Quit", KeyEvent.VK_Q, this, this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == _quitItem) {
			handleQuit();
		}
	}
	
	//handle quit selection
	private void handleQuit() {
		System.exit(0);
	}

}
