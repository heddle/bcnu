package cnuphys.advisors.graphics;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

import cnuphys.advisors.model.DataManager;

public class AdvisorButtonPanel extends JPanel implements ActionListener {
	

	private JButton _advisorButton;
	private JButton _studentButton;
	private JButton _saveButton;
	private JButton _assignmentsButton;
	private JButton _classesButton;

	
	public AdvisorButtonPanel() {
		setLayout(new FlowLayout(FlowLayout.CENTER, 10, 4));
		_advisorButton = createButton("Advisors", true);
		_studentButton = createButton("Students", true);
		_assignmentsButton = createButton("Assignments", false);
		_saveButton = createButton("Save", false);
		_classesButton = createButton("Classes", true);

	}
	
	private JButton createButton(String text, boolean enabled) {
		JButton button = new JButton(text);
		button.addActionListener(this);
		button.setEnabled(enabled);
		button.addActionListener (this);
		add(button);
		return button;
	}
	
	//handle click on advisor button
	private void handleAdvisors() {
		AdvisorDisplay.getInstance().setContent(DataManager.getAdvisorData().getScrollPane());		
	}
	
	//handle click on students button
	private void handleStudents() {
		AdvisorDisplay.getInstance().setContent(DataManager.getStudentData().getScrollPane());		
	}
	
	//handle click on save button
	private void handleSave() {
		
	}
	
	//handle click on assignments button
	private void handleAssignments() {
	}
	
	//handle click on classes button
	private void handleClasses() {
		AdvisorDisplay.getInstance().setContent(DataManager.getSchedule().getScrollPane());		
	}
	
	

	@Override
	public void actionPerformed(ActionEvent e) {
		Object o = e.getSource();
		
		if (o == _advisorButton) {
			handleAdvisors();
		}
		else if (o == _studentButton) {
			handleStudents();
		}
		else if (o == _saveButton) {
			handleSave();
		}
		else if (o == _assignmentsButton) {
			handleAssignments();
		}
		else if (o == _classesButton) {
			handleClasses();
		}



	}
}
