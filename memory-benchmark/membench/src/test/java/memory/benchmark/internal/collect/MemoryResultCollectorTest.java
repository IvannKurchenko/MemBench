package memory.benchmark.internal.collect;

import memory.benchmark.api.result.MemoryFootprint;
import memory.benchmark.api.result.StatisticView;
import memory.benchmark.internal.ResultBuilder;
import org.junit.Before;
import org.junit.Test;

import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MemoryResultCollectorTest {

    private MemoryDataCollector memoryResultCollector;
    private MemoryMXBean mockedMemoryMXBean;

    @Before
    public void setUp(){
        mockedMemoryMXBean = mock(MemoryMXBean.class);
        memoryResultCollector = new MemoryDataCollector(mockedMemoryMXBean);
    }

    @Test
    public void testCollectBenchmarkResult(){
        MemoryUsage beforeHeapMemoryUsage = new MemoryUsage(100, 100, 100, 100);
        MemoryUsage beforeNonHeapMemoryUsage = new MemoryUsage(100, 100, 100, 100);
        mockMemoryMXBeanBehaviour(beforeHeapMemoryUsage, beforeNonHeapMemoryUsage);

        memoryResultCollector.onBeforeTest();

        MemoryUsage afterHeapMemoryUsage = new MemoryUsage(200, 200, 200, 200);
        MemoryUsage afterNonHeapMemoryUsage = new MemoryUsage(200, 200, 200, 200);
        mockMemoryMXBeanBehaviour(afterHeapMemoryUsage, afterNonHeapMemoryUsage);

        memoryResultCollector.onAfterTest();

        ResultBuilder expectedResult = new ResultBuilder(null, null);
        MemoryFootprint heapMemoryFootprint = new MemoryFootprint(beforeHeapMemoryUsage, afterHeapMemoryUsage);
        MemoryFootprint nonHeapMemoryFootprint = new MemoryFootprint(beforeNonHeapMemoryUsage, afterNonHeapMemoryUsage);
        expectedResult.setHeapMemoryDifference(new StatisticView<>(heapMemoryFootprint), new StatisticView<>(nonHeapMemoryFootprint));

        ResultBuilder actualResult = new ResultBuilder(null, null);
        memoryResultCollector.collectBenchmarkData(actualResult);

        assertEquals(expectedResult.build(), actualResult.build());
    }

    private void mockMemoryMXBeanBehaviour(MemoryUsage heapMemoryUsage, MemoryUsage nonHeapMemoryUsage) {
        when(mockedMemoryMXBean.getHeapMemoryUsage()).thenReturn(heapMemoryUsage);
        when(mockedMemoryMXBean.getNonHeapMemoryUsage()).thenReturn(nonHeapMemoryUsage);
    }
}
