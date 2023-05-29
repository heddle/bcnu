package cnuphys.advisors.dialogs;

import java.awt.Component;
import java.awt.GridLayout;
import java.awt.Insets;

import javax.swing.JCheckBox;
import javax.swing.JPanel;

import cnuphys.bCNU.dialog.SimpleDialog;
import cnuphys.bCNU.graphics.component.CommonBorder;

public class OptionsDialog extends SimpleDialog {
	
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
	protected Component createNorthComponent() {
		return createCheckBoxPanel();
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


}
