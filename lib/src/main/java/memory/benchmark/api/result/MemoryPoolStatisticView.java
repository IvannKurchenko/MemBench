package memory.benchmark.api.result;

import java.lang.management.MemoryType;

/**
 * Represents {@link memory.benchmark.api.result.StatisticView} which contains name and type of related memory pool.
 *
 * @see java.lang.management.MemoryPoolMXBean
 * @see java.lang.management.ManagementFactory#getGarbageCollectorMXBeans()
 * @see memory.benchmark.api.result.MemoryFootprint
 */
public class MemoryPoolStatisticView extends StatisticView<MemoryFootprint> {

    private final MemoryType memoryType;
    private final String name;

    public MemoryPoolStatisticView(MemoryFootprint minimumValue, MemoryFootprint maximumValue, MemoryFootprint averageValue, MemoryType memoryType, String name) {
        super(minimumValue, maximumValue, averageValue);
        this.memoryType = memoryType;
        this.name = name;
    }

    public MemoryPoolStatisticView(MemoryFootprint singleValue, MemoryType memoryType, String name) {
        super(singleValue);
        this.memoryType = memoryType;
        this.name = name;
    }

    /**
     * @see java.lang.management.MemoryPoolMXBean#getType() ()
     * @return type of related memory pool.
     */
    public MemoryType getMemoryType() {
        return memoryType;
    }

    /**
     * @see java.lang.management.MemoryPoolMXBean#getType()
     * @return name of related memory pool.
     */
    public String getName() {
        return name;
    }
}
