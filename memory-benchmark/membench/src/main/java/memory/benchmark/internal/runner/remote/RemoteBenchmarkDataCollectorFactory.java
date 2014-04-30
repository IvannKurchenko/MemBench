package memory.benchmark.internal.runner.remote;

import memory.benchmark.api.Options;
import memory.benchmark.api.exception.BenchmarkRunException;
import memory.benchmark.internal.collect.*;
import memory.benchmark.internal.util.Factory;

import javax.management.*;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;
import java.io.IOException;
import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryPoolMXBean;
import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.toList;
import static javax.management.JMX.newMBeanProxy;

public class RemoteBenchmarkDataCollectorFactory implements Factory<BenchmarkDataCollector, Object> {

    private final Options options;

    public RemoteBenchmarkDataCollectorFactory(Options options) {
        this.options = options;
    }

    @Override
    public BenchmarkDataCollector create() {
        try {

            JMXServiceURL target = new JMXServiceURL("service:jmx:rmi:///jndi/rmi://localhost:1099/jmxrmi");
            JMXConnector connector = JMXConnectorFactory.connect(target);
            MBeanServerConnection remote = connector.getMBeanServerConnection();

            List<GarbageCollectorMXBean> garbageCollectorMXBeans = createMBeanProxies(ManagementFactory.GARBAGE_COLLECTOR_MXBEAN_DOMAIN_TYPE, GarbageCollectorMXBean.class, remote);
            MemoryMXBean memoryMXBean = createMBeanProxy(ManagementFactory.MEMORY_MXBEAN_NAME, MemoryMXBean.class, remote);
            List<MemoryPoolMXBean> memoryPoolMxBeans = createMBeanProxies(ManagementFactory.MEMORY_POOL_MXBEAN_DOMAIN_TYPE, MemoryPoolMXBean.class, remote);

            BenchmarkDataCollector gcResultCollector = new GcDataCollector(garbageCollectorMXBeans);
            BenchmarkDataCollector memoryResultCollector = new MemoryDataCollector(memoryMXBean);
            BenchmarkDataCollector memoryPoolResultCollector = new MemoryPoolDataCollector(memoryPoolMxBeans);

            return new RemoteBenchmarkDataCollectors(connector, gcResultCollector, memoryResultCollector, memoryPoolResultCollector);

        } catch (IOException | MalformedObjectNameException e) {
            e.printStackTrace();
            throw new BenchmarkRunException(e);
        }
    }

    private <T> T createMBeanProxy(String beanName, Class<T> beanClass, MBeanServerConnection remote) throws MalformedObjectNameException {
        ObjectName mBeanName = new ObjectName(beanName);
        return newMBeanProxy(remote, mBeanName, beanClass, true);
    }

    private <T> List<T> createMBeanProxies(String beanType, Class<T> beanClass, MBeanServerConnection remote) throws MalformedObjectNameException, IOException {
        ObjectName mBeanType = new ObjectName(beanType);
        Set<ObjectInstance> instances = remote.queryMBeans(mBeanType, Query.isInstanceOf(Query.value(MemoryPoolMXBean.class.getName())));
        return instances.stream().map(i -> newMBeanProxy(remote, i.getObjectName(), beanClass, true)).collect(toList());
    }
}
