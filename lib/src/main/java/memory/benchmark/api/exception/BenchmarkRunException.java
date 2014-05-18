package memory.benchmark.api.exception;

/**
 * Exception that signals about some problems during benchmark executions.
 * Usually wraps other exceptions that was thrown from benchmark class.
 */
public class BenchmarkRunException extends RuntimeException {

    public BenchmarkRunException(Throwable cause) {
        super(cause);
    }
}
