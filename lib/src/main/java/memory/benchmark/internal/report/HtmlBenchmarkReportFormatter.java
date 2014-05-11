package memory.benchmark.internal.report;

import memory.benchmark.api.Options;
import memory.benchmark.api.result.BenchmarkResult;

import java.util.List;

public class HtmlBenchmarkReportFormatter implements BenchmarkReportFormatter {

    private final Options options;

    public HtmlBenchmarkReportFormatter(Options options) {
        this.options = options;
    }

    @Override
    public void formatReport(List<BenchmarkResult> benchmarkResults) {

    }
}
