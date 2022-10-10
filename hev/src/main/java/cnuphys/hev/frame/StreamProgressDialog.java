package cnuphys.hev.frame;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.Box;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

import cnuphys.bCNU.dialog.ButtonPanel;
import cnuphys.bCNU.dialog.DialogUtilities;
import cnuphys.bCNU.graphics.GraphicsUtilities;
import cnuphys.bCNU.graphics.ImageManager;
import cnuphys.bCNU.graphics.component.CommonBorder;

public class StreamProgressDialog extends JDialog {

	// why the dialog closed.
	private int _reason = DialogUtilities.CANCEL_RESPONSE;


	// progress bar as events are accumulated
	private JProgressBar _progressBar;
	
	// path to event file
	private JLabel _pathLabel;

	// number of events total
	private JLabel _totalLabel;
	
	//stream tracker
	private StreamTracker _tracker;

	public StreamProgressDialog(StreamTracker streamTracker) {
		setTitle("Streaming Progress");
		setModal(true);
		setIconImage(ImageManager.cnuIcon.getImage());
		_tracker = streamTracker;

		// close is like a cancel
		WindowAdapter wa = new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent we) {
				doClose(DialogUtilities.CANCEL_RESPONSE);
			}
		};
		addWindowListener(wa);

		addComponents();
		pack();
		GraphicsUtilities.centerComponent(this);
		
	}
	
	// add all the widgets
	private void addComponents() {
		setLayout(new BorderLayout(6, 6));

		Box box = Box.createVerticalBox();

		// path label

		Box subBox = Box.createVerticalBox();
		_pathLabel = new JLabel("Current event source: " + _tracker._fileName);
		_totalLabel = new JLabel("Total number to stream: " + _tracker._numToStream);

		subBox.add(DialogUtilities.paddedPanel(6, 6, _pathLabel));
		subBox.add(DialogUtilities.paddedPanel(6, 6, _totalLabel));
		subBox.setBorder(new CommonBorder("Event File"));
		box.add(DialogUtilities.paddedPanel(6, 6, subBox));


		// progress bar
		_progressBar = new JProgressBar(0, 100) {
			@Override
			public Dimension getPreferredSize() {
				return new Dimension(160, 20);
			}
		};
		_progressBar.setStringPainted(true);

		box.add(DialogUtilities.paddedPanel(20, 6, _progressBar));

		// add the completed composite box
		add(box, BorderLayout.NORTH);

		// the closeout buttons
		add(createButtonPanel(), BorderLayout.SOUTH);

		// padding
		add(Box.createHorizontalStrut(4), BorderLayout.EAST);
		add(Box.createHorizontalStrut(4), BorderLayout.WEST);

	}
	
	//update the progress bar
	public void updateProgress() {
		double fract = ((double)_tracker._numStreamed)/((double)_tracker._numToStream);
		int percent = (int)(100*fract);
		System.out.println("Percent done: " + percent);
		_progressBar.setValue(percent);
	}


	/**
	 * Create the button panel.
	 * 
	 * @return the button panel.
	 */
	private JPanel createButtonPanel() {
		// closeout buttons-- use OK and CANCEL

		// buttons

		ActionListener alist = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String command = e.getActionCommand();

				if (ButtonPanel.CANCEL_LABEL.equals(command)) {
					doClose(DialogUtilities.CANCEL_RESPONSE);
				}

			}

		};

		return ButtonPanel.closeOutPanel(ButtonPanel.USE_CANCEL, alist, 50);

	}
	
	// user has cancel
	private void doClose(int reason) {
		setVisible(false);
		_tracker.stopStreaming();
	}



}
