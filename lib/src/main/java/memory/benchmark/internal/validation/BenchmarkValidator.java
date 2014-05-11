package memory.benchmark.internal.validation;

import memory.benchmark.api.exception.InvalidBenchmarkException;

public interface BenchmarkValidator<T> {

    T validate(T t) throws InvalidBenchmarkException;
}
