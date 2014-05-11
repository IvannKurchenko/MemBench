package memory.benchmark.internal.runner.remote.collect;

import memory.benchmark.internal.collect.AbstractGcDataCollector;

import javax.management.MBeanServerConnection;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.util.List;

import static java.lang.Integer.parseInt;
import static java.util.stream.Collectors.toList;
import static memory.benchmark.internal.util.ThrowableHandlers.rethrowThrowableFunction;

public class RemoteGcDataCollector extends AbstractGcDataCollector {

    static final String NAME = "Name";
    static final String GC_COUNT = "CollectionCount";
    static final String GC_TIME = "CollectionTime";

    private final MBeanServerConnection remote;
    private final List<ObjectName> gcBeanPoolNames;

    public RemoteGcDataCollector(MBeanServerConnection remote) {
        this.remote = remote;
        this.gcBeanPoolNames = rethrowThrowableFunction(this::queryGcBeanNames);
    }

    private List<ObjectName> queryGcBeanNames() throws MalformedObjectNameException, IOException {
        return remote.queryNames(null, null).
                stream().
                filter(i -> i.getCanonicalName().startsWith(ManagementFactory.GARBAGE_COLLECTOR_MXBEAN_DOMAIN_TYPE)).
                collect(toList());
    }

    @Override
    public void onBeforeTest() {
        gcBeanPoolNames.forEach(g -> addBeforeGcUsage(getName(g), getGcCount(g), getGcTime(g)));
    }

    @Override
    public void onAfterTest() {
        gcBeanPoolNames.forEach(g -> addAfterGcUsage(getName(g), getGcCount(g), getGcTime(g)));
    }

    private String getName(ObjectName name) {
        return getAttribute(name, NAME).toString();
    }

    private int getGcCount(ObjectName name) {
        return parseInt(getAttribute(name, GC_COUNT).toString());
    }

    private int getGcTime(ObjectName name) {
        return parseInt(getAttribute(name, GC_TIME).toString());
    }

    private Object getAttribute(ObjectName name, String attribute) {
        return rethrowThrowableFunction(() -> remote.getAttribute(name, attribute));
    }
}
