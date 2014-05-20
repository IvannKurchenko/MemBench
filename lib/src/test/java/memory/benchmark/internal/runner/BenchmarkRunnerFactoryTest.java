package memory.benchmark.internal.runner;

import memory.benchmark.api.BenchmarkOptions;
import memory.benchmark.internal.util.Log;
import org.junit.Test;

import java.util.Collection;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertNotNull;

public class BenchmarkRunnerFactoryTest {

    @Test
    public void testCreateBenchmarkRunner() throws Exception {
        BenchmarkOptions options = new BenchmarkOptions.Builder().build();
        Collection<Class<?>> benchmarkClasses = asList(TestClass.class);
        assertNotNull(BenchmarkRunnerFactory.createBenchmarkRunner(benchmarkClasses, options, Log.EMPTY_LOG));
    }

    private static class TestClass{}
}
