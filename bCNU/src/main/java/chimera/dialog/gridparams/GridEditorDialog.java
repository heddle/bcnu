package chimera.dialog.gridparams;
import javax.swing.*;
import javax.swing.event.EventListenerList;
import javax.swing.table.DefaultTableModel;

import chimera.grid.CartesianGrid;
import chimera.grid.ChimeraGrid;
import chimera.grid.IGridChangeListener;
import chimera.grid.SphericalGrid;
import cnuphys.bCNU.dialog.SimpleDialog;

import java.awt.*;

public class GridEditorDialog extends SimpleDialog {
    private CartesianGrid cartesianGridCopy;
    private SphericalGrid sphericalGridCopy;
    
	// List of grid change listeners
	private EventListenerList _listenerList;

    //the overall grid
    private ChimeraGrid grid;
    
    //the grid copy
    private ChimeraGrid gridCopy;
    
    //the grid param table
    private GridTable _table;


    public GridEditorDialog(Frame owner, ChimeraGrid grid) {
        super("Grid Parameters", false, "OK", "Cancel"); // Modeless dialog
        this.grid = grid;
        reset();

        pack();
        setLocationRelativeTo(null);
    }
    
    
    private void reset() {
        //edit copies
        this.cartesianGridCopy = new CartesianGrid(grid.getCartesianGrid()); // Make a copy
        this.sphericalGridCopy = new SphericalGrid(grid.getSphericalGrid()); // Make a copy);

        gridCopy = new ChimeraGrid(cartesianGridCopy, sphericalGridCopy);

    }
    
    @Override
	protected JComponent createNorthComponent() {
		return new JLabel("Distances are in radii, angles in degrees.");
	}
    
    @Override
        protected JComponent createCenterComponent() {
    	  JPanel panel = new JPanel() {
  			@Override
  			public Insets getInsets() {
  				Insets def = super.getInsets();
  				return new Insets(def.top + 2, def.left + 2, def.bottom + 2, def.right + 2);
  			}

    	  };
    	  
    	  panel.setLayout(new BorderLayout(6,6));
    	  
    	  _table = new GridTable();
    	 panel.add(new JScrollPane(_table), BorderLayout.CENTER);
    	 
    	 return panel;
    }
    
    
    /**
     * Get the grid copy (being edited)
     * @return the grid copy
     */
	public ChimeraGrid getGridCopy() {
		return gridCopy;
	}

	//user hit OK
    private void handleOK() {
		System.err.println("Hit OK");
		// Update the grid
		grid.setCartesianGrid(cartesianGridCopy);
		grid.setSphericalGrid(sphericalGridCopy);
		notifyListeners();
	}
    
	@Override
	public void handleCommand(String command) {
		reason = command;
		if (command.equals("OK")) {
			handleOK();
		}
		else {
			System.err.println("Hit Cancel");
			reset();
		}
		setVisible(false);
	}
	
	/**
	 * Add a grid change listener
	 *
	 * @param gridChangeListener the listener to add
	 */
	public void addGridChangeListener(IGridChangeListener gridChangeListener) {

		if (_listenerList == null) {
			_listenerList = new EventListenerList();
		}

		// avoid adding duplicates
		_listenerList.remove(IGridChangeListener.class, gridChangeListener);
		_listenerList.add(IGridChangeListener.class, gridChangeListener);

	}


	/**
	 * Notify all listeners that a change has occurred in the grid
	 */
	protected void notifyListeners() {

		if (_listenerList == null) {
			return;
		}

		// Guaranteed to return a non-null array
		Object[] listeners = _listenerList.getListenerList();

		for (int i = 0; i < listeners.length; i += 2) {
			if (listeners[i] == IGridChangeListener.class) {
				IGridChangeListener listener = (IGridChangeListener) listeners[i + 1];
				listener.gridChanged();
			}

		}
	}


}

