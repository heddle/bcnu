package cnuphys.bCNU.util;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;

import cnuphys.bCNU.format.DoubleFormat;


public class CSVWriter {

	private OutputStreamWriter _writer;

	/**
	 * Create a CSVWrite from a path
	 * @param path the path to a file. The disk file will
	 * be created or overwritten.
	 */
	public CSVWriter(String path) {
		this(new File(path));
	}

	/**
	 * Create a CSV Write from a File object
	 * @param file the File object. The disk file will
	 * be created or overwritten.
	 */
	public CSVWriter(File file) {
		OutputStream os;
		try {
			os = new FileOutputStream(file);
			_writer = new OutputStreamWriter(os, "UTF-8");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}


	/**
	 * Close the stream
	 */
	public void close() {
		if (_writer != null) {
			try {
				_writer.close();
				_writer = null;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}


	/**
	 * Write a row of doubles to the csv file
	 * @param vals the row of doubles
	 */
	public void writeRow(double...vals) {
		if ((vals == null) || (vals.length == 0)) {
			return;
		}

		int len = vals.length;
		int guessSize = 20*len;

		StringBuffer sb = new StringBuffer(guessSize);

		for (int i = 0; i < len; i++) {
			boolean last = (i == (len-1));
			sb.append(DoubleFormat.doubleFormat(vals[i], 6, 3));
			if (!last) {
				sb.append(",");
			}
			else {
				sb.append("\n");
			}
		}

		try {
			_writer.write(sb.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Write a row of doubles to the csv file
	 * @param vals the row of doubles
	 */
	public void writeStartOfRow(double...vals) {
		if ((vals == null) || (vals.length == 0)) {
			return;
		}

		int len = vals.length;
		int guessSize = 20*len;

		StringBuffer sb = new StringBuffer(guessSize);

		for (int i = 0; i < len; i++) {
			sb.append(DoubleFormat.doubleFormat(vals[i], 6, 3));
			sb.append(",");
		}

		try {
			_writer.write(sb.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Write a row of strings to the csv file
	 * @param strings the row of strings
	 */
	public void writeRow(String...strings) {

		if ((strings == null) || (strings.length == 0)) {
			return;
		}

		int len = strings.length;

		int guessSize = 20*len;

		StringBuffer sb = new StringBuffer(guessSize);

		for (int i = 0; i < len; i++) {
			boolean last = (i == (len-1));
			sb.append(strings[i]);
			if (!last) {
				sb.append(",");
			}
			else {
				sb.append("\n");
			}
		}

		try {
			_writer.write(sb.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Write a row of strings to the csv file
	 * @param strings the row of strings
	 */
	public void writeStartOfRow(String...strings) {

		if ((strings == null) || (strings.length == 0)) {
			return;
		}

		int len = strings.length;

		int guessSize = 20*len;

		StringBuffer sb = new StringBuffer(guessSize);

		for (int i = 0; i < len; i++) {
			sb.append(strings[i]);
			sb.append(",");
		}

		try {
			_writer.write(sb.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	/**
	 * Write a blank row
	 */
	public void newLine() {
		try {
			_writer.write("\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
