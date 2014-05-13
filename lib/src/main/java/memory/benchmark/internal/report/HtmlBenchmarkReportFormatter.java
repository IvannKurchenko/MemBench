package memory.benchmark.internal.report;

import freemarker.template.*;
import memory.benchmark.api.Options;
import memory.benchmark.api.result.BenchmarkResult;

import java.io.*;
import java.util.*;

import static java.util.stream.Collectors.toList;

public class HtmlBenchmarkReportFormatter implements BenchmarkReportFormatter {

    public static final String RES_DIRECTORY = "res";
    private final Options options;

    public HtmlBenchmarkReportFormatter(Options options) {
        this.options = options;
    }

    @Override
    public void formatReport(List<BenchmarkResult> benchmarkResults) {
        Configuration cfg = new Configuration();
        cfg.setClassForTemplateLoading(HtmlBenchmarkReportFormatter.class, "");
        cfg.setIncompatibleImprovements(new Version(2, 3, 20));
        cfg.setDefaultEncoding("UTF-8");
        cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);

        Map<String, Object> input = new HashMap<>();
        input.put("title", "Benchmark");
        List<ChartData> chartDataList = benchmarkResults.stream().map(this::memoryHeapUsageToChartData).collect(toList());
        input.put("chartData", chartDataList);

        Writer fileWriter = null;
        try {

            Template template = cfg.getTemplate("chart.ftl");
            Writer consoleWriter = new OutputStreamWriter(System.out);
            template.process(input, consoleWriter);

            // For the sake of example, also write output into a file:
            fileWriter = new FileWriter(new File("output.html"));
            template.process(input, fileWriter);

        } catch (TemplateException | IOException e) {
            e.printStackTrace();
        } finally {
            if (fileWriter != null) {
                try {
                    fileWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private ChartData memoryHeapUsageToChartData(BenchmarkResult benchmarkResult){
        String title = benchmarkResult.getBenchmarkMethod().getName();
        long value = benchmarkResult.getHeapMemoryFootprint().getAverageValue().getUsedMemoryFootprint();
        return new ChartData(title, value);
    }
}
