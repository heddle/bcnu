package cnuphys.advisors.graphics;

import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Rectangle;

import javax.swing.JComponent;
import javax.swing.JPanel;

import cnuphys.advisors.simulation.AdvisorSimulation;
import cnuphys.bCNU.graphics.component.CommonBorder;
import cnuphys.simanneal.SimulationPanel;
import cnuphys.simanneal.SimulationPlot;

public class AdvisorPanel extends JPanel {


	/** width of the active layout bounds */
	public static final int width = 1000;

	/** height of the active layout bounds */
	public static final int height = 800;

	/** the size of the layout bounds */
	public static final Rectangle bounds = new Rectangle(width, height);


	// the simulation panel
	private SimulationPanel _simPanel;

	// the simulation
	private AdvisorSimulation _simulation;

	public AdvisorPanel(AdvisorSimulation simulation, JComponent content) {
		_simulation = simulation;

		AdvisorDisplay.getInstance().setPreferredSize(new Dimension(width, height));
		_simPanel = new SimulationPanel(_simulation, 0, AdvisorDisplay.getInstance(), content);

		_simPanel.buttonPanelRemove();
		_simPanel.stateLabelRemove();

		add(_simPanel);
	}

	@Override
	public Insets getInsets() {
		Insets def = super.getInsets();
		return new Insets(def.top + 2, def.left + 2, def.bottom + 2, def.right + 2);
	}

	public SimulationPlot getSimulationPlot() {
		return _simPanel.getSimulationPlot();
	}


}
