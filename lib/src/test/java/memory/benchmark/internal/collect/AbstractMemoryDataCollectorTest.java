package memory.benchmark.internal.collect;

import memory.benchmark.api.result.MemoryFootprint;
import memory.benchmark.api.result.StatisticView;
import memory.benchmark.internal.runner.ResultBuilder;
import org.junit.Before;
import org.junit.Test;

import java.lang.management.MemoryUsage;

import static junit.framework.Assert.assertEquals;

public class AbstractMemoryDataCollectorTest {

    private AbstractMemoryDataCollector memoryDataCollector;

    @Before
    public void setUp() {
        memoryDataCollector = new TestMemoryDataCollector();
    }

    @Test
    public void testCollectBenchmarkResult() {
        MemoryUsage beforeHeapMemoryUsage = new MemoryUsage(100, 100, 100, 100);
        MemoryUsage beforeNonHeapMemoryUsage = new MemoryUsage(100, 100, 100, 100);
        memoryDataCollector.addBeforeMemoryUsage(beforeHeapMemoryUsage, beforeNonHeapMemoryUsage);

        MemoryUsage afterHeapMemoryUsage = new MemoryUsage(200, 200, 200, 200);
        MemoryUsage afterNonHeapMemoryUsage = new MemoryUsage(200, 200, 200, 200);
        memoryDataCollector.addAfterMemoryUsage(afterHeapMemoryUsage, afterNonHeapMemoryUsage);

        ResultBuilder expectedResult = new ResultBuilder(null, null, null);
        MemoryFootprint heapMemoryFootprint = new MemoryFootprint(beforeHeapMemoryUsage, afterHeapMemoryUsage);
        MemoryFootprint nonHeapMemoryFootprint = new MemoryFootprint(beforeNonHeapMemoryUsage, afterNonHeapMemoryUsage);
        expectedResult.setHeapMemoryDifference(new StatisticView<>(heapMemoryFootprint), new StatisticView<>(nonHeapMemoryFootprint));

        ResultBuilder actualResult = new ResultBuilder(null, null, null);
        memoryDataCollector.collectBenchmarkData(actualResult);

        assertEquals(expectedResult.build(), actualResult.build());
    }

    private static class TestMemoryDataCollector extends AbstractMemoryDataCollector {

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
