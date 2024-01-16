package cnuphys.ced.event.data;

import java.awt.Color;

import cnuphys.bCNU.graphics.colorscale.ColorScaleModel;
import cnuphys.ced.alldata.DataWarehouse;
import cnuphys.lund.X11Colors;

public class AdcColorScale extends ColorScaleModel {
	
	//for 0 adc values
	private static final Color ASDZERO1 = new Color(0, 0, 0, 64);
	private static final Color ASDZERO2 = X11Colors.getX11Color("Light Sky Blue", 80);

	private static DataWarehouse _dataWarehouse = DataWarehouse.getInstance();

	private static AdcColorScale _instance;

	private AdcColorScale() {
		super(getScaleValues(), ColorScaleModel.getWeatherMapColors(8));
	}

	public static AdcColorScale getInstance() {
		if (_instance == null) {
			_instance = new AdcColorScale();
		}
		return _instance;
	}

	/**
	 * Get the values array for the color scale. Note the range is 0..1 so use
	 * fraction of max value to get color
	 *
	 * @return the values array.
	 */
	private static double[] getScaleValues() {

		int len = ColorScaleModel.getWeatherMapColors(8).length + 1;

		double values[] = new double[len];

		double min = 0.0;
		double max = 1.001;
		double del = (max - min) / (values.length - 1);
		for (int i = 0; i < values.length; i++) {
			values[i] = i * del;
		}
		return values;
	}
	
	/**
	 * Get the fill color for a CTOF paddle
	 * @param component 1-based component [1..48]
	 * @return the fill color
	 */
	public Color getCTOFADCColor(short component) {
		//for CTOF sector always 1, layer always 1
		int maxADC = getMaxInt("CTOF::adc", "ADC");
		return getADCColor((byte)1, (byte)1, component, maxADC, "CTOF::adc", "ADC");
	}
	
	/**
	 * Get the fill color for a CND paddle
	 * @param sector 1-based sector [1..24]
	 * @param layer 1-based layer [1..3
	 * @param order 0 (left) or 1 (right)
	 * @return the fill color
	 */
	public Color getCNDADCColor(byte sector, byte layer,  byte order) {
		//for CND component is always 1
		int maxADC = getMaxInt("CND::adc", "ADC");
		return getADCColor(sector, layer, (byte)1, order, maxADC, "CND::adc", "ADC");
	}
	
	
	//get the adc color
	private Color getADCColor(byte sector, byte layer, short component, int maxADC,
			String bankName, String colName) {
		int maxComponentADC = 0;
		int adc[] = _dataWarehouse.getInt(bankName, colName);

		if (adc != null) {
			byte sect[] = _dataWarehouse.getByte(bankName, "sector");
			byte lay[] = _dataWarehouse.getByte(bankName, "layer");
			short comp[] = _dataWarehouse.getShort(bankName, "component");
			for (int i = 0; i < adc.length; i++) {
				if ((sect[i] == sector) && (lay[i] == layer) && (comp[i] == component)) {
					maxComponentADC = Math.max(maxComponentADC, adc[i]);
				}
			}
		}
		
		if (maxComponentADC < 1) {
			return ASDZERO1;
		}
		
		double fract = ((double) maxComponentADC) / maxADC;
		fract = Math.max(0, Math.min(1.0, fract));
		return AdcColorScale.getInstance().getAlphaColor(fract, 255);
	}
	
	//get the adc color
	private Color getADCColor(byte sector, byte layer, short component, byte order,
			int maxADC, String bankName, String colName) {
		
		int maxComponentADC = 0;
		int adc[] = _dataWarehouse.getInt(bankName, colName);

		if (adc != null) {
			byte sect[] = _dataWarehouse.getByte(bankName, "sector");
			byte lay[] = _dataWarehouse.getByte(bankName, "layer");
			short comp[] = _dataWarehouse.getShort(bankName, "component");
			byte ord[] = _dataWarehouse.getByte(bankName, "order");
			for (int i = 0; i < adc.length; i++) {
				if ((sect[i] == sector) && (lay[i] == layer) && (comp[i] == component) && (ord[i] == order)) {
					maxComponentADC = Math.max(maxComponentADC, adc[i]);
				}
			}
		}
		
		if (maxComponentADC < 1) {
			return ASDZERO1;
		}
		
		double fract = ((double) maxComponentADC) / maxADC;
		fract = Math.max(0, Math.min(1.0, fract));
		return AdcColorScale.getInstance().getAlphaColor(fract, 255);
	}


	//get the maximum int for the given bank and column
	private int getMaxInt(String bankName, String colName) {
		int maxVal = 0;
		int vals[] = _dataWarehouse.getInt(bankName, colName);
		
		if (vals != null) {
			for (int i = 0; i < vals.length; i++) {
				maxVal = Math.max(maxVal, vals[i]);
			}
		}
		return maxVal;
	}

	
	

}
