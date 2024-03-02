package cnuphys.fastMCed.fastmc;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.List;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

import org.jlab.clas.physics.PhysicsEvent;

import cnuphys.fastMCed.eventgen.AEventGenerator;
import cnuphys.fastMCed.eventio.IPhysicsEventListener;
import cnuphys.fastMCed.eventio.PhysicsEventManager;
import cnuphys.fastMCed.streaming.IStreamProcessor;
import cnuphys.fastMCed.streaming.StreamDialog;
import cnuphys.fastMCed.streaming.StreamManager;
import cnuphys.fastMCed.streaming.StreamProcessStatus;
import cnuphys.fastMCed.streaming.StreamReason;

public class FastMCMenuAddition implements ActionListener, IPhysicsEventListener, IStreamProcessor {

	// the physics event manager
	private PhysicsEventManager _physicsEventManager = PhysicsEventManager.getInstance();

	private StreamDialog _streamDialog;

	// the next menu item
	private JMenuItem _nextItem;

	// the stream menu item
	private JMenuItem _streamItem;

	// the parent menu
	private JMenu _menu;

	/**
	 * Create a set of FastMC Menu items, add to the given menu
	 */
	public FastMCMenuAddition(JMenu menu) {
		_menu = menu;

		// add things in reverse order because of the items already in the file
		// menu
		_menu.insertSeparator(0);
		_streamItem = addItem("Stream Events...", KeyEvent.VK_S);
		_nextItem = addItem("Next Event", KeyEvent.VK_N);
		_menu.insertSeparator(0);

		_physicsEventManager.addPhysicsListener(this, 2);
		StreamManager.getInstance().addStreamListener(this);

		fixMenuState();
		// setEnabled(false);
	}

	// convenience method to add menu item
	private JMenuItem addItem(String label, int accelKey) {
		JMenuItem item = new JMenuItem(label);
		if (accelKey > 0) {
			item.setAccelerator(KeyStroke.getKeyStroke(accelKey, ActionEvent.CTRL_MASK));
		}

		item.addActionListener(this);
		_menu.add(item, 0);
		return item;
	}

	/**
	 * New fast mc event
	 *
	 * @param event the generated physics event
	 */
	@Override
	public void newPhysicsEvent(PhysicsEvent event, List<ParticleHits> particleHits) {
		fixMenuState();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object o = e.getSource();

		if (o == _nextItem) {
			_physicsEventManager.nextEvent();
		} else if (o == _streamItem) {
			if ((_streamDialog == null) || (StreamManager.getInstance().getStreamState() == StreamReason.STOPPED)) {
				_streamDialog = new StreamDialog();
			}
			_streamDialog.setVisible(true);
			_streamDialog.toFront();
			fixMenuState();
		}
	}

	// fix the menus state
	private void fixMenuState() {
		boolean hasAnotherEvent = _physicsEventManager.moreEvents();
		boolean streaming = StreamManager.getInstance().isStarted();

		_nextItem.setEnabled(hasAnotherEvent && !streaming);
		_streamItem.setEnabled(hasAnotherEvent && !streaming);
	}

	@Override
	public void streamingChange(StreamReason reason) {
		fixMenuState();
	}

	@Override
	public StreamProcessStatus streamingPhysicsEvent(PhysicsEvent event, List<ParticleHits> particleHits) {
		return null;
	}

	@Override
	public String flagExplanation() {
		return "No way";
	}

}