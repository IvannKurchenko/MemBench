package memory.benchmark.internal.runner.local.collect;

import memory.benchmark.internal.collect.AbstractMemoryDataCollector;

import java.lang.management.MemoryMXBean;

public class LocalMemoryDataCollector extends AbstractMemoryDataCollector {

    private final MemoryMXBean memoryMXBean;

    public LocalMemoryDataCollector(MemoryMXBean memoryMXBean) {
        this.memoryMXBean = memoryMXBean;
    }

    @Override
    public void onBeforeTest() {
        addBeforeMemoryUsage(memoryMXBean.getHeapMemoryUsage(), memoryMXBean.getNonHeapMemoryUsage());
    }

    @Override
    public void onAfterTest() {
        addAfterMemoryUsage(memoryMXBean.getHeapMemoryUsage(), memoryMXBean.getNonHeapMemoryUsage());
    }
}
