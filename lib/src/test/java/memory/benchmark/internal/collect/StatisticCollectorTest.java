package memory.benchmark.internal.collect;

import org.junit.Test;

import java.util.List;

import static com.google.common.primitives.Longs.asList;
import static junit.framework.Assert.assertEquals;
import static memory.benchmark.internal.collect.Statistic.from;

public class StatisticCollectorTest {

    @Test
    public void getStatisticTest() {
        List<Long> beforeValueItems = asList(100, 200, 300, 400);
        List<Long> afterValueItems = asList(200, 300, 400, 500);
        Statistic.ValueExtractor<Long> extractor = Long::longValue;
        Statistic expected = new Statistic(100, 100, 100);
        Statistic actual = from(afterValueItems, beforeValueItems, extractor);
        assertEquals(expected, actual);
    }
}
