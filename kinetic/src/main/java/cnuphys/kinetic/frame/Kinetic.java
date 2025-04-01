package cnuphys.kinetic.frame;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import cnuphys.kinetic.box3D.Box3D;
import cnuphys.splot.plot.GraphicsUtilities;

public class Kinetic extends JFrame {

	//a shared random number generator
	public static Random random = new Random();

	/**
	 * Create a new Kinetic frame
	 */
	public Kinetic() {
		super("Kinetic"); // Set the title of the JFrame
		initializeLayout();
		setupComponents();
		GraphicsUtilities.centerComponent(this);
	}

	// Initialize the layout of the JFrame
	private void initializeLayout() {
		this.setSize(getScreenSize(0.8, 0.8)); // Set size to 80% of screen size
		this.setLayout(new BorderLayout()); // Set the layout manager
		this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				System.exit(0); // Exit the application when window is closed
			}
		});
	}

	// Get the screen size based on the width and height percentages
	private Dimension getScreenSize(double widthPercent, double heightPercent) {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int width = (int) (screenSize.width * widthPercent);
		int height = (int) (screenSize.height * heightPercent);
		return new Dimension(width, height);
	}

	// Setup the components of the JFrame
	private void setupComponents() {
		addEast();
		addWest();
		addCenter();
		addNorth();
		addSouth();
	}

	// Add components to the East region
	private void addEast() {
		this.add(new JPanel(), BorderLayout.EAST);
	}

	// Add components to the West region
	private void addWest() {
		this.add(new JPanel(), BorderLayout.WEST);
	}

	// Add components to the Center region
	private void addCenter() {
		//the main components is a Box3D
		this.add(new Box3D(), BorderLayout.CENTER);
	}

	// Add components to the North region
	private void addNorth() {
		this.add(new JPanel(), BorderLayout.NORTH);
	}

	// Add components to the South region
	private void addSouth() {
		this.add(new JPanel(), BorderLayout.SOUTH);
	}

	// Main method to run the application
	// command args are ignored
	public static void main(String[] args) {

		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				Kinetic kineticFrame = new Kinetic();
				kineticFrame.setVisible(true);
			}
		});

	}
}
