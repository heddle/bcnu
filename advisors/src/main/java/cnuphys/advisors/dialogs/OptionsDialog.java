package cnuphys.advisors.dialogs;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
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

	//check boxes for grouping majors
	private JCheckBox _busnFamilyCB;
	private JCheckBox _bioFamilyCB;
	private JCheckBox _chemFamilyCB;
	private JCheckBox _engrFamilyCB;
	
	//checkboxes for force assignment
	private JCheckBox _forceMusicCB;
	private JCheckBox _forceTheaterAndDanceCB;
	private JCheckBox _forceEngineeringCB;
	private JCheckBox _forceComputerScienceCB;
	private JCheckBox _forceCyberCB;
	private JCheckBox _forceInformationScienceCB;
	private JCheckBox _forcePhysicsCB;
	private JCheckBox _forceHonorsCB;
	private JCheckBox _forcePresScholarsCB;
	private JCheckBox _forceCommCaptCB;
	
	//done button
	private JButton _doneButton;
	

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
		return createMajorGroupPanel();
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
	 * Override to create the component that goes in the south.
	 *
	 * @return the component that is placed in the center
	 */
	@Override
	protected Component createSouthComponent() {
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout(0, 8));
		
		panel.add(createForceAssignPanel(), BorderLayout.CENTER);
		
		_doneButton = new JButton("Done");
		
		_doneButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
			}
		});
		
		panel.add(_doneButton, BorderLayout.SOUTH);
		
		return panel;
	}
	
	/**
	 * Create the panel that holds the checkboxes for bold and italic.
	 *
	 * @return the panel that holds the checkboxes for bold and italic.
	 */
	private JPanel createForceAssignPanel() {
		
		
		JPanel checkBoxPanel = new JPanel() {
			@Override
			public Insets getInsets() {
				Insets def = super.getInsets();
				return new Insets(def.top + 2, def.left + 2, def.bottom + 2, def.right + 2);
			}

		};
		
		_forceMusicCB = new JCheckBox("Force Music majors on Music Advisors", true);
		_forceTheaterAndDanceCB = new JCheckBox("Force Theater and Dance majors on Theater and Dance Advisors", true);
		_forceEngineeringCB = new JCheckBox("Force Engineering majors on Engineering Advisors", true);
		_forceComputerScienceCB = new JCheckBox("Force Computer Science majors on Computer Science Advisors", true);
		_forceCyberCB = new JCheckBox("Force Cybersecurity majors on Cybersecurity Advisors", true);
		_forceInformationScienceCB = new JCheckBox("Force Information Science majors on Information Science Advisors", true);
		_forcePhysicsCB = new JCheckBox("Force Physics majors on Physics Advisors", true);
		_forceHonorsCB = new JCheckBox("Force Honors College students on Honors College Advisors", true);
		_forcePresScholarsCB = new JCheckBox("Force Presidential Scholars on Presidential Scholars Advisors", true);
		_forceCommCaptCB = new JCheckBox("Force Community Captains on Community Captains Advisors", true);
		
		checkBoxPanel.setLayout(new GridLayout(0, 1));
        checkBoxPanel.add(_forceHonorsCB);
        checkBoxPanel.add(_forcePresScholarsCB);
		checkBoxPanel.add(_forceCommCaptCB);
        checkBoxPanel.add(_forceMusicCB);
        checkBoxPanel.add(_forceTheaterAndDanceCB);
        checkBoxPanel.add(_forceEngineeringCB);
        checkBoxPanel.add(_forceComputerScienceCB);
        checkBoxPanel.add(_forceCyberCB);
        checkBoxPanel.add(_forceInformationScienceCB);
        checkBoxPanel.add(_forcePhysicsCB);
        
		
		checkBoxPanel.setBorder(new CommonBorder("Force certain majors on certain advisors"));

		return checkBoxPanel;

	}


	/**
	 * Create the panel that holds the checkboxes for bold and italic.
	 *
	 * @return the panel that holds the checkboxes for bold and italic.
	 */
	private JPanel createMajorGroupPanel() {
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
		_engrFamilyCB = new JCheckBox("Group EE and CPEN together", true);


		checkBoxPanel.setLayout(new GridLayout(0, 1));
		checkBoxPanel.add(_busnFamilyCB);
		checkBoxPanel.add(_bioFamilyCB);
		checkBoxPanel.add(_chemFamilyCB);
		checkBoxPanel.add(_engrFamilyCB);

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

		algPanel.setBorder(new CommonBorder("Select an optimization algorithm"));
		return algPanel;
	}
	
	/**
	 * Check if we force assignment of honors college students
	 * 
	 * @return <code>true</code> if we force
	 */
	public boolean forceHonors() {
		return _forceHonorsCB.isSelected();
	}
	
	/**
	 * Check if we force assignment of presidential scholars
	 * 
	 * @return <code>true</code> if we force
	 */
	public boolean forcePresScholars() {
		return _forcePresScholarsCB.isSelected();
	}
	
	/**
	 * Check if we force assignment of community captains
	 * 
	 * @return <code>true</code> if we force
	 */
	public boolean forceCommCapt() {
		return _forceCommCaptCB.isSelected();
	}
	
	/**
	 * Check if we force assignment of physics majors
	 * 
	 * @return <code>true</code> if we force
	 */
	public boolean forcePhysics() { return _forcePhysicsCB.isSelected(); }
	
	/**
	 * Check if we force assignment of information science majors
	 * 
	 * @return <code>true</code> if we force
	 */
	public boolean forceInformationScience() { return _forceInformationScienceCB.isSelected(); }
	
	/**
	 * Check if we force assignment of cybersecurity majors
	 * 
	 * @return <code>true</code> if we force
	 */
	public boolean forceCyber() { return _forceCyberCB.isSelected(); }

	/**
	 * Check if we force assignment of music majors
	 * 
	 * @return <code>true</code> if we force
	 */
	public boolean forceMusic() {
		return _forceMusicCB.isSelected();
	}
	
	/**
	 * Check if we force assignment of theater and dance majors
	 * 
	 * @return <code>true</code> if we force
	 */
	public boolean forceTheaterAndDance() {
		return _forceTheaterAndDanceCB.isSelected();
	}
	
	/**
	 * Check if we force assignment of engineering majors
	 * 
	 * @return <code>true</code> if we force
	 */
	public boolean forceEngineering() {
		return _forceEngineeringCB.isSelected();
	}
	
	/**
	 * Check if we force assignment of computer science majors
	 * 
	 * @return <code>true</code> if we force
	 */
	public boolean forceComputerScience() {
		return _forceComputerScienceCB.isSelected();
	}

	
	/**
	 * Check if we group certain business majors together
	 * 
	 * @return <code>true</code> if we group
	 */
	public boolean useBusinessFamily() {
		return _busnFamilyCB.isSelected();
	}

	/**
	 * Check if we group certain biology majors together
	 * 
	 * @return <code>true</code> if we group
	 */
	public boolean useBioFamily() {
		return _bioFamilyCB.isSelected();
	}

	/**
     * Check if we group certain chemistry related majors together
     * 
     * @return <code>true</code> if we group
     */
	public boolean useChemFamily() {
		return _chemFamilyCB.isSelected();
	}
	
	/**
	 * Check if we group certain engineering majors together
	 * 
	 * @return <code>true</code> if we group
	 */
	public boolean useEngFamily() {
		return _engrFamilyCB.isSelected();
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
