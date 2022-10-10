package cnuphys.hev.frame;

import java.awt.EventQueue;
import java.io.File;

import javax.swing.SwingUtilities;

import org.jlab.io.base.DataEvent;

import cnuphys.bCNU.ping.IPing;
import cnuphys.bCNU.ping.Ping;
import cnuphys.eventManager.event.EventManager;
import cnuphys.eventManager.event.IEventListener;

public class StreamTracker implements IEventListener, IPing {
	
	
	private Ping _awtPing;
	
	//are we streaming
	private boolean _streaming;
	
	//number to stream
	protected int _numToStream;
	
	//number streamed
	protected int _numStreamed;
	
	//filename of hipo file
	protected String _fileName;
	
	//track the progress
	private StreamProgressDialog _progressDialog;

	public StreamTracker() {
		EventManager.getInstance().addEventListener(this, 2);
		_awtPing = new Ping(2000);
		System.out.println("Adding as ping listener");
		_awtPing.addPingListener(this);
	}

	@Override
	public void newEvent(DataEvent event, boolean isStreaming) {
		if (isStreaming) {
			_numStreamed++;
		}
	}

	@Override
	public void openedNewEventFile(File file) {
		_fileName = file.getPath();
	}

	@Override
	public void rewoundFile(File file) {
	}

	@Override
	public void streamingStarted(File file, int numToStream) {
		_streaming = true;
		_numToStream = numToStream;
		_numStreamed = 0;
		
		createProgressDialog();
	}
	
    private void createProgressDialog() {
		// now make the frame visible, in the AWT thread
    	
    	final StreamTracker tracker = this;
		EventQueue.invokeLater(new Runnable() {

			@Override
			public void run() {
				_progressDialog = new StreamProgressDialog(tracker);
				_progressDialog.setVisible(true);
			}

		});
    	
    }
	
    /**
     * Stop the streaming
     */
	public void stopStreaming() {
		EventManager.getInstance().interruptStreaming();
	}

	@Override
	public void streamingEnded(File file, int reason) {
		_streaming = false;
		_numStreamed = 0;
		if (_progressDialog != null) {
			_progressDialog.setVisible(false);
		}
		_progressDialog = null;

	}

	@Override
	public void ping() {
//		boolean isAWTThread = SwingUtilities.isEventDispatchThread();

		if (_streaming) {
			System.out.println("Streamed " + _numStreamed + " out of " + _numToStream);

			if (_progressDialog != null) {
				_progressDialog.updateProgress();
			}
		}
	}
}
