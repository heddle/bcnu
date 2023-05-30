package cnuphys.advisors.dialogs;

import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import cnuphys.advisors.enums.EAlgorithm;
import cnuphys.advisors.graphics.SizedText;
import cnuphys.bCNU.dialog.SimpleDialog;
import cnuphys.bCNU.graphics.component.CommonBorder;
import cnuphys.bCNU.util.Fonts;

public class OptionsDialog extends SimpleDialog {

	public static EAlgorithm currentAlgorithm = EAlgorithm.PutInFCAOptNumMaj;

	private JCheckBox _busnFamilyCB;
	private JCheckBox _bioFamilyCB;
	private JCheckBox _chemFamilyCB;

	public OptionsDialog() {
		super("Algorithm Options", true, "Done");
	}


	/**
	 * Override to create the component that goes in the north.
	 *
	 * @return the component that is placed in the north
	 */
	@Override
	protected Component createNorthComponent() {
		return createCheckBoxPanel();
	}

	/**
	 * Override to create the component that goes in the center.
	 *
	 * @return the component that is placed in the center
	 */
	@Override
	protected Component createCenterComponent() {
		return createAlgorithmPanel();
	}

	/**
	 * Create the panel that holds the checkboxes for bold and italic.
	 *
	 * @return the panel that holds the checkboxes for bold and italic.
	 */
	private JPanel createCheckBoxPanel() {
		JPanel checkBoxPanel = new JPanel() {
			@Override
			public Insets getInsets() {
				Insets def = super.getInsets();
				return new Insets(def.top + 2, def.left + 2, def.bottom + 2, def.right + 2);
			}

		};

		_busnFamilyCB = new JCheckBox("Group ACCT, BUSN, FIN, MARKT, and MGMT together", true);
		_bioFamilyCB = new JCheckBox("Group BIOL, CELLMB, KINES, and OEBIO together", true);
		_chemFamilyCB = new JCheckBox("Group BIOCHEM and CHEM together", true);


		checkBoxPanel.setLayout(new GridLayout(0, 1));
		checkBoxPanel.add(_busnFamilyCB);
		checkBoxPanel.add(_bioFamilyCB);
		checkBoxPanel.add(_chemFamilyCB);

		checkBoxPanel.setBorder(new CommonBorder("Group certain similar majors together"));

		return checkBoxPanel;
	}

	/**
	 * Create the panel that holds the checkboxes for bold and italic.
	 *
	 * @return the panel that holds the checkboxes for bold and italic.
	 */
	private JPanel createAlgorithmPanel() {
		JPanel algPanel = new JPanel() {


			@Override
			public Insets getInsets() {
				Insets def = super.getInsets();
				return new Insets(def.top + 2, def.left + 2, def.bottom + 2, def.right + 2);
			}
		};

		algPanel.setLayout(new GridLayout(0, 1));

		ButtonGroup bg = new ButtonGroup();

		for (EAlgorithm ealg : EAlgorithm.values()) {
			RBLabel rb = new RBLabel(bg, ealg, Fonts.defaultFont, 300, ealg == currentAlgorithm);
			algPanel.add(rb);
		}

		algPanel.setBorder(new CommonBorder("Select and optimization algorithm"));
		return algPanel;
	}


	public boolean useBusinessFamily() {
		return _busnFamilyCB.isSelected();
	}

	public boolean useBioFamily() {
		return _bioFamilyCB.isSelected();
	}

	public boolean useChemFamily() {
		return _chemFamilyCB.isSelected();
	}

	@Override
	public Insets getInsets() {
		Insets def = super.getInsets();
		return new Insets(def.top + 4, def.left + 4, def.bottom + 4, def.right + 4);
	}

	class RBLabel extends JPanel {
		public JRadioButton radioButton;

		private EAlgorithm  _ealg;

		public RBLabel(ButtonGroup bg, EAlgorithm ealg, Font font, int width, boolean set) {
			setLayout(new FlowLayout(FlowLayout.LEFT, 4, 4));
			_ealg = ealg;

			ActionListener al = new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					currentAlgorithm = _ealg;
					System.out.println("current algorithm: " + currentAlgorithm);
				}

			};

			radioButton = new JRadioButton(" ", set);
			radioButton.addActionListener(al);
			bg.add(radioButton);
			SizedText st = new SizedText(ealg.description(), font, width);

			add(radioButton);
			add(st);
		}
	}



}
