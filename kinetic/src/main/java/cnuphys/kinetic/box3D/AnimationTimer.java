package cnuphys.kinetic.box3D;

import javax.swing.Timer;
import javax.swing.JComponent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AnimationTimer extends Timer {
    private JComponent component;
    private int initialDelay;

    /**
     * Constructs an AnimationTimer.
     *
     * @param duration the initial delay in milliseconds between timer events.
     * @param component the JComponent to be repainted on each timer tick.
     */
    public AnimationTimer(int duration, JComponent component) {
        super(duration, null);
        this.component = component;
        this.initialDelay = duration;
        this.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                component.repaint();  // Redraw the component
            }
        });
    }

    /**
     * Pauses the timer.
     */
    public void pause() {
        this.stop();
    }

    /**
     * Restarts the timer using the current delay setting.
     */
    public void restartTimer() {
        this.start();
    }

    /**
     * Adjusts the duration of the timer.
     *
     * @param newDuration the new delay in milliseconds between timer events.
     */
    public void adjustDuration(int newDuration) {
        this.setDelay(newDuration);
        this.setInitialDelay(newDuration);  // Adjust initial delay if needed
    }

    /**
     * Returns the initial delay set during the creation of the timer.
     *
     * @return the initial delay in milliseconds.
     */
    public int getInitialDelay() {
        return initialDelay;
    }
}
