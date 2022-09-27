package cnuphys.cnf.export;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.List;

import org.jlab.io.base.DataBank;
import org.jlab.io.base.DataEvent;

import cnuphys.bCNU.dialog.DialogUtilities;
import cnuphys.bCNU.log.Log;
import cnuphys.eventManager.graphics.ColumnsDialog;
import cnuphys.eventManager.namespace.NameSpaceManager;

/**
 * A exporter to CSV. The use must choose what bank, and then what columns to export.
 * @author heddle
 *
 */
public class CSVExporter extends AExporter {

	//the data output stream
	//private DataOutputStream _dos;

	private OutputStreamWriter _osw;

	//used to write column data names
	private boolean _first = true;

	//simple counter
	private int _count;

	//bank name selected from dialog
	private String _bankName;

	//list of column names selected from the dialog
	private List<String> _columnNames;

	@Override
	public String getMenuName() {
		return "CSV";
	}

	@Override
	public boolean prepareToExport() {
		_count = 0;
		Log.getInstance().info("CSV export requested");

		//reset
		_bankName = null;
		_columnNames = null;

		//what columns to export?
		ColumnsDialog columnDialog = new ColumnsDialog("Select Bank and Columns to Export");
		columnDialog.setVisible(true);

		int reason = columnDialog.getReason();
		if (reason == DialogUtilities.CANCEL_RESPONSE) {
			Log.getInstance().info("CSV Export was cancelled.");
			return false;
		}

		//see what I have selected
		_bankName = columnDialog.getSelectedBank();
		if (_bankName == null) {
			Log.getInstance().error("null bankname in CSV prepareToExport. That should not have happened.");
			return false;
		}
		_columnNames = columnDialog.getSelectedColumns();

		if ((_columnNames == null) || _columnNames.isEmpty()) {
			Log.getInstance().error("null or empty column names in CSV prepareToExport. That should not have happened.");
			return false;
		}
		Log.getInstance().info("CSV exporting bank [" + _bankName + "]");

		StringBuffer sb = new StringBuffer(256);
		sb.append("CSV exporting column[s] ");
		for (String c : _columnNames) {
			sb.append(" [" + c + "]");
		}

		Log.getInstance().info(sb.toString());


		//open a file for writing
		_exportFile = getFile("CSV Files", "csv", "csv", "CSV");
		if (_exportFile != null) {
			Log.getInstance().info("CSV: export to [" + _exportFile.getAbsolutePath() + "]");


			try {
				OutputStream os = new FileOutputStream(_exportFile);
				_osw = new OutputStreamWriter(os, "UTF-8");

				//_dos = new DataOutputStream(new FileOutputStream(_exportFile));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} //export file != null
		else {
			return false;
		}

		_first = true;
		return true;
	}


	@Override
	public void nextEvent(DataEvent event) {

		DataBank bank = event.getBank(_bankName);
		if (bank == null || _columnNames == null || _columnNames.isEmpty()) {
			return;
		}


		_count++;
		if ((_count % 1000) == 0) {
			System.err.println("Export count: " + _count);
		}


			if (_first) {
				writeColumnNames();
				_first  = false;
			}

			// the data

			for (int row = 0; row < bank.rows(); row++) {
				StringBuffer sb = new StringBuffer(512);

				for (int i = 0; i < _columnNames.size(); i++) {
					String columnName = _columnNames.get(i);

					if (i > 0) {
						sb.append(",");
					}

					int type = NameSpaceManager.getInstance().getDataType(_bankName, columnName);

					String s;
					switch (type) {
					case NameSpaceManager.INT8:
						s = "" + bank.getByte(columnName, row);
						break;
					case NameSpaceManager.INT16:
						s = "" + bank.getShort(columnName, row);
						break;
					case NameSpaceManager.INT32:
						s = "" + bank.getInt(columnName, row);
						break;
					case NameSpaceManager.INT64:
						s = "" + bank.getLong(columnName, row);
						break;
					case NameSpaceManager.FLOAT32:
						float f = bank.getFloat(columnName, row);
						s = String.format("%5.4g", f);
						break;
					case NameSpaceManager.FLOAT64:
						double d = bank.getDouble(columnName, row);
						s = String.format("%5.4g", d);
						break;
					default:
						s = "???";
					}

					sb.append(s);

				}

				stringLn(_osw, sb.toString());
			}

	}

	//write the column names
	private void writeColumnNames() {
		StringBuffer sb = new StringBuffer(512);

		for (int i = 0; i < _columnNames.size(); i++) {
			String cname = _columnNames.get(i);
			if (i > 0) {
				sb.append(",");
			}
			System.err.print(String.format("[%s]", cname));
			sb.append(cname);
		}
		System.err.println();
		stringLn(_osw, sb.toString());
	}

	@Override
	public void done() {
		System.out.println("CSV: I am done");

		if (_osw != null) {
			try {
				_osw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		_osw = null;

	}


}
