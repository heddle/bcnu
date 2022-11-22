package cnuphys.simanneal.example.ising2D;

import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import cnuphys.bCNU.attributes.Attributes;
import cnuphys.simanneal.Simulation;
import cnuphys.simanneal.Solution;

public class Ising2DSimulation extends Simulation {

	// custom attributes
	public static final String NUMROWS = "num rows";
	public static final String NUMCOLUMNS = "num columns";

	private Ising2DSolution _i2dSolution;

	@Override
	protected Solution setInitialSolution() {
		_i2dSolution = new Ising2DSolution(this);
		return _i2dSolution;
	}

	/**
	 * Get the number of rows in the current simulation
	 * 
	 * @return the number of rows
	 */
	public int getNumRows() {
		return _i2dSolution.getNumRows();
	}

	/**
	 * Get the number of columns in the current simulation
	 * 
	 * @return the number of columns
	 */
	public int getNumColumns() {
		return _i2dSolution.getNumColumns();
	}

	@Override
	protected void setInitialAttributes() {

		// change some defaults
		_attributes.setPlotTitle("2D Ising Model");
		_attributes.setYAxisLabel("|Magnetization|");
		_attributes.setXAxisLabel("Temp");
		_attributes.setUseLogTemp(false);
		_attributes.setCoolRate(0.002);
		_attributes.setThermalizationCount(2000);
		_attributes.setMaxSteps(10000);

		// add custom attributes
		_attributes.add(NUMROWS, 100);
		_attributes.add(NUMCOLUMNS, 100);

	}

	// main program for testing
	public static void main(String arg[]) {

		final JFrame frame = new JFrame();

		// set up what to do if the window is closed
		WindowAdapter windowAdapter = new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent event) {
				System.exit(1);
			}
		};

		frame.addWindowListener(windowAdapter);

		frame.setLayout(new BorderLayout());

		Ising2DSimulation simulation = new Ising2DSimulation();

		Ising2DPanel tsPanel = new Ising2DPanel(simulation);

		frame.add(tsPanel, BorderLayout.CENTER);

		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				frame.pack();
				frame.setVisible(true);
				frame.setLocationRelativeTo(null);
			}
		});
	}

}
