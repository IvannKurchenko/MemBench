package memory.benchmark.internal.runner.local;

import memory.benchmark.api.exception.BenchmarkRunException;
import memory.benchmark.internal.util.Factory;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class LocalBenchmarkObjectFactoryTest {

    private Factory<Object, Class> objectClassFactory;

    @Before
    public void setUp() throws Exception {
        objectClassFactory = new LocalBenchmarkObjectFactory();
    }

    @Test
    public void testCreate() throws Exception {
        assertNotNull(objectClassFactory.create(Object.class));
    }

    @Test(expected = BenchmarkRunException.class)
    public void testCreateThrowsException() throws Exception {
        assertNotNull(objectClassFactory.create(TestClass.class));
    }


    @Test(expected = UnsupportedOperationException.class)
    public void testCreateWithoutArgsUnsupported() throws Exception {
        assertNotNull(objectClassFactory.create());
    }

    private static class TestClass {
        private TestClass() { throw new RuntimeException(); }
    }
}
