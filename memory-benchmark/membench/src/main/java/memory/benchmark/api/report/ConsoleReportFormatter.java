package memory.benchmark.api.report;

import memory.benchmark.api.Options;
import memory.benchmark.api.Options.ReportInformation;
import memory.benchmark.api.result.*;
import memory.benchmark.api.util.MemoryValueConverter;

import java.lang.management.MemoryType;
import java.util.List;

import static memory.benchmark.api.Options.ReportInformation.*;

/**
 *
 */
public class ConsoleReportFormatter implements ReportFormatter<String> {

    private static final String EOL = Character.toString('\n');
    private static final String TAB = Character.toString('\t');

    private final Options options;

    public ConsoleReportFormatter(Options options) {
        this.options = options;
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

        appendMemoryFootprint(builder, TAB + TAB, TAB, "- Heap memory footprint : ", HEAP_MEMORY_FOOTPRINT, result.getHeapMemoryFootprint());
        appendMemoryFootprint(builder, TAB + TAB, TAB, "- Non heap memory footprint : ", NON_HEAP_MEMORY_FOOTPRINT, result.getHeapMemoryFootprint());

        appendMemoryPoolFootPrints(builder, TAB, result.getMemoryPoolFootprints());

        appendGcUsages(builder, TAB, result.getGcUsages());

        builder.append(EOL);
    }

    private void appendMemoryFootprint(StringBuilder builder, String appender, String headerAppender, String header, ReportInformation information, StatisticView<MemoryFootprint> footprint) {
        if (!allowedToPrint(information)) {
            return;
        }
        append(builder, headerAppender, header);
        appendMemoryFootprint(builder, appender, footprint);
    }

    private void appendMemoryFootprint(StringBuilder builder, String appender, StatisticView<MemoryFootprint> footprint) {
        if (footprint.containsSingleValue()) {
            appendMemoryFootprint(builder, appender, footprint.getSingleValue());
        } else {
            append(builder, appender, "- Minimum : ");
            appendMemoryFootprint(builder, appender + TAB, footprint.getMinimumValue());
            append(builder, appender, "- Maximum : ");
            appendMemoryFootprint(builder, appender + TAB, footprint.getMaximumValue());
            append(builder, appender, "- Average : ");
            appendMemoryFootprint(builder, appender + TAB, footprint.getAverageValue());
        }
    }

    private void appendMemoryFootprint(StringBuilder builder, String appender, MemoryFootprint footprint) {
        MemoryValueConverter memoryValueConverter = options.getMemoryValueConverter();
        append(builder, appender, "- Used memory footprint : " + memoryValueConverter.convert(footprint.getUsedMemoryFootprint()));
        append(builder, appender, "- Committed memory footprint : " + memoryValueConverter.convert(footprint.getCommittedMemoryFootprint()));
        append(builder, appender, "- Max memory footprint : " + memoryValueConverter.convert(footprint.getMaxMemoryFootprint()));
    }

    private void appendMemoryPoolFootPrints(StringBuilder builder, String appender, List<MemoryPoolStatisticView> footprints) {
        if (!allowedToPrint(HEAP_MEMORY_POOL_FOOTPRINT) && !allowedToPrint(NON_HEAP_MEMORY_POOL_FOOTPRINT)) {
            return;
        }
        append(builder, appender, "- Memory pool footprint : ");
        footprints.forEach(f -> appendMemoryPoolFootPrint(builder, TAB + appender, f));
    }

    private void appendMemoryPoolFootPrint(StringBuilder builder, String appender, MemoryPoolStatisticView footprint) {
        if (!allowedMemoryPoolFootprint(footprint)) {
            return;
        }

        append(builder, appender, "- Pool name : " + footprint.getName());
        append(builder, appender, "- Pool memory type : " + footprint.getMemoryType());

        if (footprint.containsSingleValue()) {
            appendMemoryFootprint(builder, TAB + appender, footprint.getSingleValue());
        } else {
            appender = appender + TAB;
            append(builder, appender, "- Minimum : ");
            appendMemoryFootprint(builder, TAB + appender, footprint.getMinimumValue());
            append(builder, appender, "- Maximum : ");
            appendMemoryFootprint(builder, TAB + appender, footprint.getMaximumValue());
            append(builder, appender, "- Average : ");
            appendMemoryFootprint(builder, TAB + appender, footprint.getAverageValue());
        }
    }

    private boolean allowedMemoryPoolFootprint(MemoryPoolStatisticView footprint) {
        MemoryType footprintType = footprint.getMemoryType();
        return (footprintType == MemoryType.HEAP && allowedToPrint(HEAP_MEMORY_POOL_FOOTPRINT)) ||
                (footprintType == MemoryType.NON_HEAP && allowedToPrint(NON_HEAP_MEMORY_POOL_FOOTPRINT));
    }

    private void appendGcUsages(StringBuilder builder, String appender, List<StatisticView<GcUsage>> gcUsages) {
        if (!allowedToPrint(GC_USAGE)) {
            return;
        }

        append(builder, appender, "- GC usage: ");
        gcUsages.forEach(gc -> appendGcUsage(builder, appender + TAB, gc));
    }

    private void appendGcUsage(StringBuilder builder, String appender, StatisticView<GcUsage> gcUsage) {
        if (gcUsage.containsSingleValue()) {
            appendGcUsage(builder, appender, gcUsage.getSingleValue());
        } else {
            append(builder, appender, "- Minimum : ");
            appendGcUsage(builder, appender, gcUsage.getMinimumValue());
            append(builder, appender, "- Maximum : ");
            appendGcUsage(builder, appender, gcUsage.getMaximumValue());
            append(builder, appender, "- Average : ");
            appendGcUsage(builder, appender, gcUsage.getAverageValue());
        }
    }

    private void appendGcUsage(StringBuilder builder, String appender, GcUsage gcUsage) {
        append(builder, appender, "- Name : " + gcUsage.getName());
        append(builder, appender + TAB, "- GC count : " + gcUsage.getGcCount());
        append(builder, appender + TAB, "- GC time : " + gcUsage.getGcTime());
    }

    private void append(StringBuilder builder, String appender, String message) {
        builder.append(appender).append(message).append(EOL);
    }

    private boolean allowedToPrint(ReportInformation information) {
        return options.getReportInformation().contains(information);
    }
}
