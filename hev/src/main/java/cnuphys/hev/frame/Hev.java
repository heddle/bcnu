package cnuphys.hev.frame;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.UIManager;

import org.jlab.io.base.DataEvent;

import cnuphys.bCNU.menu.FileMenu;
import cnuphys.bCNU.util.FileUtilities;
import cnuphys.bCNU.util.X11Colors;
import cnuphys.eventManager.event.EventManager;
import cnuphys.eventManager.event.EventMenu;
import cnuphys.eventManager.event.IEventListener;
import cnuphys.eventManager.graphics.DefinitionManager;
import cnuphys.eventManager.namespace.NameSpaceManager;
import cnuphys.eventManager.properties.PropertiesManager;
import cnuphys.eventManager.table.NodePanel;
import cnuphys.splot.plot.GraphicsUtilities;

public class Hev extends JFrame implements IEventListener {

	private static final String _release = "0.10";

	//the singleton
	private static Hev _instance;

	//the file menu
	private FileMenu _fileMenu;

	//definition menu
	private JMenu _definitionMenu;

	// holds the panel that has the table
	protected NodePanel _nodePanel;

	// event menu
	private EventMenu _eventMenu;

	// for the event count
	private JMenuItem _eventCountLabel;

	// event remaining label
	private JMenuItem _eventRemainingLabel;


	//rewind item
	private static JMenuItem _rewindItem;

	//stream events menu item
	private static JMenuItem _streamItem;


	//private constructor
	private Hev() {

		super("hev release " + _release);

		UIManager.put("List.focusCellHighlightBorder", BorderFactory.createEmptyBorder());

		setLayout(new BorderLayout(4, 4));
		addCenter();
		createMenus();

		WindowAdapter wa = new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent we) {
				System.out.println("Exiting.");
				System.exit(0);
			}
		};
		addWindowListener(wa);

		EventManager.getInstance().addEventListener(this, 2);

		//size based on screen, and center
		Dimension d = GraphicsUtilities.screenFraction(.7);
		setSize(d);
		GraphicsUtilities.centerComponent(this);
		
		//track streaming
		new StreamTracker();

	}
	
	/**
	 * Get the application
	 * @return the singleton
	 */
	public static Hev getHev() {
		if (_instance == null) {
			_instance = new Hev();
		}
		return _instance;
	}

	//add the center component
	private void addCenter() {
		_nodePanel = new NodePanel();
		add(_nodePanel, BorderLayout.CENTER);
	}


	//create the menus
	private void createMenus() {
		setJMenuBar(new JMenuBar());
		JMenuBar menuBar = getJMenuBar();

		_eventMenu = new EventMenu(false);
		//add to the event menu
		addToEventMenu();


		_fileMenu = new FileMenu(false);
		addToFileMenu();

		menuBar.add(_fileMenu);
		menuBar.add(_eventMenu);

		//the definition menu
		_definitionMenu = DefinitionManager.getInstance().getMenu();
		menuBar.add(_definitionMenu);
	}

	// add to the file menu
	private void addToFileMenu() {

		//open hipo files
		_fileMenu.add(EventMenu.getRecentEventFileMenu(), 0);
		_fileMenu.add(EventMenu.getOpenHipoEventFileItem(), 0);

	}

	// add to the event menu
	private void addToEventMenu() {
		createStreamMenuItem();
		_eventCountLabel = addEventCountToEventMenu(_eventMenu);
		_eventRemainingLabel = addEventRemainingToEventMenu(_eventMenu);
	}

	//create the menu item to stream to the end of the file
	private void createStreamMenuItem() {

		ActionListener al = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Object source = e.getSource();
				if (source == _streamItem) {

					Runnable runner = new Runnable() {

						@Override
						public void run() {
							EventManager.getInstance().streamToEndOfFile();
						}

					};

					(new Thread(runner)).start();
				}
				else if (source == _rewindItem) {
					EventManager.getInstance().rewindFile();
				}
			}

		};

		_eventMenu.insertSeparator(1);

		_rewindItem = new JMenuItem("Rewind to Start of File");
		_rewindItem.setEnabled(false);
		_rewindItem.addActionListener(al);
		_eventMenu.add(_rewindItem, 2);

		_streamItem = new JMenuItem("Stream to End of File");
		_streamItem.setEnabled(false);
		_streamItem.addActionListener(al);
		_eventMenu.add(_streamItem, 3);
		_eventMenu.insertSeparator(4);
	}

	//set menus busy while streaming
	private void setBusy(boolean busy) {
		_fileMenu.setEnabled(!busy);
		_eventMenu.setEnabled(!busy);
		_definitionMenu.setEnabled(!busy);
	}

	@Override
	public void newEvent(DataEvent event, boolean isStreaming) {
		if (EventManager.getInstance().isStreaming()) {
			return;
		}

		fixState();
	}

	@Override
	public void openedNewEventFile(File file) {
		fixState();
	}

	@Override
	public void rewoundFile(File file) {
		fixState();
	}

	@Override
	public void streamingStarted(File file, int numToStream) {
		setBusy(true);
	}

	@Override
	public void streamingEnded(File file, int reason) {
		setBusy(false);
		fixState();
	}

	//fix the state of the menus
	private void fixState() {

		//any events remaining
		int numRemaining = EventManager.getInstance().getNumRemainingEvents();

		//number of events
		int eventCount = EventManager.getInstance().getEventCount();

		//set selectability
		_streamItem.setEnabled(numRemaining > 0);

		_rewindItem.setEnabled(eventCount > 0);

		//fix labels
		fixEventMenuLabels(_eventCountLabel, _eventRemainingLabel);
	}


	/**
	 * Fix the event count label
	 */
	private void fixEventMenuLabels(JMenuItem eventCountLabel, JMenuItem eventRemainingLabel) {
		int count = EventManager.getInstance().getEventCount();
		if (count < Integer.MAX_VALUE) {
			eventCountLabel.setText("Event Count: " + count);
		} else {
			eventCountLabel.setText("Event Count: N/A");
		}

		int numRemain = EventManager.getInstance().getNumRemainingEvents();
		eventRemainingLabel.setText("Events Remaining: " + numRemain);
	}


	// add to the event menu
	private JMenuItem addEventCountToEventMenu(JMenu eventMenu) {

		JMenuItem eventCountLabel = new JMenuItem("Event Count: N/A");
		eventCountLabel.setOpaque(true);
		eventCountLabel.setBackground(Color.white);
		eventCountLabel.setForeground(X11Colors.getX11Color("Dark Blue"));
		eventMenu.add(eventCountLabel);
		return eventCountLabel;
	}


	// add to the event menu
	private JMenuItem addEventRemainingToEventMenu(JMenu eventMenu) {

		JMenuItem  eventRemainingLabel = new JMenuItem("Events Remaining: N/A");
		eventRemainingLabel.setOpaque(true);
		eventRemainingLabel.setBackground(Color.white);
		eventRemainingLabel.setForeground(X11Colors.getX11Color("Dark Blue"));
		eventMenu.add(eventRemainingLabel);

		return eventRemainingLabel;
	}

	/**
	 * Main program
	 * 
	 * @param arg command line arguments
	 */
	public static void main(String arg[]) {

		// read in userprefs
		PropertiesManager.getInstance();
		FileUtilities.setDefaultDir("data");
		
		// initialize managers
		NameSpaceManager.getInstance(); // data column
		DefinitionManager.getInstance();


		// now make the frame visible, in the AWT thread
		EventQueue.invokeLater(new Runnable() {

			@Override
			public void run() {
				getHev().setVisible(true);
				System.out.println("hev  " + _release + " is ready.");
			}

		});

	}

}
