package cnuphys.fastMCed.graphics;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.SwingConstants;

public class SectorSelector extends JPanel {
    private final ButtonGroup group = new ButtonGroup();
    private ActionListener selectionChangeListener;

	/**
	 * Create a sector selector
	 * 
	 * @param font        the font to use
	 * @param orientation the orientation of the selector
	 */
    public SectorSelector(Font font, int orientation) {
        // Determine the layout based on orientation
        setLayout(new BoxLayout(this, orientation == SwingConstants.HORIZONTAL ? BoxLayout.X_AXIS : BoxLayout.Y_AXIS));

        boolean isFirstButton = true;
        
        // Create and add radio buttons for each sector
        for (Sector sector : Sector.values()) {
            if (!isFirstButton) {
                add(createSpacer(orientation, 10));
            } else {
                isFirstButton = false;
            }

            JRadioButton button = new JRadioButton(sector.getName());
            button.setActionCommand(sector.getName());
            button.setFont(font);
            if (sector == Sector.SECTOR1) {
                button.setSelected(true); // Default selection
            }
            
            // Add action listener to each radio button to handle selection changes
            button.addActionListener(e -> {
                if (selectionChangeListener != null) {
                    selectionChangeListener.actionPerformed(e);
                }
            });
            
            group.add(button);
            add(button);
        }
    }
    
    // Add a spacer between each radio button
    private Component createSpacer(int orientation, int space) {
        // Create a spacer component
        Component spacer = Box.createRigidArea(new Dimension(orientation == SwingConstants.HORIZONTAL ? space : 0, orientation == SwingConstants.VERTICAL ? space : 0));
        return spacer;
    }


	/**
	 * Get the selected sector
	 * 
	 * @return the selected sector
	 */
    public Sector getSelectedSector() {
        return Sector.valueOf(group.getSelection().getActionCommand());
    }

    /**
     * Add an action listener to handle selection changes
     * @param listener the action listener
     */
    public void addSelectionChangeListener(ActionListener listener) {
        this.selectionChangeListener = listener;
    }
}
