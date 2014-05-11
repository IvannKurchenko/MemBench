package memory.benchmark.internal.runner.local;

import memory.benchmark.api.Options;
import memory.benchmark.api.exception.BenchmarkRunException;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static java.util.Arrays.asList;
import static java.util.Optional.empty;
import static org.mockito.Mockito.*;

public class LocalBenchmarkMethodInvokerTest {

    private Options options;
    private TestClass benchmarkObject;
    private Optional<Method> beforeMethod;
    private List<Method> testMethods;
    private Optional<Method> afterMethod;

    @Before
    public void setUp() throws Exception {
        options = new Options.Builder().gcTimeUnit(TimeUnit.NANOSECONDS, 1).build();
        benchmarkObject = mock(TestClass.class);
        beforeMethod = Optional.of(TestClass.class.getMethod("setUp"));
        testMethods = asList(TestClass.class.getMethod("benchmarkMethod"));
        afterMethod = Optional.of(TestClass.class.getMethod("tearDown"));
    }

    @Test
    public void invokeBeforeMethodPresent() {
        LocalBenchmarkMethodInvoker benchmarkMethodInvoker = new LocalBenchmarkMethodInvoker(options, benchmarkObject, beforeMethod, null, null);
        benchmarkMethodInvoker.invokeBefore();
        verify(benchmarkObject).setUp();
        verifyNoMoreInteractions(benchmarkObject);
    }

    @Test
    public void invokeBeforeMethodNotPresent() {
        LocalBenchmarkMethodInvoker benchmarkMethodInvoker = new LocalBenchmarkMethodInvoker(options, benchmarkObject, empty(), null, null);
        benchmarkMethodInvoker.invokeBefore();
        verifyNoMoreInteractions(benchmarkObject);
    }

    @Test(expected = BenchmarkRunException.class)
    public void invokeBeforeMethodThrowsException() {
        doThrow(new RuntimeException()).when(benchmarkObject).setUp();
        LocalBenchmarkMethodInvoker benchmarkMethodInvoker = new LocalBenchmarkMethodInvoker(options, benchmarkObject, beforeMethod, null, null);
        benchmarkMethodInvoker.invokeBefore();
    }

    @Test
    public void invokeAfterMethodPresent() {
        LocalBenchmarkMethodInvoker benchmarkMethodInvoker = new LocalBenchmarkMethodInvoker(options, benchmarkObject, null, null, afterMethod);
        benchmarkMethodInvoker.invokeAfter();
        verify(benchmarkObject).tearDown();
        verifyNoMoreInteractions(benchmarkObject);
    }

    @Test
    public void invokeAfterMethodNotPresent() {
        LocalBenchmarkMethodInvoker benchmarkMethodInvoker = new LocalBenchmarkMethodInvoker(options, benchmarkObject, null, null, empty());
        benchmarkMethodInvoker.invokeAfter();
        verifyNoMoreInteractions(benchmarkObject);
    }

    @Test(expected = BenchmarkRunException.class)
    public void invokeAfterMethodThrowsException() {
        doThrow(new RuntimeException()).when(benchmarkObject).tearDown();
        LocalBenchmarkMethodInvoker benchmarkMethodInvoker = new LocalBenchmarkMethodInvoker(options, benchmarkObject, null, null, afterMethod);
        benchmarkMethodInvoker.invokeAfter();
    }

    @Test
    public void invokeBenchmark() {
        LocalBenchmarkMethodInvoker benchmarkMethodInvoker = new LocalBenchmarkMethodInvoker(options, benchmarkObject, null, testMethods, null);
        benchmarkMethodInvoker.invokeBenchmark(testMethods.get(0));
        verify(benchmarkObject).benchmarkMethod();
        verifyNoMoreInteractions(benchmarkObject);
    }

    @Test(expected = BenchmarkRunException.class)
    public void invokeBenchmarkThrowsException() {
        doThrow(new RuntimeException()).when(benchmarkObject).benchmarkMethod();
        LocalBenchmarkMethodInvoker benchmarkMethodInvoker = new LocalBenchmarkMethodInvoker(options, benchmarkObject, null, testMethods, null);
        benchmarkMethodInvoker.invokeBenchmark(testMethods.get(0));
    }

    private static class TestClass {
        @memory.benchmark.api.annotations.Before
        public void setUp(){}

        @memory.benchmark.api.annotations.Benchmark
        public void benchmarkMethod(){}

        @memory.benchmark.api.annotations.After
        public void tearDown(){}
    }
}
