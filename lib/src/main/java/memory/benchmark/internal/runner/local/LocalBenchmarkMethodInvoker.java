package memory.benchmark.internal.runner.local;

import memory.benchmark.internal.runner.BenchmarkMethodInvoker;
import memory.benchmark.internal.util.GcHelper;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Optional;

import static memory.benchmark.internal.util.ThrowableHandlers.rethrowThrowableAction;
import static memory.benchmark.internal.util.ThrowableHandlers.rethrowThrowableFunction;

public class LocalBenchmarkMethodInvoker implements BenchmarkMethodInvoker {

    private final GcHelper gcHelper;
    private final Object benchmarkObject;
    private final Optional<Method> beforeMethod;
    private final List<Method> testMethods;
    private final Optional<Method> afterMethod;

    public LocalBenchmarkMethodInvoker(GcHelper gcHelper,
                                       Object benchmarkObject,
                                       Optional<Method> beforeMethod,
                                       List<Method> testMethods,
                                       Optional<Method> afterMethod) {
        this.gcHelper = gcHelper;
        this.benchmarkObject = benchmarkObject;
        this.beforeMethod = beforeMethod;
        this.testMethods = testMethods;
        this.afterMethod = afterMethod;
    }

    @Override
    public void invokeBefore() {
        rethrowThrowableAction(() -> invokeOptionalMethod(beforeMethod));
    }

    @Override
    public void invokeBenchmark(Method benchmarkMethod) {
        rethrowThrowableFunction(() -> benchmarkMethod.invoke(benchmarkObject));
    }

    @Override
    public void invokeAfter() {
        rethrowThrowableAction(() -> invokeOptionalMethod(afterMethod));
    }

    @Override
    public List<Method> getBenchmarkMethods() {
        return testMethods;
    }

    private void invokeOptionalMethod(Optional<Method> optional) throws InvocationTargetException, IllegalAccessException {
        optional.ifPresent(m -> {
            rethrowThrowableFunction(() -> m.invoke(benchmarkObject));
        });
        gcHelper.tryGc();
    }
}
