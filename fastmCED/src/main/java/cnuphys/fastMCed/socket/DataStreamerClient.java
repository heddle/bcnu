package cnuphys.fastMCed.socket;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class DataStreamerClient {

    public static void main(String[] args) {
        String serverName = "localhost"; // Server name or IP
        int port = 12345; // Port must match the server's listening port

        try (Socket server = new Socket(serverName, port);
             DataInputStream dis = new DataInputStream(server.getInputStream())) {
        	
//        	if (dis.available() == 0) {
//				System.out.println("No data available.");
//				return;
//        	}
//            
            // Read an int
            int intValue = dis.readInt();
            System.out.println("Received int: " + intValue);

            // Read six reals (doubles)
            double[] reals = new double[6];
            for (int i = 0; i < reals.length; i++) {
                reals[i] = dis.readDouble();
                System.out.println("Received real #" + (i + 1) + ": " + reals[i]);
            }

            // Read twelve byte arrays
            byte[][] byteArrays = new byte[12][112];
            for (int i = 0; i < byteArrays.length; i++) {
                dis.readFully(byteArrays[i]); // Ensure reading exactly 112 bytes for each array
                System.out.println("Received byte array #" + (i + 1) + ": " + byteArrayToHex(byteArrays[i]));
            }
 
        } catch (UnknownHostException e) {
            System.err.println("Host unknown: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("I/O Error: " + e.getMessage());
        }
    }

    // Helper method to convert byte array to a hex string for readable output
    private static String byteArrayToHex(byte[] a) {
        StringBuilder sb = new StringBuilder(a.length * 2);
        for(byte b: a)
            sb.append(String.format("%02x", b));
        return sb.toString();
    }
}
