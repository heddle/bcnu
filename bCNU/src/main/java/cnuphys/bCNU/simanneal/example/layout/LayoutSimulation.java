package cnuphys.bCNU.simanneal.example.layout;

import java.awt.BorderLayout;
import java.awt.Rectangle;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import cnuphys.bCNU.attributes.Attributes;
import cnuphys.bCNU.simanneal.Simulation;
import cnuphys.bCNU.simanneal.Solution;

public class LayoutSimulation extends Simulation {

	/** width of the active layout bounds */
	public static final int width = 1000;
	
	/** height of the active layout bounds */
	public static final int height = 800;
	
	/** the size of the layout bounds */
	public static final Rectangle bounds = new Rectangle(width, height);

	/** the icon size */
	public static final int size = 48;

	/** a pixel gap */
	public static final int gap = 48;
	
	/** random number generator */
	public static Random random = new Random(53197711);

	@Override
	protected void setInitialAttributes(Attributes attributes) {
	}

	@Override
	protected Solution setInitialSolution() {
		return null;
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

		LayoutSimulation simulation = new LayoutSimulation();

		LayoutPanel tsPanel = new LayoutPanel(simulation);

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
