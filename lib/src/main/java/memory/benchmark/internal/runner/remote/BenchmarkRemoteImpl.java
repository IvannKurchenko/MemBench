package memory.benchmark.internal.runner.remote;

import memory.benchmark.internal.util.GcHelper;

import java.lang.reflect.Method;
import java.rmi.RemoteException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toMap;
import static memory.benchmark.internal.util.ThrowableHandlers.rethrowThrowableFunction;

public class BenchmarkRemoteImpl implements BenchmarkRemote {

    private final Object benchmarkObject;
    private final GcHelper gcHelper;
    private final Map<String, Method> methodNameMap;

    public BenchmarkRemoteImpl(Object benchmarkObject, GcHelper gcHelper) {
        this.benchmarkObject = benchmarkObject;
        this.gcHelper = gcHelper;
        this.methodNameMap = getMethodNameMap();
    }

    private Map<String, Method> getMethodNameMap() {
        return asList(benchmarkObject.getClass().getDeclaredMethods()).stream().collect(toMap(Method::getName, m -> m));
    }

    @Override
    public void invoke(String benchmarkMethod) {
        rethrowThrowableFunction(() -> methodNameMap.get(benchmarkMethod).invoke(benchmarkObject));
    }

    @Override
    public void gc() throws RemoteException {
        gcHelper.tryGc();
    }
}
