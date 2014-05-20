package memory.benchmark.api.result;

import memory.benchmark.api.annotations.Benchmark;
import memory.benchmark.internal.runner.ResultBuilder;

import java.lang.reflect.Method;
import java.util.List;

/**
 * Represents result of benchmark test for each method annotated with
 * {@link memory.benchmark.api.annotations.Benchmark} annotation.
 *
 * @see memory.benchmark.api.result.StatisticView
 * @see memory.benchmark.api.result.MemoryFootprint
 * @see memory.benchmark.api.result.MemoryPoolStatisticView
 * @see memory.benchmark.api.result.GcUsageStatisticView
 * @see memory.benchmark.api.result.GcUsage
 */
public class BenchmarkResult {

    private final Class benchmarkClass;
    private final Method benchmarkMethod;
    private final Benchmark benchmark;
    private final StatisticView<MemoryFootprint> heapMemoryFootprint, nonHeapMemoryFootprint;
    private final List<MemoryPoolStatisticView> memoryPoolFootprints;
    private final List<GcUsageStatisticView> gcUsages;
    private final Throwable testFailCause;

    public BenchmarkResult(ResultBuilder builder) {
        this.benchmarkClass = builder.getBenchmarkClass();
        this.benchmarkMethod = builder.getBenchmarkMethod();
        this.benchmark = builder.getBenchmark();
        this.heapMemoryFootprint = builder.getHeapMemoryFootprint();
        this.nonHeapMemoryFootprint = builder.getNonHeapMemoryFootprint();
        this.memoryPoolFootprints = builder.getMemoryPoolFootprints();
        this.gcUsages = builder.getGcUsages();
        this.testFailCause = builder.getTestFailCause();
    }

    /**
     * @return collected data about consumption of heap memory.
     */
    public StatisticView<MemoryFootprint> getHeapMemoryFootprint() {
        return heapMemoryFootprint;
    }

    /**
     * @return collected data about consumption of heap non memory.
     */
    public StatisticView<MemoryFootprint> getNonHeapMemoryFootprint() {
        return nonHeapMemoryFootprint;
    }

    /**
     * @return collected data about memory consumption of memory pools.
     */
    public List<MemoryPoolStatisticView> getMemoryPoolFootprints() {
        return memoryPoolFootprints;
    }

    /**
     * @return collected data about garbage collecting.
     */
    public List<GcUsageStatisticView> getGcUsages() {
        return gcUsages;
    }

    /**
     * @return class of benchmark test method.
     */
    public Class getBenchmarkClass() {
        return benchmarkClass;
    }

    /**
     * @return benchmark method under the test.
     */
    public Method getBenchmarkMethod() {
        return benchmarkMethod;
    }


    /**
     * @return {@link java.lang.Throwable} which was thrown during benchmark test execution.
     */
    public Throwable getTestFailCause() {
        return testFailCause;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        BenchmarkResult benchmarkResult = (BenchmarkResult) o;

        if (benchmarkClass != null ? !benchmarkClass.equals(benchmarkResult.benchmarkClass) : benchmarkResult.benchmarkClass != null)
            return false;
        if (benchmarkMethod != null ? !benchmarkMethod.equals(benchmarkResult.benchmarkMethod) : benchmarkResult.benchmarkMethod != null)
            return false;
        if (gcUsages != null ? !gcUsages.equals(benchmarkResult.gcUsages) : benchmarkResult.gcUsages != null)
            return false;
        if (heapMemoryFootprint != null ? !heapMemoryFootprint.equals(benchmarkResult.heapMemoryFootprint) : benchmarkResult.heapMemoryFootprint != null)
            return false;
        if (memoryPoolFootprints != null ? !memoryPoolFootprints.equals(benchmarkResult.memoryPoolFootprints) : benchmarkResult.memoryPoolFootprints != null)
            return false;
        if (nonHeapMemoryFootprint != null ? !nonHeapMemoryFootprint.equals(benchmarkResult.nonHeapMemoryFootprint) : benchmarkResult.nonHeapMemoryFootprint != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = benchmarkClass != null ? benchmarkClass.hashCode() : 0;
        result = 31 * result + (benchmarkMethod != null ? benchmarkMethod.hashCode() : 0);
        result = 31 * result + (heapMemoryFootprint != null ? heapMemoryFootprint.hashCode() : 0);
        result = 31 * result + (nonHeapMemoryFootprint != null ? nonHeapMemoryFootprint.hashCode() : 0);
        result = 31 * result + (memoryPoolFootprints != null ? memoryPoolFootprints.hashCode() : 0);
        result = 31 * result + (gcUsages != null ? gcUsages.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "BenchmarkResult{" +
                "benchmarkClass = " + benchmarkClass +
                ", benchmarkMethod=" + benchmarkMethod +
                ", heapMemoryFootprint=" + heapMemoryFootprint +
                ", nonHeapMemoryFootprint=" + nonHeapMemoryFootprint +
                ", memoryPoolFootprints=" + memoryPoolFootprints +
                ", gcUsages=" + gcUsages +
                '}';
    }

    public Benchmark getBenchmark() {
        return benchmark;
    }
}
