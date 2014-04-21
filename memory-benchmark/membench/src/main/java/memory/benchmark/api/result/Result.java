package memory.benchmark.api.result;

import memory.benchmark.internal.ResultBuilder;

import java.lang.reflect.Method;
import java.util.List;

/**
 *
 */
public class Result {

    private final Class benchmarkClass;
    private final Method benchmarkMethod;
    private final MemoryFootprint heapMemoryFootprint, nonHeapMemoryFootprint;
    private final List<MemoryPoolFootprint> memoryPoolFootprints;
    private final List<GcUsage> gcUsages;

    public Result(ResultBuilder builder) {
        this.benchmarkClass = builder.getBenchmarkClass();
        this.benchmarkMethod = builder.getBenchmarkMethod();
        this.heapMemoryFootprint = builder.getHeapMemoryFootprint();
        this.nonHeapMemoryFootprint = builder.getNonHeapMemoryFootprint();
        this.memoryPoolFootprints = builder.getMemoryPoolFootprints();
        this.gcUsages = builder.getGcUsages();
    }

    /**
     *
     * @return
     */
    public MemoryFootprint getHeapMemoryFootprint() {
        return heapMemoryFootprint;
    }

    /**
     *
     * @return
     */
    public MemoryFootprint getNonHeapMemoryFootprint() {
        return nonHeapMemoryFootprint;
    }

    /**
     *
     * @return
     */
    public List<MemoryPoolFootprint> getMemoryPoolFootprints() {
        return memoryPoolFootprints;
    }

    /**
     *
     * @return
     */
    public List<GcUsage> getGcUsages() {
        return gcUsages;
    }

    /**
     *
     * @return
     */
    public Class getBenchmarkClass() {
        return benchmarkClass;
    }

    /**
     *
     * @return
     */
    public Method getBenchmarkMethod() {
        return benchmarkMethod;
    }

    @Override
    public String toString() {
        return "Result{" +
                "benchmarkClass = " + benchmarkClass +
                ", benchmarkMethod=" + benchmarkMethod +
                ", heapMemoryFootprint=" + heapMemoryFootprint +
                ", nonHeapMemoryFootprint=" + nonHeapMemoryFootprint +
                ", memoryPoolFootprints=" + memoryPoolFootprints +
                ", gcUsages=" + gcUsages +
                '}';
    }
}
