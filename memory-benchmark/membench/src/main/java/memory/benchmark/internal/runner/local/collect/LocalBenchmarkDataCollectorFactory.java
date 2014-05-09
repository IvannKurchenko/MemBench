package memory.benchmark.internal.runner.local.collect;

import memory.benchmark.internal.collect.*;
import memory.benchmark.internal.util.Factory;

import java.lang.management.ManagementFactory;

public class LocalBenchmarkDataCollectorFactory implements Factory<BenchmarkDataCollector, Object> {

    public BenchmarkDataCollector create() {
        BenchmarkDataCollector gcResultCollector = new LocalGcDataCollector(ManagementFactory.getGarbageCollectorMXBeans());
        BenchmarkDataCollector memoryResultCollector = new LocalMemoryDataCollector(ManagementFactory.getMemoryMXBean());
        BenchmarkDataCollector memoryPoolResultCollector = new LocalMemoryPoolDataCollector(ManagementFactory.getMemoryPoolMXBeans());
        return new BenchmarkDataCollectors(gcResultCollector, memoryResultCollector, memoryPoolResultCollector);
    }
}
