package memory.benchmark.internal.collect;

import memory.benchmark.api.result.GcUsage;
import memory.benchmark.api.result.StatisticView;
import memory.benchmark.internal.ResultBuilder;

import java.lang.management.GarbageCollectorMXBean;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static memory.benchmark.internal.collect.StatisticCollector.*;

public class GcDataCollector implements BenchmarkDataCollector {

    private final List<GarbageCollectorMXBean> garbageCollectorMXBeans;
    private final Map<GarbageCollectorMXBean, List<GcUsage>> beforeGarbageCollection;
    private final Map<GarbageCollectorMXBean, List<GcUsage>> afterGarbageCollection;

    public GcDataCollector(List<GarbageCollectorMXBean> garbageCollectorMXBeans) {
        this.garbageCollectorMXBeans = garbageCollectorMXBeans;
        this.beforeGarbageCollection = new HashMap<>();
        this.afterGarbageCollection = new HashMap<>();
    }

    @Override
    public void onBeforeTest() {
        addGc(beforeGarbageCollection);
    }

    @Override
    public void onAfterTest() {
        addGc(afterGarbageCollection);
    }

    private void addGc(Map<GarbageCollectorMXBean, List<GcUsage>> mxBeanMap) {
        garbageCollectorMXBeans.forEach(g -> {
            List<GcUsage> gcList = mxBeanMap.get(g);
            if(gcList == null) {
                gcList = new ArrayList<>();
                mxBeanMap.put(g, gcList);
            }
            gcList.add(new GcUsage(g.getName(), g.getCollectionCount(), g.getCollectionTime()));
        });
    }

    @Override
    public void collectBenchmarkData(ResultBuilder result) {
        List<StatisticView<GcUsage>> gcUsages = new ArrayList<>();
        garbageCollectorMXBeans.forEach(g -> gcUsages.add(createGcUsage(g)));
        result.setGcUsages(gcUsages);
    }

    @Override
    public void clear() {
        beforeGarbageCollection.clear();
        afterGarbageCollection.clear();
    }

    private StatisticView<GcUsage> createGcUsage(GarbageCollectorMXBean gcMxBean) {
        List<GcUsage> before = beforeGarbageCollection.get(gcMxBean);
        List<GcUsage> after = afterGarbageCollection.get(gcMxBean);

        if(before.size() == 1) {
            GcUsage beforeGcUsage = before.get(0);
            GcUsage afterGcUsage = after.get(0);
            return new StatisticView<>(new GcUsage(beforeGcUsage, afterGcUsage));
        }  else {

            StatisticCollector.Statistic gcTime = getStatistic(after, before, GcUsage::getGcTime);
            StatisticCollector.Statistic gcCount = getStatistic(after, before, GcUsage::getGcCount);

            GcUsage minimum = new GcUsage(gcMxBean.getName(), gcTime.min, gcCount.min);
            GcUsage maximum = new GcUsage(gcMxBean.getName(), gcTime.max, gcCount.max);
            GcUsage average = new GcUsage(gcMxBean.getName(), gcTime.average, gcCount.average);
            return new StatisticView<>(minimum, maximum, average);
        }
    }
}
