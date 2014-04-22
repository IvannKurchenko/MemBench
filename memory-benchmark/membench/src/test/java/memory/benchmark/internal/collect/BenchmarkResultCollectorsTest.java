package memory.benchmark.internal.collect;

import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

public class BenchmarkResultCollectorsTest {

    private BenchmarkResultCollector mockedCollector;
    private BenchmarkResultCollectors benchmarkResultCollectors;

    @Before
    public void setUp(){
        mockedCollector = mock(BenchmarkResultCollector.class);
        benchmarkResultCollectors = new BenchmarkResultCollectors(mockedCollector);
    }

    @Test
    public void testOnBefore() {
        benchmarkResultCollectors.onBeforeTest();
        verify(mockedCollector).onBeforeTest();
        verifyNoMoreInteractions(mockedCollector);
    }

    @Test
    public void testOnAfter(){
        benchmarkResultCollectors.onAfterTest();
        verify(mockedCollector).onAfterTest();
        verifyNoMoreInteractions(mockedCollector);
    }

    @Test
    public void testCollectBenchmarkResult(){
        benchmarkResultCollectors.collectBenchmarkResult(null);
        verify(mockedCollector).collectBenchmarkResult(null);
        verifyNoMoreInteractions(mockedCollector);
    }
}
