package cnuphys.fastMCed.consumers;

import java.util.List;

import org.jlab.clas.physics.PhysicsEvent;

import cnuphys.fastMCed.fastmc.ParticleHits;
import cnuphys.fastMCed.streaming.StreamProcessStatus;
import cnuphys.fastMCed.streaming.StreamReason;
import cnuphys.fastMCed.socket.DataStreamerServer;

public class SocketConsumer extends ASNRConsumer {

	//the port to listen on
	private int _port = 49152;
	
	//the server for sending dtat
	private DataStreamerServer _server;

	@Override
	public String getConsumerName() {
		return "Socket Consumer";
	}

	@Override
	public void streamingChange(StreamReason reason) {

		System.out.println("CSVTestDataConsumer streaming change reason: [" + reason.name() + "]");

		if (reason == StreamReason.STARTED) {
		} else if (reason == StreamReason.PAUSED) {
		} else if (reason == StreamReason.STOPPED) {
			System.out.println("Stream Stopped");
		}

	}

	private static long count = 0;
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
		
		if (_server == null) {
			try {
				_server = new DataStreamerServer(_port);
				System.out.println("Server created on port " + _port);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		_server.streamPhysicsEvent(event);
		
        System.out.println("SocketConsumer.streamingPhysicsEvent: " + (++count));
		return null;
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
