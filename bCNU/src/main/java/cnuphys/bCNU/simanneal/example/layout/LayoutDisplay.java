package cnuphys.bCNU.simanneal.example.layout;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

import cnuphys.bCNU.simanneal.SimulationDisplay;

public class LayoutDisplay extends SimulationDisplay {
	
	//layout bounds
	protected static Rectangle _bounds = LayoutSimulation.bounds;
	
	public LayoutDisplay(LayoutSimulation simulation) {
		super(simulation);
	}


	@Override
	public void paintComponent(Graphics g) {
		LayoutSolution solution = (LayoutSolution) _simulation.currentSolution();
		if (solution == null) {
			return;
		}
		
		
		g.setColor(Color.white);
		g.fillRect(_bounds.x, _bounds.y, _bounds.width, _bounds.height);

		g.setColor(Color.black);
		g.drawRect(_bounds.x, _bounds.y, _bounds.width, _bounds.height);
		
		
		solutionDraw(g, solution);

	}
	
	private void solutionDraw(Graphics g, LayoutSolution solution) {
		
		for (Connection cnx : solution.connections) {
			cnx.draw(g);
		}
		
		
		for (Box box : solution.boxes) {
			box.draw(g);
		}
		
		for (Singleton singleton : solution.singletons) {
			singleton.draw(g);
		}

	}

}
