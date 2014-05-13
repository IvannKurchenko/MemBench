package memory.benchmark.internal.report;

import memory.benchmark.api.Options;

public class BenchmarkReportFormatterFactory {

    public static BenchmarkReportFormatter createFormatter(Options options) {
        Options.ReportFormat reportFormat = options.getReportFormat();
        switch (reportFormat) {
            case CONSOLE:
                return new ConsoleBenchmarkReportFormatter(options);

            case HTML:
                return new HtmlBenchmarkReportFormatter(options);

            default:
                throw new UnsupportedOperationException();
        }
    }
}
