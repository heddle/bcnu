package cnuphys.advisors.graphics;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;

import cnuphys.advisors.AdvisorFilter;
import cnuphys.advisors.StudentFilter;
import cnuphys.advisors.model.DataManager;
import cnuphys.bCNU.util.Fonts;

public class AdvisorButtonPanel extends JPanel implements ActionListener {


	private JButton _allAdvisorsButton;
	private JButton _honAdvisorsButton;
	private JButton _musTheaAdvisorsButton;
	private JButton _allStudentButton;
	private JButton _assignedStudentButton;
	private JButton _unassignedStudentButton;

	private JButton _musicTheaterButton;
	private JButton _assignmentsButton;
	private JButton _ilcsButton;
	private JButton _classesButton;
	private JButton _commCaptButton;
	private JButton _prelawButton;
	private JButton _premedScholarButton;
	private JButton _presScholarButton;
	private JButton _presScholarAdvisorsButton;



	private JPanel _rows[] = new JPanel[3];
	
	private JButton _lastButton;


	public AdvisorButtonPanel() {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		for (int i = 0; i < _rows.length; i++) {
			_rows[i] = createRowPanel();
		}

		_allStudentButton = createButton("All Students", true, _rows[0]);
		_assignedStudentButton = createButton("Assigned Students", true, _rows[0]);
		_unassignedStudentButton = createButton("Unassigned Students", true, _rows[0]);
		
	    _allAdvisorsButton = createButton("All FCAs", true, _rows[1]);
	    _honAdvisorsButton = createButton("Honors FCAs", true, _rows[1]);
	    _musTheaAdvisorsButton = createButton("Music/Theater FCAs", true, _rows[1]);
	    _presScholarAdvisorsButton = createButton("Pres Scholar FCAs", true, _rows[1]);
		_ilcsButton = createButton("ILCs", true, _rows[1]);
		_classesButton = createButton("Classes", true, _rows[1]);
		_assignmentsButton = createButton("Assignments", false, _rows[1]);
		
		_musicTheaterButton = createButton("Music/Theater Students", true, _rows[2]);
		_commCaptButton = createButton("Community Cptns", true, _rows[2]);
		_prelawButton = createButton("Pre-Law", true, _rows[2]);
		_premedScholarButton = createButton("Pre-Med Scholars", true, _rows[2]);
		_presScholarButton = createButton("Pres Scholars", true, _rows[2]);

		
	}
	

	private JPanel createRowPanel() {
		JPanel rp = new JPanel();
		rp.setLayout(new FlowLayout(FlowLayout.CENTER, 4, 4));

		rp.setBackground(Color.gray);
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

	//handle click on honors advisors button
	private void handleHonorsAdvisors() {
		AdvisorDisplay.getInstance().setContent(DataManager.getFilteredAdvisorData(AdvisorFilter.honorsAdvisors).getScrollPane());
		AdvisorInfoLabel.getInstance().setText("Active Honors Advisors");
	}
	
	//handle click on musics theater advisors button
	private void handleMusTheaAdvisors() {
		AdvisorDisplay.getInstance().setContent(DataManager.getFilteredAdvisorData(AdvisorFilter.musTheaAdvisors).getScrollPane());
		AdvisorInfoLabel.getInstance().setText("Active Music and Theater Advisors");
	}


	//handle click on pres scholar advisors button
	private void handlePresScholarAdvisors() {
		AdvisorDisplay.getInstance().setContent(DataManager.getFilteredAdvisorData(AdvisorFilter.presScholarAdvisors).getScrollPane());
		AdvisorInfoLabel.getInstance().setText("Active Honors Advisors");
	}

	//handle click on assigned students button
	private void handleAssignedStudents() {
		AdvisorDisplay.getInstance().setContent(DataManager.getFilteredStudentData(StudentFilter.assignedStudents).getScrollPane());
		AdvisorInfoLabel.getInstance().setText("Assigned Students");
	}

	//handle click on unassigned students button
	private void handleUnassignedStudents() {
		AdvisorDisplay.getInstance().setContent(DataManager.getFilteredStudentData(StudentFilter.unassignedStudents).getScrollPane());
		AdvisorInfoLabel.getInstance().setText("Unassigned Students");
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

	//handle click on premed scholars button
	private void handlePremedScholars() {
		AdvisorDisplay.getInstance().setContent(DataManager.getFilteredStudentData(StudentFilter.preMedScholars).getScrollPane());
		AdvisorInfoLabel.getInstance().setText("Pre-Med Scholars");
	}

	//handle click on presidential scholars button
	private void handlePresScholars() {
		AdvisorDisplay.getInstance().setContent(DataManager.getFilteredStudentData(StudentFilter.presScholars).getScrollPane());
		AdvisorInfoLabel.getInstance().setText("Presidential Scholars");
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
	private void handleMusicTheaterStudents() {
		AdvisorDisplay.getInstance().setContent(DataManager.getFilteredStudentData(StudentFilter.musicTheaterStudents).getScrollPane());
		AdvisorInfoLabel.getInstance().setText("Music and Theater Majors");

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
	
	public void redoLastButton() {
		if (_lastButton != null) {
			handleButton(_lastButton);
		}
	}

	private void handleButton(JButton button) {
		if (button == _allAdvisorsButton) {
			handleAllAdvisors();
		}
		else if (button == _allStudentButton) {
			handleAllStudents();
		}
		else if (button == _honAdvisorsButton) {
			handleHonorsAdvisors();
		}
		else if (button == _musTheaAdvisorsButton) {
			handleMusTheaAdvisors();
		}
		else if (button == _presScholarAdvisorsButton) {
			handlePresScholarAdvisors();
		}
		else if (button == _commCaptButton) {
			handleCommunityCaptains();
		}
		else if (button == _prelawButton) {
			handlePrelaw();
		}
		else if (button == _premedScholarButton) {
			handlePremedScholars();
		}
		else if (button == _presScholarButton) {
			handlePresScholars();
		}
		else if (button == _musicTheaterButton) {
			handleMusicTheaterStudents();
		}
		else if (button == _assignmentsButton) {
			handleAssignments();
		}
		else if (button == _classesButton) {
			handleClasses();
		}
		else if (button == _ilcsButton) {
			handleILCs();
		}
		else if (button == _ilcsButton) {
			handleILCs();
		}
		else if (button == _assignedStudentButton) {
			handleAssignedStudents();
		}
		else if (button == _unassignedStudentButton) {
			handleUnassignedStudents();
		}	
		
		_lastButton = button;
	}


	@Override
	public void actionPerformed(ActionEvent e) {
		Object o = e.getSource();
		
		if (o instanceof JButton) {
			handleButton((JButton)o);
		}
	}
}
