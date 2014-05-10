package memory.benchmark.internal.runner.local.collect;

import memory.benchmark.internal.collect.AbstractMemoryPoolDataCollector;

import java.lang.management.MemoryPoolMXBean;
import java.util.List;

public class LocalMemoryPoolDataCollector extends AbstractMemoryPoolDataCollector {

    private final List<MemoryPoolMXBean> memoryPoolMxBeans;

    public LocalMemoryPoolDataCollector(List<MemoryPoolMXBean> memoryPoolMxBeans) {
        this.memoryPoolMxBeans = memoryPoolMxBeans;
    }

    @Override
    public void onBeforeTest() {
        memoryPoolMxBeans.forEach(b -> putBeforeMemoryUsage(new Pool(b.getName(), b.getType()), b.getUsage()));
    }

    @Override
    public void onAfterTest() {
        memoryPoolMxBeans.forEach(b -> putAfterMemoryUsage(new Pool(b.getName(), b.getType()), b.getUsage()));
    }
}
