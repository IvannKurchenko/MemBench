package memory.benchmark.internal.runner.remote.collect;

import org.junit.Before;
import org.junit.Test;

import javax.management.remote.JMXConnector;

import java.io.IOException;

import static org.mockito.Mockito.*;

public class RemoteBenchmarkDataCollectorsTest {

    private JMXConnector connector;
    private RemoteBenchmarkDataCollectors remoteBenchmarkDataCollectors;

    @Before
    public void setUp() throws IOException {
        connector = mock(JMXConnector.class);
        remoteBenchmarkDataCollectors = new RemoteBenchmarkDataCollectors(connector);
        doThrow(new IOException()).when(connector).close();
    }

    @Test
    public void testClose() throws IOException {
        remoteBenchmarkDataCollectors.close();
        verify(connector).close();
        verifyNoMoreInteractions(connector);
    }
}
