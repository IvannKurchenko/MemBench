package memory.benchmark.internal;

import memory.benchmark.api.annotations.After;
import memory.benchmark.api.annotations.Before;
import memory.benchmark.api.annotations.Benchmark;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.Arrays.asList;
import static memory.benchmark.internal.ArgumentChecker.checkListSize;

public class BenchmarkMethodExtractor {

    public Optional<Method> getBeforeMethod(Class testClass) {
        return extractAnnotatedMethod(testClass, Before.class);
    }

    public Optional<Method> getAfterMethod(Class testClass) {
        return extractAnnotatedMethod(testClass, After.class);
    }

    public List<Method> getTestMethods(Class testClass) {
        return extractAnnotatedMethods(testClass, Benchmark.class);
    }

    private Optional<Method> extractAnnotatedMethod(Class testClass, Class<? extends Annotation> annotation) {
        List<Method> beforeMethods = checkListSize(extractAnnotatedMethods(testClass, annotation), 1);
        return beforeMethods.size() > 0 ? Optional.of(beforeMethods.get(0)) : Optional.empty();
    }

    private List<Method> extractAnnotatedMethods(Class testClass, Class<? extends Annotation> annotation) {
        Method[] methods = testClass.getMethods();
        return asList(methods).stream().filter(m -> containsAnnotation(m, annotation)).collect(Collectors.toList());
    }

    private boolean containsAnnotation(Method method, Class<? extends Annotation> annotation) {
        Annotation[] annotations = method.getDeclaredAnnotations();
        return asList(annotations).stream().filter(a -> annotation == a.annotationType()).count() > 0;
    }
}
