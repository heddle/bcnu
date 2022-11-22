package cnuphys.simanneal.advisors.menu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

import cnuphys.simanneal.advisors.Advisor;
import cnuphys.simanneal.advisors.enums.Department;
import cnuphys.simanneal.advisors.graphics.AdvisorDisplay;
import cnuphys.simanneal.advisors.graphics.BarPlot;
import cnuphys.simanneal.advisors.io.ITabled;
import cnuphys.simanneal.advisors.model.AdvisorData;
import cnuphys.simanneal.advisors.model.DataManager;

public class PlotMenu extends JMenu implements ActionListener {

	//the menu items
	private JMenuItem _fcaByDeptItem;

	
	public PlotMenu() {
		super("Plots");
		MenuManager.getInstance().addMenu(this);
		_fcaByDeptItem = MenuManager.addMenuItem("FCAs by Department", KeyEvent.VK_1, this, this);
	}

	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == _fcaByDeptItem) {
			handleFCAByDept();
		}
	}
	
	//handle FCA by department selection
	private void handleFCAByDept() {
		System.out.println("FCA by department");
		
		String title = "FCAs by Department";
		
		Department.clearCounts();
		
		AdvisorData advisorData = DataManager.getAdvisorData();

		ArrayList<ITabled> data = advisorData.getData();
		
		for (ITabled itabled : data) {
			Advisor advisor = (Advisor)itabled;
			Department.incrementCount(advisor.department);
		}
		
		String categories[] = Department.getBaseNames();
		int counts[] = Department.getCounts();
		
		BarPlot barPlot = new BarPlot(title, categories, counts);
		AdvisorDisplay.getInstance().setContent(barPlot);
	}


}
