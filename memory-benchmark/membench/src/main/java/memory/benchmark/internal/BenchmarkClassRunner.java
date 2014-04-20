package memory.benchmark.internal;

import memory.benchmark.api.BenchmarkRunException;
import memory.benchmark.api.result.Result;
import memory.benchmark.internal.collect.BenchmarkResultCollector;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

class BenchmarkClassRunner {

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

        for (int i = 0; i < testMethods.size(); i++) {
            try {

                ResultBuilder resultBuilder = new ResultBuilder(testObject.getClass(), testMethods.get(i));

                runBefore();
                runTest(i);
                runAfter();

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
        benchmarkResultCollector.onBeforeTest();
    }

    private void runAfter() throws InvocationTargetException, IllegalAccessException {
        if(afterMethod.isPresent()) {
            afterMethod.get().invoke(testObject);
        }
        benchmarkResultCollector.onAfterTest();
    }

    private void runTest(int testNumber) throws InvocationTargetException, IllegalAccessException {
        testMethods.get(testNumber).invoke(testObject);
    }
}
