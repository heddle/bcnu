package cnuphys.ced.alldata.datacontainer.dc;

import java.util.List;

import org.jlab.io.base.DataBank;
import org.jlab.io.base.DataEvent;

import cnuphys.ced.alldata.datacontainer.ACommonTDCData;

public class DCTDCandDOCAData extends ACommonTDCData {
	
	// for feedback strings
	private static final String _fbColor = "$Orange$";
	
	// singleton
	private static volatile DCTDCandDOCAData _instance;

	/**
	 * Public access to the singleton
	 *
	 * @return the singleton
	 */
	public static DCTDCandDOCAData getInstance() {
		if (_instance == null) {
			synchronized (DCTDCandDOCAData.class) {
				if (_instance == null) {
					_instance = new DCTDCandDOCAData();
				}
			}
		}
		return _instance;
	}

	/** raw data has layer 1..36. Convert to 1..6 */
	public byte layer6[];
	
	/** superlayer 1..6 */
	public byte superlayer[];
	
	/** does snr think this is noise? */
	public boolean noise[];
	
	
	/**left right value */
	public byte LR[];
	
	/** doca value */
	public float doca[];
	
	/** sdoca value */
	public float sdoca[];
	
	/** time value */
	public float time[];

	/** stime value */
	public float stime[];
	
	@Override
	public void clear() {
		super.clear();
		layer6 = null;
		superlayer = null;
		noise = null;
		LR = null;
		doca = null;
		sdoca = null;
		time = null;
		stime = null;
		
	}

	@Override
	public void update(DataEvent event) {
		DataBank bank = event.getBank("DC::tdc");

		if (bank == null) {
			return;
		}

        sector = bank.getByte("sector");
        layer = bank.getByte("layer");
        component = bank.getShort("component");
        order = bank.getByte("order");
        tdc = bank.getInt("TDC");	
        
        //get the layer6 and superlayer from the layer	
        if (layer != null) {
			int len = layer.length;
			layer6 = new byte[len];
			superlayer = new byte[len];
			
			noise = new boolean[len];
			
			for (int i = 0; i < len; i++) {
				byte layer36 = layer[i];
				superlayer[i] = (byte) (((layer36 - 1) / 6) + 1);
				layer6[i] = (byte) (((layer36 - 1) % 6) + 1);
				
				noise[i] = false;
			}
        }
        
        //and from doca bank
        DataBank docaBank = event.getBank("DC::doca");
		if (docaBank == null) {
			return;
		}
		LR = docaBank.getByte("LR");
		doca = docaBank.getFloat("doca");
		sdoca = docaBank.getFloat("sdoca");
		time = docaBank.getFloat("time");
		stime = docaBank.getFloat("stime");
	}
	
	/**
	 * Common feedback 
	 * @param index the index of the data
	 * @param feedbackStrings the list of feedback strings
	 */
	public void adcFeedback(int index, List<String> feedbackStrings) {
		feedbackStrings.add(_fbColor + "sect " + sector[index] +
				" suplay " + superlayer[index] + " layer " + layer6[index] + " wire " + component[index]);
		
		feedbackStrings.add(_fbColor + "tdc: " + tdc[index] + "  order: " + order[index]);
	}


}
