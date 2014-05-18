package memory.benchmark.api.util;

/**
 * Util class that converts memory size value from size in bytes to size in greater values.
 */
public enum MemoryValueConverter {

    /**
     * Converts memory value in bytes to gigabytes.
     */
    TO_GIGA_BYTES(1000 * 1000 * 1024, "Gb"),

    /**
     * Converts memory value in bytes to megabytes.
     */
    TO_MEGA_BYTES(1000 * 1024, "Mb"),

    /**
     * Converts memory value in bytes to kilobytes.
     */
    TO_KILO_BYTES(1024, "Kb");

    private final int derived;
    private final String suffix;

    MemoryValueConverter(int derived, String suffix) {
        this.derived = derived;
        this.suffix = suffix;
    }

    public String convert(long value) {
        return Double.toString(Math.round(value / derived));
    }

    public String getSuffix() {
        return suffix;
    }
}
