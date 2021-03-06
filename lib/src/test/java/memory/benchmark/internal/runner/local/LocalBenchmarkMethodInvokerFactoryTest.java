package memory.benchmark.internal.runner.local;

import memory.benchmark.api.BenchmarkOptions;
import memory.benchmark.internal.runner.BenchmarkMethodExtractor;
import memory.benchmark.internal.runner.BenchmarkMethodInvoker;
import memory.benchmark.internal.util.Factory;
import memory.benchmark.internal.util.Log;
import memory.benchmark.internal.validation.BenchmarkValidator;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Method;

import static java.util.Collections.emptyList;
import static java.util.Optional.empty;
import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class LocalBenchmarkMethodInvokerFactoryTest {

    private BenchmarkOptions options;
    private BenchmarkValidator<Method> methodBenchmarkValidator;
    private BenchmarkValidator<Class> classBenchmarkValidator;
    private BenchmarkMethodExtractor methodExtractor;
    private Factory<Object, Class> benchmarkObjectClassFactory;
    private LocalBenchmarkMethodInvokerFactory localBenchmarkMethodInvokerFactory;

    @Before
    public void setUp() {
        options = new BenchmarkOptions.Builder().build();
        methodBenchmarkValidator = mock(BenchmarkValidator.class);
        classBenchmarkValidator = mock(BenchmarkValidator.class);
        methodExtractor = mock(BenchmarkMethodExtractor.class);
        benchmarkObjectClassFactory = mock(Factory.class);
        localBenchmarkMethodInvokerFactory = new LocalBenchmarkMethodInvokerFactory(options,
                                                                                    Log.EMPTY_LOG,
                                                                                    methodBenchmarkValidator,
                                                                                    classBenchmarkValidator,
                                                                                    methodExtractor,
                                                                                    benchmarkObjectClassFactory);

        when(methodExtractor.getTestMethods(TestClass.class)).thenReturn(emptyList());
        when(methodExtractor.getBeforeMethod(TestClass.class)).thenReturn(empty());
        when(methodExtractor.getAfterMethod(TestClass.class)).thenReturn(empty());
        when(benchmarkObjectClassFactory.create(TestClass.class)).thenReturn(new TestClass());
    }

    @Test
    public void create() {
        BenchmarkMethodInvoker invoker = localBenchmarkMethodInvokerFactory.create(TestClass.class);

        assertEquals(invoker.getClass(), LocalBenchmarkMethodInvoker.class);

        verify(methodExtractor).getTestMethods(TestClass.class);
        verify(methodExtractor).getBeforeMethod(TestClass.class);
        verify(methodExtractor).getAfterMethod(TestClass.class);
        verify(benchmarkObjectClassFactory).create(TestClass.class);
        verify(classBenchmarkValidator).validate(TestClass.class);

        verifyNoMoreInteractions(methodBenchmarkValidator, methodBenchmarkValidator, classBenchmarkValidator, benchmarkObjectClassFactory);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void createWithoutArgs() {
        localBenchmarkMethodInvokerFactory.create();
    }

    private static class TestClass{}
}
