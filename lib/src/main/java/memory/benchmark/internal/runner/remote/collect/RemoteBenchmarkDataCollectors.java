package memory.benchmark.internal.runner.remote.collect;

import memory.benchmark.internal.collect.BenchmarkDataCollector;
import memory.benchmark.internal.collect.BenchmarkDataCollectors;

import javax.management.remote.JMXConnector;

import static memory.benchmark.internal.util.ThrowableHandlers.ignoreThrowableAction;
import static memory.benchmark.internal.util.ThrowableHandlers.printThrowableAction;

public class RemoteBenchmarkDataCollectors extends BenchmarkDataCollectors {

    private final JMXConnector connector;

    public RemoteBenchmarkDataCollectors(JMXConnector connector, BenchmarkDataCollector... benchmarkDataCollectors) {
        super(benchmarkDataCollectors);
        this.connector = connector;
    }

    @Override
    public void close() {
        ignoreThrowableAction(connector::close);
    }
}
