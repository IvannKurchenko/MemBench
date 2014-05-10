package memory.benchmark.internal.validation;

import memory.benchmark.api.exception.InvalidBenchmarkException;
import org.junit.Before;
import org.junit.Test;

public class BenchmarkMethodValidatorTest {

    private BenchmarkMethodValidator benchmarkMethodValidator;

    @Before
    public void setUp() {
        benchmarkMethodValidator = new BenchmarkMethodValidator();
    }

    @Test(expected = InvalidBenchmarkException.class)
    public void testValidate_shouldThrowInvalidBenchmarkException_forMethodWithParams() throws NoSuchMethodException {
        benchmarkMethodValidator.validate(TestClass.class.getDeclaredMethod("methodWithParameters", Object.class));
    }

    @Test(expected = InvalidBenchmarkException.class)
    public void testValidate_shouldThrowInvalidBenchmarkException_forPrivateMethod() throws NoSuchMethodException {
        benchmarkMethodValidator.validate(TestClass.class.getDeclaredMethod("protectedMethod"));
    }

    @Test
    public void testValidate_shouldPassValidation() throws NoSuchMethodException {
        benchmarkMethodValidator.validate(TestClass.class.getDeclaredMethod("validBenchmarkMethod"));
    }

    private static class TestClass {

        public void methodWithParameters(Object param) {
        }

        protected void protectedMethod() {
        }

        public void validBenchmarkMethod() {
        }
    }
}
