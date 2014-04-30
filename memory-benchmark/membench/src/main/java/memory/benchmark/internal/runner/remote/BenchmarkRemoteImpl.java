package memory.benchmark.internal.runner.remote;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toMap;
import static java.util.stream.Collectors.toSet;
import static memory.benchmark.internal.util.ThrowableActionHandler.wrapToBenchmarkRunException;

public class BenchmarkRemoteImpl implements BenchmarkRemote {

    //private static final Set<String> OBJECT_METHODS = asList(Object.class.getDeclaredMethods()).stream().map(Method::getName).collect(toSet());

    private final Object benchmarkObject;
    private final Map<String, Method> methodNameMap;

    public BenchmarkRemoteImpl(Object benchmarkObject) {
            this.benchmarkObject = benchmarkObject;
            this.methodNameMap = asList(benchmarkObject.getClass().getDeclaredMethods()).
                                stream().
                                //filter(m -> !OBJECT_METHODS.contains(m.getName())).
                                collect(toMap(Method::getName, m -> m));
    }

    @Override
    public void invoke(String benchmarkMethod) {
        wrapToBenchmarkRunException(() -> methodNameMap.get(benchmarkMethod).invoke(benchmarkObject));
    }
}
