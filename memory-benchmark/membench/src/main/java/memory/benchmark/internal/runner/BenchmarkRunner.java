package memory.benchmark.internal.runner;

import memory.benchmark.api.annotations.Benchmark;
import memory.benchmark.api.result.Result;
import memory.benchmark.internal.ResultBuilder;
import memory.benchmark.internal.collect.BenchmarkDataCollector;
import memory.benchmark.internal.util.Factory;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class BenchmarkRunner {

    private final Collection<Class<?>> benchmarkClasses;
    private final Factory<BenchmarkDataCollector,?> collectorFactory;
    private final Factory<BenchmarkMethodInvoker, Class> methodInvokerFactory;

    BenchmarkRunner(Collection<Class<?>> benchmarkClasses,
                    Factory<BenchmarkDataCollector,?> collectorFactory,
                    Factory<BenchmarkMethodInvoker, Class> methodInvokerFactory) {

        this.benchmarkClasses = benchmarkClasses;
        this.collectorFactory = collectorFactory;
        this.methodInvokerFactory = methodInvokerFactory;
    }

    public List<Result> run() {
         List<Result> results = new ArrayList<>();
         for (Class benchmarkClass : benchmarkClasses) {

             BenchmarkMethodInvoker benchmarkMethodInvoker = methodInvokerFactory.create(benchmarkClass);
             BenchmarkDataCollector benchmarkDataCollector = collectorFactory.create();
             List<Method> benchmarkMethods = benchmarkMethodInvoker.getBenchmarkMethods();

             if(benchmarkMethods.size() > 0) {
                 results.addAll(runTests(benchmarkMethodInvoker, benchmarkDataCollector));
             }
         }
         return  results;
     }

    private List<Result> runTests(BenchmarkMethodInvoker benchmarkMethodInvoker, BenchmarkDataCollector benchmarkDataCollector) {
        List<Result> resultList = new ArrayList<>();

        for (Method testMethod : benchmarkMethodInvoker.getBenchmarkMethods()) {
            try {

                ResultBuilder resultBuilder = new ResultBuilder(testMethod.getDeclaringClass(), testMethod);
                Benchmark benchmark = testMethod.getAnnotation(Benchmark.class);

                for (int i = 0; i < benchmark.testTimes(); i++) {
                    benchmarkMethodInvoker.invokeBefore();

                    benchmarkDataCollector.onBeforeTest();

                    benchmarkMethodInvoker.invokeBenchmark(testMethod);

                    benchmarkDataCollector.onAfterTest();

                    benchmarkMethodInvoker.invokeAfter();
                }

                benchmarkDataCollector.collectBenchmarkData(resultBuilder);
                benchmarkDataCollector.clear();

                resultList.add(resultBuilder.build());

            } finally {
                benchmarkMethodInvoker.close();
                benchmarkDataCollector.close();
            }
        }

        return resultList;
    }
}
