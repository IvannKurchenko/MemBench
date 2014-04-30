package memory.benchmark.internal.runner.remote;

import memory.benchmark.internal.runner.BenchmarkMethodInvoker;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Optional;

import static memory.benchmark.internal.util.ThrowableActionHandler.wrapToBenchmarkRunException;

public class RemoteBenchmarkMethodInvoker implements BenchmarkMethodInvoker {

    private final BenchmarkRemote benchmarkRemote;
    private final Optional<Method> beforeMethod;
    private final Optional<Method> afterMethod;
    private final List<Method> benchmarkMethods;
    private final Process process;

    public RemoteBenchmarkMethodInvoker(Process process,
                                        BenchmarkRemote benchmarkRemote,
                                        Optional<Method> beforeMethod,
                                        Optional<Method> afterMethod,
                                        List<Method> benchmarkMethods) {

        this.process = process;
        this.benchmarkRemote = benchmarkRemote;
        this.beforeMethod = beforeMethod;
        this.afterMethod = afterMethod;
        this.benchmarkMethods = benchmarkMethods;
    }

    @Override
    public void invokeBefore() {
        invokeOptionalMethod(beforeMethod);
    }

    @Override
    public void invokeBenchmark(Method benchmarkMethod) {
        wrapToBenchmarkRunException(() -> benchmarkRemote.invoke(benchmarkMethod.getName()));
    }

    @Override
    public void invokeAfter() {
        invokeOptionalMethod(afterMethod);
    }

    @Override
    public List<Method> getBenchmarkMethods() {
        return benchmarkMethods;
    }

    private void invokeOptionalMethod(Optional<Method> optional) {
        optional.ifPresent(m -> wrapToBenchmarkRunException(() -> benchmarkRemote.invoke(m.getName())));
    }
}
