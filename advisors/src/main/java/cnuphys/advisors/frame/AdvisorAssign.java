package cnuphys.advisors.frame;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import cnuphys.advisors.Advisor;
import cnuphys.advisors.checklist.CheckList;
import cnuphys.advisors.dialogs.MessageDialog;
import cnuphys.advisors.dialogs.OptionsDialog;
import  cnuphys.advisors.enums.Semester;
import cnuphys.advisors.graphics.AdvisorDisplay;
import cnuphys.advisors.graphics.AdvisorPanel;
import cnuphys.advisors.graphics.SizedText;
import cnuphys.advisors.io.OutputManager;
import cnuphys.advisors.menu.MenuManager;
import cnuphys.advisors.model.DataManager;
import cnuphys.advisors.simulation.AdvisorSimulation;
import cnuphys.advisors.threading.ThreadManager;
import cnuphys.bCNU.graphics.component.CommonBorder;
import cnuphys.bCNU.util.Fonts;
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
	private static Semester _semester = Semester.Fall2024;

	//a label on the menu bar for the semester
	private static JLabel _infoLabel;

	//the check list
	private CheckList _checklist;

	//the main menu bar
	private JMenuBar _menuBar;

	//the advisor panel
	private AdvisorPanel _advisorPanel;

	private static OptionsDialog _optionsDialog = new OptionsDialog();

	private static SizedText _bigText;

	//private constructor for singleton
	private AdvisorAssign() {
		super("CNU Fall 2024 Core Advisor Assignments");
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
	 * Set up the GUI
	 */
	public void setup() {

		_menuBar = new JMenuBar();
		setJMenuBar(_menuBar);

		setLayout(new BorderLayout());

		MenuManager.getInstance().init(_menuBar);

		//add the check list
		_checklist = CheckList.getInstance();
		
		JPanel panel = new JPanel() {
			@Override
			public Insets getInsets() {
				Insets def = super.getInsets();
				return new Insets(def.top + 2, def.left + 2, def.bottom + 2, def.right + 2);
			}

		};
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
	
		SizedText st = new SizedText(OptionsDialog.currentAlgorithm.description(), Fonts.defaultFont, _checklist.getPreferredSize().width);
		panel.setBorder(new CommonBorder("Current Algorithm"));
		panel.add(st);
		

		JPanel sp = new JPanel();
		sp.setLayout(new BorderLayout(4, 4));
		sp.add(_checklist, BorderLayout.CENTER);
		sp.add(panel, BorderLayout.NORTH);

		_advisorPanel = new AdvisorPanel(AdvisorSimulation.getInstance(), sp);

		add(_advisorPanel, BorderLayout.CENTER);


		createInfoLabel();
		
		_bigText = new SizedText("Ready", Fonts.hugeFont, _checklist.getPreferredSize().width);
		sp.add(_bigText, BorderLayout.SOUTH);

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

			s = String.format(" %s    #FCA: %d    #Student: %d    #Student in FCA's class: %d    Assigned: %d    Unassigned: %d    Required Avg: %4.1f  ",
					getSemester().name(), advisorCount, studentCount, DataManager.studentsHavingAdvisorAsInstructorCount(), assignedCount1, unassignedCount, avgReq);
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

	public static void showMessage(String text) {

		MessageDialog messageDialog = new MessageDialog("Secondary Majors and Specialties", true, text, Fonts.defaultFont);
		messageDialog.setVisible(true);
	}
	
	/**
	 * Check if we force buines students onto business advisors
	 * 
	 * @return <code>true</code> if we group
	 */
	public boolean forceBusiness() {
		return _optionsDialog.forceBusiness();
	}


	public static boolean useBusinessFamily() {
		return _optionsDialog.useBusinessFamily();
	}

	/**
	 * Are we grouping bio related majors?
	 * @return true if we are grouping bio related majors
	 */
	public static boolean useBioFamily() {
		return _optionsDialog.useBioFamily();
	}

	/**
	 * Are we grouping chem related majors?
	 * @return true if we are grouping chem related majors
	 */
	public static boolean useChemFamily() {
		return _optionsDialog.useChemFamily();
	}
	
	/**
	 * Are we grouping engineering related majors?
	 * @return true if we are grouping engineering related majors
	 */
	public static boolean useEngFamily() {
		return _optionsDialog.useEngFamily();
	}


	/**
	 * Called when all done!
	 */
	public static void allDone() {
		System.out.println("Assignments are complete!");
		ThreadManager.getInstance().done();
		AdvisorDisplay.getInstance().done();
		OutputManager.outputResults();
		MenuManager.getInstance().plotMenu.done();
		setBigText("Done");
	}

	public static void setBigText(String text) {
		if (_bigText != null) {
			_bigText.setText(text != null ? text : "???");
		}
	}

	/**
	 * The main program
	 *
	 * @param arg command line arguments
	 */
	public static void main(String arg[]) {


		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {

				_optionsDialog.setVisible(true);

				JFrame frame = getInstance();

				frame.setVisible(true);
				frame.setLocationRelativeTo(null);

				String text = "This would be a good time to bring up the list of advisors" +
						" and assign any \"secondary majors\" and advising specialties. Just double-click the advisor's name.";

				showMessage(text);

			}
		});
	}



}
