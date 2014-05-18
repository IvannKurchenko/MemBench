package memory.benchmark.api.result;

import java.lang.management.MemoryUsage;

/**
 * Represents collected data about any memory consumption during benchmark execution.
 * In other words it shows memory consumption difference before benchmark method execution and after it.
 *
 *  <pre>
 *  ^               ___________
 *  |              |   |
 *  |              |   |         {@link memory.benchmark.api.result.MemoryFootprint}
 *  |    __________|___|_______
 *  |   |   |      |   |
 *  |___|___|______|___|____
 *      Before     After
 *  </pre>
 *
 * @see java.lang.management.MemoryUsage
 */
public class MemoryFootprint {

    private final long usedMemoryFootprint;
    private final long maxMemoryFootprint;
    private final long committedMemoryFootprint;

    public MemoryFootprint(MemoryUsage before, MemoryUsage after) {
        this.usedMemoryFootprint = after.getUsed() - before.getUsed();
        this.maxMemoryFootprint = after.getMax() - before.getMax();
        this.committedMemoryFootprint = after.getCommitted() - before.getCommitted();
    }

    public MemoryFootprint(long usedMemoryFootprint, long maxMemoryFootprint, long committedMemoryFootprint) {
        this.usedMemoryFootprint = usedMemoryFootprint;
        this.maxMemoryFootprint = maxMemoryFootprint;
        this.committedMemoryFootprint = committedMemoryFootprint;
    }

    /**
     * @see java.lang.management.MemoryUsage#getUsed()
     * @return used memory footprint.
     */
    public long getUsedMemoryFootprint() {
        return usedMemoryFootprint;
    }

    /**
     * @see java.lang.management.MemoryUsage#getMax()
     * @return max memory footprint.
     */
    public long getMaxMemoryFootprint() {
        return maxMemoryFootprint;
    }

    /**
     * @see java.lang.management.MemoryUsage#getCommitted()
     * @return committed memory footprint.
     */
    public long getCommittedMemoryFootprint() {
        return committedMemoryFootprint;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MemoryFootprint that = (MemoryFootprint) o;

        if (committedMemoryFootprint != that.committedMemoryFootprint) return false;
        if (maxMemoryFootprint != that.maxMemoryFootprint) return false;
        if (usedMemoryFootprint != that.usedMemoryFootprint) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (usedMemoryFootprint ^ (usedMemoryFootprint >>> 32));
        result = 31 * result + (int) (maxMemoryFootprint ^ (maxMemoryFootprint >>> 32));
        result = 31 * result + (int) (committedMemoryFootprint ^ (committedMemoryFootprint >>> 32));
        return result;
    }

    @Override
    public String toString() {
        return "MemoryFootprint{" +
                "usedMemoryFootprint=" + usedMemoryFootprint +
                ", maxMemoryFootprint=" + maxMemoryFootprint +
                ", committedMemoryFootprint=" + committedMemoryFootprint +
                '}';
    }
}
