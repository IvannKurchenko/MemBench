package memory.benchmark.internal;

import memory.benchmark.api.annotations.Benchmark;
import memory.benchmark.api.exception.InvalidBenchmarkException;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toSet;
import static junit.framework.Assert.assertEquals;

public class BenchmarkMethodExtractorTest {

    private Class<TestClass> testClass;
    private Class<InvalidTestClass> invalidTestClass;
    private BenchmarkMethodExtractor benchmarkMethodExtractor;

    @Before
    public void setUp() {
        testClass = TestClass.class;
        invalidTestClass = InvalidTestClass.class;
        benchmarkMethodExtractor = new BenchmarkMethodExtractor();
    }

    @Test
    public void testGetBeforeMethod() throws Exception {
        Method expectedBeforeMethod = testClass.getMethod("setUp");
        Optional<Method> actualBeforeMethod = benchmarkMethodExtractor.getBeforeMethod(testClass);
        assertEquals(expectedBeforeMethod, actualBeforeMethod.get());
    }

    @Test
    public void testGetAfterMethod() throws Exception {
        Method expectedAfterMethod = testClass.getMethod("tearDown");
        Optional<Method> actualAfterMethod = benchmarkMethodExtractor.getAfterMethod(testClass);
        assertEquals(expectedAfterMethod, actualAfterMethod.get());
    }

    @Test
    public void testGetTestMethods() throws Exception {
        Set<Method> expectedTestMethods = asList(testClass.getMethod("firstBenchmark"),
                testClass.getMethod("secondBenchmark")).stream().collect(toSet());
        Set<Method> actualTestMethods = benchmarkMethodExtractor.getTestMethods(testClass).
                stream().collect(toSet());
        assertEquals(expectedTestMethods, actualTestMethods);
    }

    @Test(expected = InvalidBenchmarkException.class)
    public void testGetBeforeMethod_shouldThrowException() {
        benchmarkMethodExtractor.getBeforeMethod(invalidTestClass);
    }

    @Test(expected = InvalidBenchmarkException.class)
    public void testGetAfterMethod_shouldThrowException() {
        benchmarkMethodExtractor.getAfterMethod(invalidTestClass);
    }

    private static class TestClass {

        @memory.benchmark.api.annotations.Before
        public void setUp() {
        }

        @Benchmark
        public void firstBenchmark() {
        }

        @Benchmark
        public void secondBenchmark() {
        }

        @memory.benchmark.api.annotations.After
        public void tearDown() {
        }
    }

    private static class InvalidTestClass {

        @memory.benchmark.api.annotations.Before
        public void setUpFirst() {
        }

        @memory.benchmark.api.annotations.Before
        public void setUpSecond() {
        }

        @memory.benchmark.api.annotations.After
        public void tearDownFirst() {
        }

        @memory.benchmark.api.annotations.After
        public void tearDownSecond() {
        }
    }
}
