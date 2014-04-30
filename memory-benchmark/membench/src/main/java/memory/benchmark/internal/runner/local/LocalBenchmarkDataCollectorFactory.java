package memory.benchmark.internal.runner.local;

import memory.benchmark.internal.collect.*;
import memory.benchmark.internal.util.Factory;

import java.lang.management.ManagementFactory;

public class LocalBenchmarkDataCollectorFactory implements Factory<BenchmarkDataCollector, Object> {

    public BenchmarkDataCollector create() {
        BenchmarkDataCollector gcResultCollector = new GcDataCollector(ManagementFactory.getGarbageCollectorMXBeans());
        BenchmarkDataCollector memoryResultCollector = new MemoryDataCollector(ManagementFactory.getMemoryMXBean());
        BenchmarkDataCollector memoryPoolResultCollector = new MemoryPoolDataCollector(ManagementFactory.getMemoryPoolMXBeans());
        return new BenchmarkDataCollectors(gcResultCollector, memoryResultCollector, memoryPoolResultCollector);
    }
}
