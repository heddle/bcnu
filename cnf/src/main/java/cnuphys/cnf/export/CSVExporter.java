package cnuphys.cnf.export;

import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.jlab.io.base.DataEvent;

import cnuphys.bCNU.dialog.DialogUtilities;
import cnuphys.bCNU.log.Log;
import cnuphys.cnf.alldata.ColumnData;
import cnuphys.cnf.alldata.DataManager;
import cnuphys.cnf.alldata.graphics.ColumnsDialog;

/**
 * A exporter to CSV. The use must choose what bank, and then what columns to export.
 * @author heddle
 *
 */
public class CSVExporter extends AExporter {
	
	//the data output stream
	//private DataOutputStream _dos;
	
	private OutputStreamWriter _osw;
	
	//the columns being exported
	private List<ColumnData> _columnData;
	
	//used to write column data names
	private boolean _first = true;

	@Override
	public String getMenuName() {
		return "CSV";
	}
	
	private int count;

	@Override
	public boolean prepareToExport() {
		count = 0;
		Log.getInstance().info("CSV export requested");
		
		//reset
		_columnData = null;
		
		//what columns to export?
		ColumnsDialog cd = new ColumnsDialog("Select Bank and Columns to Export");
		cd.setVisible(true);
		
		int reason = cd.getReason();
		if (reason == DialogUtilities.CANCEL_RESPONSE) {
			Log.getInstance().info("CSV Export was cancelled.");
			return false;
		}
		
		//see what I have selected
		String bankName = cd.getSelectedBank();
		if (bankName == null) {
			Log.getInstance().error("null bankname in CSV prepareToExport. That should not have happened.");
			return false;
		}
		List<String> colNames = cd.getSelectedColumns();
		
		if ((colNames == null) || colNames.isEmpty()) {
			Log.getInstance().error("null or empty column names in CSV prepareToExport. That should not have happened.");
			return false;
		}
		Log.getInstance().info("CSV exporting bank [" + bankName + "]");
		
		StringBuffer sb = new StringBuffer(256);
		sb.append("CSV exporting column[s] ");
		for (String c : colNames) {
			sb.append(" [" + c + "]");
		}
		
		Log.getInstance().info(sb.toString());
		
		
		//open a file for writing
		_exportFile = getFile("CSV Files", "csv", "csv", "CSV");
		if (_exportFile != null) {
			Log.getInstance().info("CSV: export to [" + _exportFile.getAbsolutePath() + "]");
			
			_columnData = new ArrayList<ColumnData>();
			
			for (String c : colNames) {
				ColumnData cdata = DataManager.getInstance().getColumnData(bankName, c);
				if (cdata == null) {
					Log.getInstance().error("null ColumnData in CSV prepareToExport. Bank [" + bankName + "] column [" + c + "]");
					return false;
				}
				Log.getInstance().info("CSV adding ColumnData [" + cdata.getFullName() + "]");
				_columnData.add(cdata);
			}

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
		count++;
		if ((count % 1000) == 0) {
			System.err.println("Export count: " + count);
		}
		
		if ((_columnData != null) && !_columnData.isEmpty()) {
			
			if (_first) {
				writeColumnNames();
				_first  = false;
			}
			
			int minNum = Integer.MAX_VALUE;
			int count = _columnData.size();
			
			ArrayList<double[]> doubleArrays = new ArrayList<>();
			
			//get the minimum common number of rows
			for (int i = 0; i < count; i++) {
				ColumnData cdata = _columnData.get(i);
				doubleArrays.add(cdata.getAsDoubleArray(event));
				int num = cdata.getLength(event);
				minNum = Math.min(minNum, num);
			}
			
			if (minNum < 1) {
				return;
			}

			// the data

			for (int index = 0; index < minNum; index++) {
				StringBuffer sb = new StringBuffer(512);
				for (int i = 0; i < count; i++) {
					double data[] = doubleArrays.get(i);

					if (i > 0) {
						sb.append(",");
					}
					
					String s = String.format("%5.4g", data[index]);
					sb.append(s);
					
					//sb.append(data[index]);
				}
				stringLn(_osw, sb.toString());
			}
		}
	}
	
	//write the column names
	private void writeColumnNames() {
		int count = _columnData.size();
		StringBuffer sb = new StringBuffer(512);
		String cname;
		
		for (int i = 0; i < count; i++) {
			ColumnData cdata = _columnData.get(i);
			if (i > 0) {
				sb.append(",");
			}
			cname = cdata.getColumnName();
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
	
	private static void stringLn(OutputStreamWriter osw, String str) {
		
		try {
			osw.write(str);
			
			osw.write("\n");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	

}
