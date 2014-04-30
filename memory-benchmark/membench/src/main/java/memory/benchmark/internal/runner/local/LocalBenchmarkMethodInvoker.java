package memory.benchmark.internal.runner.local;

import memory.benchmark.api.Options;
import memory.benchmark.internal.runner.BenchmarkMethodInvoker;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Optional;

import static memory.benchmark.internal.util.ThrowableActionHandler.wrapToBenchmarkRunException;

public class LocalBenchmarkMethodInvoker implements BenchmarkMethodInvoker {

    private final Options options;
    private final Object benchmarkObject;
    private final Optional<Method> beforeMethod;
    private final List<Method> testMethods;
    private final Optional<Method> afterMethod;

    public LocalBenchmarkMethodInvoker(Options options,
                                       Object benchmarkObject,
                                       Optional<Method> beforeMethod,
                                       List<Method> testMethods,
                                       Optional<Method> afterMethod) {
        this.options = options;
        this.benchmarkObject = benchmarkObject;
        this.beforeMethod = beforeMethod;
        this.testMethods = testMethods;
        this.afterMethod = afterMethod;
    }

    @Override
    public void invokeBefore() {
        wrapToBenchmarkRunException(() -> invokeOptionalMethod(beforeMethod));
    }

    @Override
    public void invokeBenchmark(Method benchmarkMethod) {
        wrapToBenchmarkRunException(() -> benchmarkMethod.invoke(benchmarkObject));
    }

    @Override
    public void invokeAfter() {
        wrapToBenchmarkRunException(() -> invokeOptionalMethod(afterMethod));
    }

    @Override
    public List<Method> getBenchmarkMethods() {
        return testMethods;
    }

    private void invokeOptionalMethod(Optional<Method> optional) throws InvocationTargetException, IllegalAccessException {
        optional.ifPresent(m -> {wrapToBenchmarkRunException(() -> m.invoke(benchmarkObject));});
        tryGc();
    }

    private void tryGc() {
        try {

            System.gc();
            options.getGcTimeUnit().sleep(options.getGcTime());

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
