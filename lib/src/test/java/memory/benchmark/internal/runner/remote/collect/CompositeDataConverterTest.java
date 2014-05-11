package memory.benchmark.internal.runner.remote.collect;

import org.junit.Test;

import javax.management.openmbean.CompositeData;
import java.lang.management.MemoryUsage;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CompositeDataConverterTest {

    @Test
    public void testToMemoryUsage() throws Exception {
        long init = 100;
        long used = 200;
        long committed = 300;
        long max = 400;

        CompositeData compositeData = mock(CompositeData.class);
        when(compositeData.get("init")).thenReturn(init);
        when(compositeData.get("used")).thenReturn(used);
        when(compositeData.get("committed")).thenReturn(committed);
        when(compositeData.get("max")).thenReturn(max);

        MemoryUsage actualUsage = CompositeDataConverter.toMemoryUsage(compositeData);

        assertEquals(init, actualUsage.getInit());
        assertEquals(used, actualUsage.getUsed());
        assertEquals(committed, actualUsage.getCommitted());
        assertEquals(max, actualUsage.getMax());
    }
}
