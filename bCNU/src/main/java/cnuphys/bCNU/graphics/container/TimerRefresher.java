package cnuphys.bCNU.graphics.container;

import java.awt.Component;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.swing.SwingUtilities;
import javax.swing.Timer;

public class TimerRefresher {
	
	//
	public static volatile TimerRefresher instance;
	
	    private final Set<Component> componentsToRefresh;
	    private Timer refreshTimer;

	    private TimerRefresher() {
	        // Wrap the HashSet in a synchronized set for thread-safe access
	        componentsToRefresh = Collections.synchronizedSet(new HashSet<>());

	        // Set up the timer to repaint at 30 times per second (approximately every 33 ms)
	        refreshTimer = new Timer(1000 / 60, e -> refreshComponents());
	        refreshTimer.start();
	    }
	    
		public static TimerRefresher getInstance() {
			if (instance == null) {
				synchronized (TimerRefresher.class) {
					if (instance == null) {
						instance = new TimerRefresher();
					}
				}
			}
			return instance;
		}

	    public void refresh(Component component) {
	        // Safely add components to the set from any thread
	        SwingUtilities.invokeLater(() -> componentsToRefresh.add(component));
	    }

	    private void refreshComponents() {
	        // Create a temporary array to hold components for thread-safe iteration
	        Component[] componentsArray;

	        // Synchronize on the set while copying components to an array for iteration
	        synchronized (componentsToRefresh) {
	            componentsArray = componentsToRefresh.toArray(new Component[0]);
	            componentsToRefresh.clear();
	        }

	        // Repaint all components in the array
	        for (Component component : componentsArray) {
	            if (component != null) {
	                component.repaint();
	            }
	        }
	    }


}
