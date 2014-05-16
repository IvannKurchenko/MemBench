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
 *
 */
public class Runner {

    /**
     * @param options
     * @param testClasses
     * @return
     */
    public static List<BenchmarkResult> run(Options options, Collection<Class<?>> testClasses) {
        Log log = Log.of(Log.SYS_OUT, options);
        BenchmarkRunner benchmarkRunner = BenchmarkRunnerFactory.createBenchmarkRunner(testClasses, options, log);
        BenchmarkReportFormatter benchmarkReportFormatter = BenchmarkReportFormatterFactory.createFormatter(options, log);
        List<BenchmarkResult> benchmarkResults = benchmarkRunner.run();
        benchmarkReportFormatter.formatReport(benchmarkResults);
        return benchmarkResults;
    }

    /**
     * @param options
     * @param testClass
     * @return
     */
    public static List<BenchmarkResult> run(Options options, Class<?>... testClass) {
        return run(options, asList(testClass));
    }

    /**
     * @param options
     * @param packageName
     * @return
     */
    public static List<BenchmarkResult> run(Options options, String packageName) {
        Reflections reflections = new Reflections(packageName);
        Set<Class<?>> allClasses = reflections.getSubTypesOf(Object.class);
        return run(options, allClasses);
    }
}
