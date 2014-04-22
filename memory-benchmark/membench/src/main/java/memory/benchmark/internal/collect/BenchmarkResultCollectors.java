package memory.benchmark.internal.collect;

import memory.benchmark.internal.ResultBuilder;

import java.util.List;

import static java.util.Arrays.asList;

public class BenchmarkResultCollectors implements BenchmarkResultCollector {

    private final List<BenchmarkResultCollector> benchmarkResultCollectors;

    public BenchmarkResultCollectors(BenchmarkResultCollector... benchmarkResultCollectors) {
        this.benchmarkResultCollectors = asList(benchmarkResultCollectors);
    }

    @Override
    public void onBeforeTest() {
        benchmarkResultCollectors.forEach(BenchmarkResultCollector::onBeforeTest);
    }

    @Override
    public void onAfterTest() {
        benchmarkResultCollectors.forEach(BenchmarkResultCollector::onAfterTest);
    }

    @Override
    public void collectBenchmarkResult(ResultBuilder result) {
        benchmarkResultCollectors.forEach(b -> b.collectBenchmarkResult(result));
    }

    @Override
    public void clear() {
        benchmarkResultCollectors.forEach(BenchmarkResultCollector::clear);
    }
}
