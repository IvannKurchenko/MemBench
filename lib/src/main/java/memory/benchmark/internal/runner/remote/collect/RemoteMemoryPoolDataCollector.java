package memory.benchmark.internal.runner.remote.collect;

import memory.benchmark.internal.collect.AbstractMemoryPoolDataCollector;

import javax.management.MBeanServerConnection;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.openmbean.CompositeData;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryType;
import java.lang.management.MemoryUsage;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static memory.benchmark.internal.runner.remote.collect.CompositeDataConverter.toMemoryUsage;
import static memory.benchmark.internal.util.ThrowableHandlers.rethrowThrowableFunction;

public class RemoteMemoryPoolDataCollector extends AbstractMemoryPoolDataCollector {

    static final String MEMORY_USAGE = "Usage";
    static final String NAME = "Name";
    static final String TYPE = "Type";

    private final MBeanServerConnection remote;
    private final List<ObjectName> memoryBeanPoolNames;

    public RemoteMemoryPoolDataCollector(MBeanServerConnection remote) {
        this.remote = remote;
        this.memoryBeanPoolNames = rethrowThrowableFunction(this::queryMemoryPoolBeanNames);
    }

    private List<ObjectName> queryMemoryPoolBeanNames() throws MalformedObjectNameException, IOException {
        return remote.queryNames(null, null).
                stream().
                filter(i -> i.getCanonicalName().startsWith(ManagementFactory.MEMORY_POOL_MXBEAN_DOMAIN_TYPE)).
                collect(toList());
    }

    @Override
    public void onBeforeTest() {
        memoryBeanPoolNames.forEach(b -> putBeforeMemoryUsage(getMemoryPool(b), getMemoryUsage(b)));
    }

    @Override
    public void onAfterTest() {
        memoryBeanPoolNames.forEach(b -> putAfterMemoryUsage(getMemoryPool(b), getMemoryUsage(b)));
    }

    private Pool getMemoryPool(ObjectName name) {
        return new Pool(getAttribute(name, NAME).toString(), MemoryType.valueOf(getAttribute(name, TYPE).toString()));
    }

    private MemoryUsage getMemoryUsage(ObjectName name) {
        return toMemoryUsage((CompositeData) getAttribute(name, MEMORY_USAGE));
    }

    private Object getAttribute(ObjectName name, String attribute) {
        return rethrowThrowableFunction(() -> remote.getAttribute(name, attribute));
    }
}
