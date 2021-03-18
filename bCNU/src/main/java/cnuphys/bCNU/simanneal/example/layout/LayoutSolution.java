package cnuphys.bCNU.simanneal.example.layout;

import cnuphys.bCNU.simanneal.Solution;

public class LayoutSolution extends Solution {
	
	/** The boxes being laid out */
	public Box[] boxes;
	
	/** The singletons being laid out */
	public Singleton[] singletons;
	
	/** The connections */
	public Connection[] connections;


	@Override
	public double getEnergy() {
		return 0;
	}

	@Override
	public Solution getRearrangement() {
		return null;
	}

	@Override
	public Solution copy() {
		LayoutSolution copy = new LayoutSolution();
		copyBoxes(this, copy);
		
		return copy;
	}
	
	//copy boxes from source to destination
	private void copyBoxes(LayoutSolution src, LayoutSolution dest) {
		int len = 
	}

	@Override
	public double getPlotY() {
		return 0;
	}
	
	/**
	 * Generate a random solution
	 * @return a random starting solution
	 */
	public static Solution randomSolution() {
		
		LayoutSolution solution = new LayoutSolution();
		//create a model
		solution.boxes = new Box[4];
		solution.boxes[0] = new Box(6);
		solution.boxes[1] = new Box(8);
		solution.boxes[2] = new Box(7);
		solution.boxes[3] = new Box(2);

		solution.singletons = new Singleton[7];
		for (int i = 0; i < solution.singletons.length; i++) {
			solution.singletons[i] = new Singleton();
		}
		
		solution.connections = new Connection[10];
		solution.connections[0] = new Connection(solution.singletons[0], solution.singletons[1]);
		solution.connections[1] = new Connection(solution.singletons[1], solution.singletons[2]);
		solution.connections[2] = new Connection(solution.singletons[2], solution.singletons[3]);
		solution.connections[3] = new Connection(solution.singletons[2], solution.singletons[4]);
		solution.connections[4] = new Connection(solution.singletons[2], solution.singletons[5]);
		solution.connections[5] = new Connection(solution.singletons[2], solution.singletons[6]);
		solution.connections[6] = new Connection(solution.singletons[3], solution.boxes[0]);
		solution.connections[7] = new Connection(solution.singletons[4], solution.boxes[1]);
		solution.connections[8] = new Connection(solution.singletons[5], solution.boxes[2]);
		solution.connections[9] = new Connection(solution.singletons[6], solution.boxes[3]);
		

		return solution;
	}

}
