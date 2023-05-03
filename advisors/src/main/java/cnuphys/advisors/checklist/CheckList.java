package cnuphys.advisors.checklist;

import java.awt.Dimension;

import javax.swing.JPanel;

import cnuphys.advisors.checklist.steps.BTMGStep;
import cnuphys.advisors.checklist.steps.CommunityCaptainStep;
import cnuphys.advisors.checklist.steps.ILCStep;
import cnuphys.advisors.checklist.steps.MusTheaStep;
import cnuphys.advisors.checklist.steps.PrelawStep;
import cnuphys.advisors.checklist.steps.PresScholarStep;
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

		presScholarStep = new CheckListComponent("Assign pres. scholars to advisors", new PresScholarStep(), true);
		ilcStep = new CheckListComponent("Assign ILC students to advisors", new ILCStep(), false);
		ccptStep = new CheckListComponent("Assign Community Captains to advisors", new CommunityCaptainStep(), false);
		musTheaStep = new CheckListComponent("Assign Music & Theater students to advisors", new MusTheaStep(), false);
		btmgStep = new CheckListComponent("Assign Bio Tech & Management to advisors", new BTMGStep(), false);
		prelawStep = new CheckListComponent("Assign Prelaw students to advisors", new PrelawStep(), false);

		add(presScholarStep);
		add(ilcStep);
		add(ccptStep);
		add(musTheaStep);
		add(btmgStep);
		add(prelawStep);
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
		boolean presScholarStepDone = presScholarStep.done;
		boolean ilcStepDone = ilcStep.done;
		boolean musTheaStepDone = musTheaStep.done;
		boolean ccptStepDone = ccptStep.done;
		boolean btmgStepDone = btmgStep.done;

		presScholarStep.setEnabled(!presScholarStepDone);
		ilcStep.setEnabled(presScholarStepDone && !ilcStepDone);
		musTheaStep.setEnabled(ilcStepDone && !musTheaStepDone);
		ccptStep.setEnabled(musTheaStepDone && !ccptStepDone);
		btmgStep.setEnabled(ccptStepDone && !btmgStepDone);

	}
}
