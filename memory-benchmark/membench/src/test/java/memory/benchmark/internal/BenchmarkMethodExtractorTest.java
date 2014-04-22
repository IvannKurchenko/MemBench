package memory.benchmark.internal;

import memory.benchmark.api.annotations.After;
import memory.benchmark.api.annotations.Benchmark;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Optional;

import static java.util.Arrays.asList;
import static junit.framework.Assert.assertEquals;

public class BenchmarkMethodExtractorTest {

    private Class<TestClass> testClass;
    private BenchmarkMethodExtractor benchmarkMethodExtractor;

    @Before
    public void setUp(){
        testClass = TestClass.class;
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
        List<Method> expectedTestMethods = asList(  testClass.getMethod("firstBenchmark"),
                                                    testClass.getMethod("secondBenchmark"));
        List<Method> actualTestMethods = benchmarkMethodExtractor.getTestMethods(testClass);
        assertEquals(expectedTestMethods, actualTestMethods);
    }

    private static class TestClass {

        @memory.benchmark.api.annotations.Before
        public void setUp() {
        }

        @Benchmark
        public void firstBenchmark(){
        }

        @Benchmark
        public void secondBenchmark(){
        }

        @memory.benchmark.api.annotations.After
        public void tearDown() {
        }
    }
}
