package memory.benchmark.internal;

import memory.benchmark.api.annotations.Benchmark;
import memory.benchmark.api.result.*;

import java.lang.reflect.Method;
import java.util.List;

public class ResultBuilder {

    private Class benchmarkClass;
    private Method benchmarkMethod;
    private Benchmark benchmark;

    private StatisticView<MemoryFootprint> heapMemoryFootprint, nonHeapMemoryFootprint;
    private List<MemoryPoolStatisticView> memoryPoolFootprints;
    private List<GcUsageStatisticView> gcUsages;

    public ResultBuilder(Class benchmarkClass, Method benchmarkMethod, Benchmark benchmark) {
        this.benchmarkClass = benchmarkClass;
        this.benchmarkMethod = benchmarkMethod;
        this.benchmark = benchmark;
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

    public List<GcUsageStatisticView> getGcUsages() {
        return gcUsages;
    }

    public void setGcUsages(List<GcUsageStatisticView> gcUsages) {
        this.gcUsages = gcUsages;
    }

    public Class getBenchmarkClass() {
        return benchmarkClass;
    }

    public Method getBenchmarkMethod() {
        return benchmarkMethod;
    }

    public Benchmark getBenchmark() {
        return benchmark;
    }

    public BenchmarkResult build() {
        return new BenchmarkResult(this);
    }
}
