package cnuphys.fastMCed.graphics;

import java.util.EnumMap;

public enum Sector {
	SECTOR1, SECTOR2, SECTOR3, SECTOR4, SECTOR5, SECTOR6;
	
	/**
	 * A map for the names of the attribute types
	 */
	public static EnumMap<Sector, String> names = new EnumMap<>(Sector.class);

	static {
		names.put(SECTOR1, "Sector 1");
		names.put(SECTOR2, "Sector 2");
		names.put(SECTOR3, "Sector 3");
		names.put(SECTOR4, "Sector 4");
		names.put(SECTOR5, "Sector 5");
		names.put(SECTOR6, "Sector 6");
	}
	
	/**
	 * Get the nice name of the sector.
	 *
	 * @return the nice name
	 */
	public String getName() {
		return names.get(this);
	}


}
