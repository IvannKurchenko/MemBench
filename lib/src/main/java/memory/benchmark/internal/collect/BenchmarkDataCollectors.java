package memory.benchmark.internal.collect;

import memory.benchmark.internal.runner.ResultBuilder;

import java.util.List;

import static java.util.Arrays.asList;

public class BenchmarkDataCollectors implements BenchmarkDataCollector {

    private final List<BenchmarkDataCollector> benchmarkDataCollectors;

    public BenchmarkDataCollectors(BenchmarkDataCollector... benchmarkDataCollectors) {
        this.benchmarkDataCollectors = asList(benchmarkDataCollectors);
    }

    @Override
    public void onBeforeTest() {
        benchmarkDataCollectors.forEach(BenchmarkDataCollector::onBeforeTest);
    }

    @Override
    public void onAfterTest() {
        benchmarkDataCollectors.forEach(BenchmarkDataCollector::onAfterTest);
    }

    @Override
    public void collectBenchmarkData(ResultBuilder result) {
        benchmarkDataCollectors.forEach(b -> b.collectBenchmarkData(result));
    }

    @Override
    public void clear() {
        benchmarkDataCollectors.forEach(BenchmarkDataCollector::clear);
    }
}
