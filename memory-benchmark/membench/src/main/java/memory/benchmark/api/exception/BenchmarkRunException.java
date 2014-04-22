package memory.benchmark.api.exception;

/**
 *
 */
public class BenchmarkRunException extends RuntimeException {

    public BenchmarkRunException() {
        super();
    }

    public BenchmarkRunException(Throwable cause) {
        super(cause);
    }

    public BenchmarkRunException(String message) {
        super(message);
    }

    public BenchmarkRunException(String message, Throwable cause) {
        super(message, cause);
    }
}
