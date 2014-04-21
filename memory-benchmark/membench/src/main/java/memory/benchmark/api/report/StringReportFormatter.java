package memory.benchmark.api.report;

import memory.benchmark.api.Options;
import memory.benchmark.api.Options.ReportInformation;
import memory.benchmark.api.result.GcUsage;
import memory.benchmark.api.result.MemoryFootprint;
import memory.benchmark.api.result.MemoryPoolFootprint;
import memory.benchmark.api.result.Result;
import memory.benchmark.api.util.MemoryValueConverter;

import java.lang.management.MemoryType;
import java.util.List;

import static memory.benchmark.api.Options.ReportInformation.*;

/**
 *
 */
public class StringReportFormatter implements ReportFormatter<String> {

    private static final String EOL = Character.toString('\n');
    private static final String TAB = Character.toString('\t');

    private final MemoryValueConverter memoryValueConverter;

    public StringReportFormatter(MemoryValueConverter memoryValueConverter) {
        this.memoryValueConverter = memoryValueConverter;
    }

    @Override
    public String formatReport(Options options, List<Result> results) {
        StringBuilder builder = new StringBuilder();
        results.forEach(result -> appendResult(builder, options, result));
        return builder.toString();
    }

    private void appendResult(StringBuilder builder, Options options, Result result) {
        builder.append("Class : ").append(result.getBenchmarkClass().getSimpleName()).append(EOL);
        builder.append("- Method : ").append(result.getBenchmarkMethod().getName()).append(EOL);

        appendMemoryFootprint(builder, TAB + TAB, TAB, "- Heap memory footprint : ", options, HEAP_MEMORY_FOOTPRINT, result.getHeapMemoryFootprint());
        appendMemoryFootprint(builder, TAB + TAB, TAB, "- Non heap memory footprint : ", options, NON_HEAP_MEMORY_FOOTPRINT, result.getHeapMemoryFootprint());

        appendMemoryPoolFootPrints(builder, TAB, options, result.getMemoryPoolFootprints());

        appendGcUsages(builder, TAB, options, result.getGcUsages());

        builder.append(EOL);
    }

    private void appendMemoryFootprint(StringBuilder builder, String appender, String headerAppender, String header, Options options, ReportInformation information, MemoryFootprint footprint) {
        if(!allowedToPrint(options, information)) {
            return;
        }
        append(builder, headerAppender, header);
        appendMemoryFootprint(builder, appender, footprint);
    }

    private void appendMemoryFootprint(StringBuilder builder, String appender, MemoryFootprint footprint) {
        append(builder, appender, "- Used memory footprint : " + memoryValueConverter.convert(footprint.getUsedMemoryFootprint()));
        append(builder, appender, "- Committed memory footprint : " + memoryValueConverter.convert(footprint.getCommittedMemoryFootprint()));
        append(builder, appender, "- Max memory footprint : " + memoryValueConverter.convert(footprint.getMaxMemoryFootprint()));
    }

    private void appendMemoryPoolFootPrints(StringBuilder builder, String appender, Options options, List<MemoryPoolFootprint> footprints) {
        append(builder, appender, "- Memory pool footprint : ");
        footprints.forEach(f -> appendMemoryPoolFootPrint(builder, TAB + appender, options, f));
    }

    private void appendMemoryPoolFootPrint(StringBuilder builder, String appender, Options options, MemoryPoolFootprint footprint) {
        MemoryType footprintType = footprint.getMemoryType();
        boolean allowed =   (footprintType == MemoryType.HEAP && allowedToPrint(options, HEAP_MEMORY_POOL_FOOTPRINT)) ||
                            (footprintType == MemoryType.NON_HEAP && allowedToPrint(options, NON_HEAP_MEMORY_POOL_FOOTPRINT));
        if(!allowed) {
            return;
        }

        append(builder, appender, "- Pool name : " + footprint.getPoolName());
        append(builder, appender, "- Pool memory type : " + footprint.getMemoryType());
        appendMemoryFootprint(builder, TAB + appender, footprint);
    }

    private void appendGcUsages(StringBuilder builder, String appender, Options options, List<GcUsage> gcUsages) {
        if(!allowedToPrint(options, GC_USAGE)){
            return;
        }

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

    private boolean allowedToPrint(Options options, ReportInformation information) {
        return options.getReportInformation().contains(information);
    }
}
