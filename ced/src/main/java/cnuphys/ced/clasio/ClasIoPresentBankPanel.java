package cnuphys.ced.clasio;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Arrays;
import java.util.Hashtable;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.jlab.io.base.DataEvent;

import cnuphys.bCNU.component.ActionLabel;
import cnuphys.bCNU.view.BaseView;
import cnuphys.ced.alldata.DataManager;
import cnuphys.ced.cedview.CedView;
import cnuphys.ced.cedwindow.CedDataWindow;
import cnuphys.ced.clasio.table.NodeTable;

/**
 * Panel that shows which banks are present in an event
 *
 * @author heddle
 *
 */
@SuppressWarnings("serial")
public class ClasIoPresentBankPanel extends JPanel
		implements ActionListener, IClasIoEventListener {

	//try to set a reasonable height
	private int preferredHeight;

	// the event manager
	private ClasIoEventManager _eventManager = ClasIoEventManager.getInstance();

	// hash table
	private Hashtable<String, ActionLabel> _alabels = new Hashtable<>(193);

	// the node table
	private NodeTable _nodeTable;


	//if a cedview owns this
	private CedView _view;
	
	//scroll pane
	private JScrollPane _scrollPane;

	/**
	 * This panel holds all the known banks in a grid of buttons. Banks present will
	 * be clickable, and will cause the table to scroll to that name
	 * @param view the view owner
	 * @param nodeTable the table
	 */
	public ClasIoPresentBankPanel(BaseView view, NodeTable nodeTable) {
		this(view, nodeTable, 45);
	}

	/**
     *This panel holds all the known banks in a grid of buttons. Banks present will
	 * be clickable, and will cause the table to scroll to that name	 * @param nodeTable
	 * @param view the view owner
	 * @param nodeTable the table
	 * @param numRows the number of rows for banks
	 */
	public ClasIoPresentBankPanel(BaseView view, NodeTable nodeTable, int numRows) {

		_view = (view instanceof CedView) ? (CedView)view: null;

		_nodeTable = nodeTable;
		_eventManager.addClasIoEventListener(this, 1);
		setLayout(new GridLayout(numRows, 0, 2, 0));
		setBorder(BorderFactory.createEmptyBorder(2, 8, 2, 2));

		FontMetrics gm = getFontMetrics(ActionLabel.enabledFontLarge);
		preferredHeight = numRows * (gm.getHeight() + 2);

	}


	@Override
	public Dimension getMinimumSize() {
		Dimension d = super.getMinimumSize();
		d.height = 800;
		return d;

	}

	@Override
	public Dimension getPreferredSize() {
		Dimension d = super.getPreferredSize();
		d.height = preferredHeight;
		return d;

	}


	// replace all the bank action labels as result of new event
	private void replaceBankLabels(DataEvent event) {

		removeAll();

		if (event != null) {
			String[] allBanks = event.getBankList();
			Arrays.sort(allBanks);
			if (allBanks != null) {
				for (String s : allBanks) {
					if (match(s)) {
						makeLabel(s);
					}
				}
			}
		}
	}



	// must match
	private boolean match(String s) {

		if (_view == null) {
			return true;
		}

		String[] matchList = _view.getBanksMatches();


		if (matchList == null) { //accept all
			return true;
		}
		else {
			for (String ms : matchList) {
					if (s.contains(ms)) {
					return true;
				}
			}
		}

		return false;
	}

	// update as the result of a new event arriving
	private void update() {
		String[] allBanks = DataManager.getInstance().getKnownBanks();

		if (allBanks == null) {
			return;
		}

		for (String s : allBanks) {

			ActionLabel alabel = _alabels.get(s);

			if (alabel != null) {

				boolean inCurrent = _eventManager.isBankInCurrentEvent(s);
				alabel.setEnabled(inCurrent);
			}
		}
	}

	// convenience method to make a button
	private ActionLabel makeLabel(final String label) {
		final ActionLabel alabel = new ActionLabel(label, false);
		alabel.setOpaque(true);

		MouseListener ml = new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent e) {
				if (_eventManager.isBankInCurrentEvent(label)) {
					int clickCount = e.getClickCount();

					if ((_nodeTable != null) && (clickCount == 1)) {
						_nodeTable.makeNameVisible(label);

					} else if (clickCount == 2) {
						
						// open a new window
						CedDataWindow dw = CedDataWindow.getBankWindow(label);
						dw.update();
						dw.setVisible(true);
						
					}
				}
			}

			@Override
			public void mousePressed(MouseEvent e) {
			}

			@Override
			public void mouseReleased(MouseEvent e) {
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				if (_eventManager.isBankInCurrentEvent(label)) {
					alabel.setBackground(Color.yellow);
				}

			}

			@Override
			public void mouseExited(MouseEvent e) {
				alabel.setBackground(null);
			}

		};

		alabel.addMouseListener(ml);

		// alabel.addActionListener(this);
		_alabels.put(label, alabel);
		add(alabel);
		return alabel;
	}

	@Override
	public void actionPerformed(ActionEvent ae) {
	}

	@Override
	public void newClasIoEvent(DataEvent event) {

		if (!_eventManager.isAccumulating()) {
			replaceBankLabels(event);
			update();
		}
	}

	@Override
	public void openedNewEventFile(String path) {
		replaceBankLabels(null);
	}


	/**
	 * Change the event source type
	 *
	 * @param source the new source: File, ET
	 */
	@Override
	public void changedEventSource(ClasIoEventManager.EventSourceType source) {
		replaceBankLabels(null);
	}
	
	/**
	 * Get the table's scroll pane
	 *
	 * @return te table's scroll pane
	 */
	public JScrollPane getScrollPane() {
		if (_scrollPane == null) {
			_scrollPane = new JScrollPane(this);
		}
		return _scrollPane;
	}



}
