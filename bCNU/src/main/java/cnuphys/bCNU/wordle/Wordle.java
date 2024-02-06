package cnuphys.bCNU.wordle;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Wordle extends JDialog {

    public Wordle(Frame parent) {
        super(parent, "Wordle", false); // Modeless dialog
        initializeComponents();
    }

    private void initializeComponents() {
        // Set dialog properties
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        setResizable(false);
        
        CustomKeyListener kl = new CustomKeyListener();
        addKeyListener(kl);


        // Create and configure the center component
        JComponent centerComponent = new LetterGrid();
        centerComponent.addKeyListener(kl);
        centerComponent.setFocusable(true); // To receive keyboard events
        add(centerComponent, BorderLayout.CENTER);

        // Create and add the south text area
        JTextArea southTextArea = new JTextArea(2, 20);
        southTextArea.setEditable(false);
        southTextArea.addKeyListener(kl);

        add(southTextArea, BorderLayout.SOUTH);

        // Add window listener
        addWindowListener(new CustomWindowListener());

        // Pack the components
        pack();
        setLocationRelativeTo(null); // Center on screen
    }


    private class CustomKeyListener extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
        	System.out.println("key pressed: " + e.getKeyChar());
			if (e.getKeyCode() == KeyEvent.VK_ENTER) {
				
			}
      }
    }

    private class CustomWindowListener extends WindowAdapter {
        @Override
        public void windowClosing(WindowEvent e) {
            // Handle window closing event here
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Frame parent = new JFrame();
            Wordle dialog = new Wordle(parent);
            dialog.setVisible(true);
        });
    }
}
