package memory.benchmark.internal.runner.local;

import memory.benchmark.api.Options;
import memory.benchmark.internal.BenchmarkMethodExtractor;
import memory.benchmark.internal.runner.BenchmarkMethodInvoker;
import memory.benchmark.internal.util.Factory;
import memory.benchmark.internal.util.GcHelper;
import memory.benchmark.internal.util.Log;
import memory.benchmark.internal.validation.BenchmarkValidator;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Optional;

public class LocalBenchmarkMethodInvokerFactory implements Factory<BenchmarkMethodInvoker, Class> {

    private final Options options;
    private final Log log;
    private final BenchmarkValidator<Method> methodBenchmarkValidator;
    private final BenchmarkValidator<Class> classBenchmarkValidator;
    private final BenchmarkMethodExtractor methodExtractor;
    private final Factory<Object, Class> benchmarkObjectClassFactory;

    public LocalBenchmarkMethodInvokerFactory(Options options,

                                              Log log, BenchmarkValidator<Method> methodBenchmarkValidator,
                                              BenchmarkValidator<Class> classBenchmarkValidator,
                                              BenchmarkMethodExtractor methodExtractor,
                                              Factory<Object, Class> benchmarkObjectClassFactory) {
        this.options = options;
        this.log = log;
        this.methodBenchmarkValidator = methodBenchmarkValidator;
        this.classBenchmarkValidator = classBenchmarkValidator;
        this.methodExtractor = methodExtractor;
        this.benchmarkObjectClassFactory = benchmarkObjectClassFactory;
    }

    @Override
    public BenchmarkMethodInvoker create(Class benchmarkClass) {
        List<Method> benchmarkMethods = methodExtractor.getTestMethods(benchmarkClass);
        Optional<Method> beforeMethod = methodExtractor.getBeforeMethod(benchmarkClass);
        Optional<Method> afterMethod = methodExtractor.getAfterMethod(benchmarkClass);
        Object benchmarkObject = benchmarkObjectClassFactory.create(benchmarkClass);
        GcHelper gcHelper = new GcHelper(options, log);
        validate(benchmarkClass, beforeMethod, afterMethod, benchmarkMethods);
        return new LocalBenchmarkMethodInvoker(gcHelper, benchmarkObject, beforeMethod, benchmarkMethods, afterMethod);
    }

    private void validate(Class benchmarkClass, Optional<Method> beforeMethod, Optional<Method> afterMethod, List<Method> benchmarkMethods) {
        classBenchmarkValidator.validate(benchmarkClass);
        beforeMethod.ifPresent(methodBenchmarkValidator::validate);
        afterMethod.ifPresent(methodBenchmarkValidator::validate);
        benchmarkMethods.forEach(methodBenchmarkValidator::validate);
    }
}
