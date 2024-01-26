package cnuphys.ced.alldata.datacontainer;

import java.awt.Color;
import java.util.EventListener;

import org.jlab.io.base.DataEvent;

public interface IDataContainer extends EventListener {
	
	//color for zero adc value
	public static final Color ADCZERO = new Color(230, 230, 230, 64);

	/**
	 * Clear the data
	 */
	public void clear();
	
	/**
	 * Update the data
	 * 
	 * @param event the new event
	 */
	public void update(DataEvent event);
	
	/** 
	 * Get the count of raw data, from the ac or tdc arrays
	 * @return the count of raw data
	 */
	public int rawCount();
	
	/**
	 * get the color for an adc value
	 * @param adc the adc value
	 * @return the color for the adc value
	 */
	public Color getADCColor(int adc);
	
	/** 
	 * Get the count of clusters, from a clusters bank
	 * @return the count of raw data
	 */
	public int clusterCount();
	
	/** 
	 * Get the count of a rec bank, as appropriate
	 * @return the count of rec data
	 */
	public int recCount();

}
