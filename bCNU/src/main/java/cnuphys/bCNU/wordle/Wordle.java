package cnuphys.bCNU.wordle;

import java.awt.BorderLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import cnuphys.bCNU.util.Fonts;

public class Wordle extends JDialog {

	// Singleton
	private static volatile Wordle _instance;

	//the brain
	private static final Brain _brain = Brain.getInstance();

	private JMenuItem _newGameItem;

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
			Brain.getInstance().newGame();
		}
		return _instance;
	}

	//initialize the gui components
    private void initializeComponents() {
        // Set dialog properties
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        setResizable(false);

        // Create and add the main panel
        add(createMainPanel(), BorderLayout.CENTER);

        add(Keyboard.getInstance(), BorderLayout.SOUTH);

        makeMenu();

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

	//create the main panel
    private JPanel createMainPanel() {
    	JPanel p = new JPanel();

    	p.setLayout(new BorderLayout(0, 6));

        // Create and configure the center component
        JComponent centerComponent = LetterGrid.getInstance();
        centerComponent.setFocusable(true); // To receive keyboard events
        p.add(centerComponent, BorderLayout.CENTER);

        // Create and add the south text area
        createTextArea();
        p.add(_messageArea, BorderLayout.SOUTH);

    	return p;
    }

    //make the menu from which you can start a new game
	private JMenu makeMenu() {
		JMenu menu = new JMenu("File");
		_newGameItem = new JMenuItem("New Game");
		_newGameItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				_brain.newGame();
			}
		});

		JMenuBar mb = new JMenuBar();
		setJMenuBar(mb);
		menu.add(_newGameItem);
		enableNewGame(false);
		mb.add(menu);
		return menu;
	}

	/**
	 * Enable or disable the new game menu item
	 * @param enable true to enable, false to disable
	 */
	public void enableNewGame(boolean enable) {
		_newGameItem.setEnabled(enable);
	}

	public void setNewGameAction(ActionListener actionListener) {
		_newGameItem.addActionListener(actionListener);
	}

	/**
	 * Change the message text
	 * @param message the new message
	 */
    public void setMessage(String message) {
    	_messageArea.setText(message);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Wordle wordle = getInstance();

			WindowAdapter windowAdapter = new WindowAdapter() {
				@Override
				public void windowClosing(WindowEvent e) {
					System.exit(0);
				}
			};
			wordle.addWindowListener(windowAdapter);

            wordle.setVisible(true);
        });
    }
}
