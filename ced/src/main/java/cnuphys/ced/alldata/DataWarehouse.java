package cnuphys.ced.alldata;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.jlab.io.base.DataBank;
import org.jlab.io.base.DataEvent;
import org.jlab.jnp.hipo4.data.Schema;
import org.jlab.jnp.hipo4.data.SchemaFactory;

import cnuphys.ced.clasio.ClasIoEventManager;
import cnuphys.ced.event.data.TdcAdcTOFHit;
import cnuphys.ced.event.data.lists.ClusterList;

public class DataWarehouse {

	//the singleton
	private static DataWarehouse _instance;

	// all the known banks in the current evenr
	private ArrayList<String> _knownBanks = new ArrayList<>();

	//the current schema factory (dictionary)
	private SchemaFactory _schemaFactory;


	/**
	 * Public access to the singleton
	 *
	 * @return the singleton
	 */
	public static DataWarehouse getInstance() {
		if (_instance == null) {
			_instance = new DataWarehouse();
		}
		return _instance;
	}

	/**
	 * Get the banks in the current event
	 *
	 * @return the known banks
	 */
	public ArrayList<String> getKnownBanks() {
		return _knownBanks;
	}


	/**
	 * Update the schema factory and the columa dataobjects
	 * @param schemaFactory
	 */
	public void updateSchema(SchemaFactory schemaFactory) {

		_schemaFactory = schemaFactory;
		_knownBanks.clear();

		//schemas are banks
		List<Schema> schemas = schemaFactory.getSchemaList();

		// a schema is a bank
		if (schemas == null || schemas.isEmpty()) {
			return;
		}

		for (Schema schema : schemas) {
			_knownBanks.add(schema.getName());
		}

        // sort the banks
		_knownBanks.sort(null);
	}

	/**
	 * Get the list of column names for a bank name
	 *
	 * @param bankName the bank name
	 * @return the list of column names
	 */
	public List<String> getColumnNames(String bankName) {
		if (_knownBanks.contains(bankName)) {
			return _schemaFactory.getSchema(bankName).getEntryList();
		}
		return null;
	}

	//get the current event from the IO manager
	private DataEvent getCurrentEvent() {
		return ClasIoEventManager.getInstance().getCurrentEvent();
	}

	/**
	 * Get a list of the banks in the current event
	 * @return banks in current event
	 */
	public String[] banks() {
		DataEvent event = getCurrentEvent();
		if (event != null) {
			String[] banks = event.getBankList();

			if (banks != null) {
				Arrays.sort(banks);
			}
			return banks;
		}
        return null;
    }

	/**
	 * Get a byte array for the bank and column names in the current event
	 *
	 * @param bankName   the bank name
	 * @param columnName the column name
	 * @return a byte array or null.
	 */
	public byte[] getByte(String bankName, String columnName) {
		DataEvent event = getCurrentEvent();
		if (event != null) {
			DataBank bank = event.getBank(bankName);
			if (bank != null) {
				return bank.getByte(columnName);
			}
		}
		return null;
	}
	/**
	 * Get a float array for the bank and column names in the current event
	 *
	 * @param bankName   the bank name
	 * @param columnName the column name
	 * @return a float array or null.
	 */
	public float[] getFloat(String bankName, String columnName) {
		DataEvent event = getCurrentEvent();
		if (event != null) {
			DataBank bank = event.getBank(bankName);
			if (bank != null) {
				return bank.getFloat(columnName);
			}
		}
		return null;
	}

	/**
	 * Get a short array for the bank and column names in the current event
	 * @param bankName the bank name
	 * @param columnName the column name
	 * @return a short array or null.
	 */
	public short[] getShort(String bankName, String columnName) {
		DataEvent event = getCurrentEvent();
        if (event != null) {
			DataBank bank = event.getBank(bankName);
			if (bank != null) {
				return bank.getShort(columnName);
			}
		}
		return null;
	}

	/**
	 * Get an int array for the bank and column names in the current event
	 * @param bankName the bank name
	 * @param columnName the column name
	 * @return a short array or null.
	 */
	public int[] getInt(String bankName, String columnName) {
		DataEvent event = getCurrentEvent();
        if (event != null) {
			DataBank bank = event.getBank(bankName);
			if (bank != null) {
				return bank.getInt(columnName);
			}
		}
		return null;
	}

	/**
	 * Get a long array for the bank and column names in the current event
	 *
	 * @param bankName   the bank name
	 * @param columnName the column name
	 * @return a short array or null.
	 */
	public long[] getLong(String bankName, String columnName) {
		DataEvent event = getCurrentEvent();
		if (event != null) {
			DataBank bank = event.getBank(bankName);
			if (bank != null) {
				return bank.getLong(columnName);
			}
		}
		return null;
   }

	/**
	 * Get a double array for the bank and column names in the current event
	 *
	 * @param bankName   the bank name
	 * @param columnName the column name
	 * @return a short array or null.
	 */
	public double[] getDouble(String bankName, String columnName) {
		DataEvent event = getCurrentEvent();
		if (event != null) {
			DataBank bank = event.getBank(bankName);
			if (bank != null) {
				return bank.getDouble(columnName);
			}
		}
		return null;
	}

	/**
	 * Get the number of rows (length) of a given bank
	 * @param bankName the bank name
	 * @return the number of rows
	 */
	public int rows(String bankName) {
		if (bankName == null) {
			return 0;
		}

		DataEvent event = getCurrentEvent();
		if (event == null) {
			return 0;
		}

		DataBank bank = event.getBank(bankName);
		if (bank == null) {
			return 0;
		}
		return bank.rows();
	}

	/**
	 * Get a cluster list for the given bank name
	 * @param bankName
	 * @return
	 */
	public ClusterList getClusters(String bankName) {

		DataEvent event = getCurrentEvent();
		if (event == null) {
			return null;
		}

		DataBank bank = event.getBank(bankName);
		if (bank == null) {
			return null;
		}

		ClusterList clusters = new ClusterList(bankName);
		clusters.fillList();
		return clusters;
	}


	/**
	 * Get the TdcAdcTOFHit for the given sector, layer, and component
	 * This could be CTOF or FTOF depending on the base name
	 *
	 * @return the TdcAdcTOFHit or null
	 */
	public TdcAdcTOFHit getTdcAdcTOFHit(byte sector, byte layer, short component, String base) {


		//is there an event?
		DataEvent event = getCurrentEvent();
		if (event == null) {
			return null;
		}

		//are there hits?
		String hitBankName = base + "::hits";
		DataBank hitBank = event.getBank(hitBankName);
		if (hitBank == null) {
			return null;
		}

		TdcAdcTOFHit hit = null;

		//get data columns from hit bank
		byte hsect[] = getByte(hitBankName, "sector");
		byte hlay[] = getByte(hitBankName, "layer");
		short hcomp[] = getShort(hitBankName, "component");

		//adc and tdc banks will not be the same size as the hit bank
		String adcBankName = base + "::adc";
		String tdcBankName = base + "::tdc";

		for (int i = 0; i < hsect.length; i++) {
			if ((hsect[i] == sector) && (hlay[i] == layer) && (hcomp[i] == component)) {
				hit = new TdcAdcTOFHit(sector, layer, component);

				//ADC
				byte sect[] = getByte(adcBankName, "sector");
				byte lay[] = getByte(adcBankName, "layer");
				short comp[] = getShort(adcBankName, "component");
				int adc[] = getInt(adcBankName, "ADC");
				byte order[] = getByte(adcBankName, "order");
				short ped[] = getShort(adcBankName, "ped");
				float time[] = getFloat(adcBankName, "time");

				for (int j = 0; j < sect.length; j++) {
					if ((sect[j] == sector) && (lay[j] == layer) && (comp[j] == component)) {
						if (order[j] == 0) {
							hit.adcL = adc[j];
							hit.pedL = ped[j];
							hit.timeL = time[j];
						} else {
							hit.adcR = adc[j];
							hit.pedR = ped[j];
							hit.timeR = time[j];
						}
					}
				}

				//TDC
				sect = getByte(tdcBankName, "sector");
				lay = getByte(tdcBankName, "layer");
				comp = getShort(tdcBankName, "component");
				int tdc[] = getInt(tdcBankName, "TDC");
				order = getByte(adcBankName, "order");

				for (int j = 0; j < sect.length; j++) {
					if ((sect[j] == sector) && (lay[j] == layer) && (comp[j] == component)) {
						if (order[j] == 2) {
							hit.tdcL = tdc[j];
						} else {
							hit.tdcR = tdc[j];
						}
					}
				}


			}
		}


		return hit;
	}


}
