package memory.benchmark.api.report;

import memory.benchmark.api.result.BenchmarkResult;

import java.util.List;

/**
 * @param <T> - report format type
 */
public interface BenchmarkReportFormatter<T> {

    /**
     *
     */
    T formatReport(List<BenchmarkResult> benchmarkResults);
}
