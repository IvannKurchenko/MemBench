package memory.benchmark.internal;

import memory.benchmark.api.annotations.Benchmark;
import memory.benchmark.internal.collect.BenchmarkResultCollector;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Optional;

import static java.util.Arrays.asList;
import static java.util.Optional.of;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

public class BenchmarkClassRunnerTest {

    private TestClass testObject;
    private Optional<Method> beforeMethod;
    private Optional<Method> afterMethod;
    private List<Method> testMethods;
    private BenchmarkResultCollector benchmarkResultCollector;
    private BenchmarkClassRunner benchmarkClassRunner;

    @Before
    public void setUp() throws NoSuchMethodException {
        testObject = mock(TestClass.class);
        beforeMethod = of(TestClass.class.getMethod("setUp"));
        afterMethod = of(TestClass.class.getMethod("tearDown"));
        testMethods = asList(TestClass.class.getMethod("benchmarkTest"));
        benchmarkResultCollector = mock(BenchmarkResultCollector.class);
        benchmarkClassRunner = new BenchmarkClassRunner(testObject, beforeMethod, afterMethod, testMethods, benchmarkResultCollector);
    }

    @Test
    public void testRunTests() {
        benchmarkClassRunner.runTests();
        verify(testObject).setUp();
        verify(testObject).benchmarkTest();
        verify(testObject).tearDown();
        verifyNoMoreInteractions(testObject);
    }

    private static class TestClass {

        @memory.benchmark.api.annotations.Before
        public void setUp() {
        }

        @Benchmark
        public void benchmarkTest() {
        }

        @memory.benchmark.api.annotations.After
        public void tearDown() {
        }
    }
}
