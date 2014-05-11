package memory.benchmark.internal.runner.remote;

import java.lang.reflect.Method;
import java.rmi.RemoteException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toMap;
import static memory.benchmark.internal.util.ThrowableHandler.handleThrowableFunction;

public class BenchmarkRemoteImpl implements BenchmarkRemote {

    private final Object benchmarkObject;
    private final long gcTime;
    private final TimeUnit gcTimeUnit;
    private final Map<String, Method> methodNameMap;

    public BenchmarkRemoteImpl(Object benchmarkObject, long gcTime, TimeUnit gcTimeUnit) {
        this.benchmarkObject = benchmarkObject;
        this.gcTime = gcTime;
        this.gcTimeUnit = gcTimeUnit;
        this.methodNameMap = getMethodNameMap();
    }

    private Map<String, Method> getMethodNameMap() {
        return asList(benchmarkObject.getClass().getDeclaredMethods()).stream().collect(toMap(Method::getName, m -> m));
    }

    @Override
    public void invoke(String benchmarkMethod) {
        handleThrowableFunction(() -> methodNameMap.get(benchmarkMethod).invoke(benchmarkObject));
    }

    @Override
    public void gc() throws RemoteException {
        try {

            System.gc();
            gcTimeUnit.sleep(gcTime);

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
