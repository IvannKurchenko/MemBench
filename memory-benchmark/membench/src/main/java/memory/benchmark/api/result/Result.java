package memory.benchmark.api.result;

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

    public Result(Class benchmarkClass,
                  Method benchmarkMethod,
                  MemoryFootprint heapMemoryFootprint,
                  MemoryFootprint nonHeapMemoryFootprint,
                  List<MemoryPoolFootprint> memoryPoolFootprints,
                  List<GcUsage> gcUsages) {

        this.benchmarkClass = benchmarkClass;
        this.benchmarkMethod = benchmarkMethod;
        this.heapMemoryFootprint = heapMemoryFootprint;
        this.nonHeapMemoryFootprint = nonHeapMemoryFootprint;
        this.memoryPoolFootprints = memoryPoolFootprints;
        this.gcUsages = gcUsages;
    }

    public MemoryFootprint getHeapMemoryFootprint() {
        return heapMemoryFootprint;
    }

    public MemoryFootprint getNonHeapMemoryFootprint() {
        return nonHeapMemoryFootprint;
    }

    public List<MemoryPoolFootprint> getMemoryPoolFootprints() {
        return memoryPoolFootprints;
    }

    public List<GcUsage> getGcUsages() {
        return gcUsages;
    }

    public Class getBenchmarkClass() {
        return benchmarkClass;
    }

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
