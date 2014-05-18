package memory.benchmark.api.result;

/**
 * Represents collected benchmark data about garbage collection.
 * Based on using of {@link java.lang.management.GarbageCollectorMXBean}.
 *
 * @see java.lang.management.GarbageCollectorMXBean
 * @see java.lang.management.ManagementFactory#getGarbageCollectorMXBeans()
 */
public class GcUsage {

    private final long gcCount;
    private final long gcTime;

    public GcUsage(GcUsage before, GcUsage after) {
        this.gcCount = after.getGcCount() - before.getGcCount();
        this.gcTime = after.getGcTime() - before.getGcTime();
    }

    public GcUsage(long gcCount, long gcTime) {
        this.gcCount = gcCount;
        this.gcTime = gcTime;
    }

    /**
     * @see java.lang.management.GarbageCollectorMXBean#getCollectionTime()
     * @return garbage collecting counts that takes during benchmark execution.
     */
    public long getGcCount() {
        return gcCount;
    }

    /**
     * @see java.lang.management.GarbageCollectorMXBean#getCollectionTime()
     * @return garbage collection time that takes during benchmark execution.
     */
    public long getGcTime() {
        return gcTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GcUsage gcUsage = (GcUsage) o;

        if (gcCount != gcUsage.gcCount) return false;
        if (gcTime != gcUsage.gcTime) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = 0;
        result = 31 * result + (int) (gcCount ^ (gcCount >>> 32));
        result = 31 * result + (int) (gcTime ^ (gcTime >>> 32));
        return result;
    }

    @Override
    public String toString() {
        return "GcUsage{" +
                ", gcCount=" + gcCount +
                ", gcTime=" + gcTime +
                '}';
    }
}
