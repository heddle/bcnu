package cnuphys.eventManager.graphics;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import cnuphys.bCNU.dialog.DialogUtilities;
import cnuphys.bCNU.graphics.ImageManager;
import cnuphys.eventManager.event.EventManager;
import cnuphys.splot.plot.GraphicsUtilities;

public class BanksAndColumnsDialog extends JDialog implements ListSelectionListener {
	
	//the buttons
	private JButton _okButton;
	private JButton _cancelButton;
	private JButton _addButton;
	private JButton _removeButton;
	
	private String _okLabel;
	
	//the reason
	private int _reason;

	//selection panel
	private SelectColumnsPanel _columnsPanel;
	
	//for the selected fullnames
	private FullColumnNameList _fullNameList;
	
	public BanksAndColumnsDialog(String title) {
		this(title, "  OK  ");
	}

	
	public BanksAndColumnsDialog(String title, String okLabel) {
		_okLabel = okLabel;
		setTitle(title);
		setModal(true);

		setLayout(new BorderLayout(4, 4));
		setup();

		if (ImageManager.cnuIcon != null) {
			setIconImage(ImageManager.cnuIcon.getImage());
		}

		// close is like a cancel
		WindowAdapter wa = new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent we) {
				setVisible(false);
			}
		};
		addWindowListener(wa);

		pack();
		GraphicsUtilities.centerComponent(this);
		
	}
	//		List<String> slist = _clist.getSelectedValuesList();

	//create the remove button
	private JButton makeRemoveButton() {
		
		ActionListener al = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				List<String> selectedNames = _fullNameList.getSelectedValuesList();
				if (selectedNames != null) {
					_fullNameList.removeSelected();
				}
			}
			
		};
		
		JButton button = new JButton("Remove");
		button.addActionListener(al);
		button.setEnabled(false);
		return button;
	}

	
	//create the add button
	private JButton makeAddButton() {
		
		ActionListener al = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String selectedBank = _columnsPanel.getSelectedBank();
				List<String> selectedColumns = _columnsPanel.getSelectedColumns();
				_fullNameList.add(selectedBank, selectedColumns);
				_columnsPanel.clear();
			}
			
			
		};
		
		JButton button = new JButton(" Add ");
		button.addActionListener(al);
		button.setEnabled(false);
		return button;
	}
	

	//set up the components
	private void setup() {
		addNorth();
		addCenter();
		addSouth();
	}
	
	//add the north component
	private void addNorth() {
		_addButton = makeAddButton();
		_columnsPanel = new SelectColumnsPanel("Choose a bank,then multiple columns. Select \"Add\" to include in list of exported columns.",
				_addButton);
		_columnsPanel.addBankColumnListener(this);
		add(_columnsPanel, BorderLayout.NORTH);		
	}

	//add the center component
	private void addCenter() {
		_fullNameList = new FullColumnNameList();
		_fullNameList.addListSelectionListener(this);
    	add(_fullNameList.getScrollPane(), BorderLayout.CENTER);
	}

	//close the dialog
	private void doClose(int reason) {
		_reason = reason;

		setVisible(false);
	}

	/**
	 * Get the reason the dialog closed
	 * @return DialogUtilities.OK_RESPONSE or DialogUtilities.CANCEL_RESPONSE
	 */
	public int getReason() {
		return _reason;
	}

	//ad the south (button) panel
	private void addSouth() {
		JPanel sp = new JPanel();
		sp.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 4));
		
		_removeButton = makeRemoveButton();;


		_okButton = new JButton(_okLabel);
		//use lambda for action
		_okButton.addActionListener(e -> doClose(DialogUtilities.OK_RESPONSE));
		_okButton.setEnabled(false);


		_cancelButton = new JButton("Cancel");
		//use lambda for action
		_cancelButton.addActionListener(e -> doClose(DialogUtilities.CANCEL_RESPONSE));
		


		sp.add(_removeButton);
		sp.add(_okButton);
		sp.add(_cancelButton);
		add(sp, BorderLayout.SOUTH);

	}
	
	//fix the state of all the widgets
	private void fixState()  {
		boolean haveBank = (_columnsPanel.getSelectedBank() != null);
		boolean haveColumn = (_columnsPanel.getSelectedColumns() != null);
		boolean haveFullNames = _fullNameList.count() > 0;
		boolean haveSelectedFullNames = (_fullNameList.getSelectedFullNames() != null);

		_okButton.setEnabled(haveFullNames);
		_addButton.setEnabled(haveBank && haveColumn);
		_removeButton.setEnabled(haveSelectedFullNames);
	}


	/**
	 * Get all the full names in the list
	 * @return all the full names in the list
	 */
	public List<String> getFullNames() {
		return _fullNameList.getFullNames();
	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
		fixState();
	}

	/**
	 * Get the selected bank
	 * @return the selected bank  (or <code>null</code>
	 */
	public String getSelectedBank() {
		return _columnsPanel.getSelectedBank();
	}

	/**
	 * Get the selected columns
	 * @return a list of selected columns  (or <code>null</code>
	 */
	public List<String> getSelectedColumns() {
		return _columnsPanel.getSelectedColumns();
	}
	
	/**
	 * For testing
	 * @param arg command arguments (ignored)
	 */
	public static void main(String[] arg) {
		String testFile = "/Users/heddle/data/testdata.hipo";
		try {
			EventManager.getInstance().openHipoEventFile(new File(testFile));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		UIManager.put("List.focusCellHighlightBorder", BorderFactory.createEmptyBorder());

		// now make the frame visible, in the AWT thread
		EventQueue.invokeLater(new Runnable() {

			@Override
			public void run() {
				(new BanksAndColumnsDialog("test dialog")).setVisible(true);
				
				
				System.out.println("done");
				System.exit(0);
			}

		});

		
		
	;
	}
}
