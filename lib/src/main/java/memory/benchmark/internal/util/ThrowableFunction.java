package memory.benchmark.internal.util;

@FunctionalInterface
public interface ThrowableFunction<T> {

    T apply() throws Throwable;
}
