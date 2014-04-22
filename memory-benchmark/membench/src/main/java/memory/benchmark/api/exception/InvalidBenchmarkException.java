package memory.benchmark.api.exception;

/**
 * This exception signals about incorrect incorrect benchmark class or method.
 */
public class InvalidBenchmarkException extends RuntimeException {

    /**
     *
     * @param message
     */
    public InvalidBenchmarkException(String message) {
        super(message);
    }
}
