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
    private final StatisticView<MemoryFootprint> heapMemoryFootprint, nonHeapMemoryFootprint;
    private final List<MemoryPoolStatisticView> memoryPoolFootprints;
    private final List<StatisticView<GcUsage>> gcUsages;

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
    public StatisticView<MemoryFootprint> getHeapMemoryFootprint() {
        return heapMemoryFootprint;
    }

    /**
     *
     * @return
     */
    public StatisticView<MemoryFootprint> getNonHeapMemoryFootprint() {
        return nonHeapMemoryFootprint;
    }

    /**
     *
     * @return
     */
    public List<MemoryPoolStatisticView> getMemoryPoolFootprints() {
        return memoryPoolFootprints;
    }

    /**
     *
     * @return
     */
    public List<StatisticView<GcUsage>> getGcUsages() {
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
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        Result result = (Result) o;

        if (benchmarkClass != null ? !benchmarkClass.equals(result.benchmarkClass) : result.benchmarkClass != null)
            return false;
        if (benchmarkMethod != null ? !benchmarkMethod.equals(result.benchmarkMethod) : result.benchmarkMethod != null)
            return false;
        if (gcUsages != null ? !gcUsages.equals(result.gcUsages) : result.gcUsages != null)
            return false;
        if (heapMemoryFootprint != null ? !heapMemoryFootprint.equals(result.heapMemoryFootprint) : result.heapMemoryFootprint != null)
            return false;
        if (memoryPoolFootprints != null ? !memoryPoolFootprints.equals(result.memoryPoolFootprints) : result.memoryPoolFootprints != null)
            return false;
        if (nonHeapMemoryFootprint != null ? !nonHeapMemoryFootprint.equals(result.nonHeapMemoryFootprint) : result.nonHeapMemoryFootprint != null)
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
