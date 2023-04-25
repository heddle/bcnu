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

import cnuphys.advisors.checklist.CheckList;
import cnuphys.advisors.enums.Semester;
import cnuphys.advisors.graphics.AdvisorPanel;
import cnuphys.advisors.log.LogManager;
import cnuphys.advisors.menu.MenuManager;
import cnuphys.advisors.simulation.AdvisorSimulation;

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
	private static JLabel _semesterLabel;

	//the simulation object
	private AdvisorSimulation _advisorSim;
	
	//the check list
	private CheckList _checklist;

	//the main menu bar
	private JMenuBar _menuBar;


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


		_advisorSim = new AdvisorSimulation();
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
		_checklist = new CheckList(_advisorSim);

		AdvisorPanel panel = new AdvisorPanel(_advisorSim, _checklist);
		

		add(panel, BorderLayout.CENTER);
		createSemesterLabel();

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
	private static AdvisorAssign getInstance() {
		if (_instance == null) {
			_instance = new AdvisorAssign();
		}
		return _instance;
	}

	// create the event number label
	private void createSemesterLabel() {
		_semesterLabel = new JLabel("  SEMESTER: XXXXXXXXX");
		_semesterLabel.setOpaque(true);
		_semesterLabel.setBackground(Color.black);
		_semesterLabel.setForeground(Color.yellow);
		_semesterLabel.setFont(new Font("Dialog", Font.BOLD, 12));
		_semesterLabel.setBorder(BorderFactory.createLineBorder(Color.cyan, 1));
		fixSemesterLabel();

		getJMenuBar().add(Box.createHorizontalGlue());
		getJMenuBar().add(_semesterLabel);
		getJMenuBar().add(Box.createHorizontalStrut(5));
	}

	/**
	 * Fix the semester label
	 */
	public void fixSemesterLabel() {
		_semesterLabel.setText("  SEMESTER: " + getSemester());
	}



	/**
	 * The main program
	 *
	 * @param arg command line arguments
	 */
	public static void main(String arg[]) {

		//create the log manager
		LogManager.getInstance();

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
