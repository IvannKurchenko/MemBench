package memory.benchmark.internal.runner;

import java.lang.reflect.Method;
import java.util.List;

public interface BenchmarkMethodInvoker {

    void invokeBefore();

    void invokeBenchmark(Method benchmarkMethod);

    void invokeAfter();

    List<Method> getBenchmarkMethods();

    default void close() {
    }
}
