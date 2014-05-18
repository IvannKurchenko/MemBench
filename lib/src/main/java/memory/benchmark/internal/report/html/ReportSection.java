package memory.benchmark.internal.report.html;

import java.util.List;

public class ReportSection {

    private final String reportSectionName;
    private final String chartIdentifier;
    private final String suffix;
    private final List<ReportSectionTable> reportSectionTables;

    public ReportSection(String reportSectionName, String chartIdentifier, String suffix, List<ReportSectionTable> reportSectionTables) {
        this.reportSectionName = reportSectionName;
        this.chartIdentifier = chartIdentifier;
        this.suffix = suffix;
        this.reportSectionTables = reportSectionTables;
    }

    public String getSuffix() {
        return suffix;
    }

    public String getChartIdentifier() {
        return chartIdentifier;
    }

    public String getReportSectionName() {
        return reportSectionName;
    }

    public List<ReportSectionTable> getReportSectionTables() {
        return reportSectionTables;
    }
}
