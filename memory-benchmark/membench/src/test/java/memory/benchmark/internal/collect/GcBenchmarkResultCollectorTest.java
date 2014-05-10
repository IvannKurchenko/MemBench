package memory.benchmark.internal.collect;

import memory.benchmark.api.result.GcUsage;
import memory.benchmark.api.result.StatisticView;
import memory.benchmark.internal.ResultBuilder;
import memory.benchmark.internal.runner.local.collect.LocalGcDataCollector;
import org.junit.Before;
import org.junit.Test;

import java.lang.management.GarbageCollectorMXBean;

import static java.util.Arrays.asList;
import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class GcBenchmarkResultCollectorTest {

    private static final String TEST_GC_NAME = "testGc";

    private GarbageCollectorMXBean mockedGarbageCollectorMXBean;
    private LocalGcDataCollector gcResultCollector;

    @Before
    public void setUp() {
        mockedGarbageCollectorMXBean = mock(GarbageCollectorMXBean.class);
        when(mockedGarbageCollectorMXBean.getName()).thenReturn(TEST_GC_NAME);
        gcResultCollector = new LocalGcDataCollector(asList(mockedGarbageCollectorMXBean));
    }

    @Test
    public void testCollectBenchmarkResult() {
        long firstTimeGcCount = 100;
        long firstTimeGcTime = 100;
        mockGarbageCollectorMXBeanBehaviour(firstTimeGcCount, firstTimeGcTime);

        gcResultCollector.onBeforeTest();

        long secondTimeGcCount = 200;
        long secondTimeGcTime = 200;
        mockGarbageCollectorMXBeanBehaviour(secondTimeGcCount, secondTimeGcTime);

        gcResultCollector.onAfterTest();

        ResultBuilder expectedBuilder = new ResultBuilder(null, null, null);
        expectedBuilder.setGcUsages(asList(new StatisticView<GcUsage>(new GcUsage(TEST_GC_NAME, 100, 100))));

        ResultBuilder actualBuilder = new ResultBuilder(null, null, null);
        gcResultCollector.collectBenchmarkData(actualBuilder);

        assertEquals(expectedBuilder.build(), actualBuilder.build());
    }

    private void mockGarbageCollectorMXBeanBehaviour(long gcCount, long gcTime) {
        when(mockedGarbageCollectorMXBean.getCollectionCount()).thenReturn(gcCount);
        when(mockedGarbageCollectorMXBean.getCollectionTime()).thenReturn(gcTime);
    }
}
