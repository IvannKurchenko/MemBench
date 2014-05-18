package memory.benchmark.internal.util;

import memory.benchmark.api.BenchmarkOptions;

import java.util.concurrent.TimeUnit;

import static memory.benchmark.internal.util.ThrowableHandlers.printThrowableAction;

public class GcHelper {

    private final long gcTime;
    private final TimeUnit gcTimeUnit;
    private final Log log;

    public GcHelper(BenchmarkOptions options, Log log) {
        this(options.getGcTime(), options.getGcTimeUnit(), log);
    }

    public GcHelper(long gcTime, TimeUnit gcTimeUnit, Log log) {
        this.gcTime = gcTime;
        this.gcTimeUnit = gcTimeUnit;
        this.log = log;
    }

    public void tryGc() {
        if(gcTime < 0) {
            return;
        }

        log.log("Start GC...");
        System.gc();
        printThrowableAction(() -> gcTimeUnit.sleep(gcTime));
        log.log("Finish GC...");
    }
}
