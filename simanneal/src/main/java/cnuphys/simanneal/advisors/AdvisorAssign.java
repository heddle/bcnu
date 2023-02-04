package cnuphys.simanneal.advisors;

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
import javax.swing.SwingUtilities;

import cnuphys.simanneal.Simulation;
import cnuphys.simanneal.SimulationAttributes;
import cnuphys.simanneal.Solution;
import cnuphys.simanneal.advisors.enums.Semester;
import cnuphys.simanneal.advisors.graphics.AdvisorPanel;
import cnuphys.simanneal.advisors.menu.MenuManager;
import cnuphys.simanneal.advisors.model.DataManager;
import cnuphys.simanneal.advisors.table.InputOutput;

/**
 * The main object for assigning core advisors
 * @author heddle
 *
 */
public class AdvisorAssign extends Simulation {
	
	/**
	 * A custom attribute for the number of advisors
	 */
	public static final String NUMADVISOR = "advisor count";
	
	/**
	 * A custom attribute for the number of students
	 */
	public static final String NUMSTUDENT = "student count";

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
	
	//the display frame
	private static JFrame _frame;
	
	//what semester are we dealing with
	private static Semester _semester = Semester.Fall2022;
	
	//a label on the menu bar for the semester
	private static JLabel _semesterLabel;
	
	//private constructor for singleton
	private AdvisorAssign() {
		_frame = new JFrame("CNU Core Advisor Assignments");
		screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	}
	
	/**
	 * Get the semester we are examining
	 * @return the semester we are examining
	 */
	public static Semester getSemester() {
		return _semester;
	}
	
	/**
	 * public accessor to the main frame
	 * @return the main frame
	 */
	public static JFrame getFrame() {
		return _frame;
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
	private void createSemesterLabel() {
		_semesterLabel = new JLabel("  SEMESTER: XXXXXXXXX");
		_semesterLabel.setOpaque(true);
		_semesterLabel.setBackground(Color.black);
		_semesterLabel.setForeground(Color.yellow);
		_semesterLabel.setFont(new Font("Dialog", Font.BOLD, 12));
		_semesterLabel.setBorder(BorderFactory.createLineBorder(Color.cyan, 1));
		fixSemesterLabel();

		_frame.getJMenuBar().add(Box.createHorizontalGlue());
		_frame.getJMenuBar().add(_semesterLabel);
		_frame.getJMenuBar().add(Box.createHorizontalStrut(5));
	}

	public void fixSemesterLabel() {
		_semesterLabel.setText("  SEMESTER: " + getSemester());
	}
	

	@Override
	protected void setInitialAttributes() {
		_attributes.add(NUMADVISOR, 0, false, false);
		_attributes.add(NUMSTUDENT, 0, false, false);
		_attributes.removeAttribute(SimulationAttributes.USELOGTEMP);
		_attributes.setPlotTitle("Assignment Quality");
		_attributes.setYAxisLabel("1/Quality");

	}

	@Override
	protected Solution setInitialSolution() {
		InputOutput.init();
		DataManager.init();
		return AdvisorSolution.initialSolution();
	}
	
	//last minute preparation
	private void finalPrep() {
		_attributes.setValue(NUMADVISOR, DataManager.getAdvisorData().count());		
		_attributes.setValue(NUMSTUDENT, DataManager.getStudentData().count());		
	}
	
	/**
	 * The main program
	 * @param arg command line arguments
	 */
	public static void main(String arg[]) {
		getInstance();
		

		// set up what to do if the window is closed
		WindowAdapter windowAdapter = new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent event) {
				System.exit(1);
			}
		};

		_frame.addWindowListener(windowAdapter);
		_frame.setLayout(new BorderLayout());
		
		//setup menus
		MenuManager.getInstance().init();

		AdvisorPanel panel = new AdvisorPanel(_instance);

		_frame.add(panel, BorderLayout.CENTER);
		
		getInstance().createSemesterLabel();
		
		//final preparations
		_instance.finalPrep();

		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				_frame.pack();
				_frame.setVisible(true);
				_frame.setLocationRelativeTo(null);
			}
		});
	}



}
