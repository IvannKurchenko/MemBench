package memory.benchmark.internal.runner.remote.collect;

import memory.benchmark.internal.collect.AbstractMemoryDataCollector;

import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import javax.management.openmbean.CompositeData;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryUsage;

import static memory.benchmark.internal.runner.remote.collect.CompositeDataConverter.toMemoryUsage;
import static memory.benchmark.internal.util.ThrowableHandler.handleThrowableFunction;

public class RemoteMemoryDataCollector extends AbstractMemoryDataCollector {

    static final ObjectName OBJECT_NAME = handleThrowableFunction(() -> new ObjectName(ManagementFactory.MEMORY_MXBEAN_NAME));
    static final String HEAP_MEMORY_USAGE = "HeapMemoryUsage";
    static final String NON_HEAP_MEMORY_USAGE = "NonHeapMemoryUsage";

    private final MBeanServerConnection remote;

    public RemoteMemoryDataCollector(MBeanServerConnection remote) {
        this.remote = remote;
    }

    @Override
    public void onBeforeTest() {
        addBeforeMemoryUsage(getMemoryUsage(HEAP_MEMORY_USAGE), getMemoryUsage(NON_HEAP_MEMORY_USAGE));
    }

    @Override
    public void onAfterTest() {
        addAfterMemoryUsage(getMemoryUsage(HEAP_MEMORY_USAGE), getMemoryUsage(NON_HEAP_MEMORY_USAGE));
    }

    private MemoryUsage getMemoryUsage(String usageProperty) {
        return toMemoryUsage(handleThrowableFunction(() -> (CompositeData) remote.getAttribute(OBJECT_NAME, usageProperty)));
    }
}
