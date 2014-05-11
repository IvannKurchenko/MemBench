package memory.benchmark.internal.runner.remote.collect;

import memory.benchmark.api.exception.BenchmarkRunException;
import org.junit.Before;
import org.junit.Test;

import javax.management.*;
import javax.management.openmbean.CompositeData;

import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryType;
import java.rmi.RemoteException;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toSet;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

public class RemoteMemoryPoolDataCollectorTest {

    private MBeanServerConnection remote;
    private ObjectName poolObjectName;
    private RemoteMemoryPoolDataCollector memoryPoolDataCollector;

    @Before
    public void setUp() throws Exception {
        remote = mock(MBeanServerConnection.class);
        poolObjectName = new ObjectName(ManagementFactory.MEMORY_POOL_MXBEAN_DOMAIN_TYPE);

        CompositeData memoryUsage = mock(CompositeData.class);
        when(memoryUsage.get(anyString())).thenReturn(0L);

        when(remote.queryNames(null, null)).thenReturn(asList(poolObjectName).stream().collect(toSet()));
        when(remote.getAttribute(poolObjectName, RemoteMemoryPoolDataCollector.NAME)).thenReturn("testGcName");
        when(remote.getAttribute(poolObjectName, RemoteMemoryPoolDataCollector.TYPE)).thenReturn(MemoryType.HEAP.name());
        when(remote.getAttribute(poolObjectName, RemoteMemoryPoolDataCollector.MEMORY_USAGE)).thenReturn(memoryUsage);
        
        memoryPoolDataCollector = new RemoteMemoryPoolDataCollector(remote);
    }

    @Test
    public void onBeforeTest() throws Exception {
        memoryPoolDataCollector.onBeforeTest();
        verifyRemoteInteraction();
    }

    @Test
    public void onAfterTest() throws Exception {
        memoryPoolDataCollector.onAfterTest();
        verifyRemoteInteraction();
    }

    @Test(expected = BenchmarkRunException.class)
    public void onBeforeTestThrowsException() throws Exception {
        when(remote.getAttribute(any(), any())).thenThrow(new RuntimeException());
        memoryPoolDataCollector.onBeforeTest();
    }

    @Test(expected = BenchmarkRunException.class)
    public void onAfterTestThrowsException() throws Exception {
        when(remote.getAttribute(any(), any())).thenThrow(new RuntimeException());
        memoryPoolDataCollector.onAfterTest();
    }

    private void verifyRemoteInteraction() throws Exception {
        verify(remote).queryNames(null, null);
        verify(remote).getAttribute(poolObjectName, RemoteMemoryPoolDataCollector.NAME);
        verify(remote).getAttribute(poolObjectName, RemoteMemoryPoolDataCollector.TYPE);
        verify(remote).getAttribute(poolObjectName, RemoteMemoryPoolDataCollector.MEMORY_USAGE);
        verifyNoMoreInteractions(remote);
    }
}
