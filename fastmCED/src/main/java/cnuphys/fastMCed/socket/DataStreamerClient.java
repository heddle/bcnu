package cnuphys.fastMCed.socket;

import java.io.*;
import java.net.*;

public class DataStreamerClient {
    private static final int PORT = 49152;
    private static final String SERVER_IP = "127.0.0.1"; // Change this to the server IP if needed

    public static void main(String[] args) {
        new DataStreamerClient().connectToServer();
    }

    public void connectToServer() {
    	Socket socket = null;
        try {
            socket = new Socket(SERVER_IP, PORT);
            DataInputStream in = new DataInputStream(socket.getInputStream());

            while (true) {
            	if (in.available()==0) {
                    System.out.println("No data available, sleeping for 1000ms...");
                    Thread.sleep(1000);
            		continue;
            	}

                try {
                    int numParticles = in.readInt();
                    System.out.println("Number of particles: " + numParticles);

                    for (int i = 0; i < numParticles; i++) {
                        System.out.println("Particle " + i + ":");
                        System.out.println("q: " + in.readInt());
                        System.out.println("x: " + in.readDouble());
                        System.out.println("y: " + in.readDouble());
                        System.out.println("z: " + in.readDouble());
                        System.out.println("p: " + in.readDouble());
                        System.out.println("theta: " + in.readDouble());
                        System.out.println("phi: " + in.readDouble());
                    }

                    byte[][] snrData = new byte[12][112];
                    for (int i = 0; i < 12; i++) {
                        in.readFully(snrData[i]);
                    }
                } catch (EOFException | SocketException e) {
                    System.out.println("No data available, sleeping for 100ms...");
                    Thread.sleep(100);
                }
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            try {
				socket.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
        }
    }
}
