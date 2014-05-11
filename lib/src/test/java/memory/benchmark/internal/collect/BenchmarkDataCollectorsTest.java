package memory.benchmark.internal.collect;

import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;

public class BenchmarkDataCollectorsTest {

    private BenchmarkDataCollector mockedCollector;
    private BenchmarkDataCollectors benchmarkResultCollectors;

    @Before
    public void setUp() {
        mockedCollector = mock(BenchmarkDataCollector.class);
        benchmarkResultCollectors = new BenchmarkDataCollectors(mockedCollector);
    }

    @Test
    public void testOnBefore() {
        benchmarkResultCollectors.onBeforeTest();
        verify(mockedCollector).onBeforeTest();
        verifyNoMoreInteractions(mockedCollector);
    }

    @Test
    public void testOnAfter() {
        benchmarkResultCollectors.onAfterTest();
        verify(mockedCollector).onAfterTest();
        verifyNoMoreInteractions(mockedCollector);
    }

    @Test
    public void testCollectBenchmarkResult() {
        benchmarkResultCollectors.collectBenchmarkData(null);
        verify(mockedCollector).collectBenchmarkData(null);
        verifyNoMoreInteractions(mockedCollector);
    }
}
