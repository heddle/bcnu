package cnuphys.advisors.menu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

import cnuphys.advisors.plots.PlotManager;

public class PlotMenu extends JMenu implements ActionListener {

	//the menu items
	private JMenuItem _fcaByDeptItem;
	private JMenuItem _studentByMajorItem;
	private JMenuItem _studentByDepartmentItem;
	private JMenuItem _fcaPerMajorsItem;
	private JMenuItem _uniqueMajorsItem;



	public PlotMenu() {
		super("Plots");
		MenuManager.getInstance().addMenu(this);
		_fcaByDeptItem = MenuManager.addMenuItem("FCAs by Department", KeyEvent.VK_1, this, this);
		_studentByMajorItem = MenuManager.addMenuItem("Students by Major", KeyEvent.VK_2, this, this);
		_studentByDepartmentItem = MenuManager.addMenuItem("Students by Department", KeyEvent.VK_3, this, this);
		_fcaPerMajorsItem = MenuManager.addMenuItem("FCAs per Majors", KeyEvent.VK_4, this, this);
		_uniqueMajorsItem = MenuManager.addMenuItem("Unique Majors Histogram", KeyEvent.VK_5, this, this);
		_uniqueMajorsItem.setEnabled(false);
	}


	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == _fcaByDeptItem) {
			PlotManager.handleFCAByDept();
		}
		else if (e.getSource() == _studentByMajorItem) {
			PlotManager.handleStudentsByMajor();
		}
		else if (e.getSource() == _studentByDepartmentItem) {
			PlotManager.handleStudentsByDepartment();
		}
		else if (e.getSource() == _fcaPerMajorsItem) {
			PlotManager.handleFCAsPerMajors();
		}
		else if (e.getSource() == _uniqueMajorsItem) {
			PlotManager.numberUniqueMajors();
		}
	}

	public void done() {
		_uniqueMajorsItem.setEnabled(true);
	}


}
