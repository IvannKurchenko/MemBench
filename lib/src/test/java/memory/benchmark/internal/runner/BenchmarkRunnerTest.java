package memory.benchmark.internal.runner;

import memory.benchmark.internal.collect.BenchmarkDataCollector;
import memory.benchmark.internal.util.Factory;
import memory.benchmark.internal.util.Log;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Method;
import java.util.Collection;

import static java.util.Arrays.asList;
import static org.mockito.Mockito.*;

public class BenchmarkRunnerTest {

    private Method testMethod;
    private Factory<BenchmarkDataCollector, ?> collectorFactory;
    private Factory<BenchmarkMethodInvoker, Class> methodInvokerFactory;
    private BenchmarkDataCollector benchmarkDataCollector;
    private BenchmarkMethodInvoker benchmarkMethodInvoker;
    private BenchmarkRunner benchmarkRunner;

    @Before
    public void setUp() throws NoSuchMethodException {
        testMethod = TestClass.class.getMethod("testMethod");
        Collection<Class<?>> benchmarkClasses = asList(TestClass.class);
        collectorFactory = mock(Factory.class);
        methodInvokerFactory = mock(Factory.class);

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

        verify(collectorFactory).create();
        verify(methodInvokerFactory).create(TestClass.class);

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

    @Test
    public void testBeforeMethodThrowsException() {
        doThrow(new RuntimeException()).when(benchmarkMethodInvoker).invokeBefore();

        benchmarkRunner.run();

        verify(collectorFactory).create();
        verify(methodInvokerFactory).create(TestClass.class);

        verify(benchmarkMethodInvoker, atLeastOnce()).getBenchmarkMethods();
        verify(benchmarkMethodInvoker).invokeBefore();
        verify(benchmarkMethodInvoker).invokeAfter();
        verify(benchmarkMethodInvoker).close();

        verify(benchmarkDataCollector).clear();
        verify(benchmarkDataCollector).close();

        verifyNoMoreInteractions(benchmarkMethodInvoker, benchmarkDataCollector);
    }

    @Test
    public void testBenchmarkMethodThrowsException() {
        doThrow(new RuntimeException()).when(benchmarkMethodInvoker).invokeBenchmark(testMethod);

        benchmarkRunner.run();

        verify(collectorFactory).create();
        verify(methodInvokerFactory).create(TestClass.class);

        verify(benchmarkMethodInvoker, atLeastOnce()).getBenchmarkMethods();
        verify(benchmarkMethodInvoker).invokeBefore();
        verify(benchmarkMethodInvoker).invokeBenchmark(testMethod);
        verify(benchmarkMethodInvoker).invokeAfter();
        verify(benchmarkMethodInvoker).close();

        verify(benchmarkDataCollector).onBeforeTest();
        verify(benchmarkDataCollector).clear();
        verify(benchmarkDataCollector).close();

        verifyNoMoreInteractions(benchmarkMethodInvoker, benchmarkDataCollector);
    }

    @Test
    public void testAfterMethodThrowsException() {
        doThrow(new RuntimeException()).when(benchmarkMethodInvoker).invokeAfter();

        benchmarkRunner.run();

        verify(collectorFactory).create();
        verify(methodInvokerFactory).create(TestClass.class);

        verify(benchmarkMethodInvoker, atLeastOnce()).getBenchmarkMethods();
        verify(benchmarkMethodInvoker).invokeBefore();
        verify(benchmarkMethodInvoker).invokeAfter();
        verify(benchmarkMethodInvoker).invokeBenchmark(testMethod);
        verify(benchmarkMethodInvoker).close();

        verify(benchmarkDataCollector).onBeforeTest();
        verify(benchmarkDataCollector).onAfterTest();
        verify(benchmarkDataCollector).clear();
        verify(benchmarkDataCollector).close();

        verifyNoMoreInteractions(benchmarkMethodInvoker, benchmarkDataCollector);
    }

    private static class TestClass {

        @memory.benchmark.api.annotations.Before
        public void setUp(){}

        @memory.benchmark.api.annotations.Benchmark
        public void testMethod() {}

        @memory.benchmark.api.annotations.After
        public void tearDown(){}
    }
}
