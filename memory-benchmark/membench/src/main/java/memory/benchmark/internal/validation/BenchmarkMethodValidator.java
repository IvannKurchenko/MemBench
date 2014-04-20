package memory.benchmark.internal.validation;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class BenchmarkMethodValidator implements BenchmarkValidator<Method>{

    @Override
    public Method validate(Method method) throws BenchmarkValidationException {
        validateMethodNoParams(method);
        validateMethodPublic(method);
        validateMethodNotAbstract(method);
        return method;
    }

    private void validateMethodNoParams(Method method) {
        if(method.getParameterCount() > 0) {
            throw new BenchmarkValidationException("Method should have no parameters!");
        }
    }

    private void validateMethodPublic(Method method) {
        if(!Modifier.isPublic(method.getModifiers())) {
            throw new BenchmarkValidationException("Method should have be public!");
        }
    }

    private void validateMethodNotAbstract(Method method) {
        if(Modifier.isAbstract(method.getModifiers())){
            throw new BenchmarkValidationException("Method should have be public!");
        }
    }
}
