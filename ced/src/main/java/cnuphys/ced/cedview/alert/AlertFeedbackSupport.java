package cnuphys.ced.cedview.alert;

import java.util.List;

import cnuphys.ced.alldata.DataWarehouse;

public class AlertFeedbackSupport {

	private static DataWarehouse _dataWarehouse = DataWarehouse.getInstance();

	public static void handleInt(String bankName, String columnName, int index, String colorStr, List<String> feedbackStrings) {
		int[] array = _dataWarehouse.getInt(bankName, columnName);
		if ((array == null) || (index < 0) || (index >= array.length)) {
			return;
		}

		String cs = (colorStr == null) ? "$cyan$" : colorStr;
		feedbackStrings.add(String.format("%s%s: %d", cs, columnName, array[index]));
	}
	public static void handleByte(String bankName, String columnName, int index, String colorStr, List<String> feedbackStrings) {
		byte[] array = _dataWarehouse.getByte(bankName, columnName);
		if ((array == null) || (index < 0) || (index >= array.length)) {
			return;
		}

		String cs = (colorStr == null) ? "$cyan$" : colorStr;
		feedbackStrings.add(String.format("%s%s: %d", cs, columnName, array[index]));
	}

	public static void handleShort(String bankName, String columnName, int index, String colorStr, List<String> feedbackStrings) {
		short[] array = _dataWarehouse.getShort(bankName, columnName);
		if ((array == null) || (index < 0) || (index >= array.length)) {
			return;
		}

		String cs = (colorStr == null) ? "$cyan$" : colorStr;
		feedbackStrings.add(String.format("%s%s: %d", cs, columnName, array[index]));
	}

	public static void handleFloat(String bankName, String columnName, int index, String colorStr, List<String> feedbackStrings) {
		float[] array = _dataWarehouse.getFloat(bankName, columnName);
		if ((array == null) || (index < 0) || (index >= array.length)) {
			return;
		}

		String cs = (colorStr == null) ? "$cyan$" : colorStr;
		feedbackStrings.add(String.format("%s%s: %10.5f", cs, columnName, array[index]));
	}

}
