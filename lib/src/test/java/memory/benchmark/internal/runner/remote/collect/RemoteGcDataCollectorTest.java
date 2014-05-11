package memory.benchmark.internal.runner.remote.collect;

import memory.benchmark.api.exception.BenchmarkRunException;
import org.junit.Before;
import org.junit.Test;

import javax.management.*;
import java.lang.management.ManagementFactory;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toSet;
import static org.mockito.Mockito.*;

public class RemoteGcDataCollectorTest {

    private MBeanServerConnection remote;
    private ObjectName gcObjectName;
    private RemoteGcDataCollector remoteGcDataCollector;

    @Before
    public void setUp() throws Exception {
        remote = mock(MBeanServerConnection.class);
        gcObjectName = new ObjectName(ManagementFactory.GARBAGE_COLLECTOR_MXBEAN_DOMAIN_TYPE);

        when(remote.queryNames(null, null)).thenReturn(asList(gcObjectName).stream().collect(toSet()));
        when(remote.getAttribute(gcObjectName, RemoteGcDataCollector.NAME)).thenReturn("testGcName");
        when(remote.getAttribute(gcObjectName, RemoteGcDataCollector.GC_TIME)).thenReturn(100);
        when(remote.getAttribute(gcObjectName, RemoteGcDataCollector.GC_COUNT)).thenReturn(100);

        remoteGcDataCollector = new RemoteGcDataCollector(remote);
    }

    @Test
    public void onBeforeTest() throws Exception {
        remoteGcDataCollector.onBeforeTest();
        verifyRemoteInteraction();
    }

    @Test(expected = BenchmarkRunException.class)
    public void onBeforeTestThrowsException() throws Exception {
        when(remote.getAttribute(any(), any())).thenThrow(new RuntimeException());
        remoteGcDataCollector.onBeforeTest();
    }

    @Test
    public void onAfterTest() throws Exception {
        remoteGcDataCollector.onAfterTest();
        verifyRemoteInteraction();
    }

    @Test(expected = BenchmarkRunException.class)
    public void onAfterTestThrowsException() throws Exception {
        when(remote.getAttribute(any(), any())).thenThrow(new RuntimeException());
        remoteGcDataCollector.onAfterTest();
    }

    private void verifyRemoteInteraction() throws Exception {
        verify(remote).getAttribute(gcObjectName, RemoteGcDataCollector.NAME);
        verify(remote).getAttribute(gcObjectName, RemoteGcDataCollector.GC_TIME);
        verify(remote).getAttribute(gcObjectName, RemoteGcDataCollector.GC_COUNT);
        verify(remote).queryNames(any(), any());
        verifyNoMoreInteractions(remote);
    }

}
