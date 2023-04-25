package cnuphys.advisors.checklist;

import java.awt.Dimension;

import javax.swing.JPanel;

import cnuphys.advisors.simulation.AdvisorSimulation;
import cnuphys.bCNU.graphics.component.CommonBorder;

public class CheckList extends JPanel {
	
	private static int HEIGHT = 300;
	
	private AdvisorSimulation _simulation;
	
	public CheckList(AdvisorSimulation simulation) {
		_simulation = simulation;
		setBorder(new CommonBorder("Checklist"));
	}
	
	@Override
	public Dimension getPreferredSize() {
		Dimension d = super.getPreferredSize();
		d.height = HEIGHT;
		return d;
	}

}
