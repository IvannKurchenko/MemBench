package memory.benchmark.api.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Method of benchmark annotated with
 * {@link memory.benchmark.api.annotations.Before} will be called before each
 * {@link memory.benchmark.api.annotations.Benchmark} annotated method.
 * Usually should be used for some initialization operations.
 *
 * Should be just one method annotated with {@link memory.benchmark.api.annotations.Before} per each
 * benchmark class. In case if benchmark class contains more than method with
 * {@link memory.benchmark.api.annotations.Before} annotation
 * {@link memory.benchmark.api.exception.InvalidBenchmarkException} will be thrown.
 *
 * In case if some exception will be thrown during {@link memory.benchmark.api.annotations.Before}
 * method execution,  benchmark test method will be ignored and
 * {@link memory.benchmark.api.annotations.After} will be called to clean initialized resources.
 *
 * @see memory.benchmark.api.annotations.After
 * @see memory.benchmark.api.annotations.Benchmark
 * @see memory.benchmark.api.exception.InvalidBenchmarkException
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Before {
}
