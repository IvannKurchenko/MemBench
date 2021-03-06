package memory.benchmark.internal.runner;

import memory.benchmark.api.annotations.Benchmark;
import memory.benchmark.api.result.BenchmarkResult;
import memory.benchmark.internal.collect.BenchmarkDataCollector;
import memory.benchmark.internal.util.Action;
import memory.benchmark.internal.util.Factory;
import memory.benchmark.internal.util.Log;
import memory.benchmark.internal.util.ThrowableAction;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class BenchmarkRunner {

    private final Collection<Class<?>> benchmarkClasses;
    private final Factory<BenchmarkDataCollector, ?> collectorFactory;
    private final Factory<BenchmarkMethodInvoker, Class> methodInvokerFactory;
    private final Log log;

    BenchmarkRunner(Collection<Class<?>> benchmarkClasses,
                    Factory<BenchmarkDataCollector, ?> collectorFactory,
                    Factory<BenchmarkMethodInvoker, Class> methodInvokerFactory,
                    Log log) {

        this.benchmarkClasses = benchmarkClasses;
        this.collectorFactory = collectorFactory;
        this.methodInvokerFactory = methodInvokerFactory;
        this.log = log;
    }

    public List<BenchmarkResult> run() {
        List<BenchmarkResult> benchmarkResults = new ArrayList<>();
        for (Class benchmarkClass : benchmarkClasses) {
            log.log("Starting benchmark class : " + benchmarkClass.getCanonicalName());

            Optional<BenchmarkMethodInvoker> benchmarkMethodInvokerOpt = Optional.empty();
            Optional<BenchmarkDataCollector> benchmarkDataCollectorOpt = Optional.empty();

            try {
                BenchmarkMethodInvoker benchmarkMethodInvoker = methodInvokerFactory.create(benchmarkClass);
                benchmarkMethodInvokerOpt = Optional.of(benchmarkMethodInvoker);

                List<Method> benchmarkMethods = benchmarkMethodInvoker.getBenchmarkMethods();

                if (benchmarkMethods.size() > 0) {
                    BenchmarkDataCollector benchmarkDataCollector = collectorFactory.create();
                    benchmarkDataCollectorOpt = Optional.of(benchmarkDataCollector);
                    List<BenchmarkResult> results = runTests(benchmarkMethodInvoker, benchmarkDataCollector);
                    benchmarkResults.addAll(results);
                }

            } finally {
                benchmarkMethodInvokerOpt.ifPresent(BenchmarkMethodInvoker::close);
                benchmarkDataCollectorOpt.ifPresent(BenchmarkDataCollector::close);
            }

            log.log("Finished benchmark class : " + benchmarkClass.getCanonicalName());
        }
        return benchmarkResults;
    }

    private List<BenchmarkResult> runTests(BenchmarkMethodInvoker benchmarkMethodInvoker, BenchmarkDataCollector benchmarkDataCollector) {
        List<BenchmarkResult> benchmarkResultList = new ArrayList<>();

        for (Method testMethod : benchmarkMethodInvoker.getBenchmarkMethods()) {
            log.log("Starting benchmark method : " + testMethod.getName());

            Benchmark benchmark = testMethod.getAnnotation(Benchmark.class);
            ResultBuilder resultBuilder = new ResultBuilder(testMethod.getDeclaringClass(), testMethod, benchmark);

            for (int i = 0; i < benchmark.testTimes(); i++) {

                log.log("Benchmark #" + i);

                log.log("Invoke before method");

                executeSafety(benchmarkMethodInvoker::invokeBefore, resultBuilder);

                executeIfResultSucceed(benchmarkDataCollector::onBeforeTest, resultBuilder);

                log.log("Invoke benchmark method");

                executeSafetyIfResultSucceed(() -> benchmarkMethodInvoker.invokeBenchmark(testMethod), resultBuilder);

                log.log("Invoke after method");

                executeIfResultSucceed(benchmarkDataCollector::onAfterTest, resultBuilder);

                executeSafety(benchmarkMethodInvoker::invokeAfter, resultBuilder);
            }

            executeIfResultSucceed(() -> benchmarkDataCollector.collectBenchmarkData(resultBuilder), resultBuilder);
            benchmarkDataCollector.clear();

            benchmarkResultList.add(resultBuilder.build());

            log.log("Finished benchmark method : " + testMethod.getName());
        }
        return benchmarkResultList;
    }

    private boolean executeSafety(ThrowableAction throwableAction, ResultBuilder resultBuilder) {
        try {
            throwableAction.execute();
        } catch (Throwable throwable) {
            resultBuilder.setFailedTestCause(throwable);
            throwable.printStackTrace();
            return false;
        }
        return true;
    }

    private void executeIfResultSucceed(Action action, ResultBuilder resultBuilder) {
        if(resultBuilder.isSucceedResult()) {
            action.execute();
        }
    }

    private void executeSafetyIfResultSucceed(ThrowableAction throwableAction, ResultBuilder resultBuilder) {
        if(resultBuilder.isSucceedResult()) {
            executeSafety(throwableAction, resultBuilder);
        }
    }
}
