package memory.benchmark.internal.runner.remote.collect;

import javax.management.openmbean.CompositeData;
import java.lang.management.MemoryUsage;

public class CompositeDataConverter {

    private static final String INIT_USAGE_PROPERTY = "init";
    private static final String USED_USAGE_PROPERTY = "used";
    private static final String COMMITTED_USAGE_PROPERTY = "committed";
    private static final String MAX_USAGE_PROPERTY = "max";

    public static MemoryUsage toMemoryUsage(CompositeData compositeData) {
        long init = (long) compositeData.get(INIT_USAGE_PROPERTY);
        long used = (long) compositeData.get(USED_USAGE_PROPERTY);
        long committed = (long) compositeData.get(COMMITTED_USAGE_PROPERTY);
        long max = (long) compositeData.get(MAX_USAGE_PROPERTY);

        return new MemoryUsage(init, used, committed, max);
    }
}
