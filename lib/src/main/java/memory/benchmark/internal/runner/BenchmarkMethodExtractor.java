package memory.benchmark.internal.runner;

import memory.benchmark.api.annotations.After;
import memory.benchmark.api.annotations.Before;
import memory.benchmark.api.annotations.Benchmark;
import memory.benchmark.api.exception.InvalidBenchmarkException;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Optional;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;

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
        List<Method> beforeMethods = checkAnnotatedMethodsSize(testClass, extractAnnotatedMethods(testClass, annotation), annotation);
        return beforeMethods.size() > 0 ? Optional.of(beforeMethods.get(0)) : Optional.empty();
    }

    private List<Method> extractAnnotatedMethods(Class testClass, Class<? extends Annotation> annotation) {
        Method[] methods = testClass.getMethods();
        return asList(methods).stream().filter(m -> m.isAnnotationPresent(annotation)).collect(toList());
    }

    private List<Method> checkAnnotatedMethodsSize(Class clazz, List<Method> list, Class<? extends Annotation> annotation) {
        if (list.size() > 1) {
            throw new InvalidBenchmarkException(clazz, "should have one method annotated with : " + annotation.getSimpleName());
        }
        return list;
    }
}
