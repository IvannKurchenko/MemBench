package memory.benchmark.internal.runner.local.collect;

import memory.benchmark.internal.collect.AbstractGcDataCollector;

import java.lang.management.GarbageCollectorMXBean;
import java.util.List;

public class LocalGcDataCollector extends AbstractGcDataCollector {

    private final List<GarbageCollectorMXBean> garbageCollectorMXBeans;

    public LocalGcDataCollector(List<GarbageCollectorMXBean> garbageCollectorMXBeans) {
        this.garbageCollectorMXBeans = garbageCollectorMXBeans;
    }

    @Override
    public void onBeforeTest() {
        garbageCollectorMXBeans.forEach(g -> addBeforeGcUsage(g.getName(), g.getCollectionCount(), g.getCollectionTime()));
    }

    @Override
    public void onAfterTest() {
        garbageCollectorMXBeans.forEach(g -> addAfterGcUsage(g.getName(), g.getCollectionCount(), g.getCollectionTime()));
    }

}
