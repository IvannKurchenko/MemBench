package memory.benchmark.internal.runner.remote;

import memory.benchmark.internal.runner.BenchmarkMethodExtractor;
import memory.benchmark.internal.runner.BenchmarkMethodInvoker;
import memory.benchmark.internal.util.Factory;
import memory.benchmark.internal.validation.BenchmarkValidator;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Optional;

public class RemoteBenchmarkMethodInvokerFactory implements Factory<BenchmarkMethodInvoker, Class> {

    private final BenchmarkValidator<Method> methodBenchmarkValidator;
    private final BenchmarkValidator<Class> classBenchmarkValidator;
    private final BenchmarkMethodExtractor methodExtractor;
    private final Factory<BenchmarkProcess, Class> benchmarkRemoteFactory;

    public RemoteBenchmarkMethodInvokerFactory(BenchmarkValidator<Method> methodBenchmarkValidator,
                                               BenchmarkValidator<Class> classBenchmarkValidator,
                                               BenchmarkMethodExtractor methodExtractor,
                                               Factory<BenchmarkProcess, Class> benchmarkRemoteFactory) {

        this.methodBenchmarkValidator = methodBenchmarkValidator;
        this.classBenchmarkValidator = classBenchmarkValidator;
        this.methodExtractor = methodExtractor;
        this.benchmarkRemoteFactory = benchmarkRemoteFactory;
    }

    @Override
    public BenchmarkMethodInvoker create(Class benchmarkClass) {
        List<Method> benchmarkMethods = methodExtractor.getTestMethods(benchmarkClass);
        Optional<Method> beforeMethod = methodExtractor.getBeforeMethod(benchmarkClass);
        Optional<Method> afterMethod = methodExtractor.getAfterMethod(benchmarkClass);
        validate(benchmarkClass, beforeMethod, afterMethod, benchmarkMethods);
        BenchmarkProcess benchmarkProcess = benchmarkRemoteFactory.create(benchmarkClass);
        return new RemoteBenchmarkMethodInvoker(benchmarkProcess, beforeMethod, afterMethod, benchmarkMethods);
    }

    private void validate(Class benchmarkClass, Optional<Method> beforeMethod, Optional<Method> afterMethod, List<Method> benchmarkMethods) {
        classBenchmarkValidator.validate(benchmarkClass);
        beforeMethod.ifPresent(methodBenchmarkValidator::validate);
        afterMethod.ifPresent(methodBenchmarkValidator::validate);
        benchmarkMethods.forEach(methodBenchmarkValidator::validate);
    }
}
