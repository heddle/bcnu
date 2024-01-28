package cnuphys.ced.event;

import java.awt.Color;

import javax.swing.event.EventListenerList;

import org.jlab.io.base.DataEvent;

import cnuphys.bCNU.graphics.colorscale.ColorScaleModel;
import cnuphys.bCNU.log.Log;
import cnuphys.ced.alldata.DataWarehouse;
import cnuphys.ced.alldata.datacontainer.cal.ECalADCData;
import cnuphys.ced.alldata.datacontainer.cal.PCalADCData;
import cnuphys.ced.cedview.central.CentralXYView;
import cnuphys.ced.clasio.ClasIoEventManager;
import cnuphys.ced.clasio.IAccumulator;
import cnuphys.ced.clasio.IClasIoEventListener;
import cnuphys.ced.event.data.AdcHit;
import cnuphys.ced.event.data.AdcList;
import cnuphys.ced.event.data.BST;
import cnuphys.ced.event.data.DC;
import cnuphys.ced.event.data.DCTdcHit;
import cnuphys.ced.event.data.arrays.adc.ADCArrays;
import cnuphys.ced.event.data.arrays.adc.CC_ADCArrays;
import cnuphys.ced.event.data.arrays.adc.LR_ADCArrays;
import cnuphys.ced.event.data.lists.DCTdcHitList;
import cnuphys.ced.geometry.BSTGeometry;
import cnuphys.ced.geometry.BSTxyPanel;
import cnuphys.ced.geometry.GeoConstants;
import cnuphys.ced.geometry.PCALGeometry;
import cnuphys.ced.geometry.ftof.FTOFGeometry;

/**
 * Manages the accumulation of data
 *
 * @author heddle
 *
 */
public class AccumulationManager implements IAccumulator, IClasIoEventListener, IAccumulationListener {

	/** Indicates that accumulation has started */
	public static final int ACCUMULATION_STARTED = 0;

	/** Indicates hat accumulation has been cancelled */
	public static final int ACCUMULATION_CANCELLED = -1;

	/** Indicates hat accumulation has finished */
	public static final int ACCUMULATION_FINISHED = 1;

	/** Indicates hat accumulation has received clear */
	public static final int ACCUMULATION_CLEAR = 2;

	// common colorscale
	public static ColorScaleModel colorScaleModel = new ColorScaleModel(getAccumulationValues(),
			ColorScaleModel.getSimpleMapColors(8));

	// the singleton
	private static AccumulationManager instance;

	//the DataWarehouse singleton
	private static DataWarehouse _dataWarehouse = DataWarehouse.getInstance();

	// CND accumulated accumulated data indices are sector, layer, order (0 or 1,
	// adc only)
	private int _CNDAccumulatedData[][][];

	// HTCC accumulated accumulated data indices are sector, half, ring
	private int _HTCCAccumulatedData[][][];

	// LTCC accumulated accumulated data indices are sector, half, ring
	private int _LTCCAccumulatedData[][][];

	// ftcc accumulated data
	private int _FTCALAccumulatedData[];

	//rtpc accumulated data
	private int _RTPCAccumulatedData[][];

	// dc accumulated data indices are sector, superlayer, layer, wire
	private int _DCAccumulatedData[][][][];

	// BST accumulated data (layer[0..7], sector[0..23])
	private int _BSTAccumulatedData[][];

	// BST accumulated data (layer[0..7], sector[0..23], strip [0..254])
	private int _BSTFullAccumulatedData[][][];

	// CTOF accumulated data
	private int _CTOFAccumulatedData[];

	// FTOF accumulated Data
	private int _FTOF1AAccumulatedData[][];
	private int _FTOF1BAccumulatedData[][];
	private int _FTOF2AccumulatedData[][];

	// EC [sector, stack (inner, outer), view (uvw), strip]
	private int _ECALAccumulatedData[][][][];

	// PCAL [sector, view (uvw), strip]
	private int _PCALAccumulatedData[][][];

	// overall event count
	private long _eventCount;

	/** Colors used for accumulated related feedback */
	public static final String accumulationFBColor = "$Pale Green$";

	// occupancy data by sector, superlayer
	public static double avgDcOccupancy[][] = new double[6][6];

	// event manager
	private ClasIoEventManager _eventManager = ClasIoEventManager.getInstance();

	// list of accumulation listeners
	private EventListenerList _listeners;

	/**
	 * private constructor for singleton.
	 */
	private AccumulationManager() {
		addAccumulationListener(this);
		_eventManager.addClasIoEventListener(this, 1);

		// FTCAL data
		_FTCALAccumulatedData = new int[476];

		// RTPC Data
		_RTPCAccumulatedData = new int[GeoConstants.RTPC_NUMCOMPONENT][GeoConstants.RTPC_NUMLAYER];

		// cnd data (24 sectors, 3 layers, left and right)
		_CNDAccumulatedData = new int[24][3][2];

		// htcc data
		_HTCCAccumulatedData = new int[GeoConstants.NUM_SECTOR][2][4];

		// ltcc data NOTICE THE DIFFERENT ORDER FROM HTCC
		_LTCCAccumulatedData = new int[GeoConstants.NUM_SECTOR][2][18];

		// dc data
		_DCAccumulatedData = new int[GeoConstants.NUM_SECTOR][GeoConstants.NUM_SUPERLAYER][GeoConstants.NUM_LAYER][GeoConstants.NUM_WIRE];

		// down to layer
		_BSTAccumulatedData = new int[6][];
		for (int lay0 = 0; lay0 < 6; lay0++) {
			_BSTAccumulatedData[lay0] = new int[BSTGeometry.sectorsPerLayer[lay0]];
		}

		// down to strip
		_BSTFullAccumulatedData = new int[6][][];
		for (int lay0 = 0; lay0 < 6; lay0++) {
			_BSTFullAccumulatedData[lay0] = new int[BSTGeometry.sectorsPerLayer[lay0]][256];
		}

		// ctof storage
		_CTOFAccumulatedData = new int[48];

		// ftof storage
		_FTOF1AAccumulatedData = new int[6][FTOFGeometry.numPaddles[0]];
		_FTOF1BAccumulatedData = new int[6][FTOFGeometry.numPaddles[1]];
		_FTOF2AccumulatedData = new int[6][FTOFGeometry.numPaddles[2]];

		// ec and pcal storage  sector, stack (inner, outer), view (uvw), strip
		_ECALAccumulatedData = new int[6][2][3][36];

		// PCAL [sector, view (uvw), strip]
		_PCALAccumulatedData = new int[6][3][];
		for (int sect0 = 0; sect0 < 6; sect0++) {
			for (int view0 = 0; view0 < 3; view0++) {
				_PCALAccumulatedData[sect0][view0] = new int[PCALGeometry.PCAL_NUMSTRIP[view0]];
			}
		}

		clear();
	}

	/**
	 * Clears all accumulated data.
	 */
	@Override
	public void clear() {
		_eventCount = 0;

		// clear ftcal
		for (int i = 0; i < _FTCALAccumulatedData.length; i++) {
			_FTCALAccumulatedData[i] = 0;
		}

		//clear RTPC
		for (int i = 0; i < GeoConstants.RTPC_NUMCOMPONENT; i++) {
			for (int j = 0; j < GeoConstants.RTPC_NUMLAYER; j++) {
				_RTPCAccumulatedData[i][j] = 0;
			}
		}

		// clear accumulated CND
		for (int sector = 0; sector < 24; sector++) {
			for (int layer = 0; layer < 3; layer++) {
				for (int leftright = 0; leftright < 2; leftright++) {
					_CNDAccumulatedData[sector][layer][leftright] = 0;
				}
			}
		}

		// clear accumulated HTCC
		for (int sector = 0; sector < GeoConstants.NUM_SECTOR; sector++) {
			for (int half = 0; half < 2; half++) {
				for (int ring = 0; ring < 4; ring++) {
					_HTCCAccumulatedData[sector][half][ring] = 0;
				}
			}
		}

		// clear accumulated LTCC
		// NOTICE THE DIFFERENT ORDER FROM HTCC
		for (int sector = 0; sector < GeoConstants.NUM_SECTOR; sector++) {
			for (int half = 0; half < 2; half++) {
				for (int ring = 0; ring < 18; ring++) {
					_LTCCAccumulatedData[sector][half][ring] = 0;
				}
			}
		}

		// clear accumulated dc data
		for (int sector = 0; sector < GeoConstants.NUM_SECTOR; sector++) {
			for (int superLayer = 0; superLayer < GeoConstants.NUM_SUPERLAYER; superLayer++) {
				avgDcOccupancy[sector][superLayer] = 0;
				for (int layer = 0; layer < GeoConstants.NUM_LAYER; layer++) {
					for (int wire = 0; wire < GeoConstants.NUM_WIRE; wire++) {
						_DCAccumulatedData[sector][superLayer][layer][wire] = 0;
					}
				}
			}
		}

		// clear ecal data
		for (int sector = 0; sector < 6; sector++) {
			for (int stack = 0; stack < 2; stack++) {
				for (int view = 0; view < 3; view++) {
					for (int strip = 0; strip < 36; strip++) {
						_ECALAccumulatedData[sector][stack][view][strip] = 0;
					}
				}
			}
		}

		// clear pcal data
		for (int sector = 0; sector < 6; sector++) {
			for (int view = 0; view < 3; view++) {
				for (int strip = 0; strip < PCALGeometry.PCAL_NUMSTRIP[view]; strip++) {
					_PCALAccumulatedData[sector][view][strip] = 0;
				}
			}
		}

		// clear bst panel accumulation
		for (int layer = 0; layer < 6; layer++) {
			for (int sector = 0; sector < BSTGeometry.sectorsPerLayer[layer]; sector++) {
				_BSTAccumulatedData[layer][sector] = 0;
				for (int strip = 0; strip < 256; strip++) {
					_BSTFullAccumulatedData[layer][sector][strip] = 0;
				}
			}
		}

		// clear CTOF data
		for (int i = 1; i < 48; i++) {
			_CTOFAccumulatedData[i] = 0;
		}

		// clear ftof data
		for (int sector = 0; sector < 6; sector++) {
			for (int paddle = 0; paddle < _FTOF1AAccumulatedData[0].length; paddle++) {
				_FTOF1AAccumulatedData[sector][paddle] = 0;
			}
			for (int paddle = 0; paddle < _FTOF1BAccumulatedData[0].length; paddle++) {
				_FTOF1BAccumulatedData[sector][paddle] = 0;
			}
			for (int paddle = 0; paddle < _FTOF2AccumulatedData[0].length; paddle++) {
				_FTOF2AccumulatedData[sector][paddle] = 0;
			}
		}

		notifyListeners(ACCUMULATION_CLEAR);
	}

	/**
	 * Public access to the singleton.
	 *
	 * @return the singleton AccumulationManager
	 */
	public static AccumulationManager getInstance() {
		if (instance == null) {
			instance = new AccumulationManager();
		}
		return instance;
	}

	/**
	 * Get the accumulated CTOF data
	 *
	 * @return the accumulated FTCAL data
	 */

	public int[] getAccumulatedCTOFData() {
		return _CTOFAccumulatedData;
	}

	/**
	 * Get the accumulated FTCAL data
	 *
	 * @return the accumulated FTCAL data
	 */

	public int[] getAccumulatedFTCALData() {
		return _FTCALAccumulatedData;
	}

	/**
	 * Get the accumulated RTPC data
	 *
	 * @return the accumulated RTPC data
	 */

	public int[][] getAccumulatedRTPCData() {
		return _RTPCAccumulatedData;
	}

	/**
	 * Get the accumulated CND data
	 *
	 * @return the accumulated CND data
	 */
	public int[][][] getAccumulatedCNDData() {
		return _CNDAccumulatedData;
	}

	/**
	 * Get the accumulated HTCC data
	 *
	 * @return the accumulated HTCC data
	 */
	public int[][][] getAccumulatedHTCCData() {
		return _HTCCAccumulatedData;
	}

	/**
	 * Get the accumulated LTCC data
	 *
	 * @return the accumulated LTCC data
	 */
	public int[][][] getAccumulatedLTCCData() {
		return _LTCCAccumulatedData;
	}

	/**
	 * Get the accumulated EC data
	 *
	 * @return the accumulated ec data
	 */
	public int[][][][] getAccumulatedECALData() {
		return _ECALAccumulatedData;
	}

	/**
	 * Get the accumulated PCAL data
	 *
	 * @return the accumulated PCAL data
	 */
	public int[][][] getAccumulatedPCALData() {
		return _PCALAccumulatedData;
	}

	/**
	 * Get the accumulated DC data
	 *
	 * @return the accumulated dc data
	 */
	public int[][][][] getAccumulatedDCData() {
		return _DCAccumulatedData;
	}

	// // BST accumulated data (layer[0..7], sector[0..23])
	// private int _BSTAccumulatedData[][];
	//
	// // BST accumulated data (layer[0..7], sector[0..23], strip [0..254])
	// private int _BSTFullAccumulatedData[][][];

	/**
	 * Get the Max counts on any bst panel
	 *
	 * @return the Max counts for any bst panel.
	 */
	public int getMaxBSTCount() {
		return getMax(_BSTAccumulatedData);
	}

	public int getMaxCTOFCount() {
		return getMax(_CTOFAccumulatedData);
	}

	/**
	 * Get the Max counts on any BST strip
	 *
	 * @return the Max counts for any BST strip.
	 */
	public int getMaxFullBSTCount() {
		return getMax(_BSTFullAccumulatedData);
	}

	/**
	 * Get the Max count of accumulated hits
	 *
	 * @return the Max count of accumulated hits
	 */
	public int getMaxPCALCount() {
		return getMax(_PCALAccumulatedData);
	}

	// _ECALAccumulatedData = new int[6][2][3][36];

	public int getMaxECALCount(int plane) {
		return getMax(_ECALAccumulatedData);
	}

	/**
	 * Get the Max count for a given superlayer across all sectors
	 *
	 * @param suplay the superlayer 0..5
	 * @return the Max count for a given superlayer across all sectors
	 */
	public int getMaxDCCount(int suplay) {

		int max = 0;
		for (int sect = 0; sect < 6; sect++) {
			for (int lay = 0; lay < 6; lay++) {
				for (int wire = 0; wire < 12; wire++) {
					int count = _DCAccumulatedData[sect][suplay][lay][wire];
					if (count > max) {
						max = count;
					}
				}
			}
		}

		return max;
	}

	/**
	 * Get the accumulated Bst panel data
	 *
	 * @return the accumulated bst panel data
	 */
	public int[][] getAccumulatedBSTData() {
		return _BSTAccumulatedData;
	}

	/**
	 * Get the accumulated full Bst strip data
	 *
	 * @return the accumulated bst strip data
	 */
	public int[][][] getAccumulatedBSTFullData() {
		return _BSTFullAccumulatedData;
	}

	/**
	 * Get the Max counts for FTCAL
	 *
	 * @return the Max counts for FTCAL
	 */
	public int getMaxFTCALCount() {
		return getMax(_FTCALAccumulatedData);
	}

	/**
	 * Get the Max counts for RTPC
	 *
	 * @return the Max counts for RTPC
	 */
	public int getMaxRTPCCount() {
		return getMax(_RTPCAccumulatedData);
	}

	/**
	 * Get the Max counts for CND
	 *
	 * @return the Max counts for CND
	 */
	public int getMaxCNDCount() {
		return getMax(_CNDAccumulatedData);
	}

	/**
	 * Get the Max counts for HTCC
	 *
	 * @return the Max counts for HTCC
	 */
	public int getMaxHTCCCount() {
		return getMax(_HTCCAccumulatedData);
	}

	/**
	 * Get the Max counts for LTCC
	 *
	 * @return the Max counts for LTCC
	 */
	public int getMaxLTCCCount() {
		return getMax(_LTCCAccumulatedData);
	}

	/**
	 * Get the accumulated ftof panel 1a
	 *
	 * @return the accumulated ftof panel 1a
	 */
	public int[][] getAccumulatedFTOF1AData() {
		return _FTOF1AAccumulatedData;
	}

	/**
	 * Get the accumulated ftof panel 1b
	 *
	 * @return the accumulated ftof panel 1b
	 */
	public int[][] getAccumulatedFTOF1BData() {
		return _FTOF1BAccumulatedData;
	}

	/**
	 * Get the accumulated ftof panel 2
	 *
	 * @return the accumulated ftof panel 2
	 */
	public int[][] getAccumulatedFTOF2Data() {
		return _FTOF2AccumulatedData;
	}

	/**
	 * Get the Max counts on any ftof1a panel
	 *
	 * @return the Max counts for any ftof1a panel.
	 */
	public int getMaxFTOF1ACount() {
		return getMax(_FTOF1AAccumulatedData);
	}

	/**
	 * Get the Max counts on any ftof1b panel
	 *
	 * @return the Max counts for any ftof1b panel.
	 */
	public int getMaxFTOF1BCount() {
		return getMax(_FTOF1BAccumulatedData);
	}

	/**
	 * Get the Max counts on any ftof2 panel
	 *
	 * @return the Max counts for any ftof2 panel.
	 */
	public int getMaxFTOF2Count() {
		return getMax(_FTOF2AccumulatedData);
	}

	/**
	 * Get the color to use
	 *
	 * @param fract the fraction (compared to max hits)
	 * @return the color to use
	 */
	public Color getColor(ColorScaleModel model, double fract) {
		fract = Math.max(0.0001f, Math.min(fract, 0.9999f));
		return model.getColor(fract);
	}

	/**
	 * Get a color via getColor but add an alpha value
	 *
	 * @param value the value
	 * @param alpha the alpha value [0..255]
	 * @return the color corresponding to the value.
	 */
	public Color getAlphaColor(ColorScaleModel model, double value, int alpha) {
		Color c = getColor(model, value);
		Color color = new Color(c.getRed(), c.getGreen(), c.getBlue(), alpha);
		return color;
	}

	/**
	 * Get the number of events in the current accumulation
	 *
	 * @return the number of events in the current accumulation
	 */
	public long getAccumulationEventCount() {
		return _eventCount;
	}

	/**
	 * Get the average occupancy for a given sector and superlayer
	 *
	 * @param sect0 0 based sector 0..5
	 * @param supl0 0 based superlayer 0..5
	 * @return the occupancy
	 */
	public double getAverageDCOccupancy(int sect0, int supl0) {
		return avgDcOccupancy[sect0][supl0];
	}

	/**
	 * Here is an event, so increment the correct accumulation arrays
	 */
	@Override
	public void newClasIoEvent(DataEvent event) {

		// only care if I am accumulating
		if (!_eventManager.isAccumulating() || (event == null)) {
			return;
		}

		_dataWarehouse.clearCache();

		_eventCount++;

		// FTCal Data
		accumFTCAL();

		//RTPCData
		accumRTPC();

		// CND a special case
		accumCND();

		// htcc data
		accumHTCC();

		// ltcc data
		accumLTCC();

		// dc data
		DCTdcHitList dclist = DC.getInstance().updateTdcAdcList();
		accumDC(dclist);

		// ctof data
		accumCTOF();

		// ftof data
		accumFTOF();

		// ecal
		accumECal();

		//pcal
		accumPCal();

		// BST
		AdcList bstList = BST.getInstance().updateAdcList();
		accumBST(bstList);


	}

	// accumulate bst
	private void accumBST(AdcList list) {
		if ((list == null) || list.isEmpty()) {
			return;
		}

		for (AdcHit hit : list) {
			BSTxyPanel panel = CentralXYView.getPanel(hit.layer, hit.sector);
			if (panel != null) {
				int lay0 = hit.layer - 1;
				int sect0 = hit.sector - 1;
				int strip0 = hit.component - 1;
				try {
					_BSTAccumulatedData[lay0][sect0] += 1;

					if (strip0 >= 0) {
						_BSTFullAccumulatedData[lay0][sect0][strip0] += 1;
					}

				} catch (ArrayIndexOutOfBoundsException e) {
					String msg = String.format("Index out of bounds (BST). Event# %d lay %d sect %d  strip %d",
							_eventManager.getSequentialEventNumber(), hit.layer, hit.sector, hit.component);
					Log.getInstance().warning(msg);
				}

			}
		}
	}

	// accumulate ftcal
	private void accumFTCAL() {
		ADCArrays arrays = ADCArrays.getArrays("FTCAL::adc");

		if (arrays.hasData()) {
			for (int i = 0; i < arrays.sector.length; i++) {
				_FTCALAccumulatedData[arrays.component[i]] += 1;
			}
		}
	}

	// accumulate rtpc
		private void accumRTPC() {

			ADCArrays arrays = ADCArrays.getArrays("RTPC::adc");


			if (arrays.hasData()) {
				for (int i = 0; i < arrays.sector.length; i++) {
					int cm1 = arrays.component[i] - 1;
					int lm1 = arrays.layer[i] - 1;
					_RTPCAccumulatedData[cm1][lm1] += 1;
				}
			}

		}

	// accumulate CND which is a special case
	private void accumCND() {

		LR_ADCArrays arrays = LR_ADCArrays.getArrays("CND::adc");
		if (arrays.hasData()) {
			for (int i = 0; i < arrays.sector.length; i++) {
				int sect0 = arrays.sector[i] - 1;
				int lay0 = arrays.layer[i] - 1;
				// note order is already a zero based quantity
				int ord0 = arrays.order[i];
				_CNDAccumulatedData[sect0][lay0][ord0] += 1;
			}
		}
	}

	// accumulate htcc
	private void accumHTCC() {

		//use the adc arrays to accumulate
		CC_ADCArrays arrays = CC_ADCArrays.getArrays("HTCC::adc");
		if (arrays.hasData()) {
			for (int i = 0; i < arrays.sector.length; i++) {
				int sect0 = arrays.sector[i] - 1; // make 0 based

				//sometimes happens
				if (sect0 < 0) {
					continue;
				}
				int half0 = arrays.layer[i] - 1; // make 0 based so (0-1) (layer)
				int ring0 = arrays.component[i] - 1; // make 0 based so (0-3) (component)
				_HTCCAccumulatedData[sect0][half0][ring0] += 1;
			}
		} // end has data
	}

	// accumulate ltcc
	private void accumLTCC() {

		//use the adc arrays to accumulate
		CC_ADCArrays arrays = CC_ADCArrays.getArrays("LTCC::adc");
		if (arrays.hasData()) {
			for (int i = 0; i < arrays.sector.length; i++) {
				int sect0 = arrays.sector[i] - 1; // make 0 based

				//sometimes happens
				if (sect0 < 0) {
					continue;
				}
				int half0 = arrays.layer[i] - 1; // make 0 based so (0-1) (layer)
				int ring0 = arrays.component[i] - 1; // make 0 based so (0-17) (component)
				_LTCCAccumulatedData[sect0][half0][ring0] += 1;
			}
		} // end has data
	}

	// accumulate ecal data
	private void accumECal() {

		//use ADC data
		ECalADCData ecADCData = ECalADCData.getInstance();
		for (int i = 0; i < ecADCData.count(); i++) {
			if (ecADCData.adc.get(i) > 0) {
				int sect0 = ecADCData.sector.get(i) - 1;
				int plane0 = ecADCData.plane.get(i); // already zero based
				int view0 = ecADCData.view.get(i); // already zero based
				int strip0 = ecADCData.strip.get(i) - 1;
				_ECALAccumulatedData[sect0][plane0][view0][strip0] += 1;
			}
		}
	}


	// accumulate pcal data
	private void accumPCal() {
		//use ADC data
		PCalADCData pcADCData = PCalADCData.getInstance();

		for (int i = 0; i < pcADCData.count(); i++) {
			if (pcADCData.adc.get(i) > 0) {
				int sect0 = pcADCData.sector.get(i) - 1;
				int view0 = pcADCData.view.get(i); // already zero based
				int strip0 = pcADCData.strip.get(i) - 1;
				_PCALAccumulatedData[sect0][view0][strip0] += 1;
			}
		}
	}

	// accumulate dc data
	private void accumDC(DCTdcHitList list) {
		if ((list == null) || list.isEmpty()) {
			return;
		}

		for (DCTdcHit hit : list) {
			if (hit.inRange()) {
				_DCAccumulatedData[hit.sector - 1][hit.superlayer - 1][hit.layer6 - 1][hit.wire - 1] += 1;
			} else {
				Log.getInstance().warning("In accumulation, DC hit has bad indices: " + hit);
			}

		}
	}

	// for ctof accumulating
	private void accumCTOF() {

		//use the adc arrays to accumulate
		LR_ADCArrays arrays = LR_ADCArrays.getArrays("CTOF::adc");
		if (!arrays.hasData()) {
			return;
		}

		for (int comp : arrays.component) {
			_CTOFAccumulatedData[comp - 1] += 1;
		}
	}

	// for ftof accumulating
	private void accumFTOF() {

		//use the adc arrays to accumulate
		LR_ADCArrays arrays = LR_ADCArrays.getArrays("FTOF::adc");
		if (!arrays.hasData()) {
			return;
		}

		for (int i = 0; i < arrays.sector.length; i++) {
			int sect0 = arrays.sector[i] - 1;
			int paddle0 = arrays.component[i] - 1;

			if (arrays.layer[i] == 1) {
				_FTOF1AAccumulatedData[sect0][paddle0] += 1;
			} else if (arrays.layer[i] == 2) {
				_FTOF1BAccumulatedData[sect0][paddle0] += 1;
			} else if (arrays.layer[i] == 3) {
				_FTOF2AccumulatedData[sect0][paddle0] += 1;
			}
			else {
				System.out.println("ERROR:  accumFTOF layer out of bounds: " + arrays.layer[i]);
			}
		}
	}

	@Override
	public void openedNewEventFile(String path) {
		clear();
	}

	/**
	 * Change the event source type
	 *
	 * @param source the new source: File, ET
	 */
	@Override
	public void changedEventSource(ClasIoEventManager.EventSourceType source) {
		clear();
	}

	/**
	 * Get the values array for the color scale. Note the range is 0..1 so use
	 * fraction of max value to get color
	 *
	 * @return the values array.
	 */
	private static double getAccumulationValues()[] {

		int len = ColorScaleModel.getSimpleMapColors(8).length + 1;

		double values[] = new double[len];

		double min = 0.0;
		double max = 1.0;
		double del = (max - min) / (values.length - 1);
		for (int i = 0; i < values.length; i++) {
			values[i] = i * del;
		}
		return values;
	}

	/**
	 * Notify listeners we of an accumulation event
	 *
	 * @param reason should be one of the ACCUMULATION_X constants
	 *
	 */
	public void notifyListeners(int reason) {

		if (_listeners != null) {

			// Guaranteed to return a non-null array
			Object[] listeners = _listeners.getListenerList();

			// This weird loop is the bullet proof way of notifying all
			// listeners.
			for (int i = listeners.length - 2; i >= 0; i -= 2) {
				if (listeners[i] == IAccumulationListener.class) {
					((IAccumulationListener) listeners[i + 1]).accumulationEvent(reason);
				}
			}
		}
	}

	/**
	 * Remove an Accumulation listener.
	 *
	 * @param listener the Accumulation listener to remove.
	 */
	public void removeAccumulationListener(IAccumulationListener listener) {

		if (listener == null) {
			return;
		}

		if (_listeners != null) {
			_listeners.remove(IAccumulationListener.class, listener);
		}
	}

	/**
	 * Add an Accumulation listener.
	 *
	 * @param listener the Accumulation listener to add.
	 */
	public void addAccumulationListener(IAccumulationListener listener) {

		if (listener == null) {
			return;
		}

		if (_listeners == null) {
			_listeners = new EventListenerList();
		}

		_listeners.add(IAccumulationListener.class, listener);
	}

	@Override
	public void accumulationEvent(int reason) {
		switch (reason) {
		case AccumulationManager.ACCUMULATION_STARTED:
			break;

		case AccumulationManager.ACCUMULATION_CANCELLED:
		case AccumulationManager.ACCUMULATION_FINISHED:

			if (_eventCount != 0) {

				for (int sect0 = 0; sect0 < 6; sect0++) {
					for (int supl0 = 0; supl0 < 6; supl0++) {

						long count = 0;

						for (int lay0 = 0; lay0 < 6; lay0++) {
							for (int wire0 = 0; wire0 < 112; wire0++) {
								count += _DCAccumulatedData[sect0][supl0][lay0][wire0];
							}
						} // lay0

						double avgHits = avgDcOccupancy[sect0][supl0] = ((double) count) / _eventCount;
						// divide by num wires in superlayer
						avgDcOccupancy[sect0][supl0] = avgHits / (6 * 112);
					} // supl0
				} // sect0
			} // _eventCount != 0

			break;
		}
	}

	/**
	 * Get the percentage hit rate in the accumulated data for a given wire
	 *
	 * @param sect0 0 based sector 0..5
	 * @param supl0 0 based superlayer 0..5
	 * @param lay0  0 based layer 0..5
	 * @param wire0 0 based wire 0..111
	 * @return the occupancy
	 */
	public double getAccumulatedWireHitPercentage(int sect0, int supl0, int lay0, int wire0) {

		if (_eventCount < 1) {
			return 0;
		}
		return 100.0 * (_DCAccumulatedData[sect0][supl0][lay0][wire0] / (double) _eventCount);
	}


	// get the median of a 1D array of ints
	private int getMax(int[] data) {

		if ((data == null) || (data.length < 1)) {
			return 0;
		}

		int max = 0;
		for (int val : data) {
			if (val > max) {
				max = val;
			}
		}
		return max;
	}

	// get the max of a 2D array of ints
	private int getMax(int[][] data) {

		if (data == null) {
			return 0;
		}

		int max = 0;
		for (int iarry[] : data) {
			for (int val : iarry) {
				if (val > max) {
					max = val;
				}
			}
		}
		return max;
	}

	// get the max of a 3D array of ints
	private int getMax(int[][][] data) {

		if (data == null) {
			return 0;
		}

		int max = 0;

		for (int iarry1[][] : data) {
			for (int iarray2[] : iarry1) {
				for (int val : iarray2) {
					if (val > max) {
						max = val;
					}
				}
			}
		}
		return max;
	}

	// get the max of a 4D array of ints
	private int getMax(int[][][][] data) {

		if (data == null) {
			return 0;
		}

		int max = 0;
		for (int iarry1[][][] : data) {
			for (int iarray2[][] : iarry1) {
				for (int iarray3[] : iarray2) {
					for (int val : iarray3) {
						if (val > max) {
							max = val;
						}
					}
				}
			}
		}
		return max;
	}

}
