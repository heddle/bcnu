package cnuphys.advisors.checklist;

import java.awt.Dimension;

import javax.swing.JPanel;

import cnuphys.advisors.checklist.steps.CommunityCaptainStep;
import cnuphys.advisors.checklist.steps.HonorsAlgorithmStep;
import cnuphys.advisors.checklist.steps.HonorsInALCStep;
import cnuphys.advisors.checklist.steps.HonorsMajorStep;
import cnuphys.advisors.checklist.steps.HonorsStudentInClassStep;
import cnuphys.advisors.checklist.steps.MusTheaStep;
import cnuphys.advisors.checklist.steps.PSPStep;
import cnuphys.advisors.checklist.steps.PresScholarStep;
import cnuphys.advisors.checklist.steps.StudentInClassStep;
import cnuphys.advisors.checklist.steps.StudentsAlgorithmStep;
import cnuphys.advisors.checklist.steps.StudentsInALCStep;
import cnuphys.advisors.checklist.steps.StudentsMajorStep;
import cnuphys.advisors.dialogs.OptionsDialog;
import cnuphys.advisors.enums.EAlgorithm;
import cnuphys.advisors.frame.AdvisorAssign;
import cnuphys.advisors.threading.ThreadManager;
import cnuphys.bCNU.dialog.VerticalFlowLayout;
import cnuphys.bCNU.graphics.component.CommonBorder;

public class CheckList extends JPanel {

	private static int HEIGHT = 450;

	//assign pres scholars
	private CheckListLaunchable presScholarStep;

	//assign ALC advisors
	private CheckListLaunchable alcStep;

	//assign music and theater
	private CheckListLaunchable musTheaStep;

	//assign community captains
	private CheckListLaunchable ccptStep;
	
	//assign honors students in honors advisor's class
	private CheckListLaunchable honorsStudentInALCStep;


	//assign honors students in honors advisor's class
	private CheckListLaunchable honorsStudentInClassStep;

	//assign honors students by major
	private CheckListLaunchable honorsMajorStep;

	//assign honors students by algorithm
	private CheckListLaunchable honorsAlgorithmStep;

	//assign  students in FCA's class
	private CheckListLaunchable studentInClassStep;
	
	//assign  PSP Students to PSP advisors
	private CheckListLaunchable pspStep;

	//assign honors students by major and 2ndary major
	private CheckListLaunchable studentsMajorStep;

	//assign remaining students by algorithm
	private CheckListLaunchable studentsAlgorithmStep;


	//singleton
	private static CheckList _instance;

	private CheckList() {
		setLayout(new VerticalFlowLayout());
		setBorder(new CommonBorder("Checklist"));
		addComponents();
	}

	/**
	 * public access for singleton
	 * @return the CheckList singleton
	 */
	public static CheckList getInstance() {
		if (_instance == null) {
			_instance = new CheckList();
		}
		return _instance;
	}

	//add the graphical components
	private void addComponents() {

		EAlgorithm ealg = OptionsDialog.currentAlgorithm;
		boolean inClass = (ealg == EAlgorithm.PutInFCAOptNumMaj);


		presScholarStep = new PresScholarStep("Presidential scholars", true);
		ccptStep = new CommunityCaptainStep("Community Captains", false);
		musTheaStep = new MusTheaStep("Music & Theater majors", false);
		
		honorsStudentInALCStep = new HonorsInALCStep("Honors in Honors ALC Class", false);
		alcStep = new StudentsInALCStep("Sdutents in ALCs", false);

		if (inClass) {
			honorsStudentInClassStep = new HonorsStudentInClassStep("Honors in Honors FCA Class", false);
		}
		honorsMajorStep = new HonorsMajorStep("Honors students by major", false);
		honorsAlgorithmStep = new HonorsAlgorithmStep("Honors students by algorithm", false);

		pspStep = new PSPStep("Premed scholars to PSP advisors", false);
		if (inClass) {
			studentInClassStep = new StudentInClassStep("Student in FCA Class", false);
		}
		studentsMajorStep = new StudentsMajorStep("Students by major", false);
		studentsAlgorithmStep = new StudentsAlgorithmStep("Remaining students by algorithm", false);

		add(presScholarStep);
		add(ccptStep);
		add(musTheaStep);
		
		add(honorsStudentInALCStep);

		if (inClass) {
			add(honorsStudentInClassStep);
		}

		add(honorsMajorStep);
		add(honorsAlgorithmStep);
		add(pspStep);
		
		add(alcStep);

		if (inClass) {
			add(studentInClassStep);
		}
		add(studentsMajorStep);
		add(studentsAlgorithmStep);

	}

	@Override
	public Dimension getPreferredSize() {
		Dimension d = super.getPreferredSize();
		d.height = HEIGHT;
		return d;
	}

	public void initRun() {
		ThreadManager tm = ThreadManager.getInstance();
		tm.queue(presScholarStep);
		tm.queue(ccptStep);
		tm.queue(musTheaStep);
	}

	/**
	 * Make sure the right buttons are enabled
	 */
	public void checkState() {

		EAlgorithm ealg = OptionsDialog.currentAlgorithm;
		boolean inClass = (ealg == EAlgorithm.PutInFCAOptNumMaj);

		boolean presScholarStepDone = presScholarStep.done;
		boolean alcStepDone = alcStep.done;
		boolean musTheaStepDone = musTheaStep.done;
		boolean ccptStepDone = ccptStep.done;
		
		boolean honorsStudentInALCStepDone = honorsStudentInALCStep.done;
		honorsStudentInALCStep.setEnabled(!honorsStudentInALCStepDone);
		
		boolean honorsMajorStepDone = honorsMajorStep.done;
		boolean honorsAlgorithmStepDone = honorsAlgorithmStep.done;
		boolean pspStepDone = pspStep.done;
		boolean studentsMajorStepDone = studentsMajorStep.done;
		boolean studentsAlgorithmStepDone = studentsAlgorithmStep.done;


		presScholarStep.setEnabled(!presScholarStepDone);
//		ilcStep.setEnabled(presScholarStepDone && !ilcStepDone);
//		musTheaStep.setEnabled(ilcStepDone && !musTheaStepDone);
		ccptStep.setEnabled(musTheaStepDone && !ccptStepDone);

		if (inClass) {

			boolean honorsStudentInClassStepDone = honorsStudentInClassStep.done;
			boolean studentInClassStepDone = studentInClassStep.done;

			honorsStudentInClassStep.setEnabled(honorsStudentInALCStepDone && !honorsStudentInClassStepDone);
			honorsMajorStep.setEnabled(honorsStudentInClassStepDone && !honorsMajorStepDone);
			honorsAlgorithmStep.setEnabled(honorsMajorStepDone && !honorsAlgorithmStepDone);
			pspStep.setEnabled(honorsAlgorithmStepDone && !pspStepDone);
			alcStep.setEnabled(pspStepDone && !alcStepDone);
			studentInClassStep.setEnabled(alcStepDone && !studentInClassStepDone);
			studentsMajorStep.setEnabled(studentInClassStepDone && !studentsMajorStepDone);
			studentsAlgorithmStep.setEnabled(studentsMajorStepDone && !studentsAlgorithmStepDone);
		}

		else {
			honorsMajorStep.setEnabled(!honorsMajorStepDone);
			honorsAlgorithmStep.setEnabled(honorsMajorStepDone && !honorsAlgorithmStepDone);
			pspStep.setEnabled(honorsAlgorithmStepDone && !pspStepDone);
			alcStep.setEnabled(pspStepDone && !alcStepDone);
			studentsMajorStep.setEnabled(alcStepDone && !studentsMajorStepDone);
			studentsAlgorithmStep.setEnabled(studentsMajorStepDone && !studentsAlgorithmStepDone);
		}

		if (studentsAlgorithmStepDone) {
			AdvisorAssign.allDone();
		}

	}
}
