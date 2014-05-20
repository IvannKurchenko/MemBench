package memory.benchmark.internal.runner.local.collect;

import memory.benchmark.api.result.MemoryFootprint;
import memory.benchmark.api.result.MemoryPoolStatisticView;
import memory.benchmark.internal.runner.ResultBuilder;
import org.junit.Before;
import org.junit.Test;

import java.lang.management.MemoryPoolMXBean;
import java.lang.management.MemoryType;
import java.lang.management.MemoryUsage;

import static java.util.Arrays.asList;
import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class LocalMemoryPoolDataCollectorTest {

    private static final String TEST_POOL_BEAN_NAME = "testPool";
    private static final MemoryType TEST_MEMORY_TYPE = MemoryType.HEAP;

    private MemoryPoolMXBean mockedMemoryPoolMXBean;
    private LocalMemoryPoolDataCollector memoryPoolResultCollector;

    @Before
    public void setUp() {
        mockedMemoryPoolMXBean = mock(MemoryPoolMXBean.class);
        memoryPoolResultCollector = new LocalMemoryPoolDataCollector(asList(mockedMemoryPoolMXBean));

        when(mockedMemoryPoolMXBean.getName()).thenReturn(TEST_POOL_BEAN_NAME);
        when(mockedMemoryPoolMXBean.getType()).thenReturn(TEST_MEMORY_TYPE);
    }

    @Test
    public void testCollectBenchmarkResult() {
        MemoryUsage beforeMemoryUsage = new MemoryUsage(100, 100, 100, 100);
        when(mockedMemoryPoolMXBean.getUsage()).thenReturn(beforeMemoryUsage);

        memoryPoolResultCollector.onBeforeTest();

        MemoryUsage afterMemoryUsage = new MemoryUsage(100, 100, 100, 100);
        when(mockedMemoryPoolMXBean.getUsage()).thenReturn(afterMemoryUsage);

        memoryPoolResultCollector.onAfterTest();

        ResultBuilder expectedResult = new ResultBuilder(null, null, null);
        MemoryFootprint memoryPoolFootprint = new MemoryFootprint(beforeMemoryUsage, afterMemoryUsage);
        expectedResult.setMemoryPoolFootprints(asList(new MemoryPoolStatisticView(memoryPoolFootprint, TEST_MEMORY_TYPE, TEST_POOL_BEAN_NAME)));

        ResultBuilder actualResult = new ResultBuilder(null, null, null);
        memoryPoolResultCollector.collectBenchmarkData(actualResult);

        assertEquals(expectedResult.build(), actualResult.build());
    }
}
