package cnuphys.cnf.export;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;

import org.jlab.clas.physics.Particle;
import java.awt.Dimension;
import java.awt.Toolkit;

import org.jlab.detector.base.DetectorType;
import org.jlab.groot.data.DataLine;
import org.jlab.groot.data.H1F;
import org.jlab.groot.data.H2F;
import org.jlab.groot.ui.TCanvas;
import org.jlab.io.base.DataBank;
import org.jlab.io.base.DataEvent;
import org.jlab.io.base.DataSource;
import org.jlab.io.hipo.HipoDataSource;
import org.jlab.jnp.hipo4.io.HipoReader;

import cnuphys.eventManager.event.EventManager;

//import cnuphys.cnf.event.dictionary.Bank;
//import cnuphys.cnf.event.dictionary.Column;
//import cnuphys.cnf.event.dictionary.Dictionary;

public class RecParticleExporter extends AExporter {

	static Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	static int xDim = (int) (0.85 * screenSize.getWidth());
	static int yDim = (int) (0.85 * screenSize.getHeight());

	// -----Plots-----//
	static H1F hist_Q2_part = new H1F("hQ2", 100, 0, 6);
	static H1F hist_W_part = new H1F("hW", 100, 1, 4);
	static H2F hist2_Q2vsW = new H2F("Q2 vs W", 100, 1, 4, 100, 1, 6);

	// -----IMPORTANT VALUES-----//
	private final static double targetMass = 0.938272;
	private final static double beamEnergy = 6.535;

	// the data output stream
	// private DataOutputStream _dos;
	private OutputStreamWriter _osw;
	
	//used to write column data names
	private boolean _first = true;



	private int nevent;
	private int ntrack = 0;
	
	@Override
	public String getMenuName() {
		return "RECParticle";
	}

	@Override
	public boolean prepareToExport() {
		nevent = 0;

		// open a file for writing
		_exportFile = getFile("CSV Files", "csv", "csv", "CSV");
		if (_exportFile != null) {

			try {
				OutputStream os = new FileOutputStream(_exportFile);
				_osw = new OutputStreamWriter(os, "UTF-8");

				// _dos = new DataOutputStream(new FileOutputStream(_exportFile));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}

		} // export file != null
		else {
			return false;
		}

		_first = true;

		return true;
	}

	// -----Functions-----//
	public static void properties_single_H1F(H1F plot, String mainTitle, String xTitle, int color) {
		plot.setTitle(mainTitle);
		plot.setTitleX(xTitle);
		plot.setTitleY("Counts");
		plot.setFillColor(color);
	}

	public static void properties_canvas_H1F_0line(TCanvas canvas, H1F plot, int position) {
		canvas.cd(position).draw(plot);
		canvas.getPad().setAxisLabelFontSize(18);
		canvas.getPad().setAxisTitleFontSize(24);
		canvas.getPad().setTitleFontSize(18);
	}

	public static void properties_single_H2F(H2F plot, String mainTitle, String xTitle, String yTitle) {
		plot.setTitle(mainTitle);
		plot.setTitleX(xTitle);
		plot.setTitleY(yTitle);
	}

	public static void properties_canvas_H2F(TCanvas canvas, H2F plot, int position) {
		canvas.cd(position).draw(plot);
		canvas.getPad().setAxisLabelFontSize(18);
		canvas.getPad().setAxisTitleFontSize(24);
		canvas.getPad().setTitleFontSize(18);
	}

	//write the header row
	private void writeHeader() {
		stringLn(_osw, "event, status/1000, pid, px, py, pz, vx, vy, vz, q2, w");
	}
	
	
	//write rec particle bank
	private void writeRecParticle(int event, short  status, 
			int pid, 
			float px, float py, float pz,
			float vx, float vy, float vz,
			double q2, double w) {
		
		String s;
		if (q2 < 0) {	
			s = String.format("%d,%d,%d,%4.2f,%4.2f,%4.2f,%4.2f,%4.2f,%4.2f,-1,-1",
					event, status, pid, px, py, pz, vx, vy, vz);
		}
		else {
			s = String.format("%d,%d,%d,%4.2f,%4.2f,%4.2f,%4.2f,%4.2f,%4.2f,%4.2f,%4.2f",
					event, status, pid, px, py, pz, vx, vy, vz, q2, w);
		}
		
		
		stringLn(_osw, s);
	}
	
	private void writeRecParticle(int event, int row, DataBank recPart) {
		
		int pid = recPart.getInt("pid", row);
		
		
		Particle beam_Part = new Particle(11, 0, 0, beamEnergy, 0, 0, 0);
		Particle target_Part = Particle.createWithMassCharge(targetMass, +1, 0, 0, 0, 0, 0, 0);
		Particle electron = null;
//		Particle proton = null;
		Particle q_Part = new Particle();
		Particle w_Part = new Particle();

		
	//	float beta = recPart.getFloat("beta", row);
	//	byte charge = recPart.getByte("charge", row);
	//	float chi2pid = recPart.getFloat("chi2pid", row);
		
		float px = recPart.getFloat("px", row);
		float py = recPart.getFloat("py", row);
		float pz = recPart.getFloat("pz", row);
		short status = (short) (recPart.getShort("status", row)/1000);

	//	float vt = recPart.getFloat("vt", row);
		float vx = recPart.getFloat("vx", row);
		float vy = recPart.getFloat("vy", row);
		float vz = recPart.getFloat("vz", row);
		
		double q2 = -1;
		double w = -1;

		// electron is trigger particle (ii==0) and has correct pid
		if (pid == 11 && row == 0) {
			electron = new Particle(pid, px, py, pz, vx, vy, vz);

			// W and Q2
			q_Part.copy(beam_Part);
			q_Part.combine(electron, -1);
			w_Part.copy(target_Part);
			w_Part.combine(q_Part, +1);

			q2 = -q_Part.mass2();
			w = w_Part.mass();
		}
		
		writeRecParticle(event, status, pid, px, py, pz, vx, vy, vz, q2, w);
	}

	
	@Override
	public void nextEvent(DataEvent event) {
		
		
		if (_first) {
			writeHeader();
			_first  = false;
		}


		nevent++;

		DataSource reader = EventManager.getInstance().getDataSource();
		if (nevent % 5000 == 0)
			System.out.printf("Analyzed %d events - %.1f%% complete %n", nevent,
					100 * ((double) nevent / /* (double) nmax */ (double) reader.getSize()));

		// get relevant data banks
		DataBank recPart = null;
		DataBank recTrack = null;
		DataBank recTraj = null;
		DataBank recScin = null;
		DataBank recCal = null;
		DataBank recCher = null;
		if (event.hasBank("REC::Particle"))
			recPart = event.getBank("REC::Particle");
		if (event.hasBank("REC::Track"))
			recTrack = event.getBank("REC::Track");
		if (event.hasBank("REC::Traj"))
			recTraj = event.getBank("REC::Traj");
		if (event.hasBank("REC::Scintillator"))
			recScin = event.getBank("REC::Scintillator");
		if (event.hasBank("REC::Calorimeter"))
			recCal = event.getBank("REC::Calorimeter");
		if (event.hasBank("REC::Cherenkov"))
			recCher = event.getBank("REC::Cherenkov");

		// ignore events that don't have necessary banks
		if (recPart == null || recTrack == null || recTraj == null || recScin == null || recCal == null
				|| recCher == null) {
			return;
		}
		

		// ----------------------------//
		// ----------PARTICLES---------//
		// ----------------------------//
		Particle beam_Part = new Particle(11, 0, 0, beamEnergy, 0, 0, 0);
		Particle target_Part = Particle.createWithMassCharge(targetMass, +1, 0, 0, 0, 0, 0, 0);
		Particle electron = null;
		Particle proton = null;
		Particle q_Part = new Particle();
		Particle w_Part = new Particle();
		

		// -----------------------------//
		// --------REC::Particle--------//
		// -----------------------------//
		for (int ii = 0; ii < recPart.rows(); ii++) {
			
			writeRecParticle(nevent, ii, recPart);
			
			int pid = recPart.getInt("pid", ii);

			if (pid != 11 && pid != 2212) {
				continue;
			}

			float chi2pid = recPart.getFloat("chi2pid", ii);
			int status = Math.abs(recPart.getShort("status", ii));
			// if status_abs == 2 then particle is in FD but if status_abs == 4 then
			// particle is in CD
			int status_abs = status / 1000;
			int charge = recPart.getInt("charge", ii);
			float b = recPart.getFloat("beta", ii);
			float px = recPart.getFloat("px", ii);
			float py = recPart.getFloat("py", ii);
			float pz = recPart.getFloat("pz", ii);
			float vx = recPart.getFloat("vx", ii);
			float vy = recPart.getFloat("vy", ii);
			float vz = recPart.getFloat("vz", ii);

			// double p = Math.sqrt(px*px + py*py + pz*pz);

			Particle part = new Particle(pid, px, py, pz, vx, vy, vz);

			double p = part.p();

			// electron is trigger particle (ii==0) and has correct pid
			if (pid == 11 && ii == 0) {
				electron = part;

				// W and Q2
				q_Part.copy(beam_Part);
				q_Part.combine(electron, -1);
				w_Part.copy(target_Part);
				w_Part.combine(q_Part, +1);

				hist_Q2_part.fill(-q_Part.mass2());
				hist_W_part.fill(w_Part.mass());
				hist2_Q2vsW.fill(w_Part.mass(), -q_Part.mass2());

				// check REC::Traj in the Drift Chambers
				for (int it = 0; it < recTraj.rows(); it++) {
					int pindex_traj = recTraj.getShort("pindex", it);
					int layer_traj = recTraj.getByte("layer", it);
					int detector_traj = recTraj.getByte("detector", it);
					// check if the calorimeter bank has same index as row in particle bank
					if (pindex_traj == ii) {
						// check detector is DC
						if (detector_traj == DetectorType.DC.getDetectorId()) {
							int region = (int) (layer_traj - 1) / 12 + 1;
							// System.out.printf("Region in DC: %d\n", region);
						}
					}
				} // end of recTrack rows
			} // end of check for electron

			// proton has correct pid
			if (pid == 2212) {
				proton = part;
			} // end of check for proton

		} // end of loop over recPart rows

	}

	@Override
	public void done() {
		
		System.out.println("\nFINISHED ANALYZING HIPO FILE!");
		
		properties_single_H1F(hist_Q2_part, "all e", "Q^2 [GeV^2]", 25);
      	properties_single_H1F(hist_W_part, "all e", "W [GeV]", 25);
      	properties_single_H2F(hist2_Q2vsW, "all e", "W [GeV]", "Q^2 [GeV^2]");
      	
      	TCanvas canvas = new TCanvas(EventManager.getInstance().getDataSourceName(), xDim, yDim);
      	canvas.divide(3,1); //cols, rows
      	canvas.setTitle("W and Q2 for trigger electrons");

  		properties_canvas_H1F_0line(canvas, hist_Q2_part, 0);
  		properties_canvas_H1F_0line(canvas, hist_W_part, 1);
  		properties_canvas_H2F(canvas, hist2_Q2vsW, 2);

		System.out.println("RecParticle export: I am done");

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
