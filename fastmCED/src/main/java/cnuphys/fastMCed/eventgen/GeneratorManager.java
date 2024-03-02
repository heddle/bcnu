package cnuphys.fastMCed.eventgen;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

import cnuphys.fastMCed.eventgen.random.RandomEventGenerator;
import cnuphys.fastMCed.eventio.PhysicsEventManager;

public class GeneratorManager implements ActionListener {

	// suggestions
	private static double _pMin = 6.0; // GeV/c
	private static double _pMax = 11.5; // GeV/c
	private static double _thetaMin = 10.; // degrees
	private static double _thetaMax = 40.; // degrees
	private static double _phiMin = -20; // degrees
	private static double _phiMax = 20.; // degrees

	// menu stuff
	private JMenu _menu;
	private static JMenuItem _randomGenerator;

	// singleton
	private static GeneratorManager instance;

	// private constructor for
	private GeneratorManager() {
		RandomEventGenerator generator = RandomEventGenerator.createRandomGenerator(false);
		PhysicsEventManager.getInstance().setEventGenerator(generator);
	}

	/**
	 * Access to the singleton GeneratorManager
	 * 
	 * @return the GeneratorManager
	 */
	public static GeneratorManager getInstance() {
		if (instance == null) {
			instance = new GeneratorManager();
		}
		return instance;
	}

	/**
	 * Get the Generator menu
	 * 
	 * @return
	 */
	public JMenu getMenu() {
		if (_menu == null) {
			createMenu();
		}
		return _menu;
	}

	// create the menu
	private void createMenu() {
		_menu = new JMenu("Random Generator");
		_randomGenerator = menuItem("Ranges...");
	}

	private JMenuItem menuItem(String label) {

		JMenuItem item = new JMenuItem(label);
		item.addActionListener(this);
		_menu.add(item);

		return item;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();

        if (source == _randomGenerator) {
			RandomEventGenerator generator = RandomEventGenerator.createRandomGenerator(true);
			if (generator != null) {
				PhysicsEventManager.getInstance().setEventGenerator(generator);
			}
		} 
	}

	public static double getPMin() {
		return _pMin;
	}

	public static void setpMin(double pMin) {
		GeneratorManager._pMin = pMin;
	}

	public static double getPMax() {
		return _pMax;
	}

	public static void setpMax(double pMax) {
		GeneratorManager._pMax = pMax;
	}

	public static double getThetaMin() {
		return _thetaMin;
	}

	public static void setThetaMin(double thetaMin) {
		GeneratorManager._thetaMin = thetaMin;
	}

	public static double getThetaMax() {
		return _thetaMax;
	}

	public static void setThetaMax(double thetaMax) {
		GeneratorManager._thetaMax = thetaMax;
	}

	public static double getPhiMin() {
		return _phiMin;
	}

	public static void setPhiMin(double phiMin) {
		GeneratorManager._phiMin = phiMin;
	}

	public static double getPhiMax() {
		return _phiMax;
	}

	public static void setPhiMax(double phiMax) {
		GeneratorManager._phiMax = phiMax;
	}

}
