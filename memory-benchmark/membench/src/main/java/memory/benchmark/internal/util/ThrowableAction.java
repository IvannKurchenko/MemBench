package memory.benchmark.internal.util;

@FunctionalInterface
public interface ThrowableAction {

    public void execute() throws Throwable;
}
