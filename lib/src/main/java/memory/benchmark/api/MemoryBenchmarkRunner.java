package memory.benchmark.api;

import memory.benchmark.api.result.BenchmarkResult;
import memory.benchmark.internal.report.BenchmarkReportFormatter;
import memory.benchmark.internal.report.BenchmarkReportFormatterFactory;
import memory.benchmark.internal.runner.BenchmarkRunner;
import memory.benchmark.internal.runner.BenchmarkRunnerFactory;
import memory.benchmark.internal.util.Log;

import java.io.File;
import java.net.URL;
import java.util.Collection;
import java.util.List;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;
import static memory.benchmark.internal.util.ThrowableHandlers.rethrowThrowableFunction;

/**
 * Main class to run memory benchmark tests.
 *
 * @see memory.benchmark.api.BenchmarkOptions
 * @see memory.benchmark.api.result.BenchmarkResult
 */
public class MemoryBenchmarkRunner {

    /**
     * Runs given collection of benchmark classes and creates benchmark benchmark report.
     *
     * @param options general benchmark options.
     * @param testClasses benchmark classes.
     * @throws java.lang.NullPointerException is testClasses is null
     * @throws memory.benchmark.api.exception.BenchmarkRunException if benchmark execution failed
     * @throws memory.benchmark.api.exception.InvalidBenchmarkException if benchmark class have some defects
     * @return list of benchmark test results.
     */
    public static List<BenchmarkResult> run(BenchmarkOptions options, Collection<Class<?>> testClasses) {
        Log log = Log.of(Log.SYS_OUT, options);
        BenchmarkRunner benchmarkRunner = BenchmarkRunnerFactory.createBenchmarkRunner(testClasses, options, log);
        BenchmarkReportFormatter benchmarkReportFormatter = BenchmarkReportFormatterFactory.createFormatter(options, log);
        List<BenchmarkResult> benchmarkResults = benchmarkRunner.run();
        benchmarkReportFormatter.formatReport(benchmarkResults);
        return benchmarkResults;
    }

    /**
     * Runs given collection of benchmark classes and creates benchmark benchmark report.
     *
     * @param options general benchmark options.
     * @param testClasses benchmark classes.
     * @throws java.lang.NullPointerException is testClasses is null
     * @throws memory.benchmark.api.exception.BenchmarkRunException if benchmark execution failed
     * @throws memory.benchmark.api.exception.InvalidBenchmarkException if benchmark class have some defects
     * @return list of benchmark test results.
     */
    public static List<BenchmarkResult> run(BenchmarkOptions options, Class<?>... testClasses) {
        return run(options, asList(testClasses));
    }

    /**
     * Runs all benchmark classes inside given package.
     *
     * @param options general benchmark options.
     * @param testPackage package with benchmark classes.
     * @throws java.lang.NullPointerException is testPackage is null
     * @throws memory.benchmark.api.exception.BenchmarkRunException if benchmark execution failed
     * @throws memory.benchmark.api.exception.InvalidBenchmarkException if benchmark class have some defects
     * @return list of benchmark test results.
     */
    public static List<BenchmarkResult> run(BenchmarkOptions options, String testPackage) {
        List<Class<?>> allClasses = rethrowThrowableFunction(() -> getClassesInPackage(testPackage));
        return run(options, allClasses);
    }

    private static List<Class<?>> getClassesInPackage(String testPackage) throws ClassNotFoundException {
        URL root = Thread.currentThread().getContextClassLoader().getResource(testPackage.replace(".", "/"));
        File[] files = new File(root.getFile()).listFiles((File dir, String name) -> name.endsWith(".class"));
        return asList(files).
                stream().
                map(f -> forName(testPackage, f)).
                filter(MemoryBenchmarkRunner::isAcceptableClass).
                collect(toList());
    }

    private static boolean isAcceptableClass(Class<?> clazz) {
        return !clazz.isAnnotation() && !clazz.isAnonymousClass() && !clazz.isInterface();
    }

    private static Class<?> forName(String packagePath, File classFile) {
        return rethrowThrowableFunction(() -> Class.forName(packagePath + "." + classFile.getName().replaceAll(".class$", "")));
    }

    /**
     * Cannot be instantiated directly.
     */
    private MemoryBenchmarkRunner(){}
}
