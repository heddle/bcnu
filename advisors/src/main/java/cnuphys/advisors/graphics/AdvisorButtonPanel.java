package cnuphys.advisors.graphics;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import cnuphys.advisors.Person;
import cnuphys.advisors.Student;
import cnuphys.advisors.model.DataManager;
import cnuphys.bCNU.util.Fonts;

public class AdvisorButtonPanel extends JPanel implements ActionListener {


	private JButton _preBusAdvisorsButton;
	private JButton _allAdvisorsButton;
	private JButton _honAdvisorsButton;
	private JButton _pspAdvisorsButton;
	private JButton _alcAdvisorsButton;
	private JButton _musTheaAdvisorsButton;
	
	
	private JButton _preBusStudentButton;
	private JButton _allStudentButton;
	private JButton _assignedStudentButton;
	private JButton _unassignedStudentButton;

	private JButton _musicTheaterButton;
	private JButton _assignmentsButton;
	private JButton _alcsButton;
	private JButton _classesButton;
	private JButton _commCaptButton;
	private JButton _honorsButton;

	private JButton _premedScholarButton;
	private JButton _presScholarButton;
	private JButton _presScholarAdvisorsButton;
	private JButton _ccptAdvisorsButton;

	private JButton _departMajorButton;
	

	private String rowLabels[] = {" STUDENTS", " ADVISORS", " "};
	private JPanel _rows[] = new JPanel[rowLabels.length];

	private JButton _lastButton;

	private static final Font _font = Fonts.tinyFont;


	public AdvisorButtonPanel() {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		for (int i = 0; i < _rows.length; i++) {
			_rows[i] = createRowPanel(rowLabels[i]);
		}

		_allStudentButton = createButton("All", true, _rows[0]);
		_assignedStudentButton = createButton("Assigned", true, _rows[0]);
		_unassignedStudentButton = createButton("Unassigned", true, _rows[0]);
		_honorsButton = createButton("Honors", true, _rows[0]);
		_musicTheaterButton = createButton("Musc/Thea", true, _rows[0]);
		_commCaptButton = createButton("CCptns", true, _rows[0]);
		_premedScholarButton = createButton("PreMed", true, _rows[0]);
		_presScholarButton = createButton("Pres Sch", true, _rows[0]);
        _preBusStudentButton = createButton("PreBus", true, _rows[0]);

	    _allAdvisorsButton = createButton("All", true, _rows[1]);
		_alcAdvisorsButton = createButton("ALC", true, _rows[1]);
		_honAdvisorsButton = createButton("Honors", true, _rows[1]);
		_pspAdvisorsButton = createButton("PreMed", true, _rows[1]);
		_musTheaAdvisorsButton = createButton("Musc/Thea", true, _rows[1]);
		_ccptAdvisorsButton = createButton("CCptns", true, _rows[1]);
		_presScholarAdvisorsButton = createButton("Pres Sch", true, _rows[1]);
		_preBusAdvisorsButton = createButton("PreBus", true, _rows[1]);

		_alcsButton = createButton("ALCs", true, _rows[2]);
		_classesButton = createButton("Classes", true, _rows[2]);
		_departMajorButton = createButton("Dept/Maj", true, _rows[2]);

	}

	/**
	 * Add done. Set certain buttons enabled.
	 */
	public void done() {
	}


	private JPanel createRowPanel(String text) {
		final JPanel rp = new JPanel();
		rp.setFont(_font);

		rp.setLayout(new FlowLayout(FlowLayout.LEFT, -2, 0));
		rp.setBorder(BorderFactory.createEtchedBorder());

		JLabel label = new JLabel(text) {
			@Override
			public Dimension getPreferredSize() {
				Dimension d = super.getPreferredSize();
				d.width = rp.getFontMetrics(_font).stringWidth(" ADVISORS   ");
				return d;
			}
		};
		label.setFont(_font);
		label.setForeground(Color.white);

		rp.setBackground(Color.gray);

		rp.add(label);
		add(rp);
		return rp;
	}

	//convenience method to create a button
	private JButton createButton(String text, boolean enabled, JPanel panel) {
		JButton button = new JButton(text);
		button.addActionListener(this);
		button.setEnabled(enabled);
		button.addActionListener (this);
		button.setFont(_font);
		panel.add(button);
		return button;
	}

	//handle click on honors advisors button
	private void handleHonorsAdvisors() {
		AdvisorDisplay.getInstance().setContent(DataManager.getFilteredAdvisorData(Person.HONOR).getScrollPane());
		AdvisorInfoLabel.getInstance().setText("Active Honors Advisors");
	}
	
	//handle click on psp advisors button
	private void handlePSPAdvisors() {
		AdvisorDisplay.getInstance().setContent(DataManager.getFilteredAdvisorData(Person.PREMEDSCHOLAR).getScrollPane());
		AdvisorInfoLabel.getInstance().setText("Active Premed Scholar Advisors");
	}


	//handle click on honors advisors button
	private void handleALCAdvisors() {
		AdvisorDisplay.getInstance().setContent(DataManager.getFilteredAdvisorData(Person.ALC).getScrollPane());
		AdvisorInfoLabel.getInstance().setText("ALC Advisors");
	}

	//handle click on pre bus advisors button
	private void handlePreBusAdvisors() {
		AdvisorDisplay.getInstance().setContent(DataManager.getFilteredAdvisorData(Person.PREBUS).getScrollPane());
		AdvisorInfoLabel.getInstance().setText("Active Pre-Business Advisors");
	}


	//handle click on musics theater advisors button
	private void handleMusTheaAdvisors() {
		AdvisorDisplay.getInstance().setContent(DataManager.getFilteredAdvisorData(Person.MUSICTHEATER).getScrollPane());
		AdvisorInfoLabel.getInstance().setText("Active Music and Theater Advisors");
	}

	//handle click on CCPT advisors button
	private void handleCCPTAdvisors() {
		AdvisorDisplay.getInstance().setContent(DataManager.getFilteredAdvisorData(Person.CCPT).getScrollPane());
		AdvisorInfoLabel.getInstance().setText("Community Captain Advisors");
	}
	
	//handle click on pres scholar advisors button
	private void handlePresScholarAdvisors() {
		AdvisorDisplay.getInstance().setContent(DataManager.getFilteredAdvisorData(Person.PRESSCHOLAR).getScrollPane());
		AdvisorInfoLabel.getInstance().setText("Active Honors Advisors");
	}

	//handle click on assigned students button
	private void handleAssignedStudents() {
		List<Student> students = DataManager.getAssignedStudents();
		AdvisorDisplay.getInstance().setContent(DataManager.getStudentData(students).getScrollPane());
		AdvisorInfoLabel.getInstance().setText("Assigned Students");
	}

	//handle click on unassigned students button
	private void handleUnassignedStudents() {
		List<Student> students = DataManager.getUnassignedStudents();
		AdvisorDisplay.getInstance().setContent(DataManager.getStudentData(students).getScrollPane());
		AdvisorInfoLabel.getInstance().setText("Unassigned Students");
	}

	//handle click on community captains button
	private void handleCommunityCaptains() {
		AdvisorDisplay.getInstance().setContent(DataManager.getFilteredStudentData(Person.CCPT).getScrollPane());
		AdvisorInfoLabel.getInstance().setText("Community Captains");
	}

	//handle click on honors students button
	private void handleHonorsStudents() {
		AdvisorDisplay.getInstance().setContent(DataManager.getFilteredStudentData(Person.HONOR).getScrollPane());
		AdvisorInfoLabel.getInstance().setText("Honors Students");
	}

	//handle click on prebus button
	private void handlePreBus() {
		AdvisorDisplay.getInstance().setContent(DataManager.getFilteredStudentData(Person.PREBUS).getScrollPane());
		AdvisorInfoLabel.getInstance().setText("Pre-Business Students");
	}

	//handle click on premed scholars button
	private void handlePremedScholars() {
		AdvisorDisplay.getInstance().setContent(DataManager.getFilteredStudentData(Person.PREMEDSCHOLAR).getScrollPane());
		AdvisorInfoLabel.getInstance().setText("Pre-Med Scholars");
	}

	//handle click on presidential scholars button
	private void handlePresScholars() {
		AdvisorDisplay.getInstance().setContent(DataManager.getFilteredStudentData(Person.PRESSCHOLAR).getScrollPane());
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
		AdvisorDisplay.getInstance().setContent(DataManager.getFilteredStudentData(Person.MUSICTHEATER).getScrollPane());
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

	//handle click department and major button
	private void handleDepartmentMajor() {
		AdvisorDisplay.getInstance().setContent(new DepartmentMajorPane());
		AdvisorInfoLabel.getInstance().setText("Departments and Majors");
	}
	
	private void handleALCs() {
		AdvisorDisplay.getInstance().setContent(DataManager.getALCData().getScrollPane());
		AdvisorInfoLabel.getInstance().setText("All ALCs");
	}


	//handle click on LCs button
//	private void handleLearningCommunities() {
//		AdvisorDisplay.getInstance().setContent(DataManager.getLearningCommunityData().getScrollPane());
//		AdvisorInfoLabel.getInstance().setText("All Learning Communities");
//	}

	//used to refresh tables
	public void redoLastButton() {
		if (_lastButton != null) {
			handleButton(_lastButton);
		}
	}

	//one of the buttons was selected
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
		else if (button == _pspAdvisorsButton) {
			handlePSPAdvisors();
		}
		else if (button == _alcAdvisorsButton) {
			handleALCAdvisors();
		}
		else if (button == _musTheaAdvisorsButton) {
			handleMusTheaAdvisors();
		}
		else if (button == _ccptAdvisorsButton) {
			handleCCPTAdvisors();
		}
		else if (button == _presScholarAdvisorsButton) {
			handlePresScholarAdvisors();
		}
		else if (button == _preBusAdvisorsButton) {
			handlePreBusAdvisors();
		}

		else if (button == _commCaptButton) {
			handleCommunityCaptains();
		}
		else if (button == _premedScholarButton) {
			handlePremedScholars();
		}
		else if (button == _presScholarButton) {
			handlePresScholars();
		}
		else if (button == _preBusStudentButton) {
			handlePreBus();
		}

		else if (button == _musicTheaterButton) {
			handleMusicTheaterStudents();
		}
		else if (button == _honorsButton) {
			handleHonorsStudents();
		}
		else if (button == _assignmentsButton) {
			handleAssignments();
		}
		else if (button == _classesButton) {
			handleClasses();
		}
		else if (button == _departMajorButton) {
			handleDepartmentMajor();
		}
		else if (button == _alcsButton) {
			handleALCs();
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
