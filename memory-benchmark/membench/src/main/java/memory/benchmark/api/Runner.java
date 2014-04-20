package memory.benchmark.api;

import memory.benchmark.api.result.Result;
import memory.benchmark.internal.BenchmarkRunner;
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
     *
     * @param options
     * @param testClasses
     * @return
     */
    public static List<Result> run(Options options, Collection<Class<?>> testClasses) {
        BenchmarkRunner benchmarkRunner = new BenchmarkRunner(testClasses, options);
        return benchmarkRunner.run();
    }

    /**
     *
     * @param options
     * @param testClass
     * @return
     */
    public static List<Result> run(Options options, Class<?>... testClass) {
        return run(options, asList(testClass));
    }

    /**
     * @param options
     * @param packageName
     * @return
     */
    public static List<Result> run(Options options, String packageName) {
        Reflections reflections = new Reflections(packageName);
        Set<Class<?>> allClasses =  reflections.getSubTypesOf(Object.class);
        return run(options, allClasses);
    }
}
