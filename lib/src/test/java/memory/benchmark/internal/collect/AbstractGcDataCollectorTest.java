package memory.benchmark.internal.collect;

import memory.benchmark.api.result.GcUsage;
import memory.benchmark.api.result.StatisticView;
import memory.benchmark.internal.ResultBuilder;
import org.junit.Before;
import org.junit.Test;

import static java.util.Arrays.asList;
import static junit.framework.Assert.assertEquals;

public class AbstractGcDataCollectorTest {

    private static final String TEST_GC_NAME = "testGc";

    private AbstractGcDataCollector gcDataCollector;

    @Before
    public void setUp() {
        gcDataCollector = new TestGcDataCollector();
    }

    @Test
    public void testCollectBenchmarkResult() {
        long firstTimeGcCount = 100;
        long firstTimeGcTime = 100;

        gcDataCollector.addBeforeGcUsage(TEST_GC_NAME, firstTimeGcCount, firstTimeGcTime);

        long secondTimeGcCount = 200;
        long secondTimeGcTime = 200;

        gcDataCollector.addAfterGcUsage(TEST_GC_NAME, secondTimeGcCount, secondTimeGcTime);

        ResultBuilder expectedBuilder = new ResultBuilder(null, null, null);
        expectedBuilder.setGcUsages(asList(new StatisticView<>(new GcUsage(TEST_GC_NAME, 100, 100))));

        ResultBuilder actualBuilder = new ResultBuilder(null, null, null);
        gcDataCollector.collectBenchmarkData(actualBuilder);

        assertEquals(expectedBuilder.build(), actualBuilder.build());
    }

    private static class TestGcDataCollector extends AbstractGcDataCollector {

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
