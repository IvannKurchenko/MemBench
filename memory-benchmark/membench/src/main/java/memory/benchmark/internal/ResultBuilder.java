package memory.benchmark.internal;

import memory.benchmark.api.result.*;

import java.lang.reflect.Method;
import java.util.List;

public class ResultBuilder {

    private Class benchmarkClass;
    private Method benchmarkMethod;

    private StatisticView<MemoryFootprint> heapMemoryFootprint, nonHeapMemoryFootprint;
    private List<MemoryPoolStatisticView> memoryPoolFootprints;
    private List<StatisticView<GcUsage>> gcUsages;

    public ResultBuilder(Class benchmarkClass, Method benchmarkMethod) {
        this.benchmarkClass = benchmarkClass;
        this.benchmarkMethod = benchmarkMethod;
    }

    public void setHeapMemoryDifference(StatisticView<MemoryFootprint> heapMemoryFootprint,
                                        StatisticView<MemoryFootprint> nonHeapMemoryFootprint) {
        this.heapMemoryFootprint = heapMemoryFootprint;
        this.nonHeapMemoryFootprint = nonHeapMemoryFootprint;
    }

    public StatisticView<MemoryFootprint> getHeapMemoryFootprint() {
        return heapMemoryFootprint;
    }

    public StatisticView<MemoryFootprint> getNonHeapMemoryFootprint() {
        return nonHeapMemoryFootprint;
    }

    public List<MemoryPoolStatisticView> getMemoryPoolFootprints() {
        return memoryPoolFootprints;
    }

    public void setMemoryPoolFootprints(List<MemoryPoolStatisticView> memoryPoolFootprints) {
        this.memoryPoolFootprints = memoryPoolFootprints;
    }

    public List<StatisticView<GcUsage>> getGcUsages() {
        return gcUsages;
    }

    public void setGcUsages(List<StatisticView<GcUsage>> gcUsages) {
        this.gcUsages = gcUsages;
    }

    public Class getBenchmarkClass() {
        return benchmarkClass;
    }

    public Method getBenchmarkMethod() {
        return benchmarkMethod;
    }

    public Result build() {
        return new Result(this);
    }
}
