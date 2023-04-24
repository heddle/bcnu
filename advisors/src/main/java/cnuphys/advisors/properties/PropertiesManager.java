package cnuphys.advisors.properties;

import java.io.File;
import java.util.Properties;

import cnuphys.bCNU.log.Log;
import cnuphys.bCNU.util.Environment;
import cnuphys.bCNU.util.SerialIO;


public class PropertiesManager {
	private static PropertiesManager _instance;

	//the actual properties object used to save state
	private static Properties _state;

	//the file holding the state
	private static File _stateFile;

	//private constructor for singleton
	private PropertiesManager() {
	}

	/**
	 * Public access for the singleton
	 * @return the PropertiesManager singleton.
	 */
	public static PropertiesManager getInstance() {
		if (_instance == null) {
			_instance = new PropertiesManager();
			_instance.getPropertiesFromDisk();
		}
		return _instance;
	}

	/**
	 * Put in a property, then write the preferences
	 * @param key the key
	 * @param value the value
	 */
	public void putAndWrite(String key, String value) {
		if ((_state != null) && (_stateFile != null) && (key != null) && (value != null)) {

			_state.put(key, value);
			writeProperties();
		}
	}

	/**
	 * Put in a property, don't write the preferences
	 * @param key the key
	 * @param value the value
	 */
	public void put(String key, String value) {
		if ((_state != null) && (_stateFile != null) && (key != null) && (value != null)) {

			_state.put(key, value);
		}
	}


	/**
	 * Get a property from the user preferences
	 * @param key the key
	 * @return the property, or <code>null</code> if not found
	 */
	public String get(String key) {
		if ((_state != null) && (_stateFile != null)) {
			return _state.getProperty(key);
		}
		return null;
	}

	/**
	 * Write the properties file. Should be .ced.user.pref in the home dir
	 */
	public void writeProperties() {
		if ((_stateFile == null) || (_state == null)) {
			return;
		}

		SerialIO.serialWrite(_state, _stateFile.getPath());
	}

	/**
	 * Read the property from from the home directory.
	 */
	private void getPropertiesFromDisk() {
		try {
			String homeDir = Environment.getInstance().getHomeDirectory();
			_stateFile = new File(homeDir, ".ced.user.pref");
			 System.out.print("User pref file: " + _stateFile.getPath() + "     ");
			if (_stateFile.exists()) {
				_state = (Properties) SerialIO.serialRead(_stateFile.getPath());
				System.out.println("Read preferences");
			} else {
				_state = new Properties();
				System.out.println("Could not read preferences");
			}
		} catch (Exception e) {
			_state = new Properties();
			Log.getInstance().exception(e);
		}
	}
}
