package memory.benchmark.internal.runner.remote;

import memory.benchmark.api.Options;
import memory.benchmark.internal.util.Factory;
import memory.benchmark.internal.util.Log;

import java.io.File;
import java.io.IOException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static java.util.Arrays.asList;
import static memory.benchmark.internal.util.ThrowableHandlers.rethrowThrowableFunction;

public class RemoteBenchmarkProcessFactory implements Factory<BenchmarkProcess, Class> {

    private final Options options;
    private final Log log;

    public RemoteBenchmarkProcessFactory(Options options, Log log) {
        this.options = options;
        this.log = log;
    }

    @Override
    public BenchmarkProcess create(Class clazz) {
        Process process = rethrowThrowableFunction(() -> runRemoteProcess(clazz));
        BenchmarkRemote benchmarkRemote = bindToProcess();
        return new BenchmarkProcess(process, benchmarkRemote);
    }

    private Process runRemoteProcess(Class benchmarkClass) throws InterruptedException, IOException {
        String javaHome = System.getProperty("java.home");
        String javaBinary = javaHome + File.separator + "bin" + File.separator + "java";
        String classpath = System.getProperty("java.class.path");
        String serverClassName = RemoteServer.class.getCanonicalName();
        String benchmarkClassName = benchmarkClass.getCanonicalName();
        ProcessBuilder builder = new ProcessBuilder();

        List<String> command = new ArrayList<>();
        command.add(javaBinary);
        if(options.getVirtualMachineArguments().length >0 ) {
            command.addAll(asList(options.getVirtualMachineArguments()));
        }
        command.add("-cp");
        command.add(classpath);
        command.add(serverClassName);
        command.add(benchmarkClassName);
        command.add(Integer.toString(options.getRemotePort()));
        command.add(Integer.toString(options.getMxBeanRemotePort()));
        command.add(Long.toString(options.getGcTime()));
        command.add(options.getGcTimeUnit().name());
        log.log("Start remote process : " + command);
        builder.command(command);

        builder.redirectOutput(ProcessBuilder.Redirect.INHERIT);
        builder.redirectError(ProcessBuilder.Redirect.INHERIT);

        Process process = builder.start();
        process.waitFor(1, TimeUnit.SECONDS);
        return process;
    }

    public BenchmarkRemote bindToProcess() {
        Registry registry = rethrowThrowableFunction(() -> LocateRegistry.getRegistry(options.getRemotePort()));
        return rethrowThrowableFunction(() -> (BenchmarkRemote) registry.lookup(RemoteServer.BENCHMARK_OBJECT_NAME));
    }
}
