package memory.benchmark.internal.report;

public class ChartData {

    private final String title;
    private final long value;

    public ChartData(String title, long value) {
        this.title = title;
        this.value = value;
    }

    public String getTitle() {
        return title;
    }

    public long getValue() {
        return value;
    }
}
