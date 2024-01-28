package cnuphys.ced.event.data.arrays;

import java.util.List;

import org.jlab.io.base.DataBank;
import org.jlab.io.base.DataEvent;

import cnuphys.ced.alldata.DataWarehouse;
import cnuphys.ced.clasio.ClasIoEventManager;

/**
 * Base array container for event data. Just a convenience container,
 * no data are copied.
 */
public abstract class BaseArrays {

	/** the sector array */
	public byte sector[];

	/** the layer array */
	public byte layer[];

	/** the component array */
	public short component[];

	/** the bank name */
	public final String bankName;

	/** the detector name */
	public String detectorName;

	/** the event manager */
	protected static ClasIoEventManager eventManager = ClasIoEventManager.getInstance();

	/** the data warehouse */
	protected static DataWarehouse dataWarehouse = DataWarehouse.getInstance();

	/** the current event */
	protected DataEvent event = eventManager.getCurrentEvent();

	/** the backing bank */
	protected DataBank bank;

	/**
	 * Create the data array references from the current event
	 * @param bankName the bank name
	 */
	public BaseArrays(String bankName) {

		this.bankName = bankName;

		//get the detector name from the bank name
		int index = bankName.indexOf("::");
		if (index > 0) {
			detectorName = bankName.substring(0, index);
		}

		event = eventManager.getCurrentEvent();
		if (event != null) {
			bank = event.getBank(bankName);
			
			if (bank != null) {
				sector = bank.getByte("sector");
				layer = bank.getByte("layer");
				component = bank.getShort("component");
			}
		}
	}

	/**
	 * Does this array container have data?
	 *
	 * @return <code>true</code> if this array container has data
	 */
	public boolean hasData() {
		return (sector != null);
	}

	/**
	 * Find the index of the given sector, layer, component
	 *
	 * @param sector    the sector [1..6]
	 * @param layer     the layer [1..3]
	 * @param component the component [1..]
	 * @return the index, or -1 if not found
	 */
	public int find(byte sector, byte layer, short component) {
		if (hasData()) {
			for (int i = 0; i < this.sector.length; i++) {
				if ((this.sector[i] == sector) && (this.layer[i] == layer) && (this.component[i] == component)) {
					return i;
				}
			}
		}
		return -1;
	}

	/**
	 * Add to the feedback strings assuming the mouse is pointing to the given sector, layer, component.
	 * @param sector the 1-based sector
	 * @param layer the 1-based layer
	 * @param component the 1-based component
	 * @param feedback the List of feedback strings to add to.
	 */
	public abstract void addFeedback(byte sector, byte layer, short component, List<String> feedback);

}
