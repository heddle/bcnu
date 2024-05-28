package cnuphys.fastMCed.socket;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;

import org.jlab.clas.physics.Particle;
import org.jlab.clas.physics.PhysicsEvent;

import cnuphys.fastMCed.snr.SNRManager;
import cnuphys.snr.NoiseReductionParameters;

public class DataStreamerServer {
	private static final int PORT = 49152;
	private ServerSocket serverSocket;
	private final List<DataOutputStream> clientStreams = new CopyOnWriteArrayList<>();

	public static void main(String[] args) {
		new DataStreamerServer().startServer();
	}

	public void startServer() {
		try {
			serverSocket = new ServerSocket(PORT);
			System.out.println("Server started on port " + PORT);

			while (true) {
				Socket clientSocket = serverSocket.accept();
				System.out.println("Client connected.");
				DataOutputStream out = new DataOutputStream(clientSocket.getOutputStream());
				clientStreams.add(out);

				// Handle client disconnections
				new Thread(() -> {
					try {
						clientSocket.getInputStream().read();
					} catch (IOException e) {
						try {
							clientSocket.close();
						} catch (IOException ex) {
							ex.printStackTrace();
						}
						clientStreams.remove(out);
						System.out.println("Client disconnected.");
					}
				}).start();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// Call this method to send data events
	public void sendDataEvent(int numParticles, byte[][] snrData) {
		try {
			for (DataOutputStream out : clientStreams) {
				out.writeInt(numParticles);

				for (int i = 0; i < numParticles; i++) {
					out.writeInt(i); // q
					out.writeDouble(Math.random()); // x
					out.writeDouble(Math.random()); // y
					out.writeDouble(Math.random()); // z
					out.writeDouble(Math.random()); // p
					out.writeDouble(Math.random()); // theta
					out.writeDouble(Math.random()); // phi
				}

				for (byte[] row : snrData) {
					out.write(row);
				}
				out.flush();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Stream data to the client.
	 * 
	 * @param event The PhysicsEvent to stream.
	 */
	public void streamPhysicsEvent(PhysicsEvent event) {

		try {
			for (DataOutputStream out : clientStreams) {

				int numParticles = event.count();
				out.writeInt(numParticles);
				System.out.println("number of particles: " + numParticles);

				for (int i = 0; i < numParticles; i++) {
					Particle particle = event.getParticle(i);
					out.writeInt(particle.charge());
					out.writeDouble(particle.vertex().x());
					out.writeDouble(particle.vertex().y());
					out.writeDouble(particle.vertex().z());
					out.writeDouble(1000 * particle.p()); // convert to mev
					out.writeDouble(Math.toDegrees(particle.theta()));
					out.writeDouble(Math.toDegrees(particle.phi()));

				} // end of particle loop

				// now the snr results
				// data arrays filled left/right superlayers 1..6
				byte[][] snrData = new byte[12][112];

				// For now use only sector 1
				int sector = 1;
				int index = 0;
				for (int superLayer = 1; superLayer <= 6; superLayer++) {
					NoiseReductionParameters parameters = SNRManager.getInstance().getParameters(sector - 1,
							superLayer - 1);

					// left
					for (int wire = 0; wire < 112; wire++) {
						boolean leftSeg = parameters.getLeftSegments().checkBit(wire);
						if (leftSeg) {
							int numMiss = parameters.missingLayersUsed(NoiseReductionParameters.LEFT_LEAN, wire);
							snrData[index][wire] = (byte) (3 - numMiss);
						}
					}

					index++;
					// right
					for (int wire = 0; wire < 112; wire++) {
						boolean rightSeg = parameters.getRightSegments().checkBit(wire);
						if (rightSeg) {
							int numMiss = parameters.missingLayersUsed(NoiseReductionParameters.RIGHT_LEAN, wire);
							snrData[index][wire] = (byte) (3 - numMiss);
						}
					}

					index++;
				} // end of superlayer loop

				for (byte[] row : snrData) {
					out.write(row);
				}
				out.flush();
			} // end of client loop
		} catch (IOException e) {
			e.printStackTrace();
		}
	} // end of streamPhysicsEvent
}
