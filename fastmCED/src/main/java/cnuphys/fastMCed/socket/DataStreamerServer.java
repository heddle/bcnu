package cnuphys.fastMCed.socket;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import org.jlab.clas.physics.Particle;
import org.jlab.clas.physics.PhysicsEvent;

import cnuphys.fastMCed.snr.SNRManager;
import cnuphys.snr.NoiseReductionParameters;

public class DataStreamerServer {
	
	//listening for connections?
	private boolean _listening = false;

	// The server socket
    private static ServerSocket _serverSocket;
    
    // The client socket
    private static ArrayList<ProxyClient> _clientSocket = new ArrayList<>();
    
    //SNR Manager
	private SNRManager _snr = SNRManager.getInstance();
	

	/**
	 * Constructor for the DataStreamer class.
	 * 
	 * @param port The port number to listen on.
	 * @throws IOException If an I/O error occurs.
	 */
	public DataStreamerServer(int port) throws IOException {
		// Initialize the server socket on the provided port

		if (_serverSocket != null) {
			close();
		}

		System.out.println("Creating server socket on port " + port);
		_serverSocket = new ServerSocket(port);

		System.out.println("Server socket created on port " + port);
		startListening();
	}

	/**
	 * Start listening for a client connection and stream data to the client.
	 * 
	 * @throws IOException If an I/O error occurs.
	 */
	public void startListening() throws IOException {

		System.out.println("Starting server, listening for connections...");
		_listening = true;

		Runnable runner = new Runnable() {

			@Override
			public void run() {

				while (_listening) {
					try {
						// Wait for a client connection
						ProxyClient client = new ProxyClient(_serverSocket.accept());
						System.out.println("Client connected.");
						_clientSocket.add(client);
//						testStream(client);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}

		};

		Thread t = new Thread(runner);
		t.start();

	}

	/**
	 * Close the server socket.
	 */
	public void close() {
		try {
			System.out.println("Server closing...");
			_listening = false;
			_serverSocket.close();
			_serverSocket = null;
			System.out.println("Server closed.");
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
		
		int numParticles = event.count();
		System.out.println("number of particles: " + numParticles);
		
		for (int i = 0; i < numParticles; i++) {
			Particle particle = event.getParticle(i);
			int q = particle.charge();
			double x = particle.vertex().x();
			double y = particle.vertex().y();
			double z = particle.vertex().z();
			double p = 1000*particle.p(); //convert to mev
			double theta = Math.toDegrees(particle.theta());
			double phi = Math.toDegrees(particle.phi());
			
			String s = String.format("Particle %d q = %d x = %-7.4f  y = %-7.4f  z = %-7.4f  p = %-7.4f  theta = %-7.4f  phi = %-7.4f ", (i+1), q, x, y, z, p, theta, phi);
			
			System.out.println(s);
		}
		
		
		//data arrays filled left/right superlayers 1..6
		 byte[][] byteArrays = new byte[12][112];
		 
		// For now use only sector 1
		int sector = 1;
		int index = 0;
		for (int superLayer = 1; superLayer <= 6; superLayer++) {
			NoiseReductionParameters parameters = SNRManager.getInstance().getParameters(sector - 1, superLayer - 1);
			
			//left
			for (int wire = 0; wire < 112; wire++) {
				boolean leftSeg = parameters.getLeftSegments().checkBit(wire);
				if (leftSeg) {
					int numMiss = parameters.missingLayersUsed(NoiseReductionParameters.LEFT_LEAN, wire);
					byteArrays[index][wire] = (byte) (3 - numMiss);
				}
			}
			
			index++;
			//right
			for (int wire = 0; wire < 112; wire++) {
				boolean rightSeg = parameters.getRightSegments().checkBit(wire);
				if (rightSeg) {
					int numMiss = parameters.missingLayersUsed(NoiseReductionParameters.RIGHT_LEAN, wire);
					byteArrays[index][wire] = (byte) (3 - numMiss);
				}
			}
			
			index++;
		}
		
		writeBytes(byteArrays);
	}
	
	private void writeBytes(byte[][] byteArrays) {
		//write in rev to match picture
		System.out.println();
		for (int i = 11; i >= 0; i--) {
            for (int j = 111; j >= 0; j--) {
                System.out.print(byteArrays[i][j]);
            }
            System.out.println();
        }
		
	}

//	private void testStream(ProxyClient client) throws IOException {
//		// Example usage of the streamData method
//		// This part can be modified or replaced as needed
//		int intValue = 123;
//		double[] reals = { 1.1, 2.2, 3.3, 4.4, 5.5, 6.6 };
//		byte[][] byteArrays = new byte[12][112]; // Assuming the arrays are initialized elsewhere
//
//		client.stream(intValue);
//		client.stream(reals);
//		client.stream(byteArrays);
//		client.flush();
//	}
    
    

//    public static void main(String[] args) {
//        try {
//            // Initialize and start the server on port 12345 (example port)
//            new DataStreamerServer(12345);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
    
    //inner class to handle the client
	class ProxyClient {
		
		private DataOutputStream dos;
		
		public ProxyClient(Socket socket) throws IOException {
			super();
			dos = new DataOutputStream(socket.getOutputStream());
		}
		
		/**
		 * Stream an int.
		 * 
		 * @param val The int to stream.
		 * @throws IOException If an I/O error occurs.
		 */
	    public void stream(int val) throws IOException  {
	        // Stream an int
	        dos.writeInt(val);
	   	
	    }
	    
		/**
		 * Stream an array of doubles.
		 * 
		 * @param vals The array of doubles to stream.
		 * @throws IOException If an I/O error occurs.
		 */
	    public void stream(double vals[]) throws IOException  {
	        // Stream an array of doubles
			for (double val : vals) {
				dos.writeDouble(val);
			}
	    }

		/**
		 * Stream an array of byte arrays.
		 * 
		 * @param byteArrays The array of byte arrays to stream.
		 * @throws IOException If an I/O error occurs.
		 */
	    public void stream(byte[][] byteArrays) throws IOException  {
	        // Stream twelve byte arrays
	        for (byte[] byteArray : byteArrays) {
	            if (byteArray.length != 112) {
	                throw new IllegalArgumentException("Each byte array must hold 112 bytes.");
	            }
	            dos.write(byteArray);
	        }
	    }
	    
	    
	    /**
	     * Flush the output stream.
	     * @throws IOException
	     */
		public void flush() throws IOException {
			dos.flush();
		}


	}

}



