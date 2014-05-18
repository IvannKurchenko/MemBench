package memory.benchmark.internal.report;

import memory.benchmark.api.BenchmarkOptions;
import memory.benchmark.internal.report.html.HtmlBenchmarkReportFormatter;
import memory.benchmark.internal.util.Log;

public class BenchmarkReportFormatterFactory {

    public static BenchmarkReportFormatter createFormatter(BenchmarkOptions options, Log log) {
        BenchmarkOptions.ReportFormat reportFormat = options.getReportFormat();
        switch (reportFormat) {
            case CONSOLE:
                return new ConsoleBenchmarkReportFormatter(options, log);

            case HTML:
                return new HtmlBenchmarkReportFormatter(options, log);

            default:
                throw new UnsupportedOperationException();
        }
    }
}
