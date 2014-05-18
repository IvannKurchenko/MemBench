package memory.benchmark.internal.runner;

import memory.benchmark.api.annotations.Benchmark;
import memory.benchmark.internal.ResultBuilder;
import memory.benchmark.internal.collect.BenchmarkDataCollector;
import memory.benchmark.internal.util.Factory;
import memory.benchmark.internal.util.Log;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Method;
import java.util.Collection;

import static java.util.Arrays.asList;
import static org.mockito.Mockito.*;

public class BenchmarkMemoryBenchmarkRunnerTest {

    private Method testMethod;
    private BenchmarkDataCollector benchmarkDataCollector;
    private BenchmarkMethodInvoker benchmarkMethodInvoker;
    private BenchmarkRunner benchmarkRunner;

    @Before
    public void setUp() throws NoSuchMethodException {
        testMethod = TestClass.class.getMethod("testMethod");
        Collection<Class<?>> benchmarkClasses = asList(TestClass.class);
        Factory<BenchmarkDataCollector, ?> collectorFactory = mock(Factory.class);
        Factory<BenchmarkMethodInvoker, Class> methodInvokerFactory = mock(Factory.class);

        benchmarkMethodInvoker = mock(BenchmarkMethodInvoker.class);
        benchmarkDataCollector = mock(BenchmarkDataCollector.class);

        benchmarkRunner = new BenchmarkRunner(benchmarkClasses, collectorFactory, methodInvokerFactory, Log.EMPTY_LOG);

        when(methodInvokerFactory.create(TestClass.class)).thenReturn(benchmarkMethodInvoker);
        when(collectorFactory.create()).thenReturn(benchmarkDataCollector);

        when(benchmarkMethodInvoker.getBenchmarkMethods()).thenReturn(asList(testMethod));
    }

    @Test
    public void testRunTests() {
        benchmarkRunner.run();

        verify(benchmarkMethodInvoker, atLeastOnce()).getBenchmarkMethods();
        verify(benchmarkMethodInvoker).invokeBefore();
        verify(benchmarkMethodInvoker).invokeAfter();
        verify(benchmarkMethodInvoker).invokeBenchmark(testMethod);
        verify(benchmarkMethodInvoker).close();

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
