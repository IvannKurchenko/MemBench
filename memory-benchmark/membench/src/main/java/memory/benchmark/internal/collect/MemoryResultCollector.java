package memory.benchmark.internal.collect;

import memory.benchmark.api.result.MemoryFootprint;
import memory.benchmark.internal.ResultBuilder;

import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;

public class MemoryResultCollector implements BenchmarkResultCollector {

    private final MemoryMXBean memoryMXBean;
    private MemoryUsage beforeHeapMemoryUsage, beforeNonHeapMemoryUsage;
    private MemoryUsage afterHeapMemoryUsage, afterNonHeapMemoryUsage;

    public MemoryResultCollector(MemoryMXBean memoryMXBean) {
        this.memoryMXBean = memoryMXBean;
    }

    @Override
    public void onBeforeTest() {
        beforeHeapMemoryUsage = memoryMXBean.getHeapMemoryUsage();
        beforeNonHeapMemoryUsage = memoryMXBean.getNonHeapMemoryUsage();
    }

    @Override
    public void onAfterTest() {
        afterHeapMemoryUsage = memoryMXBean.getHeapMemoryUsage();
        afterNonHeapMemoryUsage = memoryMXBean.getNonHeapMemoryUsage();
    }

    @Override
    public void collectBenchmarkResult(ResultBuilder resultBuilder) {
        MemoryFootprint heapFootprint = new MemoryFootprint(beforeHeapMemoryUsage, afterHeapMemoryUsage);
        MemoryFootprint nonHeapFootprint = new MemoryFootprint(beforeNonHeapMemoryUsage, afterNonHeapMemoryUsage);
        resultBuilder.setHeapMemoryDifference(heapFootprint, nonHeapFootprint);
    }
}
