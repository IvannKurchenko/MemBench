package memory.benchmark.internal.report;

import memory.benchmark.api.result.BenchmarkResult;

import java.util.List;

public interface BenchmarkReportFormatter {

    void formatReport(List<BenchmarkResult> benchmarkResults);
}
