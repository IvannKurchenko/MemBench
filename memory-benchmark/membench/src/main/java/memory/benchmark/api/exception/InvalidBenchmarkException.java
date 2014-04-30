package memory.benchmark.api.exception;

import java.lang.reflect.Method;

/**
 * This exception signals about incorrect incorrect benchmark class or method.
 */
public class InvalidBenchmarkException extends RuntimeException {

    /**
     *
     * @param clazz
     * @param message
     */
    public InvalidBenchmarkException(Class clazz, String message) {
        super(clazz.getName() + " : " + message);
    }

    /**
     *
     * @param method
     * @param message
     */
    public InvalidBenchmarkException(Method method, String message) {
        super(method.getDeclaringClass().getName() + " : " + method.getName() + " : " + message);
    }
}
