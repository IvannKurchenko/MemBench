package memory.benchmark.internal.runner.remote;

import com.sun.jndi.rmi.registry.RegistryContextFactory;
import memory.benchmark.examples.ListMemoryTest;

import javax.management.*;
import javax.management.remote.JMXConnectorServer;
import javax.management.remote.JMXServiceURL;
import javax.management.remote.rmi.RMIConnectorServer;
import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryPoolMXBean;
import java.lang.management.PlatformManagedObject;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;

import static java.util.Arrays.asList;
import static javax.management.remote.JMXConnectorServerFactory.newJMXConnectorServer;
import static memory.benchmark.internal.util.ThrowableActionHandler.wrapToBenchmarkRunException;

public class RemoteMain {

    static final String BENCHMARK_OBJECT_NAME = "benchmark";
    static final String SERVER_URL = "service:jmx:rmi:///jndi/rmi://localhost:1099/jmxrmi";

    public static void main(String... args) throws Exception {
        args = (String[]) asList(ListMemoryTest.class.getCanonicalName(), "10000").toArray();

        System.out.println(args[0]);
        String clazz = args[0];
        int port = Integer.parseInt(args[1]);

        registerMBeansServer();

        registerBenchmarkRemote(clazz, port);
    }

    private static void registerBenchmarkRemote(String clazz, int port) throws Exception {
        Class benchmarkClass = Class.forName(clazz);
        Object benchmarkObject = benchmarkClass.newInstance();
        BenchmarkRemote benchmarkRemote = new BenchmarkRemoteImpl(benchmarkObject);
        BenchmarkRemote stub = (BenchmarkRemote) UnicastRemoteObject.exportObject(benchmarkRemote, 0);
        Registry registry = LocateRegistry.createRegistry(port);
        registry.bind(BENCHMARK_OBJECT_NAME, stub);
    }

    private static void registerMBeansServer() throws Exception {
        MBeanServer server = MBeanServerFactory.createMBeanServer("jmxrmi");

        HashMap<String, String> map = new HashMap<>();
        map.put("java.naming.factory.initial", RegistryContextFactory.class.getName());
        map.put(RMIConnectorServer.JNDI_REBIND_ATTRIBUTE, "true");
        JMXConnectorServer connector = newJMXConnectorServer(new JMXServiceURL(SERVER_URL), map, server);

        register(server, connector, ManagementFactory.getMemoryMXBean(), ManagementFactory.MEMORY_MXBEAN_NAME);

        for(MemoryPoolMXBean b : ManagementFactory.getMemoryPoolMXBeans()) {
            register(server, connector, b, ManagementFactory.MEMORY_POOL_MXBEAN_DOMAIN_TYPE + b.getName());
        }

        for(GarbageCollectorMXBean gc : ManagementFactory.getGarbageCollectorMXBeans()) {
            register(server, connector, gc, ManagementFactory.GARBAGE_COLLECTOR_MXBEAN_DOMAIN_TYPE + gc.getName());
        }
    }

    private static void register(MBeanServer server, JMXConnectorServer connector, PlatformManagedObject mBean, String name) throws Exception {
        ObjectName mBeanName = new ObjectName(name);
        if(!server.isRegistered(mBeanName)) {
            server.registerMBean(mBean, mBeanName);
        }
    }
}
