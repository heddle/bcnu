package cnuphys.ced.geometry.util;

public class CountHolder {
	
	/**
	 * Holds num superlayer per sector, num layer per superlayer, num components per layer
	 */
	
	/** the number of sectors */
	public int numSector;
	
	/** superlayer holders, one per sector */
	private SuperlayerCount[] superlayers;
	
	public CountHolder(int numSect) {
		numSector = numSect;
		
		superlayers = new SuperlayerCount[numSector];
		for (int i = 0; i < numSector; i++) {
			superlayers[i] = new SuperlayerCount();
		}
	}
	
	/**
	 * Set the number of superlayers
	 * @param 0-based sector
	 * @param superlayercount number of superlayers for this sector
	 */
	public void setNumSuperlayer(int sector, int superlayercount) {
		
	}

	
	private class SuperlayerCount {
		
		private int numSuperlayer;
		
		private SuperlayerCount() {
			
		}
	}

}
