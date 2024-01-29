package cnuphys.ced.alldata.datacontainer;

import java.awt.Color;

import cnuphys.ced.alldata.DataWarehouse;
import cnuphys.ced.clasio.ClasIoEventManager;
import cnuphys.ced.event.data.AdcColorScale;

public abstract class ACommonADCData implements IDataContainer {

	
	// the data warehouse
	protected static DataWarehouse _dataWarehouse = DataWarehouse.getInstance();

	// event manager
	protected static ClasIoEventManager _eventManager = ClasIoEventManager.getInstance();

	
	/** 1-based sectors */
	public byte[] sector;

	/** 1-based layer*/
	public byte layer[];
	
	/** 1-based component*/
	public short component[];

	/** Used for subdivisions like left/right */
	public byte order[];
	
	/** adc value */
	public int adc[];

	/** time value */
	public float time[];
	
	/** max adc value */
	public int maxADC;

	
	/**
	 * Create a data container and notify the data warehouse that it wants to be
	 * notified of data events.
     */
	public ACommonADCData() {
		_dataWarehouse.addDataContainerListener(this);
	}


	@Override
	public void clear() {
		sector = null;
		layer = null;
		component = null;
		order = null;
		adc = null;
		time = null;
	}

	@Override
	public int count() {
		return (sector == null) ? 0 : sector.length;
	}
	

	/**
	 * Get the color for a given adc value
	 * @param adc the adc value
	 * @return the color
	 */
	public Color getADCColor(int adc) {
		if (adc > 0) {
			double fract = ((double) adc) / maxADC;
			fract = Math.max(0, Math.min(1.0, fract));
			int alpha = 128 + (int) (127 * fract);
			alpha = Math.min(255, alpha);

			return AdcColorScale.getInstance().getAlphaColor(fract, alpha);
		}
		return ADCZERO;
	}

	// compute the max adc
	protected void computeMaxADC() {
		// get the max adc
		int n = (adc == null) ? 0 : adc.length;
		maxADC = 0;

		for (int i = 0; i < n; i++) {
			int a = adc[i];
			if (a > maxADC) {
				maxADC = a;
			}
		}

	}

}
