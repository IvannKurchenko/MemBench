package memory.benchmark.api.result;

/**
 *
 */
public class GcUsageStatisticView extends StatisticView<GcUsage> {

    private final String gcBame;

    public GcUsageStatisticView(GcUsage singleValue, String gcBame) {
        super(singleValue);
        this.gcBame = gcBame;
    }

    public GcUsageStatisticView(GcUsage minimumValue, GcUsage maximumValue, GcUsage averageValue, String gcBame) {
        super(minimumValue, maximumValue, averageValue);
        this.gcBame = gcBame;
    }

    /**
     *
     * @return
     */
    public String getGcBame() {
        return gcBame;
    }
}
