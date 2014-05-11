package memory.benchmark.api.util;

/**
 *
 */
public interface MemoryValueConverter {

    static final int KILOBYTE = 1024;
    static final int MEGABYTE = 1000 * KILOBYTE;
    static final int GIGABYTE = 1000 * MEGABYTE;
    /**
     *
     */
    public static final MemoryValueConverter TO_GIGA_BYTES = (value -> value / GIGABYTE + "Gb");
    /**
     *
     */
    public static final MemoryValueConverter TO_MEGA_BYTES = (value -> value / MEGABYTE + "Mb");
    /**
     *
     */
    public static final MemoryValueConverter TO_KILO_BYTES = (value -> value / KILOBYTE + "Kb");

    /**
     * @param value
     * @return
     */
    String convert(long value);
}
