package memory.benchmark.internal.runner.remote.collect;

import memory.benchmark.api.Options;
import memory.benchmark.api.exception.BenchmarkRunException;
import memory.benchmark.internal.collect.*;
import memory.benchmark.internal.runner.local.collect.LocalGcDataCollector;
import memory.benchmark.internal.util.Factory;

import javax.management.*;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;
import java.io.IOException;
import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryPoolMXBean;
import java.net.MalformedURLException;
import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.toList;
import static javax.management.JMX.newMBeanProxy;
import static memory.benchmark.internal.util.ThrowableHandler.handleThrowableFunction;

public class RemoteBenchmarkDataCollectorFactory implements Factory<BenchmarkDataCollector, Object> {

    private final Options options;

    public RemoteBenchmarkDataCollectorFactory(Options options) {
        this.options = options;
    }

    @Override
    public BenchmarkDataCollector create() {
        return handleThrowableFunction(this::createCollector);
    }

    private BenchmarkDataCollector createCollector() throws IOException {
        JMXServiceURL target = new JMXServiceURL("service:jmx:rmi:///jndi/rmi://localhost:3000/jmxrmi");
        JMXConnector connector = JMXConnectorFactory.connect(target);
        MBeanServerConnection remote = connector.getMBeanServerConnection();

        BenchmarkDataCollector gcResultCollector = new RemoteGcDataCollector(remote);
        BenchmarkDataCollector memoryResultCollector = new RemoteMemoryDataCollector(remote);
        BenchmarkDataCollector memoryPoolResultCollector = new RemoteMemoryPoolDataCollector(remote);

        return new RemoteBenchmarkDataCollectors(connector, gcResultCollector, memoryResultCollector, memoryPoolResultCollector);
    }
}
