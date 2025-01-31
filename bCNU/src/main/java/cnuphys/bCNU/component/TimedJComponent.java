package cnuphys.bCNU.component;

import java.awt.Graphics;

import javax.swing.JComponent;
import javax.swing.Timer;

public class TimedJComponent extends JComponent {

    private boolean skipRepaint = true;
    private boolean repaintPending = false;

    public TimedJComponent(int millis) {
        Timer timer = new Timer(millis, e -> {
            if (repaintPending) {
                skipRepaint = false;  // Allow next repaint
                repaintPending = false;
                repaint(); // Request repaint
            }
        });
        timer.setRepeats(true);
        timer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        if (skipRepaint) {
            repaintPending = true;
            return; // Skip rendering if not allowed
        }
        skipRepaint = true; // Prevents unnecessary repaints
        super.paintComponent(g); // Ensure background clears properly
        customDrawing(g);
    }

    /**
     * Override this method to provide custom rendering logic.
     */
    protected void customDrawing(Graphics g) {
        // To be overridden by subclasses
    }
}
