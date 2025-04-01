package cnuphys.advisors.dialogs;

import java.awt.BorderLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

import cnuphys.advisors.Student;
import cnuphys.advisors.model.Course;
import cnuphys.bCNU.dialog.SimpleDialog;
import cnuphys.bCNU.dialog.VerticalFlowLayout;
import cnuphys.bCNU.graphics.component.CommonBorder;
import cnuphys.bCNU.util.Fonts;

public class StudentDialog extends SimpleDialog {

	//student being displayed
	private Student _student;

	public StudentDialog(Student student) {
		super("Student Information", false, "Close");
		_student = student;
		addInfoPanel();
		setSize(600, 280);

	}

	private void addInfoPanel() {
		JPanel np = new JPanel();

		np.setLayout(new VerticalFlowLayout());
		np.add(makeLabel(_student.fullNameAndID()));

		String ds = valString("Dept", _student.major.getDepartment().name());
		String ss = valString("Major", _student.major.name());
		np.add(makeLabel(ds + ss));

		String as = _student.advisor != null ? _student.advisor.name : "";
		np.add(makeLabel(valString("Advisor", as)));

		boolean hasCWith = _student.hasCourseWithAdvisor();
		np.add(makeLabel(ynString("Has course with advisor", hasCWith)));

		if (hasCWith) {
			Course course = _student.courseWithThisAdvisor(_student.advisor);
			np.add(makeLabel(valString("Course with Advisor", course.infoString())));
		}



		String ccs = ynString("CCPT", _student.ccpt());
		String pss = ynString("PRESSCH", _student.prsc());
		String wds = ynString("WIND", _student.wind());
		String phs = ynString("PSP", _student.psp());
		np.add(makeLabel(ccs + pss + wds + phs ));

		np.add(makeLabel(ynString("ALC", _student.alc())));
  	    np.add(makeLabel(ynString("HONORS", _student.honors())));

		np.setBorder(new CommonBorder("Basic student information"));
		add(np, BorderLayout.NORTH);
	}

	private String valString(String prompt, String value) {
		return prompt + ": " + value + "    ";
	}

	private String ynString(String prompt, boolean val) {
		return prompt + ": " + (val ? "Y" : "N") + "    ";
	}



	//create a label
	private JLabel makeLabel(String text) {
		JLabel label = new JLabel(text);
		label.setFont(Fonts.defaultFont);
		return label;
	}


}
