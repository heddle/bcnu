package cnuphys.cnf.export;

import java.io.FileNotFoundException;

import org.jlab.clas.physics.Particle;
import org.jlab.detector.base.DetectorType;
import org.jlab.io.base.DataBank;
import org.jlab.io.base.DataEvent;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;


//import cnuphys.cnf.event.dictionary.Bank;
//import cnuphys.cnf.event.dictionary.Column;
//import cnuphys.cnf.event.dictionary.Dictionary;

public class RecParticleExporter extends AExporter {
	
	//-----IMPORTANT VALUES-----//
    private final static double targetMass = 0.938272;
    private final static double beamEnergy = 6.535;
 

	//the data output stream
	//private DataOutputStream _dos;
	private OutputStreamWriter _osw;

	//simple counter
	private int count;


	@Override
	public String getMenuName() {
		return "RECParticle";
	}

	@Override
	public boolean prepareToExport() {
		count = 0;

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
		
		
		DataBank recPart = null;
		DataBank recTrack = null;
		DataBank recTraj = null;
		DataBank recScin = null;
		DataBank recCal = null;
		DataBank recCher = null;
		
		if(event.hasBank("REC::Particle")) 		recPart = event.getBank("REC::Particle");
		if(event.hasBank("REC::Track"))			recTrack = event.getBank("REC::Track");
		if(event.hasBank("REC::Traj"))			recTraj = event.getBank("REC::Traj");
		if(event.hasBank("REC::Scintillator"))	recScin = event.getBank("REC::Scintillator");
		if(event.hasBank("REC::Calorimeter"))	recCal = event.getBank("REC::Calorimeter");
		if(event.hasBank("REC::Cherenkov"))		recCher = event.getBank("REC::Cherenkov");
		
		
		if (recPart == null || recTrack == null || recTraj == null || recScin == null || recCal == null || recCher == null) return;
		
		
		
		Particle beam_Part = new Particle(11, 0, 0, beamEnergy, 0, 0, 0);
        Particle target_Part = Particle.createWithMassCharge(targetMass, +1, 0,0,0, 0,0,0);
        Particle electron = null;
        Particle proton = null;
	
        for(int ii = 0; ii < recPart.rows(); ii++) {
        	int pid = recPart.getInt("pid", ii);
        	
        	if (pid == 0) {
        		continue;
        	}
        	
			float chi2pid = recPart.getFloat("chi2pid", ii);
            int status = Math.abs(recPart.getShort("status", ii));
            //if status_abs == 2 then particle is in FD but if status_abs == 4 then particle is in CD
            int status_abs = status/1000;
            int charge = recPart.getInt("charge", ii);
            float b = recPart.getFloat("beta", ii);
    		float px = recPart.getFloat("px", ii);
			float py = recPart.getFloat("py", ii);
			float pz = recPart.getFloat("pz", ii);
			
			Particle part = new Particle(pid,
					recPart.getFloat("px",ii),
					recPart.getFloat("py",ii),
					recPart.getFloat("pz",ii),
					recPart.getFloat("vx",ii),
                    recPart.getFloat("vy",ii),
                    recPart.getFloat("vz",ii));
			
			double p = part.p();
			
			//electron is trigger particle (ii==0) and has correct pid
			if(pid == 11 && ii == 0) {
				electron = part;
				
				//check REC::Traj in the Drift Chambers
				for(int it = 0; it < recTraj.rows(); it++) {
					int pindex_traj = recTraj.getShort("pindex", it);
            		int layer_traj = recTraj.getByte("layer", it);
            		int detector_traj = recTraj.getByte("detector", it);
            		// check if the calorimeter bank has same index as row in particle bank
            		if (pindex_traj == ii) {
            			// check detector is DC
            			if (detector_traj == DetectorType.DC.getDetectorId()) {
            				int region = (int) (layer_traj-1)/12 + 1;
            				//System.out.printf("Region in DC: %d\n", region);
            			}
            		}
				} //end of recTrack rows
			} //end of check for electron
			
			//proton has correct pid
			if(pid == 2212) {
				proton = part;
			} //end of check for proton
        }
		
		
//		if (_columns == null) {
//			return;
//		}
//		
//		//have data?
//		if (!_recParticleBank.hasData(event)) {
//			System.out.println("NO DATA");
//			return;
//		}
//	//	System.out.println("HAS DATA");
//		
		count++;
		if ((count % 1000) == 0) {
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
