package memory.benchmark.internal.runner.remote;

import memory.benchmark.examples.ListMemoryTest;

import javax.management.*;
import javax.management.remote.JMXConnectorServer;
import javax.management.remote.JMXServiceURL;
import java.lang.management.*;
import java.rmi.registry.Registry;

import static java.lang.management.ManagementFactory.*;
import static java.rmi.registry.LocateRegistry.createRegistry;
import static java.rmi.server.UnicastRemoteObject.exportObject;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyMap;
import static javax.management.MBeanServerFactory.createMBeanServer;
import static javax.management.remote.JMXConnectorServerFactory.newJMXConnectorServer;

public class RemoteServer {

    static final String BENCHMARK_OBJECT_NAME = "benchmark";
    static final String SERVER_URL = "service:jmx:rmi:///jndi/rmi://localhost:3000/jmxrmi";
    public static final String MBEAN_SERVER_DOMAIN = "jmxrmi";


    private static RemoteServer serverHolder;

    public static void main(String... args) throws Exception {
        args = (String[]) asList(ListMemoryTest.class.getCanonicalName(), "10000", "3000").toArray();

        String clazz = args[0];
        int benchmarkRmiPort = Integer.parseInt(args[1]);
        int mBeanServerRmiPort = Integer.parseInt(args[2]);

        serverHolder = new RemoteServer(clazz, benchmarkRmiPort, mBeanServerRmiPort);
        serverHolder.startServer();
    }

    private final String benchmarkClassName;
    private final int benchmarkRmiPort;
    private final int mBeanServerRmiPort;

    public RemoteServer(String benchmarkClassName, int benchmarkRmiPort, int mBeanServerRmiPort) {
        this.benchmarkClassName = benchmarkClassName;
        this.benchmarkRmiPort = benchmarkRmiPort;
        this.mBeanServerRmiPort = mBeanServerRmiPort;
    }

    private void startServer() throws Exception {
        registerBenchmarkRemote();
        registerMBeansServer();
    }

    private void registerBenchmarkRemote() throws Exception {
        Class benchmarkClass = Class.forName(benchmarkClassName);
        Object benchmarkObject = benchmarkClass.newInstance();
        BenchmarkRemote benchmarkRemote = new BenchmarkRemoteImpl(benchmarkObject);
        BenchmarkRemote stub = (BenchmarkRemote) exportObject(benchmarkRemote, 0);
        Registry registry = createRegistry(benchmarkRmiPort);
        registry.bind(BENCHMARK_OBJECT_NAME, stub);
    }

    private void registerMBeansServer() throws Exception {
        createRegistry(mBeanServerRmiPort);
        MBeanServer server = createMBeanServer(MBEAN_SERVER_DOMAIN);

        JMXConnectorServer connector = newJMXConnectorServer(new JMXServiceURL(SERVER_URL), emptyMap(), server);

        register(server, getMemoryMXBean(), MEMORY_MXBEAN_NAME);

        for(MemoryPoolMXBean b : getMemoryPoolMXBeans()) {
            register(server, b, MEMORY_POOL_MXBEAN_DOMAIN_TYPE + b.getName());
        }

        for(GarbageCollectorMXBean gc : getGarbageCollectorMXBeans()) {
            register(server, gc, GARBAGE_COLLECTOR_MXBEAN_DOMAIN_TYPE + gc.getName());
        }

        connector.start();
    }

    private void register(MBeanServer server, PlatformManagedObject mBean, String name) throws Exception {
        ObjectName mBeanName = new ObjectName(name);
        if(!server.isRegistered(mBeanName)) {
            server.registerMBean(mBean, mBeanName);
        }
    }
}
