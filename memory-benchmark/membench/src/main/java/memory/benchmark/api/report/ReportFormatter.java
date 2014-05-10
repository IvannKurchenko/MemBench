package memory.benchmark.api.report;

import memory.benchmark.api.result.Result;

import java.util.List;

/**
 * @param <T> - report format type
 */
public interface ReportFormatter<T> {

    /**
     *
     */
    T formatReport(List<Result> results);
}
