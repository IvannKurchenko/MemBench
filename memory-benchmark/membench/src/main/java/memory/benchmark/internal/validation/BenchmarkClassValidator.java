package memory.benchmark.internal.validation;

import java.lang.reflect.Constructor;

import static java.lang.reflect.Modifier.isAbstract;
import static java.lang.reflect.Modifier.isPublic;

public class BenchmarkClassValidator implements BenchmarkValidator<Class> {

    @Override
    public Class validate(Class clazz) throws BenchmarkValidationException {
        validateNotAbstract(clazz);
        validatePublic(clazz);
        validateConstructors(clazz);
        return clazz;
    }

    private void validateNotAbstract(Class clazz) {
        if(isAbstract(clazz.getModifiers())) {
            throw new BenchmarkValidationException(clazz.getName() + " should be not abstract!");
        }
    }

    private void validatePublic(Class clazz) {
        if(!isPublic(clazz.getModifiers())) {
            throw new BenchmarkValidationException(clazz.getName() + " should be public!");
        }
    }

    private void validateConstructors(Class clazz) {
        Constructor<?>[] constructors = clazz.getConstructors();
        if(constructors.length == 1) {
            validatePublicEmptyConstructor(clazz, constructors[0]);
        } else if(constructors.length > 1) {
            throw new BenchmarkValidationException(clazz.getName() + " should have one empty public constructor!");
        }
    }

    private void validatePublicEmptyConstructor(Class clazz, Constructor<?> constructor) {
        if(!isPublic(constructor.getModifiers()) || constructor.getParameterCount() > 0) {
            throw new BenchmarkValidationException(clazz.getName() + " should have one empty public constructor!");
        }
    }
}
