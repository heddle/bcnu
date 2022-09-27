package cnuphys.cnf.frame;

import java.awt.Color;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

import cnuphys.bCNU.util.X11Colors;
import cnuphys.eventManager.event.EventManager;


public class DefCommon {


	/**
	 * Fix the event count label
	 */
	protected static  void fixEventMenuLabels(JMenuItem eventCountLabel, JMenuItem eventRemainingLabel) {
		int count = EventManager.getInstance().getEventCount();
		if (count < Integer.MAX_VALUE) {
			eventCountLabel.setText("Event Count: " + count);
		} else {
			eventCountLabel.setText("Event Count: N/A");
		}

		int numRemain = EventManager.getInstance().getNumRemainingEvents();
		eventRemainingLabel.setText("Events Remaining: " + numRemain);
	}


	protected static void fixTitle(JFrame frame) {
		String title = frame.getTitle();

		// adjust title as needed
		frame.setTitle(title);
	}

	protected static void setEventNumberLabel(JLabel eventNumberLabel, int num) {

		if (num < 0) {
			eventNumberLabel.setText("  Event Num:      ");
		} else {
			eventNumberLabel.setText("  Event Num: " + num);
		}
	}

	// add to the event menu
	protected static JMenuItem addEventCountToEventMenu(JMenu eventMenu) {

		JMenuItem eventCountLabel = new JMenuItem("Event Count: N/A");
		eventCountLabel.setOpaque(true);
		eventCountLabel.setBackground(Color.white);
		eventCountLabel.setForeground(X11Colors.getX11Color("Dark Blue"));
		eventMenu.add(eventCountLabel);
		return eventCountLabel;
	}


	// add to the event menu
	protected static JMenuItem addEventRemainingToEventMenu(JMenu eventMenu) {

		JMenuItem  eventRemainingLabel = new JMenuItem("Events Remaining: N/A");
		eventRemainingLabel.setOpaque(true);
		eventRemainingLabel.setBackground(Color.white);
		eventRemainingLabel.setForeground(X11Colors.getX11Color("Dark Blue"));
		eventMenu.add(eventRemainingLabel);

		return eventRemainingLabel;
	}

	// create the event number label
	protected static JLabel createEventNumberLabel(JFrame frame) {
		JLabel _eventNumberLabel = new JLabel("  Event Num: ");
		_eventNumberLabel.setOpaque(true);
		_eventNumberLabel.setBackground(Color.black);
		_eventNumberLabel.setForeground(Color.yellow);
		_eventNumberLabel.setFont(new Font("Dialog", Font.BOLD, 12));
		_eventNumberLabel.setBorder(BorderFactory.createLineBorder(Color.cyan, 1));
		setEventNumberLabel(_eventNumberLabel, -1);

		frame.getJMenuBar().add(Box.createHorizontalGlue());
		frame.getJMenuBar().add(_eventNumberLabel);
		frame.getJMenuBar().add(Box.createHorizontalStrut(100));
		return _eventNumberLabel;
	}

}
