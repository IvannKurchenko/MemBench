package memory.benchmark.internal.collect;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static memory.benchmark.internal.collect.Statistic.from;

public class StatisticCollectorTest {

    @Test
    public void getStatisticTest() {
        List<Long> beforeValueItems = Arrays.asList(100L, 200L, 300L, 400L);
        List<Long> afterValueItems = Arrays.asList(200L, 300L, 400L, 500L);
        Statistic.ValueExtractor<Long> extractor = Long::longValue;
        Statistic expected = new Statistic(100, 100, 100);
        Statistic actual = from(afterValueItems, beforeValueItems, extractor);
        assertEquals(expected, actual);
    }
}
