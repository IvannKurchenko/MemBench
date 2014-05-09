package memory.benchmark.internal.collect;

import memory.benchmark.api.result.GcUsage;
import memory.benchmark.api.result.StatisticView;
import memory.benchmark.internal.ResultBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static memory.benchmark.internal.collect.StatisticCollector.getStatistic;

public abstract class AbstractGcDataCollector implements BenchmarkDataCollector {

    private final Map<String, List<GcUsage>> beforeGarbageCollection;
    private final Map<String, List<GcUsage>> afterGarbageCollection;

    public AbstractGcDataCollector() {
        this.beforeGarbageCollection = new HashMap<>();
        this.afterGarbageCollection = new HashMap<>();
    }

    @Override
    public void collectBenchmarkData(ResultBuilder result) {
        List<StatisticView<GcUsage>> gcUsages = new ArrayList<>();
        beforeGarbageCollection.keySet().forEach(g -> gcUsages.add(createGcUsage(g)));
        result.setGcUsages(gcUsages);
    }

    @Override
    public void clear() {
        beforeGarbageCollection.clear();
        afterGarbageCollection.clear();
    }

    protected void addBeforeGcUsage(String gcName, long collectionsCount, long collectionsTime) {
        addGc(gcName, collectionsCount, collectionsTime, beforeGarbageCollection);
    }

    protected void addAfterGcUsage(String gcName, long collectionsCount, long collectionsTime) {
        addGc(gcName, collectionsCount, collectionsTime, afterGarbageCollection);
    }

    private void addGc(String gcName, long collectionsCount, long collectionsTime, Map<String, List<GcUsage>> mxBeanMap) {
        List<GcUsage> gcList = mxBeanMap.get(gcName);
        if(gcList == null) {
            gcList = new ArrayList<>();
            mxBeanMap.put(gcName, gcList);
        }
        gcList.add(new GcUsage(gcName, collectionsCount, collectionsTime));
    }

    private StatisticView<GcUsage> createGcUsage(String gcMxBeanName) {
        List<GcUsage> before = beforeGarbageCollection.get(gcMxBeanName);
        List<GcUsage> after = afterGarbageCollection.get(gcMxBeanName);

        if(before.size() == 1) {
            GcUsage beforeGcUsage = before.get(0);
            GcUsage afterGcUsage = after.get(0);
            return new StatisticView<>(new GcUsage(beforeGcUsage, afterGcUsage));
        }  else {

            StatisticCollector.Statistic gcTime = getStatistic(after, before, GcUsage::getGcTime);
            StatisticCollector.Statistic gcCount = getStatistic(after, before, GcUsage::getGcCount);

            GcUsage minimum = new GcUsage(gcMxBeanName, gcTime.min, gcCount.min);
            GcUsage maximum = new GcUsage(gcMxBeanName, gcTime.max, gcCount.max);
            GcUsage average = new GcUsage(gcMxBeanName, gcTime.average, gcCount.average);
            return new StatisticView<>(minimum, maximum, average);
        }
    }
}