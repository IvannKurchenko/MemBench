package memory.benchmark.internal.util;

@FunctionalInterface
public interface Log {

    public static final Log SYS_OUT = System.out::println;

    public static final Log SYS_ERR = System.err::println;

    void log(Object message);
}
