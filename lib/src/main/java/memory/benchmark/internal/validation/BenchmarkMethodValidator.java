package memory.benchmark.internal.validation;

import memory.benchmark.api.exception.InvalidBenchmarkException;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class BenchmarkMethodValidator implements BenchmarkValidator<Method> {

    @Override
    public Method validate(Method method) throws InvalidBenchmarkException {
        validateMethodNoParams(method);
        validateMethodPublic(method);
        return method;
    }

    private void validateMethodNoParams(Method method) {
        if (method.getParameterCount() > 0) {
            throw new InvalidBenchmarkException(method, "Method should have no parameters!");
        }
    }

    private void validateMethodPublic(Method method) {
        if (!Modifier.isPublic(method.getModifiers())) {
            throw new InvalidBenchmarkException(method, "Method should have be public!");
        }
    }
}
