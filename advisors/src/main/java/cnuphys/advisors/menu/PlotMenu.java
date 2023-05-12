package cnuphys.advisors.menu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.List;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

import cnuphys.advisors.Advisor;
import cnuphys.advisors.Student;
import cnuphys.advisors.enums.Department;
import cnuphys.advisors.enums.Major;
import cnuphys.advisors.graphics.AdvisorDisplay;
import cnuphys.advisors.graphics.BarPlot;
import cnuphys.advisors.io.ITabled;
import cnuphys.advisors.model.AdvisorData;
import cnuphys.advisors.model.DataManager;
import cnuphys.advisors.model.StudentData;

public class PlotMenu extends JMenu implements ActionListener {

	//the menu items
	private JMenuItem _fcaByDeptItem;
	private JMenuItem _studentByMajorItem;
	private JMenuItem _studentByDepartmentItem;
	private JMenuItem _fcaPerMajorsItem;


	public PlotMenu() {
		super("Plots");
		MenuManager.getInstance().addMenu(this);
		_fcaByDeptItem = MenuManager.addMenuItem("FCAs by Department", KeyEvent.VK_1, this, this);
		_studentByMajorItem = MenuManager.addMenuItem("Students by Major", KeyEvent.VK_2, this, this);
		_studentByDepartmentItem = MenuManager.addMenuItem("Students by Department", KeyEvent.VK_3, this, this);
		_fcaPerMajorsItem = MenuManager.addMenuItem("FCAs per Majors", KeyEvent.VK_4, this, this);
	}


	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == _fcaByDeptItem) {
			handleFCAByDept();
		}
		else if (e.getSource() == _studentByMajorItem) {
			handleStudentsByMajor();
		}
		else if (e.getSource() == _studentByMajorItem) {
			handleStudentsByMajor();
		}
		else if (e.getSource() == _studentByDepartmentItem) {
			handleStudentsByDepartment();
		}
		else if (e.getSource() == _fcaPerMajorsItem) {
			handleFCAsPerMajors();
		}

	}
	
	//number of advers per number of majors
	private void handleFCAsPerMajors() {
		String title = "FCAs per Major in Department";
		Department.clearFcaCounts();
		Department.clearMajorCounts();
		
		AdvisorData advisorData = DataManager.getAdvisorData();
		
		for (Advisor fca : advisorData.getAdvisors()) {
			Department.incrementFcaCount(fca.department);
		}

		StudentData studentData = DataManager.getStudentData();
		
		for (Student student : studentData.getStudents()) {
			Department department = student.major.getDepartment();
			Department.incrementMajorCount(department);
		}
		
		String categories[] = Department.getBaseNames();
		int majorCounts[] = Department.getMajorCounts();
		int fcaCounts[] = Department.getFcaCounts();
		
		double ratio[] = new double[majorCounts.length];
		
		for (int i = 0; i < ratio.length; i++) {
			
			if ((majorCounts[i] == 0) || (fcaCounts[i] == 0)) {
				ratio[i] = 0;
				continue;
			}
			
			ratio[i] = ((double)fcaCounts[i]/(double)majorCounts[i]);
		}


		BarPlot barPlot = new BarPlot(title, categories, ratio);
		AdvisorDisplay.getInstance().setContent(barPlot);
		
	}

	//handle FCA by department selection
	private void handleFCAByDept() {
		String title = "FCAs by Department";

		Department.clearFcaCounts();

		AdvisorData advisorData = DataManager.getAdvisorData();
		
		for (Advisor fca : advisorData.getAdvisors()) {
			Department.incrementFcaCount(fca.department);
		}

		String categories[] = Department.getBaseNames();
		int counts[] = Department.getFcaCounts();

		BarPlot barPlot = new BarPlot(title, categories, counts);
		AdvisorDisplay.getInstance().setContent(barPlot);
	}
	
	//handle students by department selection
	private void handleStudentsByDepartment() {
		String title = "Students by Department";
		Department.clearMajorCounts();
		
		StudentData studentData = DataManager.getStudentData();
		
		for (Student student : studentData.getStudents()) {
			Department department = student.major.getDepartment();
			Department.incrementMajorCount(department);
		}
		String categories[] = Department.getBaseNames();
		int counts[] = Department.getMajorCounts();

		BarPlot barPlot = new BarPlot(title, categories, counts);
		AdvisorDisplay.getInstance().setContent(barPlot);
	}

	//handle students by major selection
	private void handleStudentsByMajor() {
		String title = "Students by Major";

		Major.clearCounts();

		StudentData studentData = DataManager.getStudentData();

		List<ITabled> data = studentData.getData();

		for (ITabled itabled : data) {
			Student student = (Student)itabled;
			Major.incrementCount(student.major);
		}

		String categories[] = Major.getBaseNames();
		int counts[] = Major.getCounts();

		BarPlot barPlot = new BarPlot(title, categories, counts);
		AdvisorDisplay.getInstance().setContent(barPlot);
	}



}
