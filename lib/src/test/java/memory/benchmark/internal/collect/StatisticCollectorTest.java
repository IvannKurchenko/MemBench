package memory.benchmark.internal.collect;

import org.junit.Test;

import java.util.List;

import static com.google.common.primitives.Longs.asList;
import static junit.framework.Assert.assertEquals;

public class StatisticCollectorTest {

    @Test
    public void getStatisticTest() {
        List<Long> beforeValueItems = asList(100, 200, 300, 400);
        List<Long> afterValueItems = asList(200, 300, 400, 500);
        StatisticCollector.ValueExtractor<Long> extractor = Long::longValue;
        StatisticCollector.Statistic expected = new StatisticCollector.Statistic(100, 100, 100);
        StatisticCollector.Statistic actual = StatisticCollector.getStatistic(afterValueItems, beforeValueItems, extractor);
        assertEquals(expected, actual);
    }
}
