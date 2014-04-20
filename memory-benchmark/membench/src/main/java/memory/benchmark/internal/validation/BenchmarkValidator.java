package memory.benchmark.internal.validation;


public interface BenchmarkValidator<T> {

    T validate(T t) throws BenchmarkValidationException;
}
