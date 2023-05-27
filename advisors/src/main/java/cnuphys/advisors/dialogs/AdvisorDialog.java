package cnuphys.advisors.dialogs;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.text.SimpleAttributeSet;

import cnuphys.advisors.Advisor;
import cnuphys.advisors.Person;
import cnuphys.advisors.Student;
import cnuphys.advisors.enums.Major;
import cnuphys.advisors.model.Course;
import cnuphys.advisors.model.DataManager;
import cnuphys.advisors.model.ILCCourse;
import cnuphys.bCNU.component.EnumComboBox;
import cnuphys.bCNU.dialog.SimpleDialog;
import cnuphys.bCNU.dialog.VerticalFlowLayout;
import cnuphys.bCNU.graphics.ImageManager;
import cnuphys.bCNU.graphics.component.CommonBorder;
import cnuphys.bCNU.graphics.component.TextPaneScrollPane;
import cnuphys.bCNU.util.Fonts;

public class AdvisorDialog extends SimpleDialog {

	private static SimpleAttributeSet RED_PLAIN = TextPaneScrollPane.createStyle(Color.red, "sansserif", 11, false, false);
	private static SimpleAttributeSet BLACK_PLAIN = TextPaneScrollPane.createStyle(Color.black, "sansserif", 11, false, false);
	private static SimpleAttributeSet BLACK_BOLD = TextPaneScrollPane.createStyle(Color.black, "sansserif", 11, false, true);
	private static SimpleAttributeSet GRAY_ITALIC = TextPaneScrollPane.createStyle(Color.gray, "sansserif", 11, true, false);
	private static SimpleAttributeSet GRAY_ITALIC_BOLD = TextPaneScrollPane.createStyle(Color.gray, "sansserif", 11, true, true);


	//advisor being displayed
	private Advisor _advisor;

	//choose prefrerred major (other than own)
	private EnumComboBox _preferredMajorCombo;

	public AdvisorDialog(Advisor advisor) {
		super("Advisor Information", false, "Close");
		_advisor = advisor;

		addInfoPanel();
		addListPanels();
		setSize(600, 600);
	}

	private void addInfoPanel() {
		JPanel np = new JPanel();

		np.setLayout(new VerticalFlowLayout());

//		np.setLayout(new VerticalFlowLayout());
		np.add(makeLabel(_advisor.name));

		String ds = valString("Dept", _advisor.department.name());
		String ss = valString("Subject", _advisor.subject.name());
		np.add(makeLabel(ds + ss));

		String ccs = ynString("CCPT", _advisor.check(Person.CCPT));
		String pls = ynString("PRELAW", _advisor.check(Person.CCPT));
		String pss = ynString("PRESSCH", _advisor.check(Person.PRESSCHOLAR));
		String bts = ynString("BTMG", _advisor.check(Person.BTMG));
		String wds = ynString("WIND", _advisor.check(Person.WIND));
		String mts = ynString("MUSCTHEA", _advisor.check(Person.MUSICTHEATER));
		np.add(makeLabel(ccs + pls + pss + bts + wds + mts));

		np.add(makeLabel(ynString("HONORS", _advisor.honors())));
		np.add(makeLabel(ilcString() ));

		np.add(preferred2ndMajor());

		np.setBorder(new CommonBorder("Basic advisor information"));
		add(np, BorderLayout.NORTH);
	}

	private JPanel addListPanels() {
		JPanel lp = new JPanel();

		lp.setLayout(new BorderLayout(6, 6));

		lp.add(createAdviseesPane(), BorderLayout.EAST);
		lp.add(createSchedulePane(), BorderLayout.WEST);

		add(lp, BorderLayout.CENTER);
		return lp;
	}

	//the advisors's schedule
	private TextPaneScrollPane createSchedulePane() {
		TextPaneScrollPane sp = new TextPaneScrollPane("Schedule");


		for (Course course : _advisor.schedule) {

			String info = course.infoString() + "    \n";
			if (course.isILC) {
				sp.append(info, RED_PLAIN);
			}
			else {
				sp.append(info, BLACK_PLAIN);
			}
		}

		return sp;
	}




	private TextPaneScrollPane createAdviseesPane() {
		TextPaneScrollPane sp = new TextPaneScrollPane("Advisees");



		for (Student student : _advisor.advisees) {
			boolean locked = student.locked();
			boolean plp = student.check(Person.PLP);

			String info = String.format("%s (%s)   \n", student.fullNameAndID(), student.major.name());

			if (locked && plp) {
				sp.append(info, GRAY_ITALIC_BOLD);
			} else if (locked) {
				sp.append(info, GRAY_ITALIC);
			} else if (plp) {
				sp.append(info, BLACK_BOLD);
			} else {
				sp.append(info, BLACK_PLAIN);
			}

		}

		return sp;
	}


	//panel that holds the 2nd preferred major
	private JPanel preferred2ndMajor() {
		JPanel p = new JPanel();
		p.setLayout(new FlowLayout(FlowLayout.LEFT, 4, 4));
		p.add(makeLabel("Preferred Secondary Major"));

		_preferredMajorCombo = Major.getComboBox(_advisor.preferred2ndMajor != null ? _advisor.preferred2ndMajor : Major.NONE);

		_preferredMajorCombo.setFont(Fonts.defaultFont);
		_preferredMajorCombo.addActionListener (new ActionListener () {
		    @Override
			public void actionPerformed(ActionEvent e) {
		    	 Enum en = _preferredMajorCombo.getSelectedEnum();
				Major major = (Major) en;
				System.err.println("SELECTED MAJOR: " + major);

				if (major == _advisor.subject) {
					_advisor.preferred2ndMajor = null;
				} else {
					_advisor.preferred2ndMajor = major;
				}
			}
		});
		p.add(_preferredMajorCombo);
		return p;
	}


	//create a label
	private JLabel makeLabel(String text) {
		JLabel label = new JLabel(text);
		label.setFont(Fonts.defaultFont);
		return label;
	}

	private String ilcString() {
		if (_advisor.ilc()) {
			ILCCourse course = DataManager.getILCData().getILCCourse(_advisor);
			if (course == null) {
				System.err.println("Unexpected null ILC course in AdvisorDialog");
				System.exit(1);
			}
			return String.format("ILC: %s %s%s LC: %s", course.crn, course.subject, course.course, course.learningCommunity);
		}
		else {
			return ynString("ILC", false);
		}
	}

	private String valString(String prompt, String value) {
		return prompt + ": " + value + "    ";
	}

	private String ynString(String prompt, boolean val) {
		return prompt + ": " + (val ? "Y" : "N") + "    ";
	}

}
