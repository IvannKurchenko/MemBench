package memory.benchmark.api.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Main annotation for benchmark tests.
 * Indicates benchmark method of class.
 *
 *
 * @see memory.benchmark.api.annotations.Before
 * @see memory.benchmark.api.annotations.After
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Benchmark {

    /**
     * Optionally specifies count of benchmark tests for this method.
     * Cannot be lower than 1. In other case {@link memory.benchmark.api.exception.InvalidBenchmarkException}
     * will be thrown.
     */
    int testTimes() default 1;
}

