package cnuphys.advisors.frame;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.SwingUtilities;

import cnuphys.advisors.Advisor;
import cnuphys.advisors.checklist.CheckList;
import cnuphys.advisors.enums.Semester;
import cnuphys.advisors.graphics.AdvisorPanel;
import cnuphys.advisors.menu.MenuManager;
import cnuphys.advisors.model.DataManager;
import cnuphys.advisors.simulation.AdvisorSimulation;
import cnuphys.simanneal.SimulationPlot;

/**
 * The main object for assigning core advisors
 * @author heddle
 *
 */
public class AdvisorAssign extends JFrame {

	/**
	 * The screen size
	 */
	public static Dimension screenSize;

	/**
	 * flag specifying if we are in debug mode
	 */
	public static boolean DEBUG = true;

	//the singleton
	private static AdvisorAssign _instance;

	//what semester are we dealing with
	private static Semester _semester = Semester.Fall2022;

	//a label on the menu bar for the semester
	private static JLabel _infoLabel;

	//the check list
	private CheckList _checklist;

	//the main menu bar
	private JMenuBar _menuBar;
	
	//the advisor panel
	private AdvisorPanel _advisorPanel;


	//private constructor for singleton
	private AdvisorAssign() {
		super("CNU Core Advisor Assignments");
		screenSize = Toolkit.getDefaultToolkit().getScreenSize();

		// set up what to do if the window is closed
		WindowAdapter windowAdapter = new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent event) {
				System.exit(1);
			}
		};
		addWindowListener(windowAdapter);

		setup();

		pack();
	}
	
	/**
	 * Set uo the GUI
	 */
	public void setup() {

		_menuBar = new JMenuBar();
		setJMenuBar(_menuBar);

		setLayout(new BorderLayout());

		MenuManager.getInstance().init(_menuBar);

		//add the check list
		_checklist = CheckList.getInstance();

		_advisorPanel = new AdvisorPanel(AdvisorSimulation.getInstance(), _checklist);


		add(_advisorPanel, BorderLayout.CENTER);
		createInfoLabel();

	}

	public SimulationPlot getSimulationPlot() {
		return _advisorPanel.getSimulationPlot();
	}
	
	/**
	 * Get the semester we are examining
	 * @return the semester we are examining
	 */
	public static Semester getSemester() {
		return _semester;
	}


	/**
	 * public accessor to the singleton
	 * @return the singleton AdvisorAssign
	 */
	public static AdvisorAssign getInstance() {
		if (_instance == null) {
			_instance = new AdvisorAssign();
		}
		return _instance;
	}

	// create the event number label
	private void createInfoLabel() {
		_infoLabel = new JLabel(" ");
		_infoLabel.setOpaque(true);
		_infoLabel.setBackground(Color.black);
		_infoLabel.setForeground(Color.yellow);
		_infoLabel.setFont(new Font("Dialog", Font.BOLD, 12));
		_infoLabel.setBorder(BorderFactory.createLineBorder(Color.cyan, 1));
		updateInfoLabel();

		getJMenuBar().add(Box.createHorizontalGlue());
		getJMenuBar().add(_infoLabel);
		getJMenuBar().add(Box.createHorizontalStrut(5));
	}

	/**
	 * Fix the info label
	 */
	public static void updateInfoLabel() {

		if (_infoLabel == null) {
			return;
		}

		int advisorCount = DataManager.getAdvisorData().count();
		int studentCount = DataManager.getStudentData().count();
		int assignedCount1 = DataManager.getAdvisorData().getAssignedStudentCount();
		int assignedCount2 = DataManager.getStudentData().getAssignedStudentCount();

		String s;
		if (assignedCount1 != assignedCount2) {
			s = String.format(" ERROR! Assigned student counts disagree! From adv: %d From students: %d", assignedCount1, assignedCount2);
		}

		else {
			int unassignedCount = studentCount - assignedCount1;
			double avgReq = ((double) studentCount) / advisorCount;

			s = String.format(" %s    FCA Count: %d    Student Count: %d    Assigned: %d    Unassigned: %d    Required Avg: %4.1f  ",
					getSemester().name(), advisorCount, studentCount, assignedCount1, unassignedCount, avgReq);
		}
		
		Advisor honorsDirector = DataManager.honorsDirector;
		
		s += " Honors Director: " + honorsDirector.name + "  ";

		_infoLabel.setText(s);
	}

	/**
	 * The target number of assignments per advisor
	 * @return target number of assignments per advisor
	 */
	public static int targetAverage() {
		int advisorCount = DataManager.getAdvisorData().count();
		int studentCount = DataManager.getStudentData().count();

		double avgReq = ((double)studentCount)/advisorCount;
		return (int)Math.ceil(avgReq);
	}


	/**
	 * The main program
	 *
	 * @param arg command line arguments
	 */
	public static void main(String arg[]) {

		JFrame frame = getInstance();

		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				frame.setVisible(true);
				frame.setLocationRelativeTo(null);
			}
		});
	}



}
