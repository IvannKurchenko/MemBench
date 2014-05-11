package memory.benchmark.internal.util;

import memory.benchmark.api.exception.BenchmarkRunException;

public class ThrowableHandlers {

    public static ThrowableHandler PRINT_HANDLER = Throwable::printStackTrace;
    public static ThrowableHandler RETHROW_HANDLER = t -> {throw new BenchmarkRunException(t);} ;

    public static void rethrowThrowableAction(ThrowableAction action) {
        handleThrowableAction(action, RETHROW_HANDLER);
    }

    public static <T> T rethrowThrowableFunction(ThrowableFunction<T> function) {
        return rethrowThrowableFunction(function, RETHROW_HANDLER);
    }

    public static void printThrowableAction(ThrowableAction action) {
        handleThrowableAction(action, PRINT_HANDLER);
    }

    public static <T> T printThrowableFunction(ThrowableFunction<T> function) {
        return rethrowThrowableFunction(function, PRINT_HANDLER);
    }

    public static void handleThrowableAction(ThrowableAction action, ThrowableHandler throwableHandler) {
        try {
            action.execute();
        } catch (Throwable throwable) {
            throwableHandler.handle(throwable);
        }
    }

    public static <T> T rethrowThrowableFunction(ThrowableFunction<T> function, ThrowableHandler throwableHandler) {
        try {
            return function.apply();
        } catch (Throwable throwable) {
            throwableHandler.handle(throwable);
            return null;
        }
    }
}
