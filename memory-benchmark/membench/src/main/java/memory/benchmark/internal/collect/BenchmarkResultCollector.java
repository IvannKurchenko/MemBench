package memory.benchmark.internal.collect;

import memory.benchmark.internal.ResultBuilder;

public interface BenchmarkResultCollector {

    void onBeforeTest();

    void onAfterTest();

    void collectBenchmarkResult(ResultBuilder result);

    void clear();
}
