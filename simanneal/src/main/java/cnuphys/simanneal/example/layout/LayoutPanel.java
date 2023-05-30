package cnuphys.simanneal.example.layout;

import java.awt.Dimension;
import java.awt.Insets;

import javax.swing.JPanel;

import cnuphys.simanneal.SimulationPanel;

public class LayoutPanel extends JPanel {


	// Simulation panel for display
	private LayoutDisplay _layoutDisplay;

	// the simulation panel
	private SimulationPanel _simPanel;

	// the simulation
	private LayoutSimulation _simulation;

	public LayoutPanel(LayoutSimulation simulation) {

		_simulation = simulation;

		_layoutDisplay = new LayoutDisplay(_simulation);
		_layoutDisplay.setPreferredSize(new Dimension(LayoutSimulation.width, LayoutSimulation.height));
		_simPanel = new SimulationPanel(_simulation, 0, _layoutDisplay);
		add(_simPanel);
	}

	@Override
	public Insets getInsets() {
		Insets def = super.getInsets();
		return new Insets(def.top + 2, def.left + 2, def.bottom + 2, def.right + 2);
	}

}
