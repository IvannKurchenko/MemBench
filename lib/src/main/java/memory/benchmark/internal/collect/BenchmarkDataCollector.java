package memory.benchmark.internal.collect;

import memory.benchmark.internal.runner.ResultBuilder;

public interface BenchmarkDataCollector extends AutoCloseable {

    void onBeforeTest();

    void onAfterTest();

    void collectBenchmarkData(ResultBuilder result);

    void clear();

    default void close() {
    }
}
