package memory.benchmark.internal.collect;

import memory.benchmark.api.result.MemoryFootprint;
import memory.benchmark.api.result.MemoryPoolFootprint;
import memory.benchmark.api.result.StatisticView;
import memory.benchmark.internal.ResultBuilder;

import java.lang.management.MemoryPoolMXBean;
import java.lang.management.MemoryType;
import java.lang.management.MemoryUsage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static memory.benchmark.internal.collect.StatisticCollector.*;

public class MemoryPoolResultCollector implements BenchmarkResultCollector {

    private final List<MemoryPoolMXBean> memoryPoolMxBeans;
    private final Map<MemoryPoolMXBean, List<MemoryUsage>> beforeMemoryPoolUsage;
    private final Map<MemoryPoolMXBean, List<MemoryUsage>> afterMemoryPoolUsage;

    public MemoryPoolResultCollector(List<MemoryPoolMXBean> memoryPoolMxBeans) {
        this.memoryPoolMxBeans = memoryPoolMxBeans;
        this.beforeMemoryPoolUsage = new HashMap<>();
        this.afterMemoryPoolUsage = new HashMap<>();
    }

    @Override
    public void onBeforeTest() {
        memoryPoolMxBeans.forEach(b -> putMemoryUsage(beforeMemoryPoolUsage, b));
    }

    @Override
    public void onAfterTest() {
        memoryPoolMxBeans.forEach(b -> putMemoryUsage(afterMemoryPoolUsage, b));
    }

    private void putMemoryUsage(Map<MemoryPoolMXBean, List<MemoryUsage>> memoryUsage, MemoryPoolMXBean mxBean) {
        List<MemoryUsage> memoryUsages = memoryUsage.get(mxBean);
        if(memoryUsages == null) {
            memoryUsages = new ArrayList<>();
            memoryUsage.put(mxBean, memoryUsages);
        }
        memoryUsages.add(mxBean.getUsage());
    }

    @Override
    public void collectBenchmarkResult(ResultBuilder result) {
        List<StatisticView<MemoryPoolFootprint>> memoryPoolFootprints = new ArrayList<>();
        beforeMemoryPoolUsage.keySet().forEach(mxBean-> memoryPoolFootprints.add(createMemoryPoolFootprint(mxBean)));
        result.setMemoryPoolFootprints(memoryPoolFootprints);
    }

    @Override
    public void clear() {
        beforeMemoryPoolUsage.clear();
        afterMemoryPoolUsage.clear();
    }

    private StatisticView<MemoryPoolFootprint> createMemoryPoolFootprint(MemoryPoolMXBean poolMXBean) {
        List<MemoryUsage> beforeMemoryUsages = beforeMemoryPoolUsage.get(poolMXBean);
        List<MemoryUsage> afterMemoryUsages = afterMemoryPoolUsage.get(poolMXBean);

        String name = poolMXBean.getName();
        MemoryType type = poolMXBean.getType();

        if(beforeMemoryUsages.size() == 1) {
            MemoryUsage beforeMemoryUsage = beforeMemoryUsages.get(0);
            MemoryUsage afterMemoryUsage = afterMemoryUsages.get(0);
            return new StatisticView<>(new MemoryPoolFootprint(beforeMemoryUsage, afterMemoryUsage, name, type));

        }  else {
            Statistic usedMemory = getStatistic(afterMemoryUsages, beforeMemoryUsages, MemoryUsage::getUsed);
            Statistic committedMemory = getStatistic(afterMemoryUsages, beforeMemoryUsages, MemoryUsage::getCommitted);
            Statistic maxMemory = getStatistic(afterMemoryUsages, beforeMemoryUsages, MemoryUsage::getMax);

            MemoryPoolFootprint minimum = new MemoryPoolFootprint(usedMemory.min, maxMemory.min, committedMemory.min, name, type);
            MemoryPoolFootprint maximum = new MemoryPoolFootprint(usedMemory.max, maxMemory.max, committedMemory.max, name, type);
            MemoryPoolFootprint average = new MemoryPoolFootprint(usedMemory.average, maxMemory.average, committedMemory.average, name, type);
            return new StatisticView<>(minimum, maximum, average);
        }
    }


}
