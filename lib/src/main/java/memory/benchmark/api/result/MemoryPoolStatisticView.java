package memory.benchmark.api.result;

import java.lang.management.MemoryType;

/**
 *
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
     *
     * @return
     */
    public MemoryType getMemoryType() {
        return memoryType;
    }

    /**
     *
     * @return
     */
    public String getName() {
        return name;
    }
}
