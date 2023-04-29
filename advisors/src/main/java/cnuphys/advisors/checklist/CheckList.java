package cnuphys.advisors.checklist;

import java.awt.Dimension;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

import cnuphys.advisors.checklist.steps.ILCStep;
import cnuphys.advisors.checklist.steps.PresScholarStep;
import cnuphys.bCNU.dialog.VerticalFlowLayout;
import cnuphys.bCNU.graphics.component.CommonBorder;

public class CheckList extends JPanel {
	
	private static int HEIGHT = 450;
	
	//assign pres scholars
	private CheckListComponent presScholarStep;

	//assign ILC advisors
	private CheckListComponent ilcStep;

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
		
		add(presScholarStep);
		add(ilcStep);
	}
	
	@Override
	public Dimension getPreferredSize() {
		Dimension d = super.getPreferredSize();
		d.height = HEIGHT;
		return d;
	}

	/**
	 * Make sure the right buttons are enabled
	 */
	public void checkState() {
		boolean presScholarStepDone = presScholarStep.done;
		boolean ilcStepDone = ilcStep.done;
		
		presScholarStep.setEnabled(!presScholarStepDone);
		ilcStep.setEnabled(presScholarStepDone && !ilcStepDone);
	}
}
