package memory.benchmark.internal.collect;

import memory.benchmark.api.result.MemoryFootprint;
import memory.benchmark.api.result.MemoryPoolStatisticView;
import memory.benchmark.internal.ResultBuilder;
import org.junit.Before;
import org.junit.Test;

import java.lang.management.MemoryType;
import java.lang.management.MemoryUsage;

import static java.util.Arrays.asList;
import static junit.framework.Assert.assertEquals;

public class AbstractMemoryPoolDataCollectorTest {

    private static final String TEST_POOL_BEAN_NAME = "testPool";
    private static final MemoryType TEST_MEMORY_TYPE = MemoryType.HEAP;
    private static final AbstractMemoryPoolDataCollector.Pool TEST_POOL = new AbstractMemoryPoolDataCollector.Pool(TEST_POOL_BEAN_NAME, TEST_MEMORY_TYPE);

    private AbstractMemoryPoolDataCollector memoryPoolDataCollector;

    @Before
    public void setUp() throws Exception {
        memoryPoolDataCollector = new TestMemoryPoolDataCollector();
    }

    @Test
    public void testCollectBenchmarkResult() {
        MemoryUsage beforeMemoryUsage = new MemoryUsage(100, 100, 100, 100);
        memoryPoolDataCollector.putBeforeMemoryUsage(TEST_POOL, beforeMemoryUsage);

        MemoryUsage afterMemoryUsage = new MemoryUsage(100, 100, 100, 100);
        memoryPoolDataCollector.putAfterMemoryUsage(TEST_POOL, afterMemoryUsage);

        ResultBuilder expectedResult = new ResultBuilder(null, null, null);
        MemoryFootprint memoryPoolFootprint = new MemoryFootprint(beforeMemoryUsage, afterMemoryUsage);
        expectedResult.setMemoryPoolFootprints(asList(new MemoryPoolStatisticView(memoryPoolFootprint, TEST_MEMORY_TYPE, TEST_POOL_BEAN_NAME)));

        ResultBuilder actualResult = new ResultBuilder(null, null, null);
        memoryPoolDataCollector.collectBenchmarkData(actualResult);

        assertEquals(expectedResult.build(), actualResult.build());
    }
    public static class TestMemoryPoolDataCollector extends AbstractMemoryPoolDataCollector {

        @Override
        public void onBeforeTest() {
            throw new UnsupportedOperationException();
        }

        @Override
        public void onAfterTest() {
            throw new UnsupportedOperationException();
        }
    }
}
