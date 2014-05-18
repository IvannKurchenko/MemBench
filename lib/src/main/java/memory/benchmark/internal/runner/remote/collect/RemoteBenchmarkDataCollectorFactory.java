package memory.benchmark.internal.runner.remote.collect;

import memory.benchmark.api.BenchmarkOptions;
import memory.benchmark.internal.collect.BenchmarkDataCollector;
import memory.benchmark.internal.util.Factory;

import javax.management.MBeanServerConnection;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;
import java.io.IOException;

import static memory.benchmark.internal.util.ThrowableHandlers.rethrowThrowableFunction;

public class RemoteBenchmarkDataCollectorFactory implements Factory<BenchmarkDataCollector, Object> {

    private static final String SERVICE_URL_PATTERN = "service:jmx:rmi:///jndi/rmi://localhost:%d/jmxrmi";

    private final BenchmarkOptions options;

    public RemoteBenchmarkDataCollectorFactory(BenchmarkOptions options) {
        this.options = options;
    }

    @Override
    public BenchmarkDataCollector create() {
        return rethrowThrowableFunction(this::createCollector);
    }

    private BenchmarkDataCollector createCollector() throws IOException {
        JMXServiceURL target = new JMXServiceURL(String.format(SERVICE_URL_PATTERN, options.getMxBeanRemotePort()));
        JMXConnector connector = JMXConnectorFactory.connect(target);
        MBeanServerConnection remote = connector.getMBeanServerConnection();

        BenchmarkDataCollector gcResultCollector = new RemoteGcDataCollector(remote);
        BenchmarkDataCollector memoryResultCollector = new RemoteMemoryDataCollector(remote);
        BenchmarkDataCollector memoryPoolResultCollector = new RemoteMemoryPoolDataCollector(remote);

        return new RemoteBenchmarkDataCollectors(connector, gcResultCollector, memoryResultCollector, memoryPoolResultCollector);
    }
}
