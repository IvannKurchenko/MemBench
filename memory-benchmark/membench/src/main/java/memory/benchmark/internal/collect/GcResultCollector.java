package memory.benchmark.internal.collect;

import memory.benchmark.api.result.GcUsage;
import memory.benchmark.internal.ResultBuilder;

import java.lang.management.GarbageCollectorMXBean;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GcResultCollector implements BenchmarkResultCollector {

    private final List<GarbageCollectorMXBean> garbageCollectorMXBeans;

    private final Map<GarbageCollectorMXBean, Long> beforeGarbageCount;
    private final Map<GarbageCollectorMXBean, Long> beforeGarbageTime;

    private final Map<GarbageCollectorMXBean, Long> afterGarbageCount;
    private final Map<GarbageCollectorMXBean, Long> afterGarbageTime;

    public GcResultCollector(List<GarbageCollectorMXBean> garbageCollectorMXBeans) {
        this.garbageCollectorMXBeans = garbageCollectorMXBeans;
        this.beforeGarbageCount = new HashMap<>();
        this.beforeGarbageTime = new HashMap<>();
        this.afterGarbageCount = new HashMap<>();
        this.afterGarbageTime = new HashMap<>();
    }

    @Override
    public void onBeforeTest() {
        garbageCollectorMXBeans.forEach(g -> beforeGarbageCount.put(g, g.getCollectionCount()));
        garbageCollectorMXBeans.forEach(g -> beforeGarbageTime.put(g, g.getCollectionTime()));
    }

    @Override
    public void onAfterTest() {
        garbageCollectorMXBeans.forEach(g -> afterGarbageCount.put(g, g.getCollectionCount()));
        garbageCollectorMXBeans.forEach(g -> afterGarbageTime.put(g, g.getCollectionTime()));
    }

    @Override
    public void collectBenchmarkResult(ResultBuilder result) {
        List<GcUsage> gcUsages = new ArrayList<>();
        garbageCollectorMXBeans.forEach(g -> gcUsages.add(createGcUsage(g)));
        result.setGcUsages(gcUsages);
    }

    private GcUsage createGcUsage(GarbageCollectorMXBean gcMxBean) {
        long gcCount = afterGarbageCount.get(gcMxBean) - beforeGarbageCount.get(gcMxBean);
        long gcTime = afterGarbageTime.get(gcMxBean) - beforeGarbageCount.get(gcMxBean);
        return new GcUsage(gcMxBean.getName(), gcCount, gcTime);
    }
}
