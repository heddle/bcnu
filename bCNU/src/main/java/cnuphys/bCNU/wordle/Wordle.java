package cnuphys.bCNU.wordle;

import javax.swing.*;

import cnuphys.bCNU.util.Fonts;

import java.awt.*;
import java.awt.event.*;

public class Wordle extends JDialog {
	
	// Singleton
	private static volatile Wordle _instance;
	
	//where messages are sent
	private JTextArea _messageArea;

	// Singleton
    private Wordle() {
        super(new JFrame(), "Wordle", false); // Modeless dialog
        initializeComponents();
    }
    
    /**
     * Get the singleton instance
     * @return 
     */
	public static Wordle getInstance() {
		if (_instance == null) {
			_instance = new Wordle();
		}
		return _instance;
	}

	//initialize the gui components
    private void initializeComponents() {
        // Set dialog properties
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        setResizable(false);
        
        // Add window listener
        addWindowListener(new CustomWindowListener());

        // Create and add the main panel
        add(createMainPanel(), BorderLayout.CENTER);
        
        add(new Keyboard(28), BorderLayout.SOUTH);
        
        // Pack the components
        pack();
        setLocationRelativeTo(null); // Center on screen
    }
    
	private void createTextArea() {
		_messageArea = new JTextArea(2, 20);
		_messageArea.setEditable(false);
		_messageArea.setFont(Fonts.defaultFont);
		_messageArea.setMargin(new Insets(10, 20, 10, 20)); // top, left, bottom, right margins

	}
    
    private JPanel createMainPanel() {
    	JPanel p = new JPanel();
    	
    	p.setLayout(new BorderLayout(0, 6));
    	
        // Create and configure the center component
        JComponent centerComponent = new LetterGrid();
        centerComponent.setFocusable(true); // To receive keyboard events
        p.add(centerComponent, BorderLayout.CENTER);

        // Create and add the south text area
        createTextArea();
        p.add(_messageArea, BorderLayout.SOUTH);

    	return p;
    }

    public void setMessage(String message) {
    	_messageArea.setText(message);
    }

    private class CustomWindowListener extends WindowAdapter {
        @Override
        public void windowClosing(WindowEvent e) {
            // Handle window closing event here
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Wordle wordle = getInstance();
            wordle.setMessage("Welcome to Wordle!\nI'm sure you know how to play.");
            wordle.setVisible(true);
        });
    }
}
