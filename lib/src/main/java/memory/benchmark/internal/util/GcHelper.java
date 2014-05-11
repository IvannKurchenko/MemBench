package memory.benchmark.internal.util;

import memory.benchmark.api.Options;

import java.util.concurrent.TimeUnit;

import static memory.benchmark.internal.util.ThrowableHandlers.printThrowableAction;

public class GcHelper {

    private final long gcTime;
    private final TimeUnit gcTimeUnit;

    public GcHelper(Options options) {
        this(options.getGcTime(), options.getGcTimeUnit());
    }

    public GcHelper(long gcTime, TimeUnit gcTimeUnit) {
        this.gcTime = gcTime;
        this.gcTimeUnit = gcTimeUnit;
    }

    public void tryGc() {
        System.gc();
        printThrowableAction(() -> gcTimeUnit.sleep(gcTime));
    }
}
