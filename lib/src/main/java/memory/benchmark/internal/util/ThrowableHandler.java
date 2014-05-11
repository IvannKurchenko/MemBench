package memory.benchmark.internal.util;

@FunctionalInterface
public interface ThrowableHandler {

    public void handle(Throwable t);
}
