package memory.benchmark.internal.collect;

import memory.benchmark.api.result.MemoryFootprint;
import memory.benchmark.api.result.StatisticView;
import memory.benchmark.internal.ResultBuilder;

import java.lang.management.MemoryUsage;
import java.util.ArrayList;
import java.util.List;

import static memory.benchmark.internal.collect.StatisticCollector.getStatistic;

public abstract class AbstractMemoryDataCollector implements BenchmarkDataCollector{

    private final List<MemoryUsage> beforeHeapMemoryUsages, beforeNonHeapMemoryUsages;
    private final List<MemoryUsage> afterHeapMemoryUsages, afterNonHeapMemoryUsages;

    public AbstractMemoryDataCollector() {
        beforeHeapMemoryUsages = new ArrayList<>();
        beforeNonHeapMemoryUsages = new ArrayList<>();
        afterHeapMemoryUsages = new ArrayList<>();
        afterNonHeapMemoryUsages = new ArrayList<>();
    }

    @Override
    public void collectBenchmarkData(ResultBuilder resultBuilder) {
        StatisticView<MemoryFootprint> heapFootprint = getMemoryStatistic(beforeHeapMemoryUsages, afterHeapMemoryUsages);
        StatisticView<MemoryFootprint> nonHeapFootprint = getMemoryStatistic(beforeNonHeapMemoryUsages, afterNonHeapMemoryUsages);
        resultBuilder.setHeapMemoryDifference(heapFootprint, nonHeapFootprint);
    }

    @Override
    public void clear() {
        beforeHeapMemoryUsages.clear();
        afterHeapMemoryUsages.clear();
    }

    protected void addBeforeMemoryUsage(MemoryUsage heap, MemoryUsage nonHeap) {
        beforeHeapMemoryUsages.add(heap);
        beforeNonHeapMemoryUsages.add(nonHeap);
    }

    protected void addAfterMemoryUsage(MemoryUsage heap, MemoryUsage nonHeap) {
        afterHeapMemoryUsages.add(heap);
        afterNonHeapMemoryUsages.add(nonHeap);
    }

    private StatisticView<MemoryFootprint> getMemoryStatistic(List<MemoryUsage> before, List<MemoryUsage> after) {
        if(before.size() == 1) {
            MemoryUsage beforeMemoryUsage = before.get(0);
            MemoryUsage afterMemoryUsage = after.get(0);
            return new StatisticView<>(new MemoryFootprint(beforeMemoryUsage, afterMemoryUsage));

        }  else {
            StatisticCollector.Statistic usedMemory = getStatistic(after, before, MemoryUsage::getUsed);
            StatisticCollector.Statistic committedMemory = getStatistic(after, before, MemoryUsage::getCommitted);
            StatisticCollector.Statistic maxMemory = getStatistic(after, before, MemoryUsage::getMax);

            MemoryFootprint minimum = new MemoryFootprint(usedMemory.min, maxMemory.min, committedMemory.min);
            MemoryFootprint maximum = new MemoryFootprint(usedMemory.max, maxMemory.max, committedMemory.max);
            MemoryFootprint average = new MemoryFootprint(usedMemory.average, maxMemory.average, committedMemory.average);
            return new StatisticView<>(minimum, maximum, average);
        }
    }
}
