package memory.benchmark.internal.collect;

import memory.benchmark.api.result.MemoryFootprint;
import memory.benchmark.api.result.MemoryPoolStatisticView;
import memory.benchmark.internal.ResultBuilder;

import java.lang.management.MemoryPoolMXBean;
import java.lang.management.MemoryType;
import java.lang.management.MemoryUsage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static memory.benchmark.internal.collect.StatisticCollector.*;

public class MemoryPoolDataCollector implements BenchmarkDataCollector {

    private final List<MemoryPoolMXBean> memoryPoolMxBeans;
    private final Map<MemoryPoolMXBean, List<MemoryUsage>> beforeMemoryPoolUsage;
    private final Map<MemoryPoolMXBean, List<MemoryUsage>> afterMemoryPoolUsage;

    public MemoryPoolDataCollector(List<MemoryPoolMXBean> memoryPoolMxBeans) {
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
    public void collectBenchmarkData(ResultBuilder result) {
        List<MemoryPoolStatisticView> memoryPoolFootprints = new ArrayList<>();
        beforeMemoryPoolUsage.keySet().forEach(mxBean-> memoryPoolFootprints.add(createMemoryPoolFootprint(mxBean)));
        result.setMemoryPoolFootprints(memoryPoolFootprints);
    }

    @Override
    public void clear() {
        beforeMemoryPoolUsage.clear();
        afterMemoryPoolUsage.clear();
    }

    private MemoryPoolStatisticView createMemoryPoolFootprint(MemoryPoolMXBean poolMXBean) {
        List<MemoryUsage> beforeMemoryUsages = beforeMemoryPoolUsage.get(poolMXBean);
        List<MemoryUsage> afterMemoryUsages = afterMemoryPoolUsage.get(poolMXBean);

        String name = poolMXBean.getName();
        MemoryType type = poolMXBean.getType();

        if(beforeMemoryUsages.size() == 1) {
            MemoryUsage beforeMemoryUsage = beforeMemoryUsages.get(0);
            MemoryUsage afterMemoryUsage = afterMemoryUsages.get(0);
            return new MemoryPoolStatisticView(new MemoryFootprint(beforeMemoryUsage, afterMemoryUsage), type, name);

        }  else {
            Statistic usedMemory = getStatistic(afterMemoryUsages, beforeMemoryUsages, MemoryUsage::getUsed);
            Statistic committedMemory = getStatistic(afterMemoryUsages, beforeMemoryUsages, MemoryUsage::getCommitted);
            Statistic maxMemory = getStatistic(afterMemoryUsages, beforeMemoryUsages, MemoryUsage::getMax);

            MemoryFootprint minimum = new MemoryFootprint(usedMemory.min, maxMemory.min, committedMemory.min);
            MemoryFootprint maximum = new MemoryFootprint(usedMemory.max, maxMemory.max, committedMemory.max);
            MemoryFootprint average = new MemoryFootprint(usedMemory.average, maxMemory.average, committedMemory.average);

            return new MemoryPoolStatisticView(minimum, maximum, average, type, name);
        }
    }


}
