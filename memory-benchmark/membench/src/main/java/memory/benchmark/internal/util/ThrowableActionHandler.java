package memory.benchmark.internal.util;

import memory.benchmark.api.exception.BenchmarkRunException;

public class ThrowableActionHandler {

    public static void wrapToBenchmarkRunException(ThrowableAction action) {
        try {
            action.execute();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            throw new BenchmarkRunException(throwable);
        }
    }

    public static <T> T wrapToBenchmarkRunException(ThrowableFunction<T> function) {
        try {
            return function.apply();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            throw new BenchmarkRunException(throwable);
        }
    }
}
