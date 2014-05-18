package memory.benchmark.api;

import memory.benchmark.api.result.BenchmarkResult;
import memory.benchmark.internal.report.BenchmarkReportFormatter;
import memory.benchmark.internal.report.BenchmarkReportFormatterFactory;
import memory.benchmark.internal.runner.BenchmarkRunner;
import memory.benchmark.internal.runner.BenchmarkRunnerFactory;
import memory.benchmark.internal.util.Log;
import org.reflections.Reflections;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import static java.util.Arrays.asList;

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
     * @return list of benchmark test results.
     */
    public static List<BenchmarkResult> run(BenchmarkOptions options, String testPackage) {
        Reflections reflections = new Reflections(testPackage);
        Set<Class<?>> allClasses = reflections.getSubTypesOf(Object.class);
        return run(options, allClasses);
    }

    /**
     * Cannot be instantiated directly.
     */
    private MemoryBenchmarkRunner(){}
}
