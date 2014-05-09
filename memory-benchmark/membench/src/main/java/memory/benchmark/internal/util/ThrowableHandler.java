package memory.benchmark.internal.util;

import memory.benchmark.api.exception.BenchmarkRunException;

public class ThrowableHandler {

    public static void handleThrowableAction(ThrowableAction action) {
        try {
            action.execute();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            throw new BenchmarkRunException(throwable);
        }
    }

    public static <T> T handleThrowableFunction(ThrowableFunction<T> function) {
        try {
            return function.apply();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            throw new BenchmarkRunException(throwable);
        }
    }
}
