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
import cnuphys.advisors.enums.Specialty;
import cnuphys.advisors.model.Course;
import cnuphys.bCNU.component.EnumComboBox;
import cnuphys.bCNU.dialog.SimpleDialog;
import cnuphys.bCNU.dialog.VerticalFlowLayout;
import cnuphys.bCNU.graphics.component.CommonBorder;
import cnuphys.bCNU.graphics.component.TextPaneScrollPane;
import cnuphys.bCNU.util.Fonts;
import cnuphys.bCNU.util.X11Colors;

public class AdvisorDialog extends SimpleDialog {

	private static Color _honBG = X11Colors.getX11Color("alice blue");

	private static SimpleAttributeSet BLACK_PLAIN_HON = TextPaneScrollPane.createStyle(Color.black, _honBG, "sansserif", 11, false, false);
	private static SimpleAttributeSet BLACK_BOLD_HON = TextPaneScrollPane.createStyle(Color.black, _honBG, "sansserif", 11, false, true);
	private static SimpleAttributeSet GRAY_ITALIC_HON = TextPaneScrollPane.createStyle(Color.gray, _honBG, "sansserif", 11, true, false);
	private static SimpleAttributeSet GRAY_ITALIC_BOLD_HON = TextPaneScrollPane.createStyle(Color.gray, _honBG, "sansserif", 11, true, true);


	private static SimpleAttributeSet RED_PLAIN = TextPaneScrollPane.createStyle(Color.red, "sansserif", 11, false, false);
	private static SimpleAttributeSet BLACK_PLAIN = TextPaneScrollPane.createStyle(Color.black, "sansserif", 11, false, false);
	private static SimpleAttributeSet BLACK_BOLD = TextPaneScrollPane.createStyle(Color.black, "sansserif", 11, false, true);
	private static SimpleAttributeSet GRAY_ITALIC = TextPaneScrollPane.createStyle(Color.gray, "sansserif", 11, true, false);
	private static SimpleAttributeSet GRAY_ITALIC_BOLD = TextPaneScrollPane.createStyle(Color.gray, "sansserif", 11, true, true);


	//advisor being displayed
	private Advisor _advisor;

	//choose preferred major (other than own)
	private EnumComboBox _preferredMajorCombo;

	//choose specialty
	private EnumComboBox _specialtyCombo;

	public AdvisorDialog(Advisor advisor) {
		super("Advisor Information", false, "Close");
		_advisor = advisor;

		addInfoPanel();
		addListPanels();
		setSize(800, 600);
	}

	private void addInfoPanel() {
		JPanel np = new JPanel();

		np.setLayout(new VerticalFlowLayout());
		np.add(makeLabel(_advisor.name));

		String ds = valString("Dept", _advisor.department.name());
		String ss = valString("Subject", _advisor.subject.name());
		np.add(makeLabel(ds + ss));

		String ccs = ynString("CCPT", _advisor.check(Person.CCPT));
		String pss = ynString("PRESSCH", _advisor.check(Person.PRESSCHOLAR));
		String wds = ynString("WIND", _advisor.check(Person.WIND));
		String mts = ynString("MUSCTHEA", _advisor.check(Person.MUSICTHEATER));
		np.add(makeLabel(ccs + pss + wds + mts));

		np.add(makeLabel(ynString("HONORS", _advisor.honors())));

		np.add(preferred2ndMajor());
		np.add(specialty());

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
			if (course.isALC) {
				sp.append(info, RED_PLAIN);
			}
			else {
				sp.append(info, BLACK_PLAIN);
			}
		}

		return sp;
	}

//show the advisees
	private TextPaneScrollPane createAdviseesPane() {
		TextPaneScrollPane sp = new TextPaneScrollPane("Advisees");


		for (Student student : _advisor.advisees) {
			boolean locked = student.locked();
			boolean plp = student.check(Person.PLP);

			String info = String.format("%s (%s) [%s] {%s} \n", student.fullNameAndID(), student.major.name(), student.bannerBlock, student.reason.name());

			if (student.honors()) {
				if (locked && plp) {
					sp.append(info, GRAY_ITALIC_BOLD_HON);
				} else if (locked) {
					sp.append(info, GRAY_ITALIC_HON);
				} else if (plp) {
					sp.append(info, BLACK_BOLD_HON);
				} else {
					sp.append(info, BLACK_PLAIN_HON);
				}
			} else {
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
				System.out.println("SELECTED MAJOR: " + major);

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

	//panel that holds the optional specialty
	private JPanel specialty() {
		JPanel p = new JPanel();
		p.setLayout(new FlowLayout(FlowLayout.LEFT, 4, 4));
		p.add(makeLabel("Advising Specialty"));

		_specialtyCombo = Specialty.getComboBox(_advisor.specialty);

		_specialtyCombo.setFont(Fonts.defaultFont);
		_specialtyCombo.addActionListener (new ActionListener () {
		    @Override
			public void actionPerformed(ActionEvent e) {
		    	 Enum en = _specialtyCombo.getSelectedEnum();
				Specialty specialty = (Specialty) en;
				System.out.println("SELECTED SPECIALTY: " + specialty);
				_advisor.specialty = specialty;
			}
		});
		p.add(_specialtyCombo);
		return p;
	}



	//create a label
	private JLabel makeLabel(String text) {
		JLabel label = new JLabel(text);
		label.setFont(Fonts.defaultFont);
		return label;
	}


	private String valString(String prompt, String value) {
		return prompt + ": " + value + "    ";
	}

	private String ynString(String prompt, boolean val) {
		return prompt + ": " + (val ? "Y" : "N") + "    ";
	}

}
