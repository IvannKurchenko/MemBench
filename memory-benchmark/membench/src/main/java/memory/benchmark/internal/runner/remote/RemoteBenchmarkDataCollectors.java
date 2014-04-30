package memory.benchmark.internal.runner.remote;

import memory.benchmark.internal.collect.BenchmarkDataCollector;
import memory.benchmark.internal.collect.BenchmarkDataCollectors;

import javax.management.remote.JMXConnector;
import java.io.IOException;

public class RemoteBenchmarkDataCollectors extends BenchmarkDataCollectors {

    private final JMXConnector connector;

    public RemoteBenchmarkDataCollectors(JMXConnector connector, BenchmarkDataCollector... benchmarkDataCollectors) {
        super(benchmarkDataCollectors);
        this.connector = connector;
    }

    @Override
    public void close() {
        try {
            connector.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
