package memory.benchmark.internal.runner.local.collect;

import memory.benchmark.internal.collect.BenchmarkDataCollector;
import memory.benchmark.internal.collect.BenchmarkDataCollectors;
import memory.benchmark.internal.util.Factory;

import java.lang.management.ManagementFactory;

import static java.lang.management.ManagementFactory.getGarbageCollectorMXBeans;
import static java.lang.management.ManagementFactory.getMemoryMXBean;
import static java.lang.management.ManagementFactory.getMemoryPoolMXBeans;

public class LocalBenchmarkDataCollectorFactory implements Factory<BenchmarkDataCollector, Object> {

    public BenchmarkDataCollector create() {
        BenchmarkDataCollector gcResultCollector = new LocalGcDataCollector(getGarbageCollectorMXBeans());
        BenchmarkDataCollector memoryResultCollector = new LocalMemoryDataCollector(getMemoryMXBean());
        BenchmarkDataCollector memoryPoolResultCollector = new LocalMemoryPoolDataCollector(getMemoryPoolMXBeans());
        return new BenchmarkDataCollectors(gcResultCollector, memoryResultCollector, memoryPoolResultCollector);
    }
}
