package memory.benchmark.internal;

import memory.benchmark.api.result.GcUsage;
import memory.benchmark.api.result.MemoryFootprint;
import memory.benchmark.api.result.MemoryPoolFootprint;
import memory.benchmark.api.result.Result;

import java.lang.reflect.Method;
import java.util.List;

public class ResultBuilder {

    private Class benchmarkClass;
    private Method benchmarkMethod;

    private MemoryFootprint heapMemoryFootprint, nonHeapMemoryFootprint;
    private List<MemoryPoolFootprint> memoryPoolFootprints;
    private List<GcUsage> gcUsages;

    public ResultBuilder(Class benchmarkClass, Method benchmarkMethod) {
        this.benchmarkClass = benchmarkClass;
        this.benchmarkMethod = benchmarkMethod;
    }

    public void setHeapMemoryDifference(MemoryFootprint heapMemoryFootprint, MemoryFootprint nonHeapMemoryFootprint) {
        this.heapMemoryFootprint = heapMemoryFootprint;
        this.nonHeapMemoryFootprint = nonHeapMemoryFootprint;
    }

    public void setMemoryPoolFootprints(List<MemoryPoolFootprint> memoryPoolFootprints) {
        this.memoryPoolFootprints = memoryPoolFootprints;
    }

    public void setGcUsages(List<GcUsage> gcUsages) {
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

    public Result build(){
        return new Result(this);
    }
}
