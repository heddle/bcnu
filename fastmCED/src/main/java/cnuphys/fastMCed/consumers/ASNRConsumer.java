package cnuphys.fastMCed.consumers;

import cnuphys.fastMCed.snr.SNRManager;

public abstract class ASNRConsumer extends PhysicsEventConsumer {

	protected String errStr = "???";


	protected SNRManager snr = SNRManager.getInstance();

	@Override
	public String flagExplanation() {
		return errStr;
	}



}
