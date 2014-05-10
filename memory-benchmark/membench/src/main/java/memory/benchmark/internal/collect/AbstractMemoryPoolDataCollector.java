package memory.benchmark.internal.collect;

import memory.benchmark.api.result.MemoryFootprint;
import memory.benchmark.api.result.MemoryPoolStatisticView;
import memory.benchmark.internal.ResultBuilder;

import java.lang.management.MemoryType;
import java.lang.management.MemoryUsage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static memory.benchmark.internal.collect.StatisticCollector.getStatistic;

public abstract class AbstractMemoryPoolDataCollector implements BenchmarkDataCollector {

    private final Map<Pool, List<MemoryUsage>> beforeMemoryPoolUsage;
    private final Map<Pool, List<MemoryUsage>> afterMemoryPoolUsage;

    public AbstractMemoryPoolDataCollector() {
        this.beforeMemoryPoolUsage = new HashMap<>();
        this.afterMemoryPoolUsage = new HashMap<>();
    }

    @Override
    public void collectBenchmarkData(ResultBuilder result) {
        List<MemoryPoolStatisticView> memoryPoolFootprints = new ArrayList<>();
        beforeMemoryPoolUsage.keySet().forEach(mxBean -> memoryPoolFootprints.add(createMemoryPoolFootprint(mxBean)));
        result.setMemoryPoolFootprints(memoryPoolFootprints);
    }

    @Override
    public void clear() {
        beforeMemoryPoolUsage.clear();
        afterMemoryPoolUsage.clear();
    }

    protected void putBeforeMemoryUsage(Pool pool, MemoryUsage memoryUsage) {
        putMemoryUsage(beforeMemoryPoolUsage, pool, memoryUsage);
    }

    protected void putAfterMemoryUsage(Pool pool, MemoryUsage memoryUsage) {
        putMemoryUsage(afterMemoryPoolUsage, pool, memoryUsage);
    }

    private void putMemoryUsage(Map<Pool, List<MemoryUsage>> memoryUsageMap, Pool pool, MemoryUsage memoryUsage) {
        List<MemoryUsage> memoryUsages = memoryUsageMap.get(pool);
        if (memoryUsages == null) {
            memoryUsages = new ArrayList<>();
            memoryUsageMap.put(pool, memoryUsages);
        }
        memoryUsages.add(memoryUsage);
    }

    private MemoryPoolStatisticView createMemoryPoolFootprint(Pool pool) {
        List<MemoryUsage> beforeMemoryUsages = beforeMemoryPoolUsage.get(pool);
        List<MemoryUsage> afterMemoryUsages = afterMemoryPoolUsage.get(pool);

        if (beforeMemoryUsages.size() == 1) {
            MemoryUsage beforeMemoryUsage = beforeMemoryUsages.get(0);
            MemoryUsage afterMemoryUsage = afterMemoryUsages.get(0);
            return new MemoryPoolStatisticView(new MemoryFootprint(beforeMemoryUsage, afterMemoryUsage), pool.type, pool.name);

        } else {

            StatisticCollector.Statistic usedMemory = getStatistic(afterMemoryUsages, beforeMemoryUsages, MemoryUsage::getUsed);
            StatisticCollector.Statistic committedMemory = getStatistic(afterMemoryUsages, beforeMemoryUsages, MemoryUsage::getCommitted);
            StatisticCollector.Statistic maxMemory = getStatistic(afterMemoryUsages, beforeMemoryUsages, MemoryUsage::getMax);

            MemoryFootprint minimum = new MemoryFootprint(usedMemory.min, maxMemory.min, committedMemory.min);
            MemoryFootprint maximum = new MemoryFootprint(usedMemory.max, maxMemory.max, committedMemory.max);
            MemoryFootprint average = new MemoryFootprint(usedMemory.average, maxMemory.average, committedMemory.average);

            return new MemoryPoolStatisticView(minimum, maximum, average, pool.type, pool.name);
        }
    }

    protected static class Pool {

        protected final String name;
        protected final MemoryType type;

        public Pool(String name, MemoryType type) {
            this.name = name;
            this.type = type;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Pool pool = (Pool) o;

            if (name != null ? !name.equals(pool.name) : pool.name != null) return false;
            if (type != pool.type) return false;

            return true;
        }

        @Override
        public int hashCode() {
            int result = name != null ? name.hashCode() : 0;
            result = 31 * result + (type != null ? type.hashCode() : 0);
            return result;
        }
    }
}
