package memory.benchmark.internal.collect;

import memory.benchmark.api.result.MemoryPoolFootprint;
import memory.benchmark.internal.ResultBuilder;

import java.lang.management.MemoryPoolMXBean;
import java.lang.management.MemoryUsage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MemoryPoolResultCollector implements BenchmarkResultCollector {

    private final List<MemoryPoolMXBean> memoryPoolMxBeans;
    private final Map<MemoryPoolMXBean, MemoryUsage> beforeMemoryPoolUsage;
    private final Map<MemoryPoolMXBean, MemoryUsage> afterMemoryPoolUsage;

    public MemoryPoolResultCollector(List<MemoryPoolMXBean> memoryPoolMxBeans) {
        this.memoryPoolMxBeans = memoryPoolMxBeans;
        this.beforeMemoryPoolUsage = new HashMap<>();
        this.afterMemoryPoolUsage = new HashMap<>();
    }

    @Override
    public void onBeforeTest() {
        memoryPoolMxBeans.forEach(b->beforeMemoryPoolUsage.put(b, b.getUsage()));
    }

    @Override
    public void onAfterTest() {
        memoryPoolMxBeans.forEach(b->afterMemoryPoolUsage.put(b, b.getUsage()));
    }

    @Override
    public void collectBenchmarkResult(ResultBuilder result) {
        List<MemoryPoolFootprint> memoryPoolFootprints = new ArrayList<>();
        beforeMemoryPoolUsage.keySet().forEach(entry-> memoryPoolFootprints.add(createMemoryPoolFootprint(entry)));
        result.setMemoryPoolFootprints(memoryPoolFootprints);
    }

    private MemoryPoolFootprint createMemoryPoolFootprint(MemoryPoolMXBean poolMXBean) {
        MemoryUsage beforeMemoryUsage = beforeMemoryPoolUsage.get(poolMXBean);
        MemoryUsage afterMemoryUsage = afterMemoryPoolUsage.get(poolMXBean);
        return new MemoryPoolFootprint(beforeMemoryUsage, afterMemoryUsage, poolMXBean.getName(), poolMXBean.getType());
    }
}
