package memory.benchmark.api.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Method of benchmark annotated with
 * {@link memory.benchmark.api.annotations.After} will be called after each finish of
 * {@link memory.benchmark.api.annotations.Benchmark} annotated method.
 * Usually should be used to close resources or other clean operation after benchmark test.
 *
 * Should be just one method annotated with {@link memory.benchmark.api.annotations.After} per each
 * benchmark class. In case if benchmark class contains more than method with
 * {@link memory.benchmark.api.annotations.After} annotation
 * {@link memory.benchmark.api.exception.InvalidBenchmarkException} will be thrown.
 *
 * Any thrown exceptions in this method will be ignored and tests will continued.
 *
 * @see memory.benchmark.api.annotations.Before
 * @see memory.benchmark.api.annotations.Benchmark
 * @see memory.benchmark.api.exception.InvalidBenchmarkException
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface After {
}
