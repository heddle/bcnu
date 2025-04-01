package cnuphys.simanneal.example.layout;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Random;

import cnuphys.bCNU.util.MathUtilities;
import cnuphys.simanneal.Solution;

public class LayoutSolution extends Solution {

	//shared random number generator
	protected static Random _rand = LayoutSimulation.random;

	// gravitational constant
	private static final double G = 1;

	//spring constant
	private static final double K = 2.0e-7;

	//out of bounds energy penalty;
	private static final double OOB = 100;

	//overlap energy penalty;
	private static final double OVLAP = 10;

	//wires crossing penalty
	private static final double CROSSING = 1;

	//wire under a pr penalty
	private static final double UNDER = 1;

	//the probability to switch if there is a crossing
	private static final double SWITCHPROB = 0.1;


	/** The boxes being laid out */
	public Box[] boxes;

	/** The singletons being laid out */
	public Singleton[] singletons;

	/** The connections */
	public Connection[] connections;


	//combine boxes and singletons
	private PositionedRectangle[] _everything;

	//the total energy
	private double _totalEnergy;

	//holds wire crossings
	public ArrayList<Crossing> crossings = new ArrayList<>();

	//holds wire crossings
	public ArrayList<Under> unders = new ArrayList<>();


	//combine boxes and singletons for convenience
	private void getEverything() {
		int len1 = (boxes == null) ? 0 : boxes.length;
		int len2 = (singletons == null) ? 0 : singletons.length;

		int len = len1 + len2;

		if (len > 0) {
			_everything = new PositionedRectangle[len];

			int index = 0;

			for (int i = 0; i < len1; i++) {
				_everything[index] = boxes[i];
				index++;
			}

			for (int i = 0; i < len2; i++) {
				_everything[index] = singletons[i];
				index++;
			}


		}
	}

	//get all the unders. These are cases where a connection
	//cuts through an icon or box. Cond=sidered bad!
	public void getUnders() {
		unders.clear();
		int lenC = (connections == null) ? 0 : connections.length;
		int lenE = (_everything == null) ? 0 : _everything.length;
		Point2D.Double u1 = new Point2D.Double();
		Point2D.Double u2 = new Point2D.Double();

		for (int i = 0; i < lenC; i++) {
			Connection cnx1 = connections[i];
			Point2D.Double p1 = cnx1.pr1.getPosition();
			Point2D.Double p2 = cnx1.pr2.getPosition();

			for (int j = 0; j < lenE; j++) {
				PositionedRectangle pr = _everything[j];

				if (MathUtilities.segmentCutsRectangle(p1, p2, pr.x, pr.y,
						pr.width, pr.height, u1, u2)) {

					Under under = new Under(cnx1.pr1, cnx1.pr2, pr, u1.x, u1.y, u2.x, u2.y);
					unders.add(under);
				}
			}
		}
	}


	//get all the crossings. These are where connection lines cross.
	//considered very bad.
	public void getCrossings() {
		crossings.clear();

		int len = (connections == null) ? 0 : connections.length;
		Point2D.Double u = new Point2D.Double();

		//dreaded double loop
		for (int i = 0; i < len-1; i++) {
			Connection cnx1 = connections[i];
			Point2D.Double p1 = cnx1.pr1.getPosition();
			Point2D.Double p2 = cnx1.pr2.getPosition();

			for (int j = i+1; j < len; j++) {
				Connection cnx2 = connections[j];
				Point2D.Double q1 = cnx2.pr1.getPosition();
				Point2D.Double q2 = cnx2.pr2.getPosition();



				if (MathUtilities.segmentCrossing(p1, p2, q1, q2, u)) {
					Crossing crossing = new Crossing(cnx1.pr1, cnx1.pr2, cnx2.pr1, cnx2.pr2, u.x, u.y);
					crossings.add(crossing);
				}
			}
		}

	}

	@Override
	public double getEnergy() {
		double spring = 0;
		double grav = 0;
		double oob = 0;
		double ovlap = 0;
		double crossing = 0;
		double under = 0;

		getUnders();
		under = UNDER*unders.size();

		getCrossings();
		crossing = CROSSING*crossings.size();


		int len = (_everything == null) ? 0 : _everything.length;

		for (int i = 0; i < len; i++) {
			PositionedRectangle pr = _everything[i];
			oob += outOfBoundsEnergy(pr);
		}

		//dreaded double loop
		for (int i = 0; i < len-1; i++) {
			PositionedRectangle pr1 = _everything[i];
			for (int j = i+1; j < len; j++) {
				PositionedRectangle pr2 = _everything[j];

				double distance = pr1.distance(pr2);

				grav += gravitationalEnergy(pr1, pr2, distance);
				spring += springEnergy(pr1, pr2, distance);

				if (pr1.overlaps(pr2)) {
					ovlap += OVLAP;
				}
			}
		}

		_totalEnergy = under + crossing + oob + ovlap + grav + spring;

		return _totalEnergy;
	}

	@Override
	public Solution getRearrangement() {

		LayoutSolution soln = (LayoutSolution) copy();

		int len = (soln._everything == null) ? 0 : soln._everything.length;

		//offset everybody by a small random amount
		for (int i = 0; i < len; i++) {
			double dx = (_rand.nextDouble() - 0.5) * 20;
			double dy = (_rand.nextDouble() - 0.5) * 20;
			soln._everything[i].offset(dx, dy);
		}

		//find all the crossings and potentially switch locations to uncross
		getCrossings();
		for (Crossing crossing : crossings) {
			if (_rand.nextDouble() < SWITCHPROB) {
				Singleton s1 = crossing.getSingleton1();
				Singleton s2 = crossing.getSingleton2();

				if ((s1 == null) || (s2 == null) || (s1 == s2)) {
					System.err.println("Something impossible happened.");
					System.exit(1);
				}

				PositionedRectangle.swapPosition(s1, s2);
			}
		}


		return soln;
	}

	//gravitational energy
	private double gravitationalEnergy(PositionedRectangle pr1, PositionedRectangle pr2, double distance) {
		return G*pr1.mass*pr2.mass/distance;
	}

	//spring energy
	private double springEnergy(PositionedRectangle pr1, PositionedRectangle pr2, double distance) {
		return 0.5*K*distance*distance;
	}


	//energy penalty for being out of bounds
	private double outOfBoundsEnergy(PositionedRectangle pr) {
		return pr.outOfBounds() ? OOB : 0;
	}

	@Override
	public Solution copy() {
		LayoutSolution copy = new LayoutSolution();
		copyBoxes(this, copy);
		copySingletons(this, copy);

		copy.getEverything();

		copyConnections(this, copy);

		return copy;
	}

	//copy boxes from source to destination
	private void copyBoxes(LayoutSolution src, LayoutSolution dest) {
		int len = (src.boxes == null ? 0 : src.boxes.length);

		if (len > 0) {
			dest.boxes = new Box[len];

			for (int i = 0; i < len; i++) {
				dest.boxes[i] = (Box) src.boxes[i].copy();
			}
		}
	}

	//copy singletons from source to destination
	private void copySingletons(LayoutSolution src, LayoutSolution dest) {
		int len = (src.singletons == null ? 0 : src.singletons.length);

		if (len > 0) {
			dest.singletons = new Singleton[len];

			for (int i = 0; i < len; i++) {
				dest.singletons[i] = (Singleton) src.singletons[i].copy();
			}
		}
	}

	//copy connections from source to destination
	private void copyConnections(LayoutSolution src, LayoutSolution dest) {
		int len = (src.connections == null ? 0 : src.connections.length);

		if (len > 0) {

			dest.connections = new Connection[len];

			for (int i = 0; i < len; i++) {
				Connection srcCnx = src.connections[i];
				PositionedRectangle pr1 = dest.findById(srcCnx.pr1.id);
				PositionedRectangle pr2 = dest.findById(srcCnx.pr2.id);

				if ((pr1 == null) || (pr2 == null)) {
					System.err.println("Something impossible has happened.");
					System.exit(1);
				}

				dest.connections[i] = new Connection(pr1, pr2);
			}

		}

	}

	/**
	 * Find a box by id
	 * @param id the id
	 * @return the box
	 */
	public PositionedRectangle findById(int id) {
		if (_everything != null) {
			for (PositionedRectangle pr : _everything) {
				if (pr.id == id) {
					return pr;
				}
			}
		}
		else {
			System.err.println("everything is null. That's rarely a good sign.");
			System.exit(1);
		}

		return null;
	}

	@Override
	public double getPlotY() {
		return _totalEnergy;
	}

	private void writeConnections() {
		if (connections != null) {
			for (Connection cnx : connections) {
				System.err.println(String.format("Connection from %d to %d", cnx.pr1.id, cnx.pr2.id));
			}
		}
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

		solution.getEverything();


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

		System.err.println("Random Solution");
		solution.writeConnections();

		return solution;
	}

}
