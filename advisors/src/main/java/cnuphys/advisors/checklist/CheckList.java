package cnuphys.advisors.checklist;

import java.awt.Dimension;

import javax.swing.JPanel;

import cnuphys.advisors.checklist.steps.BTMGStep;
import cnuphys.advisors.checklist.steps.CommunityCaptainStep;
import cnuphys.advisors.checklist.steps.HonorsAlgorithmStep;
import cnuphys.advisors.checklist.steps.HonorsMajorStep;
import cnuphys.advisors.checklist.steps.HonorsStudentInClassStep;
import cnuphys.advisors.checklist.steps.ILCStep;
import cnuphys.advisors.checklist.steps.MusTheaStep;
import cnuphys.advisors.checklist.steps.PSPStep;
import cnuphys.advisors.checklist.steps.PrelawStep;
import cnuphys.advisors.checklist.steps.PresScholarStep;
import cnuphys.advisors.checklist.steps.StudentInClassStep;
import cnuphys.advisors.checklist.steps.StudentsAlgorithmStep;
import cnuphys.advisors.checklist.steps.StudentsMajorStep;
import cnuphys.advisors.dialogs.OptionsDialog;
import cnuphys.advisors.enums.EAlgorithm;
import cnuphys.advisors.frame.AdvisorAssign;
import cnuphys.bCNU.dialog.VerticalFlowLayout;
import cnuphys.bCNU.graphics.component.CommonBorder;

public class CheckList extends JPanel {

	private static int HEIGHT = 450;

	//assign pres scholars
	private CheckListComponent presScholarStep;

	//assign ILC advisors
	private CheckListComponent ilcStep;

	//assign music and theater
	private CheckListComponent musTheaStep;

	//assign community captains
	private CheckListComponent ccptStep;

	//assign bio tech and management
	private CheckListComponent btmgStep;

	//assign prelaw students
	private CheckListComponent prelawStep;

	//assign honors students in honors advisor's class
	private CheckListComponent honorsStudentInClassStep;

	//assign honors students by major
	private CheckListComponent honorsMajorStep;

	//assign honors students by algorithm
	private CheckListComponent honorsAlgorithmStep;

	//assign  students in FCA's class
	private CheckListComponent studentInClassStep;
	
	//assign  PSP Students to PSP advisors
	private CheckListComponent pspStep;


	//assign honors students by major and 2ndary major
	private CheckListComponent studentsMajorStep;

	//assign remaining students by algorithm
	private CheckListComponent studentsAlgorithmStep;


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

	private void addComponents() {

		EAlgorithm ealg = OptionsDialog.currentAlgorithm;
		boolean inClass = (ealg == EAlgorithm.PutInFCAOptNumMaj);


		presScholarStep = new CheckListComponent("Presidential scholars", new PresScholarStep(), true);
		ilcStep = new CheckListComponent("ILC students", new ILCStep(), false);
		ccptStep = new CheckListComponent("Community Captains", new CommunityCaptainStep(), false);
		musTheaStep = new CheckListComponent("Music & Theater majors", new MusTheaStep(), false);
		btmgStep = new CheckListComponent("Bio Tech & Management students", new BTMGStep(), false);
		prelawStep = new CheckListComponent("Prelaw students", new PrelawStep(), false);

		if (inClass) {
			honorsStudentInClassStep = new CheckListComponent("Honors in Honors FCA Class",
					new HonorsStudentInClassStep(), false);
		}
		honorsMajorStep = new CheckListComponent("Honors students by major", new HonorsMajorStep(), false);
		honorsAlgorithmStep = new CheckListComponent("Honors students by algorithm", new HonorsAlgorithmStep(), false);

		pspStep = new CheckListComponent("Premed scholars to PSP advisors", new PSPStep(), false);
		if (inClass) {
			studentInClassStep = new CheckListComponent("Student in FCA Class", new StudentInClassStep(), false);
		}
		studentsMajorStep = new CheckListComponent("Students by major", new StudentsMajorStep(), false);
		studentsAlgorithmStep = new CheckListComponent("Remaining students by algorithm", new StudentsAlgorithmStep(),
				false);

		add(presScholarStep);
		add(ilcStep);
		add(ccptStep);
		add(musTheaStep);
		add(btmgStep);
		add(prelawStep);

		if (inClass) {
			add(honorsStudentInClassStep);
		}

		add(honorsMajorStep);
		add(honorsAlgorithmStep);
		add(pspStep);

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
		presScholarStep.run();
		ilcStep.run();
		ccptStep.run();
		musTheaStep.run();
		btmgStep.run();
		prelawStep.run();
	}

	/**
	 * Make sure the right buttons are enabled
	 */
	public void checkState() {

		EAlgorithm ealg = OptionsDialog.currentAlgorithm;
		boolean inClass = (ealg == EAlgorithm.PutInFCAOptNumMaj);

		boolean presScholarStepDone = presScholarStep.done;
		boolean ilcStepDone = ilcStep.done;
		boolean musTheaStepDone = musTheaStep.done;
		boolean ccptStepDone = ccptStep.done;
		boolean btmgStepDone = btmgStep.done;
		boolean prelawStepDone = prelawStep.done;
		boolean honorsMajorStepDone = honorsMajorStep.done;
		boolean honorsAlgorithmStepDone = honorsAlgorithmStep.done;
		boolean pspStepDone = pspStep.done;
		boolean studentsMajorStepDone = studentsMajorStep.done;
		boolean studentsAlgorithmStepDone = studentsAlgorithmStep.done;


		presScholarStep.setEnabled(!presScholarStepDone);
		ilcStep.setEnabled(presScholarStepDone && !ilcStepDone);
		musTheaStep.setEnabled(ilcStepDone && !musTheaStepDone);
		ccptStep.setEnabled(musTheaStepDone && !ccptStepDone);
		btmgStep.setEnabled(ccptStepDone && !btmgStepDone);
		prelawStep.setEnabled(btmgStepDone && !prelawStepDone);

		if (inClass) {

			boolean honorsStudentInClassStepDone = honorsStudentInClassStep.done;
			boolean studentInClassStepDone = studentInClassStep.done;

			honorsStudentInClassStep.setEnabled(prelawStepDone && !honorsStudentInClassStepDone);
			honorsMajorStep.setEnabled(honorsStudentInClassStepDone && !honorsMajorStepDone);
			honorsAlgorithmStep.setEnabled(honorsMajorStepDone && !honorsAlgorithmStepDone);
			pspStep.setEnabled(honorsAlgorithmStepDone && !pspStepDone);
			studentInClassStep.setEnabled(pspStepDone && !studentInClassStepDone);
			studentsMajorStep.setEnabled(studentInClassStepDone && !studentsMajorStepDone);
			studentsAlgorithmStep.setEnabled(studentsMajorStepDone && !studentsAlgorithmStepDone);
		}

		else {
			honorsMajorStep.setEnabled(prelawStepDone && !honorsMajorStepDone);
			honorsAlgorithmStep.setEnabled(honorsMajorStepDone && !honorsAlgorithmStepDone);
			pspStep.setEnabled(honorsAlgorithmStepDone && !pspStepDone);
			studentsMajorStep.setEnabled(pspStepDone && !studentsMajorStepDone);
			studentsAlgorithmStep.setEnabled(studentsMajorStepDone && !studentsAlgorithmStepDone);
		}

		if (studentsAlgorithmStepDone) {
			AdvisorAssign.allDone();
		}

	}
}
