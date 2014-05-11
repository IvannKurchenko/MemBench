package memory.benchmark.internal.runner.local;

import memory.benchmark.internal.util.Factory;

import static memory.benchmark.internal.util.ThrowableHandlers.rethrowThrowableFunction;

public class LocalBenchmarkObjectFactory implements Factory<Object, Class> {

    @Override
    public Object create(Class benchmarkClass) {
        return rethrowThrowableFunction(benchmarkClass::newInstance);
    }
}
