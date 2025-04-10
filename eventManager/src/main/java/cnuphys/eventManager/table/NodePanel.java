package cnuphys.eventManager.table;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.MenuSelectionManager;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.jlab.io.base.DataEvent;

import cnuphys.bCNU.format.DoubleFormat;
import cnuphys.bCNU.graphics.GraphicsUtilities;
import cnuphys.bCNU.util.Fonts;
import cnuphys.eventManager.event.EventManager;
import cnuphys.eventManager.event.IEventListener;
import cnuphys.eventManager.event.PresentBankPanel;
import cnuphys.eventManager.namespace.ColumnInfo;
import cnuphys.eventManager.namespace.DataUtils;
import cnuphys.eventManager.namespace.NameSpaceManager;


public class NodePanel extends JPanel
		implements ActionListener, ListSelectionListener, IEventListener {

	// Text area shows data values for selected nodes.
	private JTextArea _dataTextArea;

	// the event info panel
	private EventInfoPanel _eventInfoPanel;

	/** A button for selecting "next" event. */
	protected JButton nextButton;

	/** show ints as hex */
	protected JCheckBox intsInHexButton;

	/** Used for "goto" event */
	protected JTextField eventNumberInput;

	// the table
	protected NodeTable _nodeTable;

	// the event manager
	EventManager _eventManager = EventManager.getInstance();

	// set true when constructor finished
	private boolean _isReady;

	// current selected node
	private ColumnInfo _currentColumnData;

	// present banks
	private PresentBankPanel _presentPanel;

	//the center panel
	private JPanel _centerPanel;

	/**
	 * Create a node panel for displaying events
	 */
	public NodePanel() {
		_eventManager.addEventListener(this, 1);

		setLayout(new BorderLayout());
		addCenter();
		addEast();

		_isReady = true;
		fixButtons();
	}

	// add the east components
	private void addEast() {
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

		// shows which banks are present
		_presentPanel = new PresentBankPanel(_nodeTable);

		panel.add(_presentPanel);
		panel.add(Box.createVerticalGlue());

		_centerPanel.add(panel, BorderLayout.EAST);
	}

	/**
	 * Create the text area that will display structure data. What is actually
	 * returned is the scroll pane that contains the text area.
	 *
	 * @return the scroll pane holding the text area.
	 */
	private JScrollPane createDataTextArea() {

		_dataTextArea = new JTextArea(3, 40) {
			@Override
			public Dimension getMinimumSize() {
				return new Dimension(180, 200);
			}
		};
		_dataTextArea.setFont(Fonts.mediumFont);
		// _dataTextArea.setBorder(BorderFactory.createTitledBorder(null,
		// "Data",
		// TitledBorder.LEADING, TitledBorder.TOP, null, Color.blue));
		_dataTextArea.setEditable(false);

		JScrollPane scrollPane = new JScrollPane() {
			@Override
			public Dimension getMinimumSize() {
				return new Dimension(180, 200);
			}
		};
		scrollPane.getViewport().setView(_dataTextArea);
		// Borderlayout respects preferred width in east/west,
		// but ignores height -- so use this to set width only.
		// Don't use "setPreferredSize" on textArea or it messes up the
		// scrolling.
		scrollPane.setPreferredSize(new Dimension(200, 600));

		return scrollPane;
	}

	// add the center components
	private void addCenter() {
		_centerPanel = new JPanel();
		_centerPanel.setLayout(new BorderLayout(0, 0));

		// event info
		_eventInfoPanel = new EventInfoPanel();
		addEventControls();

		JPanel npanel = new JPanel();
		npanel.setLayout(new FlowLayout(FlowLayout.LEFT, 4, 4));
		npanel.add(_eventInfoPanel);
		_centerPanel.add(npanel, BorderLayout.NORTH);

		_nodeTable = new NodeTable();
		_nodeTable.getSelectionModel().addListSelectionListener(this);

		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, false, createDataTextArea(),
				_nodeTable.getScrollPane());
		splitPane.setResizeWeight(0.1);
		_centerPanel.add(splitPane, BorderLayout.CENTER);

		add(_centerPanel, BorderLayout.CENTER);
	}

	/**
	 * Set the model data based on a hipo DataEvent
	 *
	 * @param event the event
	 */
	public void setData(DataEvent event) {
		_nodeTable.setData(event);
	}

	// list of ignored tags
	/**
	 * Create a panel to change events in viewer.
	 */
	private void addEventControls() {

		JPanel sourcePanel = _eventInfoPanel.getSourcePanel();
		JPanel numPanel = _eventInfoPanel.getNumberPanel();

		nextButton = new JButton("next");
		nextButton.setFont(Fonts.smallFont);
		nextButton.addActionListener(this);

		JLabel label = new JLabel("Go to # ");
		GraphicsUtilities.setSizeSmall(label);

		eventNumberInput = new JTextField(6);

		KeyAdapter ka = new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent kev) {
				if (kev.getKeyCode() == KeyEvent.VK_ENTER) {
					MenuSelectionManager.defaultManager().clearSelectedPath();
					try {
						int enumber = Integer.parseInt(eventNumberInput.getText());
						_eventManager.gotoEvent(enumber);
					} catch (Exception e) {
						eventNumberInput.setText("");
					}
				}
			}
		};
		eventNumberInput.addKeyListener(ka);

		intsInHexButton = new JCheckBox("Show ints in hex", false);
		intsInHexButton.setFont(Fonts.defaultFont);
		GraphicsUtilities.setSizeSmall(intsInHexButton);

		ItemListener il = new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent arg0) {
				updateDataArea(_currentColumnData);
			}

		};
		intsInHexButton.addItemListener(il);

		sourcePanel.add(Box.createHorizontalStrut(4));
		sourcePanel.add(intsInHexButton);

		JPanel panel = new JPanel();
		panel.setLayout(new FlowLayout(FlowLayout.LEFT, 2, 2));
		panel.add(Box.createHorizontalStrut(2));
		panel.add(nextButton);
		panel.add(Box.createHorizontalStrut(2));
		panel.add(label);
		panel.add(eventNumberInput);

		numPanel.add(panel, 0);
	}

	/**
	 * Set the selectability of the buttons
	 */
	public void fixButtons() {
		if (!_isReady) {
			return;
		}
		nextButton.setEnabled(_eventManager.isNextOK());
		eventNumberInput.setEnabled(_eventManager.isGotoOK());
	}

	/**
	 * /** Set the displayed event source value.
	 *
	 * @param source event source.
	 */
	public void setSource(String source) {
		_eventInfoPanel.setSource(source);
	}

	/**
	 * Get the displayed event source value.
	 *
	 * @return the displayed event source value.
	 */
	public String getSource() {
		return _eventInfoPanel.getSource();
	}

	/**
	 * Set the displayed event number value.
	 *
	 * @param eventNumber event number.
	 */
	public void setEventNumber(int eventNumber) {
		_eventInfoPanel.setEventNumber(eventNumber);
	}

	/**
	 * Get the displayed event number value.
	 *
	 * @return the displayed event number value.
	 */
	public int getEventNumber() {
		return _eventInfoPanel.getEventNumber();
	}

	/**
	 * Set the displayed number-of-events value.
	 *
	 * @param numberOfEvents number of events.
	 */
	public void setNumberOfEvents(int numberOfEvents) {
		_eventInfoPanel.setNumberOfEvents(numberOfEvents);
	}

	/**
	 * Get the displayed number-of-events value.
	 *
	 * @return the displayed number-of-events value.
	 */
	public int getNumberOfEvents() {
		return _eventInfoPanel.getNumberOfEvents();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();

		if (source == nextButton) {
			_eventManager.getNextEvent();
		}
	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
		if (e.getValueIsAdjusting()) {
			return;
		}
		
		
		int row = _nodeTable.getSelectedRow();
		System.err.println("VALUE CHANGED row " + row);

		_currentColumnData = _nodeTable.getColumnData(row);

		updateDataArea(_currentColumnData);
	}

	/**
	 * Update the data text area
	 *
	 * @param treeSelectionEvent the causal event.
	 */
	protected void updateDataArea(ColumnInfo cd) {

		_dataTextArea.setText("");
		int blankLineEveryNth = 5; // put in a blank line after every Nth

		if (cd == null) {
			return;
		}

		DataEvent event = _nodeTable.getCurrentEvent();
		if (event == null) {
			return;
		}

		String bankName = cd.getBankInfo().getName();
		String columnName = cd.getName();

		int lineCounter = 1;
		int index = 1;

		switch (cd.getType()) {

		case NameSpaceManager.INT8: // byte
			byte bytes[] = DataUtils.getByteArray(event, bankName, columnName);
			if (bytes != null) {
				for (byte i : bytes) {
					String s;
					if (intsInHexButton.isSelected()) {
						s = String.format("[%02d]  %#04X", index++, i);
					} else {
						s = String.format("[%02d]  %d", index++, i);
					}
					_dataTextArea.append(s);
					if (lineCounter < bytes.length) {
						if (lineCounter % blankLineEveryNth == 0) {
							_dataTextArea.append("\n\n");
						} else {
							_dataTextArea.append("\n");
						}
						lineCounter++;
					}
				}
			} else {
				_dataTextArea.append("null data\n");
			}
			break;

		case NameSpaceManager.INT16:
			short shorts[] = DataUtils.getShortArray(event, bankName, columnName);
			if (shorts != null) {
				for (short i : shorts) {
					String s;
					if (intsInHexButton.isSelected()) {
						s = String.format("[%02d]  %#05X", index++, i);
					} else {
						s = String.format("[%02d]  %d", index++, i);
					}
					_dataTextArea.append(s);
					if (lineCounter < shorts.length) {
						if (lineCounter % blankLineEveryNth == 0) {
							_dataTextArea.append("\n\n");
						} else {
							_dataTextArea.append("\n");
						}
						lineCounter++;
					}
				}
			} else {
				_dataTextArea.append("null data\n");
			}
			break;

		case NameSpaceManager.INT32:
			int ints[] = DataUtils.getIntArray(event, bankName, columnName);
			if (ints != null) {
				for (int i : ints) {
					String s;
					if (intsInHexButton.isSelected()) {
						s = String.format("[%02d]  %#010X", index++, i);
					} else {
						s = String.format("[%02d]  %d", index++, i);
					}
					_dataTextArea.append(s);
					if (lineCounter < ints.length) {
						if (lineCounter % blankLineEveryNth == 0) {
							_dataTextArea.append("\n\n");
						} else {
							_dataTextArea.append("\n");
						}
						lineCounter++;
					}
				}
			} else {
				_dataTextArea.append("null data\n");
			}
			break;

		case NameSpaceManager.FLOAT32:
			float floats[] = DataUtils.getFloatArray(event, bankName, columnName);
			if (floats != null) {
				for (float f : floats) {
					String doubStr = DoubleFormat.doubleFormat(f, 6, 4);
					String s = String.format("[%02d]  %s", index++, doubStr);
					_dataTextArea.append(s);
					if (lineCounter < floats.length) {
						if (lineCounter % blankLineEveryNth == 0) {
							_dataTextArea.append("\n\n");
						} else {
							_dataTextArea.append("\n");
						}
						lineCounter++;
					}
				}
			} else {
				_dataTextArea.append("null data\n");
			}
			break;

		case NameSpaceManager.FLOAT64:
			double doubles[] = DataUtils.getDoubleArray(event, bankName, columnName);
			if (doubles != null) {
				for (double d : doubles) {
					String doubStr = DoubleFormat.doubleFormat(d, 6, 4);
					String s = String.format("[%02d]  %s", index++, doubStr);
					_dataTextArea.append(s);
					if (lineCounter < doubles.length) {
						if (lineCounter % blankLineEveryNth == 0) {
							_dataTextArea.append("\n\n");
						} else {
							_dataTextArea.append("\n");
						}
						lineCounter++;
					}
				}

			} else {
				_dataTextArea.append("null data\n");
			}
			break;

		default:
			_dataTextArea.append("null data\n");

		}

	}

	/**
	 * Part of the IEventListener interface
	 *
	 * @param event the new current event
	 */
	@Override
	public void newEvent(DataEvent event, boolean isStreaming) {
		if (isStreaming) {
			return;
		}
		setData(event);
		setEventNumber(_eventManager.getEventNumber());
		fixButtons();
	}


	/**
	 * Streaming start message
	 * @param file file being streamed
	 * @param numToStream number that will be streamed
	 */
	@Override
	public void streamingStarted(File file, int numToStream) {
	}

	/**
	 * Streaming ended message
	 * @param file the file that was streamed
	 * @param int the reason the streaming ended
	 */
	@Override
	public void streamingEnded(File file, int reason) {
	}

	/**
	 * Part of the IEventListener interface
	 *
	 * @param file the new file
	 */
	@Override
	public void openedNewEventFile(File file) {
		setEventNumber(0);

		// set the text field
		setSource(file.getAbsolutePath());
		setNumberOfEvents(_eventManager.getEventCount());
		fixButtons();
	}

	/**
	 * Rewound the current file
	 * @param file the file
	 */
	@Override
	public void rewoundFile(File file) {

	}

}
