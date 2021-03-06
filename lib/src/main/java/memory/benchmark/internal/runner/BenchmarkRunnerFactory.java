package memory.benchmark.internal.runner;

import memory.benchmark.api.BenchmarkOptions;
import memory.benchmark.internal.collect.BenchmarkDataCollector;
import memory.benchmark.internal.runner.local.LocalBenchmarkMethodInvokerFactory;
import memory.benchmark.internal.runner.local.LocalBenchmarkObjectFactory;
import memory.benchmark.internal.runner.local.collect.LocalBenchmarkDataCollectorFactory;
import memory.benchmark.internal.runner.remote.BenchmarkProcess;
import memory.benchmark.internal.runner.remote.RemoteBenchmarkMethodInvokerFactory;
import memory.benchmark.internal.runner.remote.RemoteBenchmarkProcessFactory;
import memory.benchmark.internal.runner.remote.collect.RemoteBenchmarkDataCollectorFactory;
import memory.benchmark.internal.util.Factory;
import memory.benchmark.internal.util.Log;
import memory.benchmark.internal.validation.BenchmarkClassValidator;
import memory.benchmark.internal.validation.BenchmarkMethodValidator;

import java.util.Collection;

public class BenchmarkRunnerFactory {

    public static BenchmarkRunner createBenchmarkRunner(Collection<Class<?>> benchmarkClasses, BenchmarkOptions options, Log log) {

        Factory<BenchmarkDataCollector, ?> collectorFactory = createCollectorFactory(options);
        Factory<BenchmarkMethodInvoker, Class> methodInvokerFactory = createMethodInvokerFactory(options, log);
        return new BenchmarkRunner(benchmarkClasses, collectorFactory, methodInvokerFactory, log);
    }

    private static Factory<BenchmarkDataCollector, ?> createCollectorFactory(BenchmarkOptions options) {
        return BenchmarkOptions.RunMode.SEPARATE_PROCESS == options.getRunMode() ?
                new RemoteBenchmarkDataCollectorFactory(options) :
                new LocalBenchmarkDataCollectorFactory();
    }

    private static Factory<BenchmarkMethodInvoker, Class> createMethodInvokerFactory(BenchmarkOptions options, Log log) {
        return BenchmarkOptions.RunMode.SEPARATE_PROCESS == options.getRunMode() ?
                createRemoteMethodInvoker(options, log) :
                createLocalMethodInvoker(options, log);
    }

    private static Factory<BenchmarkMethodInvoker, Class> createLocalMethodInvoker(BenchmarkOptions options, Log log) {
        BenchmarkMethodValidator methodBenchmarkValidator = new BenchmarkMethodValidator();
        BenchmarkClassValidator classBenchmarkValidator = new BenchmarkClassValidator();
        BenchmarkMethodExtractor methodExtractor = new BenchmarkMethodExtractor();
        Factory<Object, Class> benchmarkObjectFactory = new LocalBenchmarkObjectFactory();
        return new LocalBenchmarkMethodInvokerFactory(  options,
                                                        log,
                                                        methodBenchmarkValidator,
                                                        classBenchmarkValidator,
                                                        methodExtractor,
                                                        benchmarkObjectFactory);
    }

    private static Factory<BenchmarkMethodInvoker, Class> createRemoteMethodInvoker(BenchmarkOptions options, Log log) {
        BenchmarkMethodValidator methodBenchmarkValidator = new BenchmarkMethodValidator();
        BenchmarkClassValidator classBenchmarkValidator = new BenchmarkClassValidator();
        BenchmarkMethodExtractor methodExtractor = new BenchmarkMethodExtractor();
        Factory<BenchmarkProcess, Class> benchmarkRemoteFactory = new RemoteBenchmarkProcessFactory(options, log);
        return new RemoteBenchmarkMethodInvokerFactory( methodBenchmarkValidator,
                                                        classBenchmarkValidator,
                                                        methodExtractor,
                                                        benchmarkRemoteFactory);
    }
}
