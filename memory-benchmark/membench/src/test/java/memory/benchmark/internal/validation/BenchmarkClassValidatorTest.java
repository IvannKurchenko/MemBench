package memory.benchmark.internal.validation;

import memory.benchmark.api.exception.InvalidBenchmarkException;
import org.junit.Before;
import org.junit.Test;

public class BenchmarkClassValidatorTest {

    private BenchmarkClassValidator benchmarkClassValidator;

    @Before
    public void setUp(){
        benchmarkClassValidator = new BenchmarkClassValidator();
    }

    @Test(expected = InvalidBenchmarkException.class)
    public void testValidateBenchmark_shouldThrowExceptionForAbstractClass() {
        benchmarkClassValidator.validate(AbstractBenchmarkClass.class);
    }

    @Test(expected = InvalidBenchmarkException.class)
    public void testValidateBenchmark_shouldThrowExceptionForNotPublicClass() {
        benchmarkClassValidator.validate(NonPublicClass.class);
    }

    @Test(expected = InvalidBenchmarkException.class)
    public void testValidateBenchmark_shouldThrowExceptionForNotEmptyConstructorClass() {
        benchmarkClassValidator.validate(NotEmptyConstructorClass.class);
    }

    @Test(expected = InvalidBenchmarkException.class)
    public void testValidateBenchmark_shouldThrowExceptionForNotPublicConstructorClass() {
        benchmarkClassValidator.validate(NotPublicConstructorClass.class);
    }

    @Test
    public void testValidateBenchmark_shouldPassValidation() {
        benchmarkClassValidator.validate(ValidClass.class);
    }

    public static abstract class AbstractBenchmarkClass {
    }

    private static class NonPublicClass {
    }

    public static class NotEmptyConstructorClass {
        public NotEmptyConstructorClass(Object param){}
    }

    public static class NotPublicConstructorClass {
        private NotPublicConstructorClass(){}
    }

    public static class ValidClass {
        public ValidClass() {
        }
    }
}
