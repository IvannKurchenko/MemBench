package memory.benchmark.internal.runner.remote.collect;

import org.junit.Before;
import org.junit.Test;

import javax.management.*;
import javax.management.openmbean.CompositeData;

import static org.mockito.Mockito.*;

public class RemoteMemoryDataCollectorTest {

    private MBeanServerConnection remote;
    private RemoteMemoryDataCollector memoryDataCollector;

    @Before
    public void setUp() throws Exception {
        remote = mock(MBeanServerConnection.class);
        memoryDataCollector = new RemoteMemoryDataCollector(remote);
        CompositeData data = mock(CompositeData.class);
        when(data.get(anyString())).thenReturn(0L);
        when(remote.getAttribute(eq(RemoteMemoryDataCollector.OBJECT_NAME), any())).thenReturn(data);
    }

    @Test
    public void onBeforeTest() throws Exception {
        memoryDataCollector.onBeforeTest();
        verifyRemoteInteraction();
    }

    @Test
    public void onAfterTest() throws Exception {
        memoryDataCollector.onAfterTest();
        verifyRemoteInteraction();
    }

    private void verifyRemoteInteraction() throws Exception {
        verify(remote).getAttribute(RemoteMemoryDataCollector.OBJECT_NAME, RemoteMemoryDataCollector.HEAP_MEMORY_USAGE);
        verify(remote).getAttribute(RemoteMemoryDataCollector.OBJECT_NAME, RemoteMemoryDataCollector.NON_HEAP_MEMORY_USAGE);
        verifyNoMoreInteractions(remote);
    }
}
