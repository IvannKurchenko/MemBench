package memory.benchmark.api.report;

import memory.benchmark.api.result.GcUsage;
import memory.benchmark.api.result.MemoryFootprint;
import memory.benchmark.api.result.MemoryPoolFootprint;
import memory.benchmark.api.result.Result;
import memory.benchmark.api.util.MemoryValueConverter;

import java.util.List;

public class StringReportFormatter implements ReportFormatter<String> {

    private static final String EOL = Character.toString('\n');
    private static final String TAB = Character.toString('\t');

    private final MemoryValueConverter memoryValueConverter;

    public StringReportFormatter(MemoryValueConverter memoryValueConverter) {
        this.memoryValueConverter = memoryValueConverter;
    }

    @Override
    public String formatReport(List<Result> results) {
        StringBuilder builder = new StringBuilder();
        results.forEach(result -> appendResult(builder, result));
        return builder.toString();
    }

    private void appendResult(StringBuilder builder, Result result) {
        builder.append("Class : ").append(result.getBenchmarkClass().getSimpleName()).append(EOL);
        builder.append("- Method : ").append(result.getBenchmarkMethod().getName()).append(EOL);

        append(builder, TAB, "- Heap memory footprint : ");
        appendMemoryFootprint(builder, TAB + TAB, result.getHeapMemoryFootprint());

        append(builder, TAB, "- Non heap memory footprint : ");
        appendMemoryFootprint(builder, TAB + TAB, result.getNonHeapMemoryFootprint());

        appendMemoryPoolFootPrints(builder, TAB, result.getMemoryPoolFootprints());

        appendGcUsages(builder, TAB, result.getGcUsages());

        builder.append(EOL);
    }

    private void appendMemoryFootprint(StringBuilder builder, String appender, MemoryFootprint footprint) {
        append(builder, appender, "- Used memory footprint : " + memoryValueConverter.convert(footprint.getUsedMemoryFootprint()));
        append(builder, appender, "- Committed memory footprint : " + memoryValueConverter.convert(footprint.getCommittedMemoryFootprint()));
        append(builder, appender, "- Max memory footprint : " + memoryValueConverter.convert(footprint.getMaxMemoryFootprint()));
    }

    private void appendMemoryPoolFootPrints(StringBuilder builder, String appender, List<MemoryPoolFootprint> footprints) {
        append(builder, appender, "- Memory pool footprint : ");
        footprints.forEach(f -> appendMemoryPoolFootPrint(builder, TAB + appender, f));
    }

    private void appendMemoryPoolFootPrint(StringBuilder builder, String appender, MemoryPoolFootprint footprint) {
        append(builder, appender, "- Pool name : " + footprint.getPoolName());
        append(builder, appender, "- Pool memory type : " + footprint.getMemoryType());
        appendMemoryFootprint(builder, TAB + appender, footprint);
    }

    private void appendGcUsages(StringBuilder builder, String appender, List<GcUsage> gcUsages) {
        append(builder, appender, "- GC usage: ");
        gcUsages.forEach(gc -> appendGcUsage(builder, appender + TAB, gc));
    }

    private void appendGcUsage(StringBuilder builder, String appender, GcUsage gcUsage) {
        append(builder, appender, "- Name : " + gcUsage.getName());
        append(builder, appender + TAB, "- GC count : " + gcUsage.getGcCount());
        append(builder, appender + TAB, "- GC time : " + gcUsage.getGcTime());
    }

    private void append(StringBuilder builder, String appender, String message) {
        builder.append(appender).append(message).append(EOL);
    }
}
