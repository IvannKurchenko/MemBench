package memory.benchmark.api.util;

/**
 *
 */
public enum MemoryValueConverter {
    /**
     *
     */
    TO_GIGA_BYTES(1000 * 1000 * 1024, "Gb"),

    /**
     *
     */
    TO_MEGA_BYTES(1000 * 1024, "Mb"),

    /**
     *
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
