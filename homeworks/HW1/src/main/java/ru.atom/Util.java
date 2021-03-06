package ru.atom;

/**
 * In this assignment you need to implement the following util methods.
 * Note:
 *  throw new UnsupportedOperationException(); - is just a stub
 */
public class Util {



    /**
     * Returns the greatest of {@code int} values.
     *
     * @param values an argument. Assume values.length > 0.
     * @return the largest of values.
     */
    public static int max(int[] values) {
        if (values.length > 0) {
            int max = values[0];
            for (int i: values) {
                if (max < i) {
                    max = i;
                }
            }
            return max;
        }
        throw new IllegalArgumentException("Can't support zero-length arrays");
    }

    /**
     * Returns the sum of all {@code int} values.
     *
     * @param values an argument. Assume values.length > 0.
     * @return the sum of all values.
     */
    public static long sum(int[] values) {
        if (values.length > 0) {
            long sum = 0;
            for (int i: values) {
                sum += i;
            }
            return sum;
        }
        throw new IllegalArgumentException("Can't support zero-length arrays");
    }


}
