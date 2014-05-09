package memory.benchmark.internal.runner.remote;

import memory.benchmark.api.Options;
import memory.benchmark.internal.util.Factory;

import java.io.File;
import java.io.IOException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.concurrent.TimeUnit;

import static memory.benchmark.internal.util.ThrowableHandler.handleThrowableFunction;

public class RemoteBenchmarkObjectFactory implements Factory<BenchmarkRemote, Class> {

    private final Options options;

    public RemoteBenchmarkObjectFactory(Options options) {
        this.options = options;
    }

    @Override
    public BenchmarkRemote create(Class clazz) {
        //handleThrowableAction(() -> runRemoteProcess(clazz));
        return bindToProcess();
    }

    public void runRemoteProcess(Class clazz) throws InterruptedException, IOException {
        String javaHome = System.getProperty("java.home");
        String javaBin = javaHome + File.separator + "bin" + File.separator + "java";
        String classpath = System.getProperty("java.class.path");
        String className = clazz.getCanonicalName();
        ProcessBuilder builder = new ProcessBuilder(javaBin, "-cp", classpath, RemoteServer.class.getCanonicalName(), className);
        Process process = builder.start();
        process.waitFor(1, TimeUnit.SECONDS);
    }

    public BenchmarkRemote bindToProcess() {
        Registry registry = handleThrowableFunction(() -> LocateRegistry.getRegistry(options.getRemotePort()));
        return handleThrowableFunction(() -> (BenchmarkRemote) registry.lookup(RemoteServer.BENCHMARK_OBJECT_NAME));
    }
}
