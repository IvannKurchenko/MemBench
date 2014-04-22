package memory.benchmark.internal;

import memory.benchmark.api.exception.BenchmarkRunException;
import memory.benchmark.api.Options;
import memory.benchmark.api.result.Result;
import memory.benchmark.internal.collect.BenchmarkResultCollector;
import memory.benchmark.internal.collect.BenchmarkResultCollectorFactory;
import memory.benchmark.internal.validation.BenchmarkClassValidator;
import memory.benchmark.internal.validation.BenchmarkMethodValidator;
import memory.benchmark.internal.validation.BenchmarkValidator;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class BenchmarkRunner {

    private final Collection<Class<?>> benchmarkClasses;
    private final BenchmarkValidator<Method> methodBenchmarkValidator;
    private final BenchmarkValidator<Class> classBenchmarkValidator;
    private final BenchmarkMethodExtractor methodExtractor;
    private final Options options;

    public BenchmarkRunner(Collection<Class<?>> benchmarkClasses, Options options) {
        this.benchmarkClasses = benchmarkClasses;
        this.options = options;
        this.methodBenchmarkValidator = new BenchmarkMethodValidator();
        this.classBenchmarkValidator = new BenchmarkClassValidator();
        this.methodExtractor =  new BenchmarkMethodExtractor();
    }

     public List<Result> run() {
         List<Result> results = new ArrayList<>();
         for (Class benchmarkClass : benchmarkClasses) {

             List<Method> benchmarkMethods = methodExtractor.getTestMethods(benchmarkClass);
             if(benchmarkMethods.size() > 0) {

                 Optional<Method> beforeMethod = methodExtractor.getBeforeMethod(benchmarkClass);
                 Optional<Method> afterMethod = methodExtractor.getAfterMethod(benchmarkClass);

                 validate(benchmarkClass, beforeMethod, afterMethod, benchmarkMethods);

                 results.addAll(runTest(benchmarkClass, beforeMethod, afterMethod, benchmarkMethods));
             }

         }
         return  results;
     }

    private List<Result> runTest(Class benchmarkClass, Optional<Method> beforeMethod, Optional<Method> afterMethod, List<Method> benchmarkMethods) {
        try {

            Object benchmarkObject = benchmarkClass.newInstance();
            BenchmarkResultCollector benchmarkResultCollector = BenchmarkResultCollectorFactory.createResultCollector();
            BenchmarkClassRunner benchmarkClassRunner = new BenchmarkClassRunner(benchmarkObject, beforeMethod, afterMethod, benchmarkMethods, benchmarkResultCollector);
            return  benchmarkClassRunner.runTests();

        } catch (InstantiationException | IllegalAccessException e) {
            throw new BenchmarkRunException(e);
        }
    }

    private void validate(Class benchmarkClass, Optional<Method> beforeMethod, Optional<Method> afterMethod, List<Method> benchmarkMethods){
        classBenchmarkValidator.validate(benchmarkClass);
        beforeMethod.ifPresent(methodBenchmarkValidator::validate);
        afterMethod.ifPresent(methodBenchmarkValidator::validate);
        benchmarkMethods.forEach(methodBenchmarkValidator::validate);
    }
}
