package memory.benchmark.api.exception;

import java.lang.reflect.Method;

/**
 * This exception signals about incorrect incorrect benchmark class or method.
 * For example : benchmark class have not empty or public constructor.
 *
 * @see memory.benchmark.api.annotations.After
 * @see memory.benchmark.api.annotations.Before
 * @see memory.benchmark.api.annotations.Benchmark
 */
public class InvalidBenchmarkException extends RuntimeException {

    public InvalidBenchmarkException(Class clazz, String message) {
        super(clazz.getName() + " : " + message);
    }

    public InvalidBenchmarkException(Method method, String message) {
        super(method.getDeclaringClass().getName() + " : " + method.getName() + " : " + message);
    }
}
