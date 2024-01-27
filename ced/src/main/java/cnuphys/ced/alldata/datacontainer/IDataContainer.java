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
	 * Get the count of data, i.e the number of rows
	 * @return the count of data
	 */
	public int count();
	


}
