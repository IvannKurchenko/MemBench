package memory.benchmark.api;

import java.util.List;

public interface Runner {

    List<Result> run(Class testClass);
}
