package cnuphys.fastMCed.consumers;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.jlab.clas.physics.PhysicsEvent;

import cnuphys.bCNU.util.CSVWriter;
import cnuphys.fastMCed.fastmc.ParticleHits;
import cnuphys.fastMCed.snr.SNRManager;
import cnuphys.fastMCed.streaming.StreamProcessStatus;
import cnuphys.fastMCed.streaming.StreamReason;
import cnuphys.lund.GeneratedParticleRecord;

public class CSVTestDataConsumer extends ASNRConsumer {

	private CSVWriter _csvWriter;

	@Override
	public String getConsumerName() {
		return "CSV File for ML Test Data";
	}

	/**
	 * A message about a change in the streaming state.
	 * 
	 * @param reason the reason for the change. It will be one of the
	 *               self-explanatory values of the StreamReason class:<br>
	 *               STARTED, STOPPED, PAUSED, FINISHED, RESUMED
	 */
	@Override
	public void streamingChange(StreamReason reason) {

		System.out.println("CSVTestDataConsumer streaming change reason: [" + reason.name() + "]");

		if (reason == StreamReason.STARTED) {
			System.out.println("Stream Started");
			String homeDir = System.getProperty("user.home");
			
			new File(homeDir, "testdata").mkdirs();

			File file = new File(homeDir, "testdata/mltest.csv");
				
			if (!file.exists()) {
				try {
					file.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
			_csvWriter = new CSVWriter(file);
			writeHeaderRow();

		} else if (reason == StreamReason.PAUSED) {
			System.out.println("Stream Paused");
		} else if (reason == StreamReason.STOPPED) {
			System.out.println("Stream Stopped");
			if (_csvWriter != null) {
				_csvWriter.close();
			}
		}

	}

	//write the header row
	private void writeHeaderRow() {
		_csvWriter.writeRow("sector", "direct", "w1", "w2", "w3", "w4", "w5", "w6", "w7", "w8", "w9", "w10", "w11", "w12", "xo", "yo",
				"zo", "p", "theta", "phi");
	}

	/**
	 * A new event in the stream. This occurs when FastMCed is not looking event by
	 * event, but when it is quickly streaming through a large number of events.
	 * NOTE: this is NOT on a separate thread.It will in fact be on the GUI thread.
	 * This is by design.
	 * 
	 * @param event the new event arriving through the FastMCed streaming mechanism.
	 * @return StreamProcessStatus.CONTINUE (success) or StreamingReason.FLAG
	 *         (problem). Any consumer returning StreamingReason.FLAG will halt the
	 *         process and cause the GUI to display the event that caused the
	 *         StreamProcessStatus.FLAG. The normal return (nothing interesting) is
	 *         StreamProcessStatus.CONTINUE
	 */
	@Override
	public StreamProcessStatus streamingPhysicsEvent(PhysicsEvent event, List<ParticleHits> particleHits) {

		// first test, right benders only, segments in all sectors

		boolean haveRightSegsAllSectors = snr.segmentsInAllSuperlayers(0, SNRManager.RIGHT);
		
		String wordcsv[] = new String[6];

		if (haveRightSegsAllSectors) {
			GeneratedParticleRecord gpr = particleHits.get(0).getGeneratedParticleRecord();
			String gprcsv = gpr.csvString();

			// get the zero based sector
			int sect0 = gpr.getSector() - 1;
			
			_csvWriter.writeStartOfRow(""+(sect0+1), "R");
			
			
			for (int superlayer = 0; superlayer < 6; superlayer++) {
				wordcsv[superlayer] = snr.csvSegmentWords(sect0, superlayer, SNRManager.RIGHT);
			}
	
			if (_csvWriter != null) {
				_csvWriter.writeRow(wordcsv[0], wordcsv[1], wordcsv[2], wordcsv[3], wordcsv[4], wordcsv[5], gprcsv);
			}

		}

		return StreamProcessStatus.CONTINUE;
	}

	/**
	 * Get the sector [1..6] from the phi value
	 *
	 * @param phi the value of phi in degrees
	 * @return the sector [1..6]
	 */
	public static int getSector(double phi) {
		// convert phi to [0..360]

		while (phi < 0) {
			phi += 360.0;
		}
		while (phi > 360.0) {
			phi -= 360.0;
		}

		if ((phi > 330) || (phi <= 30)) {
			return 1;
		}
		if (phi <= 90.0) {
			return 2;
		}
		if (phi <= 150.0) {
			return 3;
		}
		if (phi <= 210.0) {
			return 4;
		}
		if (phi <= 270.0) {
			return 5;
		}
		return 6;
	}

	/**
	 * New event has arrived from the FastMC engine via the "next event" mechanism.
	 * Note that in streaming mode, do not get broadcast this way, they are
	 * broadcasted via streamingPhysicsEvent
	 * 
	 * @param event the generated physics event
	 * @see cnuphys.fastMCed.streaming.IStreamProcessor
	 */
	@Override
	public void newPhysicsEvent(PhysicsEvent event, List<ParticleHits> particleHits) {
	}

}
