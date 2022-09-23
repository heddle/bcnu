package cnuphys.cnf.export;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.Collection;

import org.jlab.io.base.DataEvent;

import cnuphys.bCNU.log.Log;
import cnuphys.cnf.event.dictionary.Bank;
import cnuphys.cnf.event.dictionary.Column;
import cnuphys.cnf.event.dictionary.Dictionary;

public class MCParticleExporter extends AExporter {

	//the data output stream
	//private DataOutputStream _dos;

	private OutputStreamWriter _osw;


	//simple counter
	private int count;

	//the columns in the bank
	private Collection<Column> _columns;
	
	//the bank
	private Bank _mcParticleBank;


	private static final String MCParticleBankName = "MC::Particle";

	@Override
	public String getMenuName() {
		return "MC Particle";
	}

	@Override
	public boolean prepareToExport() {
		count = 0;
		Log.getInstance().info("MSParticle export requested");

		// reset
		_columns = null;

		_mcParticleBank = Dictionary.getInstance().getBank(MCParticleBankName);
		if (_mcParticleBank == null) {
			return false;
		}

		_columns = _mcParticleBank.getColumns();

		// open a file for writing
		_exportFile = getFile("CSV Files", "csv", "csv", "CSV");
		if (_exportFile != null) {

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

		} // export file != null
		else {
			return false;
		}

		return true;
	}

	@Override
	public void nextEvent(DataEvent event) {
		if (_columns == null) {
			return;
		}
		
		//have data?
		if (!_mcParticleBank.hasData(event)) {
			System.out.println("NO DATA");
			return;
		}
		System.out.println("HAS DATA");
		
		count++;
		if ((count % 100) == 0) {
			System.err.println("Export count: " + count);
		}
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
