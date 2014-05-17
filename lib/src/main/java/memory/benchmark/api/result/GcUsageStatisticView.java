package memory.benchmark.api.result;

/**
 *
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
     *
     * @return
     */
    public String getGcName() {
        return gcName;
    }
}
