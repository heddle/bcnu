package cnuphys.kinetic.frame;

import javax.swing.JFrame;
import javax.swing.JPanel;

import cnuphys.kinetic.box3D.Box3D;
import cnuphys.splot.plot.GraphicsUtilities;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Kinetic extends JFrame {

    public Kinetic() {
        super("Kinetic"); // Set the title of the JFrame
        initializeLayout();
        setupComponents();
        GraphicsUtilities.centerComponent(this);
    }

    private void initializeLayout() {
        this.setSize(getScreenSize(0.8, 0.8)); // Set size to 80% of screen size
        this.setLayout(new BorderLayout()); // Set the layout manager
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0); // Exit the application when window is closed
            }
        });
    }

    private Dimension getScreenSize(double widthPercent, double heightPercent) {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int width = (int) (screenSize.width * widthPercent);
        int height = (int) (screenSize.height * heightPercent);
        return new Dimension(width, height);
    }

    private void setupComponents() {
        addEast();
        addWest();
        addCenter();
        addNorth();
        addSouth();
    }

    private void addEast() {
        // Add components to the East region
        this.add(new JPanel(), BorderLayout.EAST);
    }

    private void addWest() {
        // Add components to the West region
        this.add(new JPanel(), BorderLayout.WEST);
    }

    private void addCenter() {
        // Add components to the Center region
        this.add(new Box3D(), BorderLayout.CENTER);
    }

    private void addNorth() {
        // Add components to the North region
        this.add(new JPanel(), BorderLayout.NORTH);
    }

    private void addSouth() {
        // Add components to the South region
        this.add(new JPanel(), BorderLayout.SOUTH);
    }

    public static void main(String[] args) {
        Kinetic kineticFrame = new Kinetic();
        kineticFrame.setVisible(true);
    }
}
