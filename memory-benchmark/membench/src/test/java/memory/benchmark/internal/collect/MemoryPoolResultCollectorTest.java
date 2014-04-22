package memory.benchmark.internal.collect;

import memory.benchmark.api.result.MemoryPoolFootprint;
import memory.benchmark.internal.ResultBuilder;
import org.junit.Before;
import org.junit.Test;

import java.lang.management.MemoryPoolMXBean;
import java.lang.management.MemoryType;
import java.lang.management.MemoryUsage;

import static java.util.Arrays.asList;
import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MemoryPoolResultCollectorTest {

    private static final String TEST_POOL_BEAN_NAME = "testPool";
    private static final MemoryType TEST_MEMORY_TYPE = MemoryType.HEAP;

    private MemoryPoolMXBean mockedMemoryPoolMXBean;
    private MemoryPoolResultCollector memoryPoolResultCollector;

    @Before
    public void setUp() {
        mockedMemoryPoolMXBean = mock(MemoryPoolMXBean.class);
        memoryPoolResultCollector = new MemoryPoolResultCollector(asList(mockedMemoryPoolMXBean));

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

        ResultBuilder expectedResult = new ResultBuilder(null, null);
        MemoryPoolFootprint memoryPoolFootprint = new MemoryPoolFootprint(beforeMemoryUsage, afterMemoryUsage, TEST_POOL_BEAN_NAME, TEST_MEMORY_TYPE);
        expectedResult.setMemoryPoolFootprints(asList(memoryPoolFootprint));

        ResultBuilder actualResult = new ResultBuilder(null, null);
        memoryPoolResultCollector.collectBenchmarkResult(actualResult);

        assertEquals(expectedResult.build(), actualResult.build());
    }
}
