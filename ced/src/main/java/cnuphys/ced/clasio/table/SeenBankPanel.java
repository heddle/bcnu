package cnuphys.ced.clasio.table;

import javax.swing.*;

import cnuphys.bCNU.util.Fonts;
import cnuphys.ced.alldata.DataWarehouse;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.List;

public class SeenBankPanel extends JPanel {
    private JList<String> bankList;
    private DefaultListModel<String> listModel;
    
    private static final int WIDTH = 170;

	public SeenBankPanel() {
		// Initialize the list model and JList
		listModel = new DefaultListModel<>();
		bankList = new JList<>(listModel);
		bankList.setFont(Fonts.smallFont);

		JLabel label = new JLabel("Seen Banks");
		label.setHorizontalAlignment(SwingConstants.CENTER);
		label.setFont(Fonts.defaultFont);
		label.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEtchedBorder(),
				BorderFactory.createEmptyBorder(5, 5, 5, 5) // top, left, bottom, right spacing
		));

		// Wrap the JList in a JScrollPane for scrolling as needed
		JScrollPane scrollPane = new JScrollPane(bankList);
		// Set preferred size: width WIDTH pixels; adjust height as required
        scrollPane.setPreferredSize(new Dimension(WIDTH, 300));
        
        // Use a BorderLayout and add the scroll pane to the center
		setLayout(new BorderLayout());
		add(label, BorderLayout.NORTH);
		add(scrollPane, BorderLayout.CENTER);
	  }
    
    /**
     * Updates the panel with the new list of banks.
     * This method should be called with the result of getSortedSeenBanks().
     *
     * @param banks the list of bank strings to display.
     */
    public void updateSeenBanks() {
        listModel.clear();
		List<String> bankCounts = DataWarehouse.getInstance().getSortedSeenBanks();

        for (String bankCount : bankCounts) {
            listModel.addElement(bankCount);
        }
    }
    
    // For demonstration, assume getSortedSeenBanks() is available from another class.
    // You could call updateBanks(SomeClass.getSortedSeenBanks()) to refresh the display.
}
