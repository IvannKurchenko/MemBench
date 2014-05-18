package memory.benchmark.internal.report.html;

public class ReportSectionTable {

    private final String benchmark;
    private final String average;
    private final String minimum;
    private final String maximum;

    public ReportSectionTable(String benchmark, String average, String minimum, String maximum) {
        this.benchmark = benchmark;
        this.average = average;
        this.minimum = minimum;
        this.maximum = maximum;
    }

    public String getMaximum() {
        return maximum;
    }

    public String getMinimum() {
        return minimum;
    }

    public String getAverage() {
        return average;
    }

    public String getBenchmark() {
        return benchmark;
    }
}
