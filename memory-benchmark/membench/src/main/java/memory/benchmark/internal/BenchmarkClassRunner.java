package memory.benchmark.internal;

import memory.benchmark.api.BenchmarkRunException;
import memory.benchmark.api.annotations.Benchmark;
import memory.benchmark.api.result.Result;
import memory.benchmark.internal.collect.BenchmarkResultCollector;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

class BenchmarkClassRunner {

    private static final long GC_TIME = 1;
    private static final TimeUnit GC_TIME_UNIT = TimeUnit.SECONDS;

    private final Object testObject;
    private final Optional<Method> beforeMethod;
    private final List<Method> testMethods;
    private final Optional<Method> afterMethod;
    private final BenchmarkResultCollector benchmarkResultCollector;

    BenchmarkClassRunner(Object testObject,
                         Optional<Method> beforeMethod,
                         Optional<Method> afterMethod,
                         List<Method> testMethods,
                         BenchmarkResultCollector benchmarkResultCollector) {

        this.testObject = testObject;
        this.beforeMethod = beforeMethod;
        this.testMethods = testMethods;
        this.afterMethod = afterMethod;
        this.benchmarkResultCollector = benchmarkResultCollector;
    }

    public List<Result> runTests() {
        List<Result> resultList = new ArrayList<>();

        for (Method testMethod : testMethods) {
            try {

                ResultBuilder resultBuilder = new ResultBuilder(testObject.getClass(), testMethod);
                Benchmark benchmark = testMethod.getAnnotation(Benchmark.class);

                for (int i = 0; i < benchmark.testTimes(); i++) {
                    runBefore();
                    runTest(testMethod);
                    runAfter();
                }

                benchmarkResultCollector.collectBenchmarkResult(resultBuilder);
                resultList.add(resultBuilder.build());

            } catch (InvocationTargetException | IllegalAccessException e) {
                throw new BenchmarkRunException(e);
            }
        }

        return resultList;
    }

    private void runBefore() throws InvocationTargetException, IllegalAccessException {
        if(beforeMethod.isPresent()) {
            beforeMethod.get().invoke(testObject);
        }
        tryGc();
        benchmarkResultCollector.onBeforeTest();
    }

    private void runAfter() throws InvocationTargetException, IllegalAccessException {
        if(afterMethod.isPresent()) {
            afterMethod.get().invoke(testObject);
        }
        tryGc();
        benchmarkResultCollector.onAfterTest();
    }

    private void runTest(Method testMethod) throws InvocationTargetException, IllegalAccessException {
        testMethod.invoke(testObject);
    }

    private void tryGc() {
        try {
            System.gc();
            GC_TIME_UNIT.sleep(GC_TIME);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
