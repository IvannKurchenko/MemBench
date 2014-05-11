package memory.benchmark.internal.runner.local.collect;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class LocalBenchmarkDataCollectorFactoryTest {

    private LocalBenchmarkDataCollectorFactory collectorFactory;

    @Before
    public void setUp(){
        collectorFactory = new LocalBenchmarkDataCollectorFactory();
    }

    @Test
    public void testCreate() throws Exception {
        assertNotNull(collectorFactory.create());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCreateWithArguments(){
        collectorFactory.create(null);
    }
}
