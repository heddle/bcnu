package cnuphys.advisors.log;


import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

import javax.swing.SwingUtilities;

import cnuphys.bCNU.dialog.DialogUtilities;
import cnuphys.bCNU.log.Log;
import cnuphys.bCNU.log.TabbedLogDialog;

public class LogManager {

	//singleton
	private static LogManager _instance;

	//the tabbed dialog
	private TabbedLogDialog _dialog;

	private LogManager() {
		_dialog = new TabbedLogDialog();
		_dialog.setSize(800, 800);

		DialogUtilities.centerDialog(_dialog);

	//	captureStreamsToLog();
	}

	//capture stdout and stderr to log
	private void captureStreamsToLog() {

		//stdout
		OutputStream stdOS = new OutputStream() {

			@Override
			public void write(int b) throws IOException {
				swingWrite(b, 0);
			}

		};

		System.setOut(new PrintStream(stdOS));

		//stderr
		OutputStream errOS = new OutputStream() {

			@Override
			public void write(int b) throws IOException {
				swingWrite(b, 1);
			}

		};

		System.setErr(new PrintStream(errOS));

	}

	// write a char. Must be done on the AWT thread
	private void swingWrite(int b, int opt) {

		if (b < 0) {
			return;
		}

		boolean isAWTThread = SwingUtilities.isEventDispatchThread();

		final char c = (char) b;

		if (isAWTThread) {
			if (opt == 0) {
				info("" + c);
			} else {
				error("" + c);
			}
		} else {
			final Runnable prun = new Runnable() {

				@Override
				public void run() {
					if (opt == 0) {
						info("" + c);
					} else {
						error("" + c);
					}
				}
			};

			try {
				SwingUtilities.invokeAndWait(prun);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	/**
	 *  Access to the singleton
	 * @return the LogManager
	 */
	public static LogManager getInstance() {
		if (_instance == null) {
			_instance = new LogManager();
		}
		return _instance;
	}

	/**
	 * Log an error message
	 * @param string the error message
	 */
	public static void error(String string) {
		Log.getInstance().error(string);
	}

	/**
	 * Log a config message
	 * @param string the config message
	 */
	public static void config(String string) {
		Log.getInstance().config(string);
	}

	/**
	 * Log a warning message
	 * @param string the warning message
	 */
	public static void warning(String string) {
		Log.getInstance().warning(string);
	}

	/**
	 * Log an info message
	 * @param string the info message
	 */
	public static void info(String string) {
		Log.getInstance().info(string);
	}

	/**
	 * Log an exception
	 * @param e the exception
	 */
	public void exception(Exception e) {
		Log.getInstance().exception(e);
	}


	/**
	 * Set whether the dialog is visible or not.
	 * @param vis the visibility flag.
	 */
	public void setVisible(boolean vis) {
		_dialog.setVisible(vis);
	}

}
