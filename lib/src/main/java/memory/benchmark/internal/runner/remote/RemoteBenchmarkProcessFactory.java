package memory.benchmark.internal.runner.remote;

import memory.benchmark.api.Options;
import memory.benchmark.internal.util.Factory;

import java.io.File;
import java.io.IOException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.concurrent.TimeUnit;

import static memory.benchmark.internal.util.ThrowableHandler.handleThrowableFunction;

public class RemoteBenchmarkProcessFactory implements Factory<BenchmarkProcess, Class> {

    private final Options options;

    public RemoteBenchmarkProcessFactory(Options options) {
        this.options = options;
    }

    @Override
    public BenchmarkProcess create(Class clazz) {
        Process process = handleThrowableFunction(() -> runRemoteProcess(clazz));
        BenchmarkRemote benchmarkRemote = bindToProcess();
        return new BenchmarkProcess(process, benchmarkRemote);
    }

    private Process runRemoteProcess(Class benchmarkClass) throws InterruptedException, IOException {
        String javaHome = System.getProperty("java.home");
        String javaBin = javaHome + File.separator + "bin" + File.separator + "java";
        String classpath = System.getProperty("java.class.path");
        String serverClassName = RemoteServer.class.getCanonicalName();
        String benchmarkClassName = benchmarkClass.getCanonicalName();
        ProcessBuilder builder = new ProcessBuilder();
        builder.command(
            javaBin,
            "-cp",
            classpath,
            serverClassName,
            benchmarkClassName,
            Integer.toString(options.getRemotePort()),
            Integer.toString(options.getMxBeanRemotePort()),
            Long.toString(options.getGcTime()),
            options.getGcTimeUnit().name()
        );
        builder.redirectOutput(ProcessBuilder.Redirect.INHERIT);
        builder.redirectError(ProcessBuilder.Redirect.INHERIT);
        Process process = builder.start();
        process.waitFor(1, TimeUnit.SECONDS);
        return process;
    }

    public BenchmarkRemote bindToProcess() {
        Registry registry = handleThrowableFunction(() -> LocateRegistry.getRegistry(options.getRemotePort()));
        return handleThrowableFunction(() -> (BenchmarkRemote) registry.lookup(RemoteServer.BENCHMARK_OBJECT_NAME));
    }
}
