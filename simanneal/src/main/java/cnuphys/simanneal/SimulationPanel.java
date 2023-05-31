package cnuphys.simanneal;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import cnuphys.bCNU.attributes.AttributePanel;
import cnuphys.bCNU.graphics.component.CommonBorder;
import cnuphys.bCNU.util.Fonts;

/**
 * This panel will display the attributes for the simulation, the run and reset
 * buttons, and a plot
 *
 * @author heddle
 *
 */
public class SimulationPanel extends JPanel implements ActionListener, IUpdateListener {

	// the underlying simulation
	private Simulation _simulation;

	// the content (display_ component
	private JComponent _content;
	
	//some more (optional) content
	private JComponent _content2;

	// the attribute panel
	private AttributePanel _attributePanel;

	private JLabel _stateLabel;

	private SimulationPlot _simPlot;

	// the buttons
	private JButton runButton;
	private JButton stopButton;
	private JButton pauseButton;
	private JButton resumeButton;
	private JButton resetButton;
	
	//holds the buttons
	private JPanel _buttonPanel;
	
	
	/**
	 * Create a panel to hold all the optics for the simulation
	 *
	 * @param simulation the simulation
	 * @param content    the custom content, e.g. a map for the traveling
	 *                   salesperson problem
	 */
	public SimulationPanel(Simulation simulation, int preferredHeight, JComponent content) {
		this(simulation, preferredHeight, content, null);
	}

	/**
	 * Create a panel to hold all the optics for the simulation
	 *
	 * @param simulation the simulation
	 * @param content    the custom content, e.g. a map for the traveling
	 *                   salesperson problem
	 */
	public SimulationPanel(Simulation simulation, int preferredHeight, JComponent content, JComponent content2) {
		setLayout(new BorderLayout(4, 4));
		_simulation = simulation;
		_simulation.addUpdateListener(this);
		_content = content;
		_content2 = content2;
		
		JPanel leftP = insetPanel();
		leftP.add(_content, BorderLayout.CENTER);
		
		leftP.add(Box.createHorizontalStrut(80), BorderLayout.WEST);
		add(leftP, BorderLayout.WEST);

		addEast(preferredHeight);
		addCenter();

	}

	/**
	 * Get the simulation plot
	 *
	 * @return the simulation plot
	 */
	public SimulationPlot getSimulationPlot() {
		return _simPlot;
	}

	private JPanel insetPanel() {
		JPanel panel = new JPanel() {
			@Override
			public Insets getInsets() {
				Insets def = super.getInsets();
				return new Insets(def.top + 2, def.left + 2, def.bottom + 2, def.right + 2);
			}

		};
		panel.setLayout(new BorderLayout(4, 4));
		return panel;
	}
	
	private JPanel insetVPanel() {
		JPanel panel = new JPanel() {
			@Override
			public Insets getInsets() {
				Insets def = super.getInsets();
				return new Insets(def.top + 2, def.left + 2, def.bottom + 2, def.right + 2);
			}

		};
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		return panel;
	}


	// put the sim plot in the center
	private void addCenter() {
		JPanel panel = insetPanel();

		_simPlot = new SimulationPlot(_simulation);

		panel.setLayout(new BorderLayout(4, 4));
		panel.add(_simPlot, BorderLayout.CENTER);

		add(panel, BorderLayout.CENTER);
	}

	
	private JPanel _eastPanel;
	private JPanel _contentPanel;
	// add the east panel
	private void addEast(int preferredHeight) {
		_eastPanel = insetVPanel();

		// state label in north
		_stateLabel = new JLabel("State:            ");
		_eastPanel.add(_stateLabel);

		// attributes in center of east panel
		_attributePanel = new AttributePanel(_simulation.getAttributes(), preferredHeight);

		_contentPanel = insetVPanel();
		_contentPanel.add(_attributePanel);
		
		if (_content2 != null) {
			_contentPanel.add(_content2);
		}
		
		_eastPanel.add(_contentPanel);

		// buttons in south of east panel
		_buttonPanel = new JPanel();
		_buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 6, 2));
		runButton = makeButton("Run");
		stopButton = makeButton("Stop");
		pauseButton = makeButton("Pause");
		resumeButton = makeButton("Resume");
		resetButton = makeButton("Reset");

		_buttonPanel.add(runButton);
		_buttonPanel.add(pauseButton);
		_buttonPanel.add(resumeButton);
		_buttonPanel.add(resetButton);
		_buttonPanel.add(stopButton);
		_eastPanel.add(_buttonPanel);

		add(_eastPanel, BorderLayout.EAST);
		fixPanelState();
	}
	
	/**
	 * Remove the button panel
	 */
	public void buttonPanelRemove() {
		_eastPanel.remove(_buttonPanel);
		revalidate();
	}
	
	/**
	 * Remove the state label
	 */
	public void stateLabelRemove() {
		_eastPanel.remove(_stateLabel);
		revalidate();
	}

	
	/**
	 * Remove the attribute table
	 */
	public void attributeTableRemove() {
		_contentPanel.remove(_attributePanel);
		revalidate();
	}


	// create a button
	private JButton makeButton(String label) {
		JButton button = new JButton(label);
		button.addActionListener(this);
		button.setFont(Fonts.smallFont);
		return button;
	}

	// fix the states of the buttons
	private void fixPanelState() {
		SimulationState state = _simulation.getSimulationState();
		_stateLabel.setText("State: " + state);

		switch (state) {
		case RUNNING:
			runButton.setEnabled(false);
			pauseButton.setEnabled(true);
			resumeButton.setEnabled(false);
			resetButton.setEnabled(false);
			stopButton.setEnabled(true);
			break;

		case PAUSED:
			runButton.setEnabled(false);
			pauseButton.setEnabled(false);
			resumeButton.setEnabled(true);
			resetButton.setEnabled(false);
			stopButton.setEnabled(true);
			break;

		case STOPPED:
			runButton.setEnabled(true);
			pauseButton.setEnabled(false);
			resumeButton.setEnabled(false);
			resetButton.setEnabled(true);
			stopButton.setEnabled(false);
			break;
		}
	}

	@Override
	public Insets getInsets() {
		Insets def = super.getInsets();
		return new Insets(def.top + 2, def.left + 2, def.bottom + 2, def.right + 2);
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		Object source = e.getSource();

		if (source == runButton) {
			doRun();
		} else if (source == pauseButton) {
			doPause();
		} else if (source == resumeButton) {
			doResume();
		} else if (source == resetButton) {
			doReset();
		} else if (source == stopButton) {
			doStop();
		}
	}
	
	/**
	 * Same as hitting the run button
	 */
	public void doRun() {
		_simulation.setSimulationState(SimulationState.RUNNING);
		_simulation.startSimulation();
	}

	/**
	 * Same as hitting the pause button
	 */
	public void doPause() {
		_simulation.setSimulationState(SimulationState.PAUSED);
	}
	
	/**
	 * Same as hitting the resume button
	 */
	public void doResume() {
		_simulation.setSimulationState(SimulationState.RUNNING);
	}

	/**
	 * Same as hitting the reset button
	 */
	public void doReset() {
		_simulation.reset();
	}

	/**
	 * Same as hitting the stop button
	 */
	public void doStop() {
		_simulation.setSimulationState(SimulationState.STOPPED);
	}


	@Override
	public void updateSolution(Simulation simulation, Solution newSolution, Solution oldSolution) {
	}

	@Override
	public void reset(Simulation simulation) {
	}

	@Override
	public void stateChange(Simulation simulation, SimulationState oldState, SimulationState newState) {
		fixPanelState();
	}

}
