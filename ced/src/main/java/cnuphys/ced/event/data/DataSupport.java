package cnuphys.ced.event.data;


public class DataSupport {

	// for uniform feedback colors
	public static final String prelimColor = "$orange$";
	public static final String trueColor = "$Alice Blue$";


	/**
	 * Safe method for extracting an int from an array
	 *
	 * @param array the array
	 * @param index the index
	 * @return the value at the index or errorVal on any error
	 */
	public static int safeValue(int[] array, int index, int errorVal) {
		if (array == null) {
			return errorVal;
		} else if ((index < 0) || (index >= array.length)) {
			return errorVal;
		} else {
			return array[index];
		}
	}


	/**
	 * Safe method for extracting a float from an array
	 *
	 * @param array the array
	 * @param index the index
	 * @return the value at the index or errorVal on any error
	 */
	public static float safeValue(float[] array, int index, float errorVal) {
		if (array == null) {
			return errorVal;
		} else if ((index < 0) || (index >= array.length)) {
			return errorVal;
		} else {
			return array[index];
		}
	}


}
