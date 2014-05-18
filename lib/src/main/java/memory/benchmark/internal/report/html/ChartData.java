package memory.benchmark.internal.report.html;

public class ChartData {

    private final String title;
    private final String value;

    public ChartData(String title, String value) {
        this.title = title;
        this.value = value;
    }

    public String getTitle() {
        return title;
    }

    public String getValue() {
        return value;
    }
}
