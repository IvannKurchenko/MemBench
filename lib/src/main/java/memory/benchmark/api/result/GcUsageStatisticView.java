package memory.benchmark.api.result;

/**
 * Represents {@link memory.benchmark.api.result.StatisticView} which contains name of related
 * garbage collector.
 *
 * @see java.lang.management.GarbageCollectorMXBean
 * @see java.lang.management.ManagementFactory#getGarbageCollectorMXBeans()
 * @see memory.benchmark.api.result.GcUsage
 */
public class GcUsageStatisticView extends StatisticView<GcUsage> {

    private final String gcName;

    public GcUsageStatisticView(GcUsage singleValue, String gcName) {
        super(singleValue);
        this.gcName = gcName;
    }

    public GcUsageStatisticView(GcUsage minimumValue, GcUsage maximumValue, GcUsage averageValue, String gcName) {
        super(minimumValue, maximumValue, averageValue);
        this.gcName = gcName;
    }

    /**
     * @see java.lang.management.GarbageCollectorMXBean#getName()
     * @return name of garbage collector related to this {@link memory.benchmark.api.result.StatisticView}
     */
    public String getGcName() {
        return gcName;
    }
}
