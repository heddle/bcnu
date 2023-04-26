package cnuphys.advisors.graphics;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;

import cnuphys.advisors.StudentFilter;
import cnuphys.advisors.model.DataManager;
import cnuphys.bCNU.util.Fonts;

public class AdvisorButtonPanel extends JPanel implements ActionListener {


	private JButton _allAdvisorsButton;
	private JButton _allStudentButton;
	private JButton _assignedStudentButton;
	private JButton _unassignedStudentButton;

	private JButton _saveButton;
	private JButton _assignmentsButton;
	private JButton _ilcsButton;
	private JButton _classesButton;
	private JButton _commCaptButton;
	private JButton _prelawButton;
	private JButton _presScholarButton;


	private JButton _honAdvCaptButton;

	JPanel rows[] = new JPanel[2];


	public AdvisorButtonPanel() {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		rows[0] = createRowPanel();
		rows[1] = createRowPanel();

		_allStudentButton = createButton("All Students", true, rows[0]);
		_assignedStudentButton = createButton("Assigned Students", true, rows[0]);
		_unassignedStudentButton = createButton("Unassigned Students", true, rows[0]);
		_commCaptButton = createButton("Community Cptns", true, rows[0]);
		_prelawButton = createButton("Pre-Law", true, rows[0]);
		_presScholarButton = createButton("Pres Scholars", true, rows[0]);

	    _allAdvisorsButton = createButton("All FCAs", true, rows[1]);
	    _honAdvCaptButton = createButton("Honors FCAs", true, rows[1]);
		_ilcsButton = createButton("ILCs", true, rows[1]);
		_classesButton = createButton("Classes", true, rows[1]);
		_assignmentsButton = createButton("Assignments", false, rows[1]);
		_saveButton = createButton("Save", false, rows[1]);

	}

	private JPanel createRowPanel() {
		JPanel rp = new JPanel();
		rp.setLayout(new FlowLayout(FlowLayout.CENTER, 4, 4));

		add(rp);
		return rp;
	}

	private JButton createButton(String text, boolean enabled, JPanel panel) {
		JButton button = new JButton(text);
		button.addActionListener(this);
		button.setEnabled(enabled);
		button.addActionListener (this);
		button.setFont(Fonts.smallFont);
		panel.add(button);
		return button;
	}

	//handle click on assigned students button
	private void handleAssignedStudents() {
	}

	//handle click on honors advisors button
	private void handleHonorsAdvisors() {
	}

	//handle click on unassigned students button
	private void handleUnassignedStudents() {
	}

	//handle click on community captains button
	private void handleCommunityCaptains() {
		AdvisorDisplay.getInstance().setContent(DataManager.getFilteredStudentData(StudentFilter.communityCaptains).getScrollPane());
		AdvisorInfoLabel.getInstance().setText("Community Captains");
	}

	//handle click on prelaw button
	private void handlePrelaw() {
		AdvisorDisplay.getInstance().setContent(DataManager.getFilteredStudentData(StudentFilter.prelawStudents).getScrollPane());
		AdvisorInfoLabel.getInstance().setText("Prelaw Students");
	}

	//handle click on presidential scholars button
	private void handlePresScholars() {
		AdvisorInfoLabel.getInstance().setText("Presidenial Scholars");
	}


	//handle click on advisor button
	private void handleAllAdvisors() {
		AdvisorDisplay.getInstance().setContent(DataManager.getAdvisorData().getScrollPane());
		AdvisorInfoLabel.getInstance().setText("All FCAs accepting a FY cohort");
	}

	//handle click on students button
	private void handleAllStudents() {
		AdvisorDisplay.getInstance().setContent(DataManager.getStudentData().getScrollPane());
		AdvisorInfoLabel.getInstance().setText("All Students");
	}

	//handle click on save button
	private void handleSave() {

	}

	//handle click on assignments button
	private void handleAssignments() {
		AdvisorInfoLabel.getInstance().setText("All Assignments");
	}

	//handle click on classes button
	private void handleClasses() {
		AdvisorDisplay.getInstance().setContent(DataManager.getSchedule().getScrollPane());
		AdvisorInfoLabel.getInstance().setText("All Classes taught by FCAs");
	}

	//handle click on ILCs button
	private void handleILCs() {
		AdvisorDisplay.getInstance().setContent(DataManager.getILCData().getScrollPane());
		AdvisorInfoLabel.getInstance().setText("All ILCs");
	}



	@Override
	public void actionPerformed(ActionEvent e) {
		Object o = e.getSource();

		if (o == _allAdvisorsButton) {
			handleAllAdvisors();
		}
		else if (o == _allStudentButton) {
			handleAllStudents();
		}
		else if (o == _honAdvCaptButton) {
			handleHonorsAdvisors();
		}
		else if (o == _commCaptButton) {
			handleCommunityCaptains();
		}
		else if (o == _prelawButton) {
			handlePrelaw();
		}
		else if (o == _presScholarButton) {
			handlePresScholars();
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
		else if (o == _ilcsButton) {
			handleILCs();
		}
		else if (o == _ilcsButton) {
			handleILCs();
		}
		else if (o == _assignedStudentButton) {
			handleAssignedStudents();
		}
		else if (o == _unassignedStudentButton) {
			handleUnassignedStudents();
		}



	}
}
