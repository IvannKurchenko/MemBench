package memory.benchmark.internal.runner.local.collect;

import memory.benchmark.internal.collect.AbstractMemoryPoolDataCollector;

import java.lang.management.MemoryPoolMXBean;
import java.lang.management.MemoryUsage;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
