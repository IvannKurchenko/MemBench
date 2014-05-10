package memory.benchmark.internal.runner.remote;

import memory.benchmark.internal.runner.BenchmarkMethodInvoker;
import memory.benchmark.internal.util.ThrowableHandler;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Optional;

import static memory.benchmark.internal.util.ThrowableHandler.handleThrowableAction;

public class RemoteBenchmarkMethodInvoker implements BenchmarkMethodInvoker {

    private final BenchmarkProcess benchmarkProcess;
    private final Optional<Method> beforeMethod;
    private final Optional<Method> afterMethod;
    private final List<Method> benchmarkMethods;

    public RemoteBenchmarkMethodInvoker(BenchmarkProcess benchmarkProcess,
                                        Optional<Method> beforeMethod,
                                        Optional<Method> afterMethod,
                                        List<Method> benchmarkMethods) {

        this.benchmarkProcess = benchmarkProcess;
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
        handleThrowableAction(() -> benchmarkProcess.getBenchmarkRemote().invoke(benchmarkMethod.getName()));
    }

    @Override
    public void invokeAfter() {
        invokeOptionalMethod(afterMethod);
    }

    @Override
    public List<Method> getBenchmarkMethods() {
        return benchmarkMethods;
    }

    @Override
    public void close(){
        benchmarkProcess.getProcess().destroy();
    }

    private void invokeOptionalMethod(Optional<Method> optional) {
        optional.ifPresent(m -> handleThrowableAction(() -> benchmarkProcess.getBenchmarkRemote().invoke(m.getName())));
    }
}
