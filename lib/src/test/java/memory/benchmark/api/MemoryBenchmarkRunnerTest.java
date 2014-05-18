package memory.benchmark.api;

import memory.benchmark.api.result.BenchmarkResult;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.lang.reflect.Method;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static java.util.Arrays.asList;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

public class MemoryBenchmarkRunnerTest {

    private BenchmarkOptions options;

    @Before
    public void setUp(){
        options = new BenchmarkOptions.Builder().
                gcTimeUnit(TimeUnit.NANOSECONDS, -1).
                allowedInternalLogging(false).
                reportFormat(BenchmarkOptions.ReportFormat.CONSOLE).
                runMode(BenchmarkOptions.RunMode.SAME_PROCESS).
                build();
    }

    @Test
    public void testRunOneClass() throws NoSuchMethodException {
        List<BenchmarkResult> results = MemoryBenchmarkRunner.run(options, TestClass1.class);

        assertEquals(results.size(), 1);
        assertResultsContainsClasses(results, TestClass1.class);
        assertResultsContainsMethods(results, TestClass1.class.getMethod("benchmark"));

        verifyMockedTestClassInteraction(TestClass1.mockedTestClass);
    }

    @Test
    public void testRunTwoClasses() throws NoSuchMethodException {
        List<BenchmarkResult> results = MemoryBenchmarkRunner.run(options, TestClass1.class, TestClass2.class);

        assertEquals(results.size(), 2);
        assertResultsContainsClasses(results,   TestClass1.class, TestClass2.class);
        assertResultsContainsMethods(results,   TestClass1.class.getMethod("benchmark"),
                                                TestClass2.class.getMethod("benchmark"));

        verifyMockedTestClassInteraction(TestClass1.mockedTestClass, TestClass2.mockedTestClass);
    }

    @Ignore
    @Test
    public void testRunInPackage() throws NoSuchMethodException {
        String testPackage = this.getClass().getCanonicalName().replace("." + this.getClass().getSimpleName(), "");
        List<BenchmarkResult> results = MemoryBenchmarkRunner.run(options, testPackage);

        assertEquals(results.size(), 3);
        assertResultsContainsClasses(results,   TestClass1.class, TestClass2.class, TestClass3.class);
        assertResultsContainsMethods(results,   TestClass1.class.getMethod("benchmark"),
                                                TestClass2.class.getMethod("benchmark"),
                                                TestClass3.class.getMethod("benchmark"));

        verifyMockedTestClassInteraction(TestClass1.mockedTestClass, TestClass2.mockedTestClass, TestClass3.mockedTestClass);
    }

    private static void assertResultsContainsClasses(List<BenchmarkResult> results, Class... classes) {
        List<Class> classList = asList(classes);
        assertTrue(results.stream().filter(r -> classList.contains(r.getBenchmarkClass())).count() > 0);
    }

    private static void assertResultsContainsMethods(List<BenchmarkResult> results, Method... methods) {
        List<Method> methodList = asList(methods);
        assertTrue(results.stream().filter(r -> methodList.contains(r.getBenchmarkMethod())).count() > 0);
    }

    private static void verifyMockedTestClassInteraction(TestClass... mockedTestClasses){
        asList(mockedTestClasses).stream().forEach(m -> {
            verify(m).setUp();
            verify(m).benchmark();
            verify(m).tearDown();
            verifyNoMoreInteractions(m);
            reset(m);
        });
    }
}
