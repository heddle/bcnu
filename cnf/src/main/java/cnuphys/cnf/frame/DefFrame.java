package cnuphys.cnf.frame;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.UIManager;

import org.jlab.io.base.DataEvent;

import cnuphys.bCNU.log.Log;
import cnuphys.bCNU.log.SimpleLogDialog;
import cnuphys.bCNU.menu.FileMenu;
import cnuphys.bCNU.util.Environment;
import cnuphys.bCNU.util.FileUtilities;
import cnuphys.cnf.export.ExportManager;
import cnuphys.eventManager.event.EventManager;
import cnuphys.eventManager.event.EventMenu;
import cnuphys.eventManager.event.IEventListener;
import cnuphys.eventManager.graphics.DefinitionManager;
import cnuphys.eventManager.namespace.NameSpaceManager;
import cnuphys.eventManager.properties.PropertiesManager;
import cnuphys.eventManager.table.NodePanel;
import cnuphys.splot.plot.GraphicsUtilities;

public class DefFrame extends JFrame implements IEventListener, IDefCommon {
	
	// release string
	protected static final String _release = "build 0.4";

	//the singleton
	private static DefFrame _instance;

	//the file menu
	private FileMenu _fileMenu;

	//export menu
	private JMenu _exportMenu;

	//definition menu
	private JMenu _definitionMenu;


	//the log dialog
	private static SimpleLogDialog _logDialog;

	// holds the panel that has the table
	protected NodePanel _nodePanel;

	// event menu
	private EventMenu _eventMenu;

	// event number label
	private static JLabel _eventNumberLabel;

	// for the event count
	private JMenuItem _eventCountLabel;

	// event remaining label
	private JMenuItem _eventRemainingLabel;

	//rewind item
	private static JMenuItem _rewindItem;

	//stream events menu item
	private static JMenuItem _streamItem;

	//private constructor
	private DefFrame() {

		super("def release " + _release);

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

	}

	//add the center component
	private void addCenter() {
		_nodePanel = new NodePanel();

		add(_nodePanel, BorderLayout.CENTER);
	}

	/**
	 * public access to the singleton
	 * @return the singleton
	 */
	private static DefFrame getInstance() {
		if (_instance == null) {
			_instance = new DefFrame();
			_instance.createEventNumberLabel();
		}
		return _instance;
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

		//the export menu
		_exportMenu = ExportManager.getExportMenu();
		menuBar.add(_exportMenu);


	}

	// add to the file menu
	private void addToFileMenu() {

		//the log
		JMenuItem mitem = new JMenuItem("Log...");

		//use lambda for action
		mitem.addActionListener(e -> displayLog());
		_fileMenu.add(mitem, 0);

		//open hipo files
		_fileMenu.add(EventMenu.getRecentEventFileMenu(), 0);
		_fileMenu.add(EventMenu.getOpenHipoEventFileItem(), 0);
		_fileMenu.insertSeparator(2);

	}

	//display the log
	private void displayLog() {
		_logDialog.setVisible(true);
	}

	// add to the event menu
	private void addToEventMenu() {
		createStreamMenuItem();
		_eventCountLabel = DefCommon.addEventCountToEventMenu(_eventMenu);
		_eventRemainingLabel = DefCommon.addEventRemainingToEventMenu(_eventMenu);
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

	private void setBusy(boolean busy) {
		_fileMenu.setEnabled(!busy);
		_eventMenu.setEnabled(!busy);
		_exportMenu.setEnabled(!busy);
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

	// create the event number label
	private void createEventNumberLabel() {
		_eventNumberLabel = DefCommon.createEventNumberLabel(this);
	}

	/**
	 * Fix the title of the main frame
	 */
	@Override
	public void fixTitle() {
		DefCommon.fixTitle(this);
	}

	@Override
	public void setEventNumberLabel(int num) {
		DefCommon.setEventNumberLabel(_eventNumberLabel, num);
	}


	private void fixState() {

		//any events remaining
		int numRemaining = EventManager.getInstance().getNumRemainingEvents();

		//number of events
		int eventCount = EventManager.getInstance().getEventCount();

		//set selectability
		_streamItem.setEnabled(numRemaining > 0);

		_rewindItem.setEnabled(eventCount > 0);

		//fix labels
		DefCommon.fixEventMenuLabels(_eventCountLabel, _eventRemainingLabel);
	}


	/**
	 * Main program
	 * @param arg command line arguments
	 */
	public static void main(String arg[]) {

		// read in userprefs
		PropertiesManager.getInstance();
		FileUtilities.setDefaultDir("data");


		//process command args
				if ((arg != null) && (arg.length > 0)) {
					int len = arg.length;
					int lm1 = len - 1;
					boolean done = false;
					int i = 0;
					while (!done) {
						if (arg[i].equalsIgnoreCase("-p")) {
							if (i < lm1) {
								i++;
								FileUtilities.setDefaultDir(arg[i]);
							}
						}

						i++;
						done = (i >= len);
					} // !done
				} // end command arg processing

				//create the log dialog
				_logDialog = new SimpleLogDialog();


				// initialize managers
				NameSpaceManager.getInstance(); //data columns

				DefinitionManager.getInstance();
				ExportManager.getInstance(); //exporters
				Log.getInstance();


				// now make the frame visible, in the AWT thread
				EventQueue.invokeLater(new Runnable() {

					@Override
					public void run() {
						getInstance();
						getInstance().setVisible(true);
						getInstance().fixTitle();

						System.out.println("def  " + _release + " is ready.");
					}

				});
				Log.getInstance().info(Environment.getInstance().toString());

				Log.getInstance().info("def is ready.");


	}


}
