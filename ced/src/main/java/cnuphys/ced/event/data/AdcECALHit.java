package cnuphys.ced.event.data;

import java.util.List;

import cnuphys.lund.DoubleFormat;

public class AdcECALHit implements Comparable<AdcECALHit> {

	// for feedback strings
	private static final String _fbColor = "$Light Coral$";

	public final byte sector;
	public final byte layer;
	public final short component;
	public int adc;
	public short ped;
	public float time =-1;
	public byte order;
	public short occurances = 1;
	

	public AdcECALHit(byte sector, byte layer, short component, byte order, int adc, float time, short ped) {
		super();
		this.sector = sector;
		this.layer = layer;
		this.component = component;
		this.order = order;
		this.adc = adc;
		this.time = time;
		this.ped = ped;
		occurances = 1;
	}
	
	/**
	 * Just used for binary sort
	 * @param sector
	 * @param layer
	 * @param component
	 */
	public AdcECALHit(byte sector, byte layer, short component) {
		super();
		this.sector = sector;
		this.layer = layer;
		this.component = component;
		occurances = 1;
	}


	@Override
	public int compareTo(AdcECALHit hit) {
		int c = Integer.valueOf(sector).compareTo(Integer.valueOf(hit.sector));
		if (c == 0) {
			c = Integer.valueOf(layer).compareTo(Integer.valueOf(hit.layer));
			if (c == 0) {
				c = Integer.valueOf(component).compareTo(Integer.valueOf(hit.component));
			}
		}
		return c;
	}
	
	public void swapValues(AdcECALHit  hit) {
		
		if ((sector != hit.sector) || (layer != hit.layer) || (component != hit.component)) {
			System.err.println("Mismatch in TdcAdcECALHit swapValues");
			return;
		}
		adc = hit.adc;
		ped = hit.ped;
		time = hit.time;
		order = hit.order;
	}
	
	/**
	 * String used for hashing to look for duplicates
	 * @return hash string
	 */
	public String hash() {
		return String.format("%d|%d|%d", sector, layer, component);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if ((obj == null) || (getClass() != obj.getClass()))
			return false;
		AdcECALHit other = (AdcECALHit) obj;
		if ((sector != other.sector) || (layer != other.layer) || (component != other.component))
			return false;
		return true;
	}

	/**
	 * Get the average ADC value
	 *
	 * @return the average of left and right
	 */
	public int averageADC() {
		return adc;
	}

	// make a sensible doca string
	private String timeString() {
		if (Float.isNaN(time)) {
			return "";
		}
		return "time " + DoubleFormat.doubleFormat(time, 3);

	}

	/**
	 * Get a string for just the tdc data
	 *
	 * @return a string for just the tdc data
	 */
	private String valString(int val, String name) {
		if (val < 0) {
			return "";
		}
		return name + " " + val;
	}

	/**
	 * Get a string for just the ped data
	 *
	 * @return a string for just the ped data
	 */
	public String pedString() {
		return valString(ped, "ped");
	}

	/**
	 * Get a string for just the tdc data
	 *
	 * @return a string for just the tdc data
	 */
	public String adcString() {
		return valString(adc, "adc");
	}

	@Override
	public String toString() {
		return "sector = " + sector + " layer " + layer + " component: " + component  + " "
				+ adcString() + " " + pedString() + " " + timeString();
	}

	/**
	 * Add this hit to the feedback list
	 *
	 * @param layerName       a nice name for the layer
	 * @param componentName   a nice name for the component
	 * @param feedbackStrings
	 */
	public void adcFeedback(String layerName, String componentName, List<String> feedbackStrings) {
		feedbackStrings.add(_fbColor + "sector " + sector + " " + layerName + "  " + componentName + " " + component);
		feedbackStrings.add(String.format("%s%s %s %s",_fbColor,  adcString(), pedString(), timeString()));
	}

}
