package cnuphys.bCNU.util;

import java.util.Arrays;
import java.util.Comparator;

public class ArrayIndexSorter {

    public static <T extends Number & Comparable<T>> int[] sortIndices(T[] values, boolean ascending) {
        Integer[] indices = new Integer[values.length];
        for (int i = 0; i < indices.length; i++) {
            indices[i] = i;
        }

        Comparator<Integer> comparator = Comparator.comparing((Integer index) -> values[index]);
        if (!ascending) {
            comparator = comparator.reversed();
        }

        Arrays.sort(indices, comparator);
        return Arrays.stream(indices).mapToInt(Integer::intValue).toArray();
    }
}
