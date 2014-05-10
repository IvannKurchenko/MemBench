package memory.benchmark.internal.runner;

import memory.benchmark.api.annotations.Benchmark;
import memory.benchmark.internal.ResultBuilder;
import memory.benchmark.internal.collect.BenchmarkDataCollector;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Method;

import static java.util.Arrays.asList;
import static org.mockito.Mockito.*;

public class BenchmarkClassRunnerTest {

    private Method testMethod;
    private BenchmarkMethodInvoker benchmarkMethodInvoker;
    private BenchmarkDataCollector benchmarkDataCollector;
    //private BenchmarkClassRunner benchmarkClassRunner;

    @Before
    public void setUp() throws NoSuchMethodException {
        testMethod = TestClass.class.getMethod("testMethod");
        benchmarkMethodInvoker = mock(BenchmarkMethodInvoker.class);
        benchmarkDataCollector = mock(BenchmarkDataCollector.class);
        //benchmarkClassRunner = new BenchmarkClassRunner(benchmarkMethodInvoker, benchmarkDataCollector);

        when(benchmarkMethodInvoker.getBenchmarkMethods()).thenReturn(asList(testMethod));
    }

    @Test
    public void testRunTests() {
        //benchmarkClassRunner.runTests();

        verify(benchmarkMethodInvoker).getBenchmarkMethods();
        verify(benchmarkMethodInvoker).invokeBefore();
        verify(benchmarkMethodInvoker).invokeAfter();
        verify(benchmarkMethodInvoker).invokeBenchmark(testMethod);

        verify(benchmarkDataCollector).onBeforeTest();
        verify(benchmarkDataCollector).collectBenchmarkData(any(ResultBuilder.class));
        verify(benchmarkDataCollector).onAfterTest();
        verify(benchmarkDataCollector).clear();
        verify(benchmarkDataCollector).close();

        verifyNoMoreInteractions(benchmarkMethodInvoker, benchmarkDataCollector);
    }

    private static class TestClass {
        @Benchmark
        public void testMethod() {
        }
    }
}
