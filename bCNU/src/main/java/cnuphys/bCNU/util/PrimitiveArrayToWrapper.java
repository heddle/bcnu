package cnuphys.bCNU.util;

public class PrimitiveArrayToWrapper {

    public static Byte[] toWrapper(byte[] array) {
        Byte[] result = new Byte[array.length];
        for (int i = 0; i < array.length; i++) {
            result[i] = array[i];  // Auto-boxing
        }
        return result;
    }

    public static Short[] toWrapper(short[] array) {
        Short[] result = new Short[array.length];
        for (int i = 0; i < array.length; i++) {
            result[i] = array[i];  // Auto-boxing
        }
        return result;
    }

    public static Integer[] toWrapper(int[] array) {
        Integer[] result = new Integer[array.length];
        for (int i = 0; i < array.length; i++) {
            result[i] = array[i];  // Auto-boxing
        }
        return result;
    }

    public static Long[] toWrapper(long[] array) {
        Long[] result = new Long[array.length];
        for (int i = 0; i < array.length; i++) {
            result[i] = array[i];  // Auto-boxing
        }
        return result;
    }

    public static Float[] toWrapper(float[] array) {
        Float[] result = new Float[array.length];
        for (int i = 0; i < array.length; i++) {
            result[i] = array[i];  // Auto-boxing
        }
        return result;
    }

    public static Double[] toWrapper(double[] array) {
        Double[] result = new Double[array.length];
        for (int i = 0; i < array.length; i++) {
            result[i] = array[i];  // Auto-boxing
        }
        return result;
    }

}
