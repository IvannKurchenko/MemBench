package memory.benchmark.internal.util;

import memory.benchmark.api.BenchmarkOptions;

@FunctionalInterface
public interface Log {

    public static final Log SYS_OUT = System.out::println;

    public static final Log SYS_ERR = System.err::println;

    public static final Log EMPTY_LOG = (m) -> {};

    public static Log of(Log log, BenchmarkOptions options) {
        return options.isAllowedInternalLogging() ? log : EMPTY_LOG;
    }

    void log(Object message);
}
