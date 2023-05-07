package cnuphys.advisors.io;

import java.io.File;
import java.io.FileNotFoundException;

import cnuphys.bCNU.util.AsciiReader;

public class CSVReader {

	// will hold the raw csv data
	private DataModel _csvData;

	//flag indicating whether the data is changes and unsaved
	private boolean _dirty;

	public CSVReader(final File file, DataModel csvData) throws FileNotFoundException {

		if (!file.exists()) {
			throw new FileNotFoundException("File not found [" + file.getPath() + "]");
		}
		_csvData = csvData;

		//process the file
		new AsciiReader(file) {

			@Override
			protected void processLine(String line) {

				line = line.replace("  ", " ");

				String[] tokens = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);


				if (_csvData._header == null) {

					for(String token : tokens) {
						token = token.trim();
					}
					_csvData._header = tokens;
				}
				else {
					_csvData.addRow(tokens);
				}
			}

			@Override
			public void done() {
			}

		};
	}

	/**
	 * Set the dirty flag
	 * @param dirty the dirty flag
	 */
	public void setDirty(boolean dirty) {
		_dirty = dirty;
	}

	/**
	 * Is the data dirty?
	 * @return the dirty flag
	 */
	public boolean isDirty() {
		return _dirty;
	}

}
