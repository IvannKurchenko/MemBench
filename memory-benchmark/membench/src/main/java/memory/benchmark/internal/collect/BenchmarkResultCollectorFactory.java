package memory.benchmark.internal.collect;

import java.lang.management.ManagementFactory;

public class BenchmarkResultCollectorFactory {

    public static BenchmarkResultCollector createResultCollector() {
        BenchmarkResultCollector gcResultCollector = new GcResultCollector(ManagementFactory.getGarbageCollectorMXBeans());
        BenchmarkResultCollector memoryResultCollector = new MemoryResultCollector(ManagementFactory.getMemoryMXBean());
        BenchmarkResultCollector memoryPoolResultCollector = new MemoryPoolResultCollector(ManagementFactory.getMemoryPoolMXBeans());
        return new BenchmarkResultCollectors(gcResultCollector, memoryResultCollector, memoryPoolResultCollector);
    }
}
