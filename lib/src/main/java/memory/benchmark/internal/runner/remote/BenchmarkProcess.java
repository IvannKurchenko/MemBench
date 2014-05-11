package memory.benchmark.internal.runner.remote;

public class BenchmarkProcess {

    private final Process process;
    private final BenchmarkRemote benchmarkRemote;

    public BenchmarkProcess(Process process, BenchmarkRemote benchmarkRemote) {
        this.process = process;
        this.benchmarkRemote = benchmarkRemote;
    }

    public Process getProcess() {
        return process;
    }

    public BenchmarkRemote getBenchmarkRemote() {
        return benchmarkRemote;
    }
}
