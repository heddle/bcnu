package cnuphys.advisors.table;

import java.io.File;

import cnuphys.advisors.frame.AdvisorAssign;
import cnuphys.bCNU.util.Environment;

public class InputOutput {

	//the data dir
	private static File _dataDir;


	//the relative path from the CWD
	private static final String _relativePath = "/cnuphys/advisors/data";

	/**
	 * General initialization
	 */
	public static void init() {
		System.out.println("Data Directory [" + getDataDir().getPath() + "] exists: " + getDataDir().exists());
	}

	/**
	 * Get a file object for a file in the data directory
	 * @param baseName the base name of the file
	 * @return a file object for a file in the data directory
	 */
	public static File openDataFile(String baseName) {
		return new File(getDataDir(), baseName);
	}

	/**
	 * Get the data directory
	 * @return the data directory
	 */
	public static File getDataDir() {
		if (_dataDir == null) {
			String cwd = Environment.getInstance().getCurrentWorkingDirectory();
			_dataDir = new File(cwd + _relativePath, AdvisorAssign.getSemester().name());
		}
		return _dataDir;
	}

	/**
	 * print a string to a PrintStream if we are in debug mode
	 * @param message the text to print
	 */
	public static void debugPrint(String message) {
		if (AdvisorAssign.DEBUG) {
			System.out.print(message);
		}
	}

	/**
	 * print a string and a newline to a PrintStream if we are in debug mode
	 * @param message the text to print
	 */
	public static void debugPrintln(String message) {
		if (AdvisorAssign.DEBUG) {
			System.out.println(message);
		}
	}


}
