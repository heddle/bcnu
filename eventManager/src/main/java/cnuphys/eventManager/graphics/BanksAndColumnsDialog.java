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

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
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

	//the reason
	private int _reason;

	//selection panel
	private SelectColumnsPanel _columnsPanel;
	
	public BanksAndColumnsDialog(String title) {
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
	
	//create the add button
	private JButton makeAddButton() {
		
		ActionListener al = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			
		};
		
		JButton button = new JButton(" Add ");
		button.addActionListener(al);
		
		return button;
	}
	

	private void setup() {
		addCenter();
		addSouth();
	}

	private void addCenter() {
		_addButton = makeAddButton();
		_columnsPanel = new SelectColumnsPanel("Choose a bank,then multiple columns. Select \"Add\" to include in list of exported columns.",
				_addButton);
		_columnsPanel.addBankColumnListener(this);
		add(_columnsPanel, BorderLayout.CENTER);

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

	private void addSouth() {
		JPanel sp = new JPanel();
		sp.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 4));

		_okButton = new JButton("  OK  ");
		//use lambda for action
		_okButton.addActionListener(e -> doClose(DialogUtilities.OK_RESPONSE));
		_okButton.setEnabled(false);


		_cancelButton = new JButton("Cancel");
		//use lambda for action
		_cancelButton.addActionListener(e -> doClose(DialogUtilities.CANCEL_RESPONSE));

		sp.add(_okButton);
		sp.add(_cancelButton);
		add(sp, BorderLayout.SOUTH);

	}


	@Override
	public void valueChanged(ListSelectionEvent e) {
		boolean haveBank = (_columnsPanel.getSelectedBank() != null);
		boolean haveColumn = (_columnsPanel.getSelectedColumns() != null);

		_okButton.setEnabled(haveBank && haveColumn);
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
		String testFile = "/Users/heddle/data/out_sidis_noVtC.hipo";
		try {
			EventManager.getInstance().openHipoEventFile(new File(testFile));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
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
